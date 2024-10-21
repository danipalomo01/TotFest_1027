package es.uji.ei1027.TotFest.models;

public class Devolucion {

    private int id;

    private int numero;

    public Devolucion() {
    }

    public Devolucion(int id, int numero) {
        this.id = id;
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
