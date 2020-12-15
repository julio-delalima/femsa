package com.femsa.sferea.home.celulasDeEntrenamiento.listado;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado.ListaSolicitudesData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;

public interface ListadoSolicitudesView {
    void onViewObtenerListadoSolicitudes(ListaSolicitudesData data);
    void onViewSinSolicitudes();
    void onViewMostrarMensaje(int tipoMensaje);
    void onViewMostrarMensaje(int tipoMensaje, int codigoRespuesta);
    void onViewMostrarLoader();
    void onViewOcultarLoader();
    void onViewSinAutorizacion();
    void onViewCelulaEliminada();
    void onViewDesplegarlistadoPaises(PaisesResponseData data);
}
