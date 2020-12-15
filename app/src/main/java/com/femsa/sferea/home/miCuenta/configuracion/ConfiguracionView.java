package com.femsa.sferea.home.miCuenta.configuracion;

import com.femsa.requestmanager.Response.Configuracion.IdiomaResponseData;

public interface ConfiguracionView {

    void OnConfiguracionViewNoAuth();

    void OnConfiguracionViewMostrarMensaje(int tipoMensaje);

    void OnConfiguracionViewMostrarMensaje(int tipoMensaje, int codigoError);

    //void OnConfiguracionViewConfigurarDatos();

    void OnConfiguracionViewIdiomaCambiado();

    void OnConfiguracionViewNotificacionesCambiadas();

    void OnConfiguracionViewDescargaCambiada();

    void OnConfiguracionViewShowLoader();

    void OnConfiguracionViewHideLoader();

    void OnConfiguracionViewCargarIdiomas(IdiomaResponseData data);

    void setTokenDefinido();
}
