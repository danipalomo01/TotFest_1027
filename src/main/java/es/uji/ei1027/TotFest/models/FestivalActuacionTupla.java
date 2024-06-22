package es.uji.ei1027.TotFest.models;

import java.util.List;

public class FestivalActuacionTupla {

    private Festival festival;
    private List<Actuacio> actuacion;

    public FestivalActuacionTupla() {

    }

    public FestivalActuacionTupla(Festival festival, List<Actuacio> actuacio) {
        this.festival = festival;
        this.actuacion = actuacio;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public List<Actuacio> getActuacion() {
        return actuacion;
    }

    public void setActuacion(List<Actuacio> actuacion) {
        this.actuacion = actuacion;
    }
}
