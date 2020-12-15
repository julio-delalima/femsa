package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.ObtenerFacilitador;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.AreaDTO;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.FacilitadorData.FacilitadorData;

public interface FacilitadorView {

    void onFacilitadorViewShowLoader();

    void onFacilitadorViewHideLoader();

    void onFacilitadorViewMuestraAreas(AreaDTO data);

    void onFacilitadorViewMuestraFacilitadores(FacilitadorData facilitadores);

    void onFacilitadorViewMuestraMensaje(int tipoMensaje);

    void onFacilitadorViewMuestraMensaje(int tipoMensaje, int codigoRespuesta);

    void onFacilitadorViewSinFacilitador();

    void onFacilitadorViewSesionInvalida();
}
