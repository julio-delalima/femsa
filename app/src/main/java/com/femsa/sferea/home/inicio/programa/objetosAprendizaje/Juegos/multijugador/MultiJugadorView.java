package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.Juegos.RetadorResponseData;

public interface MultiJugadorView {
    void onMuestraMensaje(int tipoMensaje);

    void onMuestraMensaje(int tipoMensaje, int codigo);

    void onCargaListadoRetadores(RetadorResponseData data);

    void onSinRetadores();

    void onNoAuth();

    void onMuestraLoader();

    void onQuitaLoader();

    void OnRetoEnviado();

    void OnRetoAceptado();
}
