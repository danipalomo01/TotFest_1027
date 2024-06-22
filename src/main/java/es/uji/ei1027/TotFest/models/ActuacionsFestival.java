package es.uji.ei1027.TotFest.models;

public class ActuacionsFestival {

    private int idFestival;
    private int idActuacio;

    public ActuacionsFestival(){

    }
    public ActuacionsFestival(int idFestival, int idActuacio) {
        this.idFestival = idFestival;
        this.idActuacio = idActuacio;
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
}
