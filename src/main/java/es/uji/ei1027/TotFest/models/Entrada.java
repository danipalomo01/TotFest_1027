package es.uji.ei1027.TotFest.models;

import java.math.BigDecimal;
import java.sql.Date;

public class Entrada {

    private int numero;
    private BigDecimal preuVendaEntradaIndividual;
    private Date data;
    private int idFestival;
    private Date datacompra;
    private int entradaTipus;

    public Entrada() {

    }

    public Entrada(int numero, BigDecimal preuVendaEntradaIndividual, Date data, int idFestival, Date datacompra, int entradaTipus) {
        this.numero = numero;
        this.preuVendaEntradaIndividual = preuVendaEntradaIndividual;
        this.data = data;
        this.idFestival = idFestival;
        this.datacompra = datacompra;
        this.entradaTipus = entradaTipus;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public BigDecimal getPreuVendaEntradaIndividual() {
        return preuVendaEntradaIndividual;
    }

    public void setPreuVendaEntradaIndividual(BigDecimal preuVendaEntradaIndividual) {
        this.preuVendaEntradaIndividual = preuVendaEntradaIndividual;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(int idFestival) {
        this.idFestival = idFestival;
    }

    public Date getDatacompra() {
        return datacompra;
    }

    public void setDatacompra(Date datacompra) {
        this.datacompra = datacompra;
    }

    public int getEntradaTipus() {
        return entradaTipus;
    }

    public void setEntradaTipus(int entradaTipus) {
        this.entradaTipus = entradaTipus;
    }
}

