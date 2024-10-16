package es.uji.ei1027.TotFest.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CompraForm {
    private int idFestival;
    public String entradatipus;
    private Date fecha;
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

    public String getEntradatipus() {
        return entradatipus;
    }

    public void setEntradatipus(String entradatipus) {
        this.entradatipus = entradatipus;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
