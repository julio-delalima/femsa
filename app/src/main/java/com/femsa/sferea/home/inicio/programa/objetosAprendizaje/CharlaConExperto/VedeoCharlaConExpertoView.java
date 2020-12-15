package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.CharlaConExperto;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.VedeoCharlaConExpertoResponseData;

public interface VedeoCharlaConExpertoView {

    void OnVedeoCharlaConExpertoCargaLoader();

    void OnVedeoCharlaConExpertoOcultaLoader();

    void OnVedeoCharlaConExpertoCargaDatos(VedeoCharlaConExpertoResponseData data);

    void OnVedeoCharlaConExpertoMuestraMensaje(int mensaje);

    void OnVedeoCharlaConExpertoMuestraMensaje(int mensaje, int codigo);

    void OnVedeoCharlaConExpertoInsertada();

    void OnVedeoCharlaConExpertoInapartable();
}
