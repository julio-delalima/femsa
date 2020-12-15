package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Evaluacion;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.EvaluacionResponseData;

public interface EvaluacionView {

    void OnViewMostrarLoader();

    void OnViewEsconderLoader();

    void OnViewMostrarMensaje(int tipoMensaje);

    void OnViewMostrarMensaje(int tipoMensaje, int codigoRespuesta);

    void OnViewPintaEvaluacion(EvaluacionResponseData data);

    void OnViewEvaluacionAdded();
}
