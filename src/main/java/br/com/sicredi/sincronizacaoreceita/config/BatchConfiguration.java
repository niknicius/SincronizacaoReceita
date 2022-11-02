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

/** BatchConfiguration class
 * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    /** Create a new flat file reader
     * @param args ApplicationArguments
     * @return FlatFileItemReader
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
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

    /** Create a new field mapper
     * @return BeanWrapperFieldSetMapper
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    private BeanWrapperFieldSetMapper<Conta> createContaBeanMapper() {
        BeanWrapperFieldSetMapper<Conta> mapper =  new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(Conta.class);
        return mapper;
    }

    /** Create a new flat file writer
     * @return FlatFileItemWriter
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    @Bean
    public FlatFileItemWriter<Conta> writer() {
        Resource outputResource = new FileSystemResource("output/outputData.csv");
        return new FlatFileItemWriterBuilder<Conta>()
                .name("ContaItemWriter")
                .resource(outputResource)
                .append(true)
                .lineAggregator(createContaLineAggregator())
                .headerCallback(writer -> writer.write("agencia;conta;saldo;status;resultado"))
                .build();
    }

    /** Returns the processor
     * @return ContaItemProcessor
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    @Bean
    public ContaItemProcessor processor() {
        return new ContaItemProcessor();
    }

    /** Create the importContasJob
     * @param jobBuilderFactory JobBuilderFactory
     * @param listener JobCompletionNotificationListener
     * @param step1 Step
     * @return Job
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    @Bean
    public Job importContaJob(JobBuilderFactory jobBuilderFactory, JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importContasJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    /** Create the batch step
     * @param stepBuilderFactory stepBuilderFactory
     * @param args ApplicationArguments
     * @return Step
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ApplicationArguments args) {
        return stepBuilderFactory.get("step1")
                .<Conta, Conta> chunk(1000)
                .reader(reader(args))
                .processor(processor())
                .writer(writer())
                .build();
    }

    /** Create a new line aggregator
     * @return DelimitedLineAggregator
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    private DelimitedLineAggregator<Conta> createContaLineAggregator() {

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


}
