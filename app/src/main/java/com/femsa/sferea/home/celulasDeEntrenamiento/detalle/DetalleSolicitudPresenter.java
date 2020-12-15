package com.femsa.sferea.home.celulasDeEntrenamiento.detalle;

import android.os.CountDownTimer;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.sferea.Utilities.StringManager;

import java.util.logging.ErrorManager;

public class DetalleSolicitudPresenter implements DetalleSolicitudInteractor.OnInteractorObtenerDetalleSolicitud {

    private DetalleSolicitudView mView;
    private DetalleSolicitudInteractor mInteractor;

    public DetalleSolicitudPresenter(DetalleSolicitudView view, DetalleSolicitudInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    public void iniciarProcesoObtenerDetalleSolicitud(int idSolicitud){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) {
                    //No se implementa
                }

                @Override
                public void onFinish() {
                    mInteractor.peticionObtenerDetalleSolicitud(idSolicitud, DetalleSolicitudPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onInteractorObtenerDetalleSolicitud(DetalleSolicitudData data) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewObtenerDetalleSolicitud(data);
        }
    }

    @Override
    public void onInteractorTokenInvalido() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(StringManager.EXPIRED_TOKEN);
            mView.onViewTokenInvalido();
        }
    }

    @Override
    public void onInteractorSinConexion() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(StringManager.NO_INTERNET);
        }
    }

    @Override
    public void onInteractorErrorServidor() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(StringManager.SERVER_ERROR);
        }
    }

    @Override
    public void onInteractorTiempoEsperaAgotado() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(StringManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorMuestraMensaje(int mensaje, int codigo) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(StringManager.UNEXPECTED_ERROR, codigo);
        }
    }

    @Override
    public void OnInteractorSinPaisData() {
        if (mView!=null){
            mView.onViewMostrarMensaje(StringManager.UNEXPECTED_ERROR, RequestManager.CODIGO_SERVIDOR.NO_CONTENT);
        }
    }

    @Override
    public void onInteractorDetallePais(DetallePaisesResponseData data) {
        if (mView!=null){
            mView.onViewMostrarDetallePais(data);
        }
    }

    @Override
    public void onInteractorTraerPaises(String token) {
        if (mView!=null) {
            mInteractor.callListadoPaises(token, this);
        }
    }

    @Override
    public void onInteractorErrorInesperado(int codigo) {
        if (mView!=null){
        mView.onViewMostrarMensaje(StringManager.UNEXPECTED_ERROR, codigo);}
    }

    @Override
    public void onInteractorDesplegarPaises(PaisesResponseData data) {
        if (mView!=null){
        mView.onViewDesplegarlistadoPaises(data);}
    }
}
