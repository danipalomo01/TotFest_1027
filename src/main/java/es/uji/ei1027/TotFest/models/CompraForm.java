package es.uji.ei1027.TotFest.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CompraForm {
    private int idFestival;
    public EntradaTipusEnum tipusEntrada;
    private List<Date> listaFechas = new ArrayList<>();
    private int numEntrades;
    private String email;
    private String telefon;

    // Getters y Setters
    public int getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(int idFestival) {
        this.idFestival = idFestival;
    }



    public int getNumEntrades() {
        return numEntrades;
    }

    public EntradaTipusEnum getTipusEntrada() {
        return tipusEntrada;
    }

    public void setTipusEntrada(EntradaTipusEnum tipusEntrada) {
        this.tipusEntrada = tipusEntrada;
    }

    public List<Date> getListaFechas() {
        return listaFechas;
    }

    public void setListaFechas(List<Date> listaFechas) {
        this.listaFechas = listaFechas;
    }

    public void setNumEntrades(int numEntrades) {
        this.numEntrades = numEntrades;
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
