package es.uji.ei1027.TotFest.models;

import java.util.Date;

public class Promotor {

    private int id;
    private String cif;
    private String nomOrganisme;
    private String domiciliFiscal;
    private String sector;
    private Date dataAlta;
    private Date dataBaixaRelacioComercial;
    private Date datainiciGestorFest;

    public Promotor() {
    }

    public Promotor(String cif, String nomOrganisme, String domiciliFiscal, String sector, Date dataAlta, Date dataBaixaRelacioComercial, Date datainiciGestorFest) {
        this.cif = cif;
        this.nomOrganisme = nomOrganisme;
        this.domiciliFiscal = domiciliFiscal;
        this.sector = sector;
        this.dataAlta = dataAlta;
        this.dataBaixaRelacioComercial = dataBaixaRelacioComercial;
        this.datainiciGestorFest = datainiciGestorFest;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNomOrganisme() {
        return nomOrganisme;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNomOrganisme(String nomOrganisme) {
        this.nomOrganisme = nomOrganisme;
    }

    public String getDomiciliFiscal() {
        return domiciliFiscal;
    }

    public void setDomiciliFiscal(String domiciliFiscal) {
        this.domiciliFiscal = domiciliFiscal;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Date getDataAlta() {
        return dataAlta;
    }

    public void setDataAlta(Date dataAlta) {
        this.dataAlta = dataAlta;
    }

    public Date getDataBaixaRelacioComercial() {
        return dataBaixaRelacioComercial;
    }

    public void setDataBaixaRelacioComercial(Date dataBaixaRelacioComercial) {
        this.dataBaixaRelacioComercial = dataBaixaRelacioComercial;
    }

    public Date getDatainiciGestorFest() {
        return datainiciGestorFest;
    }

    public void setDatainiciGestorFest(Date datainiciGestorFest) {
        this.datainiciGestorFest = datainiciGestorFest;
    }
}


