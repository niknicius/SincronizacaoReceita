package br.com.sicredi.sincronizacaoreceita.models;

import br.com.sicredi.sincronizacaoreceita.utils.ConversionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

/** Conta Entity
 * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
 */
@Data
@Slf4j
public class Conta {

    private String agencia;
    private String conta;
    private double saldo;
    private String status;
    private Boolean resultado;


    /** Saldo setter method
     * @param saldo String
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    public void setSaldo(String saldo) {
        try {
            this.saldo = ConversionUtils.parseStringToFormattedDouble(saldo);
        } catch (ParseException e) {
            this.saldo = 0;
        }
    }

    /** Conta setter method
     * @param conta String
     * @author Vinnicius Santos<vinnicius.santos@dcx.ufpb.br>
     */
    public void setConta(String conta) {
        this.conta = conta.replace("-", "");
    }
}
