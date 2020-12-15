package com.femsa.sferea.home.celulasDeEntrenamiento.detalle;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;

public interface DetalleSolicitudView {
    void onViewObtenerDetalleSolicitud(DetalleSolicitudData data);
    void onViewMostrarMensaje(int mensaje);
    void onViewMostrarMensaje(int mensaje, int codigo);
    void onViewMostrarLoader();
    void onViewOcultarLoader();
    void onViewTokenInvalido();
    void onViewMostrarDetallePais(DetallePaisesResponseData data);
    void onViewDesplegarlistadoPaises(PaisesResponseData data);
}
