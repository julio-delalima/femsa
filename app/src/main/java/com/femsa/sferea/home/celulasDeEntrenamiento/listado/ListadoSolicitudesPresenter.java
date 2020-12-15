package com.femsa.sferea.home.celulasDeEntrenamiento.listado;

import android.os.CountDownTimer;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado.ListaSolicitudesData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

public class ListadoSolicitudesPresenter implements ListadoSolicitudesInteractor.OnInteractorObtenerListadoSolicitudes {

    private ListadoSolicitudesView mView;
    private ListadoSolicitudesInteractor mInteractor;

    public ListadoSolicitudesPresenter(ListadoSolicitudesView view, ListadoSolicitudesInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    public void iniciarProcesoObtenerListadoSolicitudes(){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionObtenerListadoSolicitudes(ListadoSolicitudesPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onInteractorObtenerListadoSolicitudes(ListaSolicitudesData data) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewObtenerListadoSolicitudes(data);
        }
    }

    @Override
    public void onInteractorTokenInvalido() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.EXPIRED_TOKEN);
            mView.onViewSinAutorizacion();
        }
    }

    @Override
    public void onInteractorSinSolicitudes() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewSinSolicitudes();
        }
    }

    @Override
    public void onInteractorSinConexion() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void onInteractorErrorServidor() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void onInteractorTiempoEsperaAgotado() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void onInteractorEliminarSolicitud(int idSolicitud) {
        if (mView!=null){
            mView.onViewMostrarLoader();
            mInteractor.callEliminarSolicitud(SharedPreferencesUtil.getInstance().getTokenUsuario(), idSolicitud, this);
        }
    }

    @Override
    public void onInteractorSolicitudeliminada() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewCelulaEliminada();
        }
    }

    @Override
    public void onInteractorErrorInesperado(int codigoRespuesta) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
        }
    }

    @Override
    public void onInteractorTraerPaises(String token) {
        mInteractor.callListadoPaises(token, this);
    }

    @Override
    public void onInteractorDesplegarPaises(PaisesResponseData data) {
        mView.onViewDesplegarlistadoPaises(data);
    }
}
