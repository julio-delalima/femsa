package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres;

import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.sferea.Utilities.DialogManager;

public class PaisesPresenter implements PaisesInteractor.OnInteractorObtenerPaises {

    private PaisesView mView;
    private PaisesInteractor mPaisesInteractor;

    public PaisesPresenter(PaisesView view, PaisesInteractor paisesInteractor) {
        mView = view;
        mPaisesInteractor = paisesInteractor;
    }

    public void onDestroy(){
        mView = null;
    }

    @Override
    public void onInteractorCallPaises(String token) {
        if (mView!=null){
            mView.onViewShowLoader();
            mPaisesInteractor.CallPaises(token, this);
        }
    }

    @Override
    public void onInteractorCallDetallePais(String token, int idPais) {
        if(mView != null)
            {
                mPaisesInteractor.CallDetallePais(token, idPais, this);
            }
    }

    @Override
    public void onInteractorObtenerPaises(PaisesResponseData data) {
        if (mView!=null){
            mView.onViewHideLoader();
            mView.onViewMuestraListadoPaises(data);
        }
    }

    @Override
    public void onInteractorObtenerDetallePais(DetallePaisesResponseData data) {
        if(mView != null)
            {
                mView.onViewMuestraDetallePais(data);
            }
    }

    @Override
    public void onInteractorTokenInvalido() {
        if (mView!=null){
            mView.onViewHideLoader();
            mView.onViewMuestraMensaje(DialogManager.EXPIRED_TOKEN);
        }
    }

    @Override
    public void onInteractorSinConexion() {
        if (mView!=null){
            mView.onViewHideLoader();
            mView.onViewMuestraMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void onInteractorErrorServidor() {
        if (mView!=null){
            mView.onViewHideLoader();
            mView.onViewMuestraMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void onInteractorTiempoEsperaAgotado() {
        if (mView!=null){
            mView.onViewHideLoader();
            mView.onViewMuestraMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void onInteractorMensajeInesperado(int tipo, int codigoRespuesta) {
        if (mView!=null){
            mView.onViewHideLoader();
            mView.onViewMuestraMensaje(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
        }
    }

    @Override
    public void onInteractorSinPaises() {
        if (mView!=null){}
    }
}
