package com.femsa.sferea.home.miCuenta.configuracion;

import com.femsa.requestmanager.Response.Configuracion.IdiomaResponseData;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

public class ConfiguracionPresenter implements ConfiguracionInteractor.OnConfiguracionInteractorListener{

    private ConfiguracionView mConfiguracionView;

    private ConfiguracionInteractor mConfiguracionInteractor;

    public ConfiguracionPresenter(ConfiguracionView view, ConfiguracionInteractor interactor)
        {
            mConfiguracionView = view;

            mConfiguracionInteractor = interactor;
        }

    @Override
    public void OnInteractorDescargasCambiada() {
        if(mConfiguracionView != null)
            {
                mConfiguracionView.OnConfiguracionViewHideLoader();
                mConfiguracionView.OnConfiguracionViewDescargaCambiada();
            }
    }

    @Override
    public void OnInteractorNotificacionesCambiadas() {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewHideLoader();
            mConfiguracionView.OnConfiguracionViewNotificacionesCambiadas();
        }
    }

    @Override
    public void OnInteractorIdiomaCambiado() {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewHideLoader();
            mConfiguracionView.OnConfiguracionViewIdiomaCambiado();
        }
    }

    @Override
    public void OnInteractorCambiarIdioma(int idioma, String token) {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewShowLoader();
            mConfiguracionInteractor.CallCambiarIdioma(idioma, token, this);
        }
    }

    @Override
    public void OnInteractorCambiarDescarga(String token, boolean descarga) {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewShowLoader();
            mConfiguracionInteractor.CallCambiarDescarga(token, descarga, this);
        }
    }

    @Override
    public void OnInteractorCambiarNotificaciones(String token, boolean notificaciones) {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewShowLoader();
            mConfiguracionInteractor.CallCambiarNotificaciones(token, notificaciones, this);
        }
    }

    @Override
    public void OnInteractorSetTokenFirebase(String tokenFirebase, String tipoDispositivo) {
        if(mConfiguracionView != null) {
            mConfiguracionInteractor.callTokenFirebase(tokenFirebase, SharedPreferencesUtil.getInstance().getTokenUsuario(), tipoDispositivo, this);
        }
    }


    @Override
    public void OnInteractorTraeIdiomas(int idIdiomaActual) {
        if(mConfiguracionView != null)
        {
           mConfiguracionView.OnConfiguracionViewShowLoader();
           mConfiguracionInteractor.callTraeIdiomas(idIdiomaActual, this);
        }
    }

    @Override
    public void OnInteractorCargaIdiomas(IdiomaResponseData data) {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewHideLoader();
            mConfiguracionView.OnConfiguracionViewCargarIdiomas(data);
        }
    }

    @Override
    public void OnInteractorServerError() {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewHideLoader();
            mConfiguracionView.OnConfiguracionViewMostrarMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewHideLoader();
            mConfiguracionView.OnConfiguracionViewMostrarMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewHideLoader();
            mConfiguracionView.OnConfiguracionViewMostrarMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void OnInteractorUnexpectedError(int errorCode) {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewHideLoader();
            mConfiguracionView.OnConfiguracionViewMostrarMensaje(DialogManager.UNEXPECTED_ERROR, errorCode);
        }
    }

    @Override
    public void OnInteractorTokenFirebaseDefinido() {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.setTokenDefinido();
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mConfiguracionView != null)
        {
            mConfiguracionView.OnConfiguracionViewHideLoader();
            mConfiguracionView.OnConfiguracionViewMostrarMensaje(DialogManager.EXPIRED_TOKEN);
            mConfiguracionView.OnConfiguracionViewNoAuth();
        }
    }

    public void OnDestroyPresenter()
        {
            mConfiguracionView = null;
            mConfiguracionInteractor = null;
        }


}
