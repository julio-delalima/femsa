package com.femsa.sferea.home.inicio.programa.objetosAprendizaje;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

import java.io.InputStream;

public class LearningObjectsPresenter implements LearningObjectInteractor.OnObjectInteractorListener{

    private LearningObjectView mView;

    private LearningObjectInteractor mInteractor;

    public LearningObjectsPresenter(LearningObjectView view, LearningObjectInteractor interactor)
    {
        mInteractor = interactor;

        mView = view;
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
            mView.OnNoInternet();
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
    public void OnInteractorCallObjectDetail(int idObject) {
        if(mView != null)
            {
                mView.showLoader();
                mInteractor.OnCallObjectDetail(idObject, this);
            }
    }

    @Override
    public void OnInteractorObjectSuccess(ObjectDetailResponseData data) {
        if(mView != null)
        {
            mView.hideLoader();
            mView.getObjectDetailSuccess(data);
        }
    }

    @Override
    public void OnInteractorSuccessLike() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.likeSuccess();
        }
    }

    @Override
    public void OnInteractorErrorInesperado(int codigoRespuesta) {
        if(mView != null)
            {
                mView.hideLoader();
                mView.OnMuestraMensajeInesperado(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
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
        if(mView != null)
            {
                mView.OnBonusSuccess();
            }
    }

    @Override
    public void OnInteractorMarkAsRead(int idObjeto, String token) {
        if(mView != null)
            {
                mView.showLoader();
                mInteractor.callMarkAsRead(idObjeto, token, this);
            }
    }

    @Override
    public void OnInteractorMarkAsReadExitoso() {
        if(mView != null)
            {
                mView.hideLoader();
                mView.OnMarkAsReadExitoso();
            }
    }

    /**
     * Llamada que hace la petición para dar like a un objeto
     *
     * @param idObject
     */
    @Override
    public void OnInteractorLike(int idObject) {
        if(mView != null)
        {
            mView.showLoader();
            mInteractor.OnLikeProgram(idObject, SharedPreferencesUtil.getInstance().getTokenUsuario(), this);
        }
    }

    @Override
    public void OnInteractorCallJuego(int idObjeto) {
        if(mView != null){
            mView.showLoader();
            mInteractor.callJuego(idObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario(), this);
        }
    }

    @Override
    public void OnInteractorJuegoRecibido(InputStream zip, int buffer) {
        if(mView != null){
            mView.OnJuegoListo(zip, buffer);
            mView.hideLoader();
        }
    }

    public void onDestroy()
    {
        mView = null;
    }
}
