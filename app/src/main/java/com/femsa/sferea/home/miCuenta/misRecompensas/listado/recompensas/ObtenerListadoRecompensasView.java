package com.femsa.sferea.home.miCuenta.misRecompensas.listado.recompensas;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoRecompensasData;

public interface ObtenerListadoRecompensasView {
    void onViewObtenerListadoRecompensas(ObtenerListadoRecompensasData data);
    void onViewSinRecompensas();
    void onViewMostrarMensaje(int mensaje);
    void onViewMostrarLoader();
    void onViewOcultarLoader();
    void onViewTokenInvalido();
}
