package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Vedeoconferencia;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.VedeoconferenciaResponseData;

public interface VedeoconferenciaView {
    void OnVedeoconferenciaMuestraLoader();
    void OnVedeoconferenciaOcultaLoader();
    void OnVedeoconferenciaAcomodaData(VedeoconferenciaResponseData data);
    void OnVedeoconferenciaMensaje(int mensaje);
    void OnVedeoconferenciaMensaje(int mensaje, int codigo);
}
