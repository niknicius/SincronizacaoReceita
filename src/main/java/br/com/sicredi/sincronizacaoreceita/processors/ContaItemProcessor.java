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
    public Conta process(Conta conta) throws Exception {
        try {
            conta.setSent(receitaService.atualizarConta(conta.getAgencia(),  conta.getConta(), conta.getSaldo(),
                    conta.getStatus()));
        }
        catch (Exception e) {
            conta.setSent(false);
        }

        return conta;
    }
}
