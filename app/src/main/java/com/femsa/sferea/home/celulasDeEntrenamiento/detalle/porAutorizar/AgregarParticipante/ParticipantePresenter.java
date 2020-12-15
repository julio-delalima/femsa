package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.AgregarParticipante;

import android.os.CountDownTimer;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteData;
import com.femsa.sferea.Utilities.DialogManager;

import java.util.ArrayList;

public class ParticipantePresenter implements ParticipanteInteractor.OnInteractorObtenerParticipante{

    private ParticipanteView mView;
    private ParticipanteInteractor mInteractor;

    public ParticipantePresenter(ParticipanteView view, ParticipanteInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    public void iniciarPeticionObtenerParticipante(String token, int numUsusrio){
        if(mView != null){
            mView.OnViewShowLoader();
            mInteractor.peticionObtenerParticipante(token, numUsusrio, ParticipantePresenter.this);
        }
    }

    public void iniciarProcesoEliminarParticipantes(String idSolicitud, ArrayList<Integer> participantes,
                                                    boolean accion){
        if (mView!=null){
            mView.OnViewShowLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionEliminarParticipantes(idSolicitud, participantes, accion, ParticipantePresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onInteractorObtenerParticipnates(ParticipanteData data) {
        if(mView != null){
            mView.OnViewHideLoader();
            mView.OnViewMostrarParticipante(data);
        }
    }

    @Override
    public void onInteractorTokenInvalido() {
        if(mView != null){
            mView.OnViewHideLoader();
            mView.OnViewMostrarMensaje(DialogManager.EXPIRED_TOKEN);
            mView.OnViewSesionInvalida();
        }
    }

    @Override
    public void onInteractorSinConexion() {
        if(mView != null){
            mView.OnViewHideLoader();
            mView.OnViewMostrarMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void onInteractorErrorServidor() {
        if (mView != null){
            mView.OnViewHideLoader();
            mView.OnViewParticipanteInexistente();//OnViewMostrarMensaje(SnackbarManager.SERVER_ERROR);
        }
    }

    @Override
    public void onInteractorTiempoEsperaAgotado() {
        if(mView != null){
            mView.OnViewHideLoader();
            mView.OnViewMostrarMensaje(DialogManager.TIMEOUT);
        }

    }

    @Override
    public void onInteractorMensajeInesperado(int codigoRespuesta) {
        mView.OnViewMostrarMensajeInesperado(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
    }

    @Override
    public void onInteractorParticipanteEliminado() {
        if(mView != null){
            mView.OnViewHideLoader();
            mView.OnViewParticipanteEliminado();
        }
    }
}
