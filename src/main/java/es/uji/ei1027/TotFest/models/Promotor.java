package es.uji.ei1027.TotFest.models;

import java.util.Date;

public class Promotor {

    private String cif;
    private String nomOrganisme;
    private String domiciliFiscal;
    private String sector;
    private Date dataAlta;
    private Date dataBaixaRelacio;
    private String nomGestorFestivals;
    private String pwd;
    private String mailGestorFestActual;
    private String telefonGestorFest;
    private String razonSocial;
    private String tipusOrganisme;

    public Promotor(){

    }
    public Promotor(String cif, String nomOrganisme, String domiciliFiscal, String sector, Date dataAlta, Date dataBaixaRelacio, String nomGestorFestivals, String pwd, String mailGestorFestActual, String telefonGestorFest, String razonSocial, String tipusOrganisme) {
        this.cif = cif;
        this.nomOrganisme = nomOrganisme;
        this.domiciliFiscal = domiciliFiscal;
        this.sector = sector;
        this.dataAlta = dataAlta;
        this.dataBaixaRelacio = dataBaixaRelacio;
        this.nomGestorFestivals = nomGestorFestivals;
        this.pwd = pwd;
        this.mailGestorFestActual = mailGestorFestActual;
        this.telefonGestorFest = telefonGestorFest;
        this.razonSocial = razonSocial;
        this.tipusOrganisme = tipusOrganisme;
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

    public Date getDataBaixaRelacio() {
        return dataBaixaRelacio;
    }

    public void setDataBaixaRelacio(Date dataBaixaRelacio) {
        this.dataBaixaRelacio = dataBaixaRelacio;
    }

    public String getNomGestorFestivals() {
        return nomGestorFestivals;
    }

    public void setNomGestorFestivals(String nomGestorFestivals) {
        this.nomGestorFestivals = nomGestorFestivals;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMailGestorFestActual() {
        return mailGestorFestActual;
    }

    public void setMailGestorFestActual(String mailGestorFestActual) {
        this.mailGestorFestActual = mailGestorFestActual;
    }

    public String getTelefonGestorFest() {
        return telefonGestorFest;
    }

    public void setTelefonGestorFest(String telefonGestorFest) {
        this.telefonGestorFest = telefonGestorFest;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTipusOrganisme() {
        return tipusOrganisme;
    }

    public void setTipusOrganisme(String tipusOrganisme) {
        this.tipusOrganisme = tipusOrganisme;
    }
}

