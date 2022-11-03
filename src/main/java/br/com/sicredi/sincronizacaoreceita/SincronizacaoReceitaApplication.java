package br.com.sicredi.sincronizacaoreceita;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Slf4j
public class SincronizacaoReceitaApplication implements CommandLineRunner {

    private final JobLauncher jobLauncher;

    private final Job importContaJob;

    public SincronizacaoReceitaApplication(JobLauncher jobLauncher, Job importContaJob) {
        this.jobLauncher = jobLauncher;
        this.importContaJob = importContaJob;
    }

    public static void main(String[] args) {
        SpringApplication.run(SincronizacaoReceitaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if(args.length == 0) {
            log.error("No input file provided");
        }else {
            JobParametersBuilder paramsBuilder = new JobParametersBuilder();
            paramsBuilder.addString("file.path", args[0]);
            jobLauncher.run(importContaJob, paramsBuilder.toJobParameters());
        }
    }
}
