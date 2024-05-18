package models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class CompraSenseRegistre {

    private int idCompra;
    private int idFestival;
    private Date data;
    private BigDecimal preuCompra;
    private String mail;
    private String telefon;
    private BigDecimal importTotal;
    private Timestamp dataCompra;

    public CompraSenseRegistre(){

    }
    public CompraSenseRegistre(int idCompra, int idFestival, Date data, BigDecimal preuCompra, String mail, String telefon, BigDecimal importTotal, Timestamp dataCompra) {
        this.idCompra = idCompra;
        this.idFestival = idFestival;
        this.data = data;
        this.preuCompra = preuCompra;
        this.mail = mail;
        this.telefon = telefon;
        this.importTotal = importTotal;
        this.dataCompra = dataCompra;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(int idFestival) {
        this.idFestival = idFestival;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getPreuCompra() {
        return preuCompra;
    }

    public void setPreuCompra(BigDecimal preuCompra) {
        this.preuCompra = preuCompra;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public BigDecimal getImportTotal() {
        return importTotal;
    }

    public void setImportTotal(BigDecimal importTotal) {
        this.importTotal = importTotal;
    }

    public Timestamp getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Timestamp dataCompra) {
        this.dataCompra = dataCompra;
    }
}

