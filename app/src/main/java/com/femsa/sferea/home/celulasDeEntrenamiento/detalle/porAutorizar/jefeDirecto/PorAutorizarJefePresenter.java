package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.jefeDirecto;

import android.os.CountDownTimer;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.autorizador.ParticipanteDTO;
import com.femsa.sferea.Utilities.StringManager;

import java.util.ArrayList;

public class PorAutorizarJefePresenter implements PorAutorizarJefeInteractor.OnInteractorObtenerAutorizacionJefe {

    private PorAutorizarJefeView mView;
    private PorAutorizarJefeInteractor mInteractor;

    public PorAutorizarJefePresenter(PorAutorizarJefeView view, PorAutorizarJefeInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    /**
     * <p>Método que permite iniciar el proceso para obtener la autorización de un jefe.</p>
     * @param idSolicitud Id de la solicitud que se quiere autorizar.
     */
    public void iniciarProcesoObtenerAutorizacionJefe(int idSolicitud, int idEmpleado, ArrayList<ParticipanteDTO> list){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionObtenerAutorizacionJefe(idSolicitud, idEmpleado, list, PorAutorizarJefePresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onInteractorAutorizacionConcedida() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewAutorizacionConcedida();
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
