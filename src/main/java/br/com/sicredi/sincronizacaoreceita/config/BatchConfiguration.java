package br.com.sicredi.sincronizacaoreceita.config;

import br.com.sicredi.sincronizacaoreceita.listeners.JobCompletionNotificationListener;
import br.com.sicredi.sincronizacaoreceita.models.Conta;
import br.com.sicredi.sincronizacaoreceita.processors.ContaItemProcessor;
import br.com.sicredi.sincronizacaoreceita.utils.ConversionUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private DelimitedLineAggregator<Conta> createContaoLineAggregator() {

        DelimitedLineAggregator<Conta> aggregator = new DelimitedLineAggregator<>();
        aggregator.setDelimiter(";");

        BeanWrapperFieldExtractor<Conta> extractor = new BeanWrapperFieldExtractor<>() {
            @Override
            public Object[] extract(Conta conta) {
                return new Object[] {
                        conta.getAgencia(),
                        ConversionUtils.stringWithMask(conta.getConta(), "#####-#"),
                        ConversionUtils.parseDoubleToString(conta.getSaldo()),
                        conta.getStatus(),
                        conta.getResultado()
                };
            }
        };
        aggregator.setFieldExtractor(extractor);

        return aggregator;
    }

    @Bean
    public FlatFileItemReader<Conta> reader(ApplicationArguments args){
        String filePath = args.getSourceArgs()[0];
        return new FlatFileItemReaderBuilder<Conta>()
                .name("ContaItemReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .delimiter(";")
                .names("agencia", "conta", "saldo", "status")
                .fieldSetMapper(createContaBeanMapper())
                .linesToSkip(1)
                .build();
    }

    private BeanWrapperFieldSetMapper<Conta> createContaBeanMapper() {
        BeanWrapperFieldSetMapper<Conta> mapper =  new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(Conta.class);
        return mapper;
    }

    @Bean
    public FlatFileItemWriter<Conta> writer() {
        Resource outputResource = new FileSystemResource("output/outputData.csv");
        return new FlatFileItemWriterBuilder<Conta>()
                .name("ContaItemWriter")
                .resource(outputResource)
                .append(true)
                .lineAggregator(createContaoLineAggregator())
                .headerCallback(writer -> writer.write("agencia;conta;saldo;status;resultado"))
                .build();
    }

    @Bean
    public ContaItemProcessor processor() {
        return new ContaItemProcessor();
    }

    @Bean
    public Job importContaJob(JobBuilderFactory jobBuilderFactory, JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importContasJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ApplicationArguments args) {
        return stepBuilderFactory.get("step1")
                .<Conta, Conta> chunk(1000)
                .reader(reader(args))
                .processor(processor())
                .writer(writer())
                .build();
    }


}
