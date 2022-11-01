package br.com.sicredi.sincronizacaoreceita.models;

import lombok.Data;

@Data
public class Conta {

    private String agencia;
    private String conta;
    private double saldo;
    private String status;
    private Boolean sent;

}
