package com.femsa.sferea.home.miCuenta.miActividad;

import android.os.CountDownTimer;

import com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros.ObtenerAllLogrosData;
import com.femsa.requestmanager.Response.MiActividad.MiActividadResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.sferea.Utilities.DialogManager;

public class MiActividadPresenter implements MiActividadInteractor.OnInteractorObtenerAllLogros {

    private MiActividadView mView;
    private MiActividadInteractor mInteractor;

    public MiActividadPresenter(MiActividadView view, MiActividadInteractor interactor){
        mView = view;
        mInteractor = interactor;
    }

    public void onDestroy(){
        mView = null;
    }

    public void iniciarProcesoObtenerAllLogros(){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //No se implementa
                }

                @Override
                public void onFinish() {
                    mInteractor.peticionObtenerAllLogros(MiActividadPresenter.this);
                }
            };
            countDownTimer.start();
        }
    }

    public void iniciarProcesoObtenerDatosRanking(){
        mInteractor.peticionObtenerDatosActividad(MiActividadPresenter.this);
    }

    public void iniciarProcesoObtenerProgramasRanking(){
        if (mView!=null){
            mView.onViewMostrarLoader();
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //No se implementa
                    }

                    @Override
                    public void onFinish() {
                        mInteractor.peticionObtenerMedidoresProgreso_Y_Programas(MiActividadPresenter.this);
                    }
                };
                countDownTimer.start();
        }
    }

    // RESPUESTAS DE LA PETICIÓN *******************************************************************

    //Respuesta de la petición al Web Service "obtenerAllLogros"
    @Override
    public void onInteractorObtenerAllLogros(ObtenerAllLogrosData datos) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarLogros(datos);
        }
    }

    //Respuesta de la petición al Web Service "obtenerDatosRanking"
    @Override
    public void onInteractorObtenerDatos(RankingResponseData data) {
        if (mView!=null) {
            mView.onViewOcultarLoader();
            mView.onViewMostrarDatos(data);
        }
    }

    //Respuesta de la petición al Web Service "obtenerProgramasRaningMiActividad"
    @Override
    public void onInteractorObtenerProgramasProgreso(ProgramRankingResponseData data) {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMedidoresProgresoYProgramas(data);
        }
    }

    @Override
    public void onInteractorLlamarDatosActividad() {

    }

    @Override
    public void onInteractorLlamarProgresoProgramas() {

    }

    // MÉTODOS QUE MUESTRAN MENSAJES DE LA RESPUESTA DEL SERVIDOR **********************************
    /**Cuando el token del usuario expire se mandará a llamar este método*/
    @Override
    public void onInteractorSesionInvalida() {
        if (mView!=null){
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.EXPIRED_TOKEN);
            mView.onViewNoAuth();
        }
    }

    @Override
    public void onInteractorErrorServidor() {
        if (mView!=null) {
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void onInteractorTiempoEsperaAgotado() {
        if (mView!=null) {
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void onInteractorSinConexion() {
        if (mView!=null) {
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.NO_INTERNET);
            mView.onViewNoInternet();
        }
    }

    @Override
    public void onInteractorMiActividadData(MiActividadResponseData data) {
        if(mView != null)
        {
            mView.onViewOcultarLoader();
            mView.onViewMiActividadData(data);
        }
    }

    @Override
    public void onInteractorMostrarMensaje(int error) {
        if(mView != null)
        {
            mView.onViewOcultarLoader();
            mView.onViewMostrarMensaje(DialogManager.UNEXPECTED_ERROR);
        }
    }

    @Override
    public void onInteractorCallMiActividad() {
        if(mView != null)
        {
            mView.onViewMostrarLoader();
            mInteractor.peticionObtenerMiActividadData(this);
        }
    }

}
