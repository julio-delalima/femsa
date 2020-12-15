package com.femsa.sferea.home.celulasDeEntrenamiento.detalle;

import android.os.CountDownTimer;

import com.femsa.sferea.Utilities.StringManager;

import java.util.ArrayList;

public class EditarParticipantesPresenter implements EditarParticipantesInteractor.OnInteractorEditarParticipantes{

    private EditarParticipantesView mView;
    private EditarParticipantesInteractor mInteractor;

    public EditarParticipantesPresenter(EditarParticipantesView view, EditarParticipantesInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    public void iniciarProcesoAgregarParticipantes(String idSolicitud, ArrayList<Integer> participantes,
                                                  boolean accion){
        /*if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionAgregarParticipantes(idSolicitud, participantes, accion, EditarParticipantesPresenter.this);
                }
            }; countDownTimer.start();
        }*/
        CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                mInteractor.peticionAgregarParticipantes(idSolicitud, participantes, accion, EditarParticipantesPresenter.this);
            }
        }; countDownTimer.start();

    }

    public void iniciarProcesoEliminarParticipantes(String idSolicitud, ArrayList<Integer> participantes,
                                                   boolean accion){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionEliminarParticipantes(idSolicitud, participantes, accion, EditarParticipantesPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onInteractorParticipanteAgregado() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewParticipanteAgregado();
        }
    }

    @Override
    public void onInteractorParticipanteEliminado() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewParticipanteEliminado();
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

}
