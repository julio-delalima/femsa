package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.ConfirmarCelula;

import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;

public interface CelulaView {

    void OnViewMostrarLoader();

    void OnViewOcultarLoader();

    void OnViewMostrarMensaje(int tipoMensaje);

    void OnViewMostrarMensaje(int tipoMensaje, int codigoRespuesta);

    void OnViewMostrarSolicitudExitosa();

    void OnViewSesionInvalida();

    void onViewDesplegarlistadoPaises(PaisesResponseData data);
}
