package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.PRUEBA;

public class AdministradorPaisPRUEBA {

    private String categoria;
    private String nombre;
    private String puesto;

    public AdministradorPaisPRUEBA(String categoria, String nombre, String puesto) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.puesto = puesto;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPuesto() {
        return puesto;
    }
}
