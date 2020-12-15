package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos;

public interface JuegosView {

    void onSesionActualizada();

    void onMuestraMensaje(int tipo);

    void onMuestraMensaje(int tipo, int codigo);

    void onNoAuth();
}
