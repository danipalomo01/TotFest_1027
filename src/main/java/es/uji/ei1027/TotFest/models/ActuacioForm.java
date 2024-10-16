package es.uji.ei1027.TotFest.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class ActuacioForm {

    private int idFestival;
    private int idActuacio;
    private int idArtista;
    private int idContracte;
    private Date data;
    private Time horaInici;
    private Time horaFiPrevista;
    private BigDecimal preuContracteActuacio;
    private String comentaris;

    public ActuacioForm(){

    }

    public ActuacioForm(int idFestival, int idActuacio, int idArtista, int idContracte, Date data, Time horaInici, Time horaFiPrevista, BigDecimal preuContracteActuacio, String comentaris) {
        this.idActuacio = idActuacio;
        this.idArtista = idArtista;
        this.idContracte = idContracte;
        this.data = data;
        this.horaInici = horaInici;
        this.horaFiPrevista = horaFiPrevista;
        this.preuContracteActuacio = preuContracteActuacio;
        this.comentaris = comentaris;
        this.idFestival = idFestival;
    }

    public int getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(int idFestival) {
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

    public BigDecimal getPreuContracteActuacio() {
        return preuContracteActuacio;
    }

    public void setPreuContracteActuacio(BigDecimal preuContracteActuacio) {
        this.preuContracteActuacio = preuContracteActuacio;
    }

    public String getComentaris() {
        return comentaris;
    }

    public void setComentaris(String comentaris) {
        this.comentaris = comentaris;
    }

    public int getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(int idArtista) {
        this.idArtista = idArtista;
    }
}


