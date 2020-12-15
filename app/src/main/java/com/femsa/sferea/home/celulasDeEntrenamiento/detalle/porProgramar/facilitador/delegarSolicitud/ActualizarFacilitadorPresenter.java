package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.delegarSolicitud;

import android.os.CountDownTimer;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.StringManager;

public class ActualizarFacilitadorPresenter implements ActualizarFacilitadorInteractor.OnInteractorActualizarFacilitador{

    private ActualizarFacilitadorView mView;
    private ActualizarFacilitadorInteractor mInteractor;

    public ActualizarFacilitadorPresenter(ActualizarFacilitadorView view, ActualizarFacilitadorInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    public void iniciarProcesoNoDisponible(String correo, String idSolicitud, String idFacilitador, boolean disponible){
       if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionNoDisponible(correo, idSolicitud, idFacilitador, disponible,ActualizarFacilitadorPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    public void iniciarProcesoNoResponsable(String nombre, String idSolicitud, String idFacilitador, boolean responsable){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionNoResponsable(idSolicitud, idFacilitador, responsable, nombre, ActualizarFacilitadorPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onInteractorFacilitadorActualizado() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewFacilitadorActualizado();
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

    @Override
    public void onInteractorSinFacilitador() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(R.string.no_se_encotnro_mail);
        }
    }
}
