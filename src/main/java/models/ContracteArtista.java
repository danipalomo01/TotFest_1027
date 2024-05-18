package models;

import java.math.BigDecimal;
import java.sql.Date;

public class ContracteArtista {

    private int idContracte;
    private int idArtista;
    private Date dataInici;
    private Date dataFi;
    private String condicionsDescriptiu;
    private int numActuacionsAny;
    private BigDecimal importContracte;

    public ContracteArtista() {

    }
    public ContracteArtista(int idContracte, int idArtista, Date dataInici, Date dataFi, String condicionsDescriptiu, int numActuacionsAny, BigDecimal importContracte) {
        this.idContracte = idContracte;
        this.idArtista = idArtista;
        this.dataInici = dataInici;
        this.dataFi = dataFi;
        this.condicionsDescriptiu = condicionsDescriptiu;
        this.numActuacionsAny = numActuacionsAny;
        this.importContracte = importContracte;
    }

    public int getIdContracte() {
        return idContracte;
    }

    public void setIdContracte(int idContracte) {
        this.idContracte = idContracte;
    }

    public int getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(int idArtista) {
        this.idArtista = idArtista;
    }

    public Date getDataInici() {
        return dataInici;
    }

    public void setDataInici(Date dataInici) {
        this.dataInici = dataInici;
    }

    public Date getDataFi() {
        return dataFi;
    }

    public void setDataFi(Date dataFi) {
        this.dataFi = dataFi;
    }

    public String getCondicionsDescriptiu() {
        return condicionsDescriptiu;
    }

    public void setCondicionsDescriptiu(String condicionsDescriptiu) {
        this.condicionsDescriptiu = condicionsDescriptiu;
    }

    public int getNumActuacionsAny() {
        return numActuacionsAny;
    }

    public void setNumActuacionsAny(int numActuacionsAny) {
        this.numActuacionsAny = numActuacionsAny;
    }

    public BigDecimal getImportContracte() {
        return importContracte;
    }

    public void setImportContracte(BigDecimal importContracte) {
        this.importContracte = importContracte;
    }
}

