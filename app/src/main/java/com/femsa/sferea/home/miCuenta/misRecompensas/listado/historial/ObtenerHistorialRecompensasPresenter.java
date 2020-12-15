package com.femsa.sferea.home.miCuenta.misRecompensas.listado.historial;

import android.os.CountDownTimer;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoHistorialData;
import com.femsa.sferea.Utilities.StringManager;

public class ObtenerHistorialRecompensasPresenter implements ObtenerHistorialRecompensasInteractor.OnInteractorObtenerHistorialRecompensas {

    private ObtenerHistorialRecompensasInteractor mInteractor;
    private ObtenerHistorialRecompensasView mView;

    public ObtenerHistorialRecompensasPresenter(ObtenerHistorialRecompensasView view, ObtenerHistorialRecompensasInteractor interactor){
        mInteractor = interactor;
        mView = view;
    }

    public void onDestroy(){
        mView = null;
    }

    public void iniciarProcesoObtenerHistorialRecompensas(){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionObtenerHistorialRecompensas(ObtenerHistorialRecompensasPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onInteractorObtenerHistorialRecompensas(ObtenerListadoHistorialData data) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewObtenerHistorialRecompensas(data);
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
    public void onInteractorSinRecompensas() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewSinRecompensas();
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
}
