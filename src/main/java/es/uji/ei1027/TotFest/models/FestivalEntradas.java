package es.uji.ei1027.TotFest.models;

import java.math.BigDecimal;
import java.sql.Date;

public class FestivalEntradas {

    private Festival festival;
    private BigDecimal preuDia;
    private BigDecimal preuComplet;


    public FestivalEntradas(Festival festival) {
        this.festival = festival;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public BigDecimal getPreuDia() {
        return preuDia;
    }

    public void setPreuDia(BigDecimal preuDia) {
        this.preuDia = preuDia;
    }

    public BigDecimal getPreuComplet() {
        return preuComplet;
    }

    public void setPreuComplet(BigDecimal preuComplet) {
        this.preuComplet = preuComplet;
    }
}
