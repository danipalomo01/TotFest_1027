package es.uji.ei1027.TotFest.models;

import java.math.BigDecimal;
import java.util.Date;

public class EntradaTipus {

    private int id;
    private EntradaTipusEnum entradaTipus;
    private int idFestival;
    private BigDecimal preu;
    private String descripcio;
    private BigDecimal percentatgeMaximAforament;

    public EntradaTipus(){

    }
    public EntradaTipus(EntradaTipusEnum entradaTipus, int idFestival, BigDecimal preu, String descripcio, BigDecimal percentatgeMaximAforament) {
        this.entradaTipus = entradaTipus;
        this.idFestival = idFestival;
        this.preu = preu;
        this.descripcio = descripcio;
        this.percentatgeMaximAforament = percentatgeMaximAforament;
    }

    public EntradaTipusEnum getEntradaTipus() {
        return entradaTipus;
    }

    public void setEntradaTipus(EntradaTipusEnum entradaTipus) {
        this.entradaTipus = entradaTipus;
    }

    public int getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(int idFestival) {
        this.idFestival = idFestival;
    }

    public BigDecimal getPreu() {
        return preu;
    }

    public void setPreu(BigDecimal preu) {
        this.preu = preu;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public BigDecimal getPercentatgeMaximAforament() {
        return percentatgeMaximAforament;
    }

    public void setPercentatgeMaximAforament(BigDecimal percentatgeMaximAforament) {
        this.percentatgeMaximAforament = percentatgeMaximAforament;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

