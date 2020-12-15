package com.femsa.sferea.home.miCuenta.misRecompensas.listado.recompensas;

import android.os.CountDownTimer;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoRecompensasData;
import com.femsa.sferea.Utilities.StringManager;

public class ObtenerListadoRecompensasPresenter implements ObtenerListadoRecompensasInteractor.OnInteractorObtenerListadoRecompensas {

    private ObtenerListadoRecompensasView mView;
    private ObtenerListadoRecompensasInteractor mInteractor;

    public ObtenerListadoRecompensasPresenter(ObtenerListadoRecompensasView view, ObtenerListadoRecompensasInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    /**
     * <p>Método que inicia la petición para obtener el listado recompensas.</p>
     */
    public void iniciarProcesoObtenerListadoRecompensas(){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionObtenerListadoRecompensas(ObtenerListadoRecompensasPresenter.this);
                }
            };
            countDownTimer.start();
        }
    }

    @Override
    public void onInteractorObtenerListadoRecompensas(ObtenerListadoRecompensasData data) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewObtenerListadoRecompensas(data);
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
