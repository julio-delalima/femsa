package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres;

import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;

public interface PaisesView {

    void onViewShowLoader();

    void onViewHideLoader();

    void onViewMuestraListadoPaises(PaisesResponseData data);

    void onViewMuestraDetallePais(DetallePaisesResponseData data);

    void onViewMuestraMensaje(int tipo, int codigo);

    void onViewNoAuth();

    void onViewMuestraMensaje(int tipo);
}
