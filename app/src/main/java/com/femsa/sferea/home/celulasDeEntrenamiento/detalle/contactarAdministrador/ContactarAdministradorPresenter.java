package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.contactarAdministrador;

import android.os.CountDownTimer;

import com.femsa.sferea.Utilities.StringManager;

public class ContactarAdministradorPresenter implements ContactarAdministradorInteractor.OnInteractorContactarAdministrador {

    private ContactarAdministradorView mView;
    private ContactarAdministradorInteractor mInteractor;

    public ContactarAdministradorPresenter(ContactarAdministradorView view, ContactarAdministradorInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    public void iniciarProcesoContactarAdministrador(String mensaje, int idSolicitud, int tipomensaje){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionContactarAdministrador(mensaje, idSolicitud, tipomensaje, ContactarAdministradorPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onInteractorMensajeEnviado() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMensajeEnviado();
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
    public void onInteractorTiempoEsperaAgotado() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(StringManager.TIMEOUT);
        }
    }

    @Override
    public void onInteractorErrorServidor() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(StringManager.SERVER_ERROR);
        }
    }
}
