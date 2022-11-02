package br.com.sicredi.sincronizacaoreceita.processors;

import br.com.sicredi.sincronizacaoreceita.models.Conta;
import br.com.sicredi.sincronizacaoreceita.services.ReceitaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ContaItemProcessor implements ItemProcessor<Conta, Conta> {

    @Autowired
    public ReceitaService receitaService;

    @Override
    public Conta process(Conta conta){
        try {
            conta.setResultado(receitaService.atualizarConta(conta.getAgencia(),  conta.getConta(), conta.getSaldo(),
                    conta.getStatus()));
        }
        catch (RuntimeException e) {
            log.error("Error sending account: " + conta.getConta());
            conta.setResultado(false);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return conta;
    }
}
