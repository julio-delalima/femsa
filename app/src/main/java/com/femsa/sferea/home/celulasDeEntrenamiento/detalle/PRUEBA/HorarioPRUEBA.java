package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.PRUEBA;

public class HorarioPRUEBA {

    private String hora;
    private boolean seleccionado;
    private boolean disponible; //Bandera para indicar si un día ya fue seleccionado por otro facilitador.

    public HorarioPRUEBA(String hora, boolean seleccionado, boolean disponible){
        this.hora = hora;
        this.seleccionado = seleccionado;
        this.disponible = disponible;
    }

    public String getHora() {
        return hora;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public boolean isDisponible() {
        return disponible;
    }
}
