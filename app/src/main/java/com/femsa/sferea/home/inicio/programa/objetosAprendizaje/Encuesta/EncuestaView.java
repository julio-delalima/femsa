package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Encuesta;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.EncuestaResponseData;

public interface EncuestaView {

    void OnViewMostrarLoader();

    void OnViewEsconderLoader();

    void OnViewMostrarMensaje(int tipoMensaje);

    void OnViewMostrarMensaje(int tipoMensaje, int codigoRespuesta);

    void OnViewPintaEncuesta(EncuestaResponseData data);

    void OnViewEncuestaAdded();
}
