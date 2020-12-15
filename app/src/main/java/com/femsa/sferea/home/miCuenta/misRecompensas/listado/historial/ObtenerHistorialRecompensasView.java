package com.femsa.sferea.home.miCuenta.misRecompensas.listado.historial;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoHistorialData;

public interface ObtenerHistorialRecompensasView {
    void onViewObtenerHistorialRecompensas(ObtenerListadoHistorialData data);
    void onViewSinRecompensas();
    void onViewMostrarMensaje(int mensaje);
    void onViewMostrarLoader();
    void onViewOcultarLoader();
    void onViewTokenInvalido();
}
