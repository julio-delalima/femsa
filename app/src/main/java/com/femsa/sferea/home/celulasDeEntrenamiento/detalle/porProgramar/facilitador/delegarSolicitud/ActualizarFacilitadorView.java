package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.delegarSolicitud;

public interface ActualizarFacilitadorView {
    void onViewFacilitadorActualizado();
    void onViewMostrarLoader();
    void onViewOcultarLoader();
    void onViewMostrarMensaje(int mensaje);
    void onViewTokenInvalido();
}
