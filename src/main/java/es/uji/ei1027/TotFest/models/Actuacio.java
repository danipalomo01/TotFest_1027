package es.uji.ei1027.TotFest.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class Actuacio {

    private int idActuacio;
    private int idContracte;
    private Date data;
    private Time horaInici;
    private Time horaFiPrevista;
    private String comentaris;
    private int idFestival;


    public Actuacio(){

    }

    public Actuacio(int idActuacio, int idContracte, Date data, Time horaInici, Time horaFiPrevista, String comentaris, int idFestival) {
        this.idActuacio = idActuacio;
        this.idContracte = idContracte;
        this.data = data;
        this.horaInici = horaInici;
        this.horaFiPrevista = horaFiPrevista;
        this.comentaris = comentaris;
        this.idFestival = idFestival;
    }

    public int getIdActuacio() {
        return idActuacio;
    }

    public void setIdActuacio(int idActuacio) {
        this.idActuacio = idActuacio;
    }

    public int getIdContracte() {
        return idContracte;
    }

    public void setIdContracte(int idContracte) {
        this.idContracte = idContracte;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Time getHoraInici() {
        return horaInici;
    }

    public void setHoraInici(Time horaInici) {
        this.horaInici = horaInici;
    }

    public Time getHoraFiPrevista() {
        return horaFiPrevista;
    }

    public void setHoraFiPrevista(Time horaFiPrevista) {
        this.horaFiPrevista = horaFiPrevista;
    }

    public String getComentaris() {
        return comentaris;
    }

    public void setComentaris(String comentaris) {
        this.comentaris = comentaris;
    }

    public int getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(int idFestival) {
        this.idFestival = idFestival;
    }
}

