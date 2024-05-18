package models;

import java.math.BigDecimal;
import java.util.Date;

public class EntradaTipus {

    private EntradaTipusEnum entradaTipus;
    private int idFestival;
    private BigDecimal preu;
    private String descripcio;
    private int nombreMaxim;
    private int nombreVendes;
    private Date dataPerTipusDia;
    private BigDecimal percentatgeMaximAforament;

    public EntradaTipus(){

    }
    public EntradaTipus(EntradaTipusEnum entradaTipus, int idFestival, BigDecimal preu, String descripcio, int nombreMaxim, int nombreVendes, Date dataPerTipusDia, BigDecimal percentatgeMaximAforament) {
        this.entradaTipus = entradaTipus;
        this.idFestival = idFestival;
        this.preu = preu;
        this.descripcio = descripcio;
        this.nombreMaxim = nombreMaxim;
        this.nombreVendes = nombreVendes;
        this.dataPerTipusDia = dataPerTipusDia;
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

    public int getNombreMaxim() {
        return nombreMaxim;
    }

    public void setNombreMaxim(int nombreMaxim) {
        this.nombreMaxim = nombreMaxim;
    }

    public int getNombreVendes() {
        return nombreVendes;
    }

    public void setNombreVendes(int nombreVendes) {
        this.nombreVendes = nombreVendes;
    }

    public Date getDataPerTipusDia() {
        return dataPerTipusDia;
    }

    public void setDataPerTipusDia(Date dataPerTipusDia) {
        this.dataPerTipusDia = dataPerTipusDia;
    }

    public BigDecimal getPercentatgeMaximAforament() {
        return percentatgeMaximAforament;
    }

    public void setPercentatgeMaximAforament(BigDecimal percentatgeMaximAforament) {
        this.percentatgeMaximAforament = percentatgeMaximAforament;
    }
}

