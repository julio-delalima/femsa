package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.VideoInteractivoResponseData;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.DialogManager;

public class VideoInteractivoPresenter implements VideoInteractivoInteractor.OnInteractorListener{

    private VideoInteractivoView mView;

    private VideoInteractivoInteractor mInteractor;

    public VideoInteractivoPresenter(VideoInteractivoView view, VideoInteractivoInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void OnInteractorServerError() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.muestraMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.muestraMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.muestraMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.muestraMensaje(DialogManager.EXPIRED_TOKEN);
        }
    }

    @Override
    public void OnInteractorCallVideoInteractivo(int idObject) {
        if(mView != null)
        {
            mView.showLoader();
            mInteractor.callVideoInteractivo(idObject, this);
        }
    }

    @Override
    public void OnInteractorGetVideoInteractivo(VideoInteractivoResponseData data) {
        if(mView != null)
        {
            mView.hideLoader();
            mView.videoInteractivoSuccess(data);
        }
    }

    @Override
    public void OnInteractorErrorInesperado(int codigoRespuesta) {
        if(mView != null)
            {
                mView.hideLoader();
                mView.muestraMensajeInesperado(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
            }
    }

    @Override
    public void OnInteractorCallBonus(int idObjeto, String token) {
        if(mView != null)
            {
                mInteractor.CallBonus(idObjeto, token, this);
            }
    }

    @Override
    public void OnInteractorBonusSuccess() {
        LogManager.d("Bonusito", "Bonus añadido");
    }

    public void onDestroy()
    {
        mView = null;
    }
}
