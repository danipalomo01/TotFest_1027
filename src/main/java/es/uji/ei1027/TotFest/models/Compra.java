package es.uji.ei1027.TotFest.models;

import java.sql.Date;

public class Compra {

    private int idcompra;

    private Date data;

    private double preucompra;

    private String email;

    private String telefon;

    public Compra() {
    }

    public Compra(Date data, double preucompra, String email, String telefon) {
        this.data = data;
        this.preucompra = preucompra;
        this.email = email;
        this.telefon = telefon;
    }

    public int getIdcompra() {
        return idcompra;
    }

    public void setIdcompra(int idcompra) {
        this.idcompra = idcompra;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getPreucompra() {
        return preucompra;
    }

    public void setPreucompra(double preucompra) {
        this.preucompra = preucompra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}
