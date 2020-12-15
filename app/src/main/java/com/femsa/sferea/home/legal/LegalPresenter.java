package com.femsa.sferea.home.legal;

import com.femsa.requestmanager.Response.Legal.PrivacyAdviceResponseData;
import com.femsa.requestmanager.Response.Legal.TermsAndConditionsResponseData;
import com.femsa.sferea.Utilities.DialogManager;

public class LegalPresenter implements  LegalInteractor.OnLegalInteractorListener {

    private LegalView mView;

    private LegalInteractor mListener;

    public LegalPresenter(LegalView view, LegalInteractor listener)
        {
            mView = view;
            mListener = listener;
        }

    public void onDestroy()
        {
            mView = null;
        }

    @Override
    public void OnInteractorGetTerms(String language) {
        if(mView != null)
            {
                mView.onShowLoader();
                mListener.getTermsAndConditions(language, this);
            }
    }

    @Override
    public void OnInteractorSuccessTerms(TermsAndConditionsResponseData data) {
        if(mView != null)
            {
                mView.onHideLoader();
                mView.onShowTermsAndConditions(data);
            }
    }

    @Override
    public void OnInteractorGetPrivacyAdvice(String language) {
        if(mView != null)
        {
            mView.onShowLoader();
            mListener.getPrivacyAdvice(language, this);
        }
    }

    @Override
    public void OnInteractorSuccessAdvice(PrivacyAdviceResponseData data) {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onShowPrivacyAdvice(data);
        }
    }

    @Override
    public void OnInteractorServerError() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onMostrarMensage(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onMostrarMensage(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onMostrarMensage(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void OnInteractorRequestFailure() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onMostrarMensage(DialogManager.UNEXPECTED_ERROR);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onBackToLogin();
            mView.onMostrarMensage(DialogManager.EXPIRED_TOKEN);
        }
    }

    @Override
    public void OnInteractorUnexpectedError(int codigoRespuesta) {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onMostrarMensageInesperado(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
        }
    }

    @Override
    public void OnInteractorNoContent() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onMostrarMensage(DialogManager.EMPTY);
        }
    }
}
