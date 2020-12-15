package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import android.os.CountDownTimer;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.FechaDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.MostrarHorarioData;
import com.femsa.sferea.Utilities.StringManager;

import java.util.ArrayList;

public class ConfirmarHorarioPresenter implements ConfirmarHorarioInteractor.OnInteractorProgramarSolicitud {

    private ConfirmarHorarioView mView;
    private ConfirmarHorarioInteractor mInteractor;

    public ConfirmarHorarioPresenter(ConfirmarHorarioView view, ConfirmarHorarioInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    /**
     * <p>Método que inicia el proceso para obtener la información del horario de la solicitud.</p>
     * @param idSolicitud Id de la solicitud de la que se quiere obtener el horario.
     * @param idFacilitador Id del facilitador que va a programar la solicitud.
     */
    public void iniciarProcesoMostrarHorario(int idSolicitud, int idFacilitador){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionMostrarHorario(idSolicitud, idFacilitador, ConfirmarHorarioPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    public void iniciarProcesoProgramarSolicitud(int idFacilitador, int idSolicitud, ArrayList<FechaDTO> list){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionProgramarSolicitud(idFacilitador, idSolicitud, list, ConfirmarHorarioPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onViewMostrarHorario(MostrarHorarioData data) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarHorario(data);
        }
    }

    @Override
    public void onInteractorSolicitudProgramada() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewSolicitudProgramada();
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
