package models;

import java.math.BigDecimal;

public class Entrada {

    private int numero;
    private String idEntradaTipus;
    private BigDecimal preuVendaEntradaIndividual;

    public Entrada() {

    }
    public Entrada(int numero, String idEntradaTipus, BigDecimal preuVendaEntradaIndividual) {
        this.numero = numero;
        this.idEntradaTipus = idEntradaTipus;
        this.preuVendaEntradaIndividual = preuVendaEntradaIndividual;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getIdEntradaTipus() {
        return idEntradaTipus;
    }

    public void setIdEntradaTipus(String idEntradaTipus) {
        this.idEntradaTipus = idEntradaTipus;
    }

    public BigDecimal getPreuVendaEntradaIndividual() {
        return preuVendaEntradaIndividual;
    }

    public void setPreuVendaEntradaIndividual(BigDecimal preuVendaEntradaIndividual) {
        this.preuVendaEntradaIndividual = preuVendaEntradaIndividual;
    }
}

