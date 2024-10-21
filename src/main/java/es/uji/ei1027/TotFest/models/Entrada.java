package es.uji.ei1027.TotFest.models;

import java.math.BigDecimal;
import java.sql.Date;

public class Entrada {

    private int numero;
    private Date data;
    private int idcompra;
    private Date datacompra;
    private int entradaTipus;


    public Entrada() {

    }

    public Entrada(int numero, BigDecimal preuVendaEntradaIndividual, Date data, int idFestival, Date datacompra, int entradaTipus, String email, String telefono) {
        this.numero = numero;
        this.data = data;
        //this.idFestival = idFestival;
        this.datacompra = datacompra;
        this.entradaTipus = entradaTipus;
        //this.email = email;
        //this.telefono = telefono;
    }

    public Entrada(int numero, BigDecimal preuVendaEntradaIndividual, Date data, int idcompra, Date datacompra, int entradaTipus) {
        this.numero = numero;
        this.data = data;
        this.idcompra = idcompra;
        this.datacompra = datacompra;
        this.entradaTipus = entradaTipus;
    }

    public int getIdcompra() {
        return idcompra;
    }

    public void setIdcompra(int idcompra) {
        this.idcompra = idcompra;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

   /*public int getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(int idFestival) {
        this.idFestival = idFestival;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }*/

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

