package com.femsa.sferea.home.inicio.programa.objetosAprendizaje;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;

import java.io.InputStream;

public interface LearningObjectView {

    void likeSuccess();

    void getObjectDetailSuccess(ObjectDetailResponseData data);

    void showLoader();

    void hideLoader();

    void muestraMensaje(int tipoMensaje);

    void OnMuestraMensajeInesperado(int tipoMensaje, int codigoRespuesta);

    void OnMarkAsReadExitoso();

    void OnBonusSuccess();

    void OnNoInternet();

    void OnJuegoListo(InputStream zip, int buffer);
}
