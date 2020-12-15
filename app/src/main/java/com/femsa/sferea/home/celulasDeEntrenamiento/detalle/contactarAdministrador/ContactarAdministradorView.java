package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.contactarAdministrador;

public interface ContactarAdministradorView {
    void onViewMensajeEnviado();
    void onViewTokenInvalido();
    void onViewMostrarMensaje(int mensaje);
    void onViewMostrarMensaje(int mensaje, int codigo);
    void onViewMostrarLoader();
    void onViewOcultarLoader();
}
