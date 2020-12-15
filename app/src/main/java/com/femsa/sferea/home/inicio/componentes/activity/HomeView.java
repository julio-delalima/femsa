package com.femsa.sferea.home.inicio.componentes.activity;

public interface HomeView {

    /**
     * Cuando se cierre exitosamente la sesión se ejecutará éste método.
     * */
    void onLogOutSuccess();

    /**
     * Método para mostrar el loader en la pantalla
     * */
    void onShowLoader();

    /**
     * Método para ocultar el loader en la pantalla
     * */
    void onHideLoader();

    void onShowMessage(int tipoMensaje);

    void onShowMessage(int tipoMensaje, int codigoRespuesta);
}
