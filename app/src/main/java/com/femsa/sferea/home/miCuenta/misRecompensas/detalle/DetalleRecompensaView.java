package com.femsa.sferea.home.miCuenta.misRecompensas.detalle;

import com.femsa.requestmanager.DTO.User.MisRecompensas.detalle.DetalleRecompensaData;

public interface DetalleRecompensaView {
    void onViewObtenerDetalleRecompensa(DetalleRecompensaData data);
    void onViewRecompensaObtenida();
    void onViewMostrarMensaje(int mensaje);
    void onViewMostrarMensaje(int mensaje, int codigo);
    void onViewMostrarLoader();
    void onViewOcultarLoader();
    void onViewTokenInvalido();
    void onViewRecompensaIncanjeable();
    void onViewRecompensaAgotada();
    void onViewRecompensaInalcanzable();
}
