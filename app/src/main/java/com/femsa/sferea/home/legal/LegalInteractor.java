package com.femsa.sferea.home.legal;

import com.femsa.requestmanager.Request.Legal.PrivacyAdviceRequest;
import com.femsa.requestmanager.Request.Legal.PrivacyNoTokenRequest;
import com.femsa.requestmanager.Request.Legal.TermsNoTokenRequest;
import com.femsa.requestmanager.Response.Legal.PrivacyAdviceResponseData;
import com.femsa.requestmanager.Response.Legal.TermsAndConditionsResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

import java.util.Locale;

public class LegalInteractor {

    private OnLegalInteractorListener mListener;

    public interface OnLegalInteractorListener
    {
        void OnInteractorGetTerms(String language);

        void OnInteractorSuccessTerms(TermsAndConditionsResponseData data);

        void OnInteractorGetPrivacyAdvice(String language);

        void OnInteractorSuccessAdvice(PrivacyAdviceResponseData data);

        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorRequestFailure();

        void OnInteractorNoAuth();

        void OnInteractorUnexpectedError(int codigoRespuesta);

        void OnInteractorNoContent();
    }

    public void getTermsAndConditions(String language, OnLegalInteractorListener listener)
    {
        mListener = listener;
        if(!language.equals("en") && !language.equals("es") && !language.equals("pt")){
            language = "en";
        }
        TermsNoTokenRequest request  = new TermsNoTokenRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(language, new TermsNoTokenRequest.TermsResponseContract() {
            @Override
            public void onTermsSuccesful(TermsAndConditionsResponseData termsResponse) {
                mListener.OnInteractorSuccessTerms(termsResponse);
            }

            @Override
            public void onTermsFailed() {
                mListener.OnInteractorRequestFailure();
            }

            @Override
            public void onNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnNoContent() {
                mListener.OnInteractorNoContent();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
                mListener.OnInteractorUnexpectedError(codigoRespuesta);
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }
        });
    }

    public void getPrivacyAdvice(String language, OnLegalInteractorListener listener)
    {
        mListener = listener;

        PrivacyNoTokenRequest request = new PrivacyNoTokenRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(language, new PrivacyNoTokenRequest.ResponseContract() {
            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }

            @Override
            public void onPrivacySuccess(PrivacyAdviceResponseData data) {
                mListener.OnInteractorSuccessAdvice(data);
            }

            @Override
            public void onPrivacyFailure() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void noAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnNoContent() {
                mListener.OnInteractorNoContent();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
                mListener.OnInteractorUnexpectedError(codigoRespuesta);
            }
        });
    }
}
