package br.com.sicredi.sincronizacaoreceita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class SincronizacaoReceitaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SincronizacaoReceitaApplication.class, args);
    }

}
