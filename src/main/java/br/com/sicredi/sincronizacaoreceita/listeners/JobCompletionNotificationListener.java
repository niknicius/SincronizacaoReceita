package br.com.sicredi.sincronizacaoreceita.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
 */
@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    /** Execute actions after job completion
     * @param jobExecution JobExecution
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job execution completed");
        }
    }

}
