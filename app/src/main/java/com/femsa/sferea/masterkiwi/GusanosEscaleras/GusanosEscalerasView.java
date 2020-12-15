package com.femsa.sferea.masterkiwi.GusanosEscaleras;

public interface GusanosEscalerasView {

    void onDatosGEenviados();

    void onMuestraMensaje(int error);

    void onMuestraMensaje(int error, int codigo);

    void onNoAuth();

    void onCargaLoader();

    void onOcultaLoader();

}
