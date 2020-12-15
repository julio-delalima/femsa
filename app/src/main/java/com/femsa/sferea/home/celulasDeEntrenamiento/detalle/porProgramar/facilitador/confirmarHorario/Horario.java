package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

/**
 * <p>Clase que representa uno de los elementos que se muestran en la lista de horarios para programar
 * una solicitud.</p>
 */
public class Horario {

    private String hora;
    private boolean seleccionado; //Bandera para indicar si una hora fue seleccionado por el facilitador.
    private boolean disponible; //Bandera para indicar si una hora se encuentra disponible.
    private String mTema, mFacilitador;

    public Horario(String hora, boolean seleccionado, boolean disponible){
        this.hora = hora;
        this.seleccionado = seleccionado;
        this.disponible = disponible;
    }

    public Horario(String hora, boolean seleccionado, boolean disponible, String mTema, String mFacilitador) {
        this.hora = hora;
        this.seleccionado = seleccionado;
        this.disponible = disponible;
        this.mTema = mTema;
        this.mFacilitador = mFacilitador;
    }

    public String getmTema() {
        return mTema;
    }

    public String getmFacilitador() {
        return mFacilitador;
    }

    public void setmTema(String mTema) {
        this.mTema = mTema;
    }

    public void setmFacilitador(String mFacilitador) {
        this.mFacilitador = mFacilitador;
    }

    public String getHora() {
        return hora;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public void setDisponible(boolean disponible){
        this.disponible = disponible;
    }
}
