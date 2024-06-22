package es.uji.ei1027.TotFest.models;

import java.util.ArrayList;
import java.util.List;

public class ContratoActuaciones {

    private ContracteArtista contrato;
    private List<Actuacio> actuaciones;

    public ContratoActuaciones() {
        this.actuaciones = new ArrayList<>();
    }
    public ContracteArtista getContrato() {
        return contrato;
    }

    public void setContrato(ContracteArtista contrato) {
        this.contrato = contrato;
    }

    public List<Actuacio> getActuaciones() {
        return actuaciones;
    }

    public void setActuaciones(List<Actuacio> actuaciones) {
        this.actuaciones = actuaciones;
    }

}
