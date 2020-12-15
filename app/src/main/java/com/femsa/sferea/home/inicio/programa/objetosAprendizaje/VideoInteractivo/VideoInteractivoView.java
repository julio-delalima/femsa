package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.VideoInteractivoResponseData;

public interface VideoInteractivoView {

    void videoInteractivoSuccess(VideoInteractivoResponseData data);

    void showLoader();

    void hideLoader();

    void muestraMensaje(int tipoMensaje);

    void muestraMensajeInesperado(int tipoMensaje, int codigoRespuesta);

    void OnNoInternet();
}
