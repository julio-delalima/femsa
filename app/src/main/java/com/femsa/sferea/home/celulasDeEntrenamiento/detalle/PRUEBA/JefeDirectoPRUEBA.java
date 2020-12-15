package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.PRUEBA;

public class JefeDirectoPRUEBA {

    private String nombre;
    private String puesto;
    private String nombreColaborador;
    private String puestoColaborador;

    public JefeDirectoPRUEBA(String nombre, String puesto, String nombreColaborador, String puestoColaborador) {
        this.nombre = nombre;
        this.puesto = puesto;
        this.nombreColaborador = nombreColaborador;
        this.puestoColaborador = puestoColaborador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public String getNombreColaborador() {
        return nombreColaborador;
    }

    public String getPuestoColaborador() {
        return puestoColaborador;
    }
}
