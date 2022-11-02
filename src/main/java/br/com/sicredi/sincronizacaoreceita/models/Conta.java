package br.com.sicredi.sincronizacaoreceita.models;

import br.com.sicredi.sincronizacaoreceita.utils.ConversionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Data
@Slf4j
public class Conta {

    private String agencia;
    private String conta;
    private double saldo;
    private String status;
    private Boolean resultado;


    public void setSaldo(String saldo) {
        try {
            this.saldo = ConversionUtils.parseStringToFormattedDouble(saldo);
        } catch (ParseException e) {
            this.saldo = 0;
        }
    }

    public void setConta(String conta) {
        this.conta = conta.replace("-", "");
    }
}
