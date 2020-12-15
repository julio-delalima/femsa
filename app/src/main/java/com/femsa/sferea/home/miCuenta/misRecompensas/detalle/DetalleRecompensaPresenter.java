package com.femsa.sferea.home.miCuenta.misRecompensas.detalle;


import android.os.CountDownTimer;

import com.femsa.requestmanager.DTO.User.MisRecompensas.detalle.DetalleRecompensaData;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;

/**
 * <p>Clase que permite mofificar la vista con base en las respuestas de una petición.
 * Peticiones que lleva a cabo:
 * -Detalle de recompensa
 * -Canjear una recompensa
 * </p>
 */
public class DetalleRecompensaPresenter implements DetalleRecompensaInteractor.OnInteractorObtenerDetalleRecompensa {

    private DetalleRecompensaView mView;
    private DetalleRecompensaInteractor mInteractor;

    public DetalleRecompensaPresenter(DetalleRecompensaView view, DetalleRecompensaInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    /**
     * <p>Método que inicia el proceso para obtener el detalle de una recompensa.</p>
     * @param idRecompensa Id de la recompensa de la que se quiere obtener la información.
     */
    public void iniciarProcesoObtenerDetalleRecompensa(int idRecompensa){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionObtenerDetalleRecompensa(idRecompensa, DetalleRecompensaPresenter.this);
                }
            };
            countDownTimer.start();
        }
    }

    /**
     * <p>Método que inicia el proceso para canjear una recompensa.</p>
     * @param idRecompensa Id de la recompensa que se quiere canjear.
     */
    public void iniciarProcesoCanjearRecompensa(int idRecompensa){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) { }

                @Override
                public void onFinish() {
                    mInteractor.peticionObtenerRecompensa(idRecompensa, DetalleRecompensaPresenter.this);
                }
            }; countDownTimer.start();
        }
    }

    @Override
    public void onInteractorObtenerDetalleRecompensa(DetalleRecompensaData data) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewObtenerDetalleRecompensa(data);
        }
    }

    @Override
    public void onInteractorRecompensaObtenida() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewRecompensaObtenida();
        }
    }

    @Override
    public void onInteractorRecompensaIncanjeable() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewRecompensaIncanjeable();
        }
    }

    @Override
    public void onInteractorInfuficiente() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewRecompensaInalcanzable();
        }
    }

    @Override
    public void onInteractorAgotado() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewRecompensaAgotada();
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
    public void onInteractorTiempoEsperaAgotado() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(StringManager.TIMEOUT);
        }
    }

    @Override
    public void onInteractorMensajeInesperado(int codigo) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.UNEXPECTED_ERROR, codigo);
        }
    }
}
