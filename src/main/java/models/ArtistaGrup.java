package models;

import java.math.BigDecimal;

public class ArtistaGrup {

    private int idArtista;
    private String nom;
    private String tipusMusica;
    private String descripcio;
    private BigDecimal cachetActual;

    public ArtistaGrup() {

    }

    public ArtistaGrup(int idArtista, String nom, String tipusMusica, String descripcio, BigDecimal cachetActual) {
        this.idArtista = idArtista;
        this.nom = nom;
        this.tipusMusica = tipusMusica;
        this.descripcio = descripcio;
        this.cachetActual = cachetActual;
    }


    public int getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(int idArtista) {
        this.idArtista = idArtista;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTipusMusica() {
        return tipusMusica;
    }

    public void setTipusMusica(String tipusMusica) {
        this.tipusMusica = tipusMusica;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public BigDecimal getCachetActual() {
        return cachetActual;
    }

    public void setCachetActual(BigDecimal cachetActual) {
        this.cachetActual = cachetActual;
    }
}

