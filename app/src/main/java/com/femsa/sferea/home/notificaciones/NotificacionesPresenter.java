package com.femsa.sferea.home.notificaciones;

import com.femsa.requestmanager.Response.NotificacionesResponse.NotificacionesResponseData;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

public class NotificacionesPresenter implements NotificacionesInteractor.onNotificacionesInteractor{

    private NotificacionesView mView;

    private NotificacionesInteractor mInteractor;

    public NotificacionesPresenter(NotificacionesView view, NotificacionesInteractor interactor)
    {
        mView = view;
        mInteractor = interactor;
    }


    @Override
    public void onInteractorSinNotificaciones() {
        if(mView != null)
            {
                mView.OnViewHideLoader();
                mView.OnViewSinNotificaciones();
            }
    }

    @Override
    public void onInteractorTraerNotificaciones() {
        if(mView != null)
            {
                mView.OnViewShowLoader();
                mInteractor.llamarNotificaciones(SharedPreferencesUtil.getInstance().getTokenUsuario(), this);
            }
    }

    @Override
    public void onInteractorMostrarNotificaciones(NotificacionesResponseData data) {
        if(mView != null)
        {
            mView.OnViewHideLoader();
            mView.OnViewMostrarNotificaciones(data);
        }
    }

    @Override
    public void onInteractorMarcarImportante(int idNotificacion) {
        if(mView != null)
        {
            mInteractor.MarcarImportanteNotificacion(SharedPreferencesUtil.getInstance().getTokenUsuario(), idNotificacion, this);
        }
    }

    @Override
    public void onInteractorNotificacionMarcada() {
        if(mView != null)
        {
            mView.OnViewNotificacionImportante();
        }
    }

    @Override
    public void onInteractorMarcarVista(int idNotificacion) {
        if(mView != null)
        {
            mInteractor.marcarNotificacionVista(SharedPreferencesUtil.getInstance().getTokenUsuario(), idNotificacion, this);
        }
    }

    @Override
    public void onInteractorNotificacionVista() {
        if(mView != null)
        {
        }
    }

    @Override
    public void onInteractorEliminarNotificacion(int idNotificacion) {
        if(mView != null)
        {
            mView.OnViewShowLoader();
            mInteractor.eliminarNotificacion(SharedPreferencesUtil.getInstance().getTokenUsuario(), idNotificacion, this);
        }
    }

    @Override
    public void onInteractorNotificacionEliminada() {
        if(mView != null)
        {
            mView.OnViewHideLoader();
            mView.OnViewNotificacionEliminada();
        }
    }

    @Override
    public void OnInteractorServerError() {
        if(mView != null)
        {
            mView.OnViewHideLoader();
            mView.OnViewMostrarMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
        {
            mView.OnViewHideLoader();
            mView.OnViewMostrarMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
        {
            mView.OnViewMostrarMensaje(DialogManager.NO_INTERNET);
            mView.OnViewHideLoader();
            mView.OnViewSinNotificaciones();
        }
    }

    @Override
    public void OnInteractorUnexpectedError(int codigoRespuesta) {
        if(mView != null)
        {
            mView.OnViewHideLoader();
            mView.OnViewMostrarMensajeInesperado(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mView != null)
        {
            mView.OnViewHideLoader();
            mView.OnViewMostrarMensaje(DialogManager.EXPIRED_TOKEN);
        }
    }

    public void onDestroy()
        {
            mView = null;
        }
}
