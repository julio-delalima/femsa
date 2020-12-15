package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.PRUEBA;

public class FacilitadorPRUEBA {

    private String nombre;
    private String puesto;
    private String tema;

    public FacilitadorPRUEBA(String nombre, String puesto, String tema) {
        this.nombre = nombre;
        this.puesto = puesto;
        this.tema = tema;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public String getTema() {
        return tema;
    }
}
