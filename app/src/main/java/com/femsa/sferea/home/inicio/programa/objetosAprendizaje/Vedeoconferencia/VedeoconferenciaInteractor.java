package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Vedeoconferencia;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.VedeoConferencia.VedeoconferenciaRequest;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.VedeoconferenciaResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.StringManager;

public class VedeoconferenciaInteractor {

    private OnVedeoconferenciaInteractorListener mListener;

    public interface OnVedeoconferenciaInteractorListener
    {
        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorTraeVedeoconferencia(int idObjeto, String token);

        void OnInteractorNoAuth();

        void OnInteractorMensaje(int mensaje);

        void OnInteractorMensaje(int mensaje, int codigo);

        void OnInteractorVedeoconferencia(VedeoconferenciaResponseData data);
    }

    public void CallVedeoconferencia(int idObjeto, String token, OnVedeoconferenciaInteractorListener listener)
        {
            mListener = listener;
            VedeoconferenciaRequest request = new VedeoconferenciaRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(token, idObjeto, new VedeoconferenciaRequest.VedeoconferenciaResponseContract() {
                @Override
                public void OnVedeoconferenciaoSuccess(VedeoconferenciaResponseData data) {
                    mListener.OnInteractorVedeoconferencia(data);
                }

                @Override
                public void OnNoAuth() {
                    mListener.OnInteractorNoAuth();
                }

                @Override
                public void OnUnexpectedError(int responseCode) {
                    mListener.OnInteractorMensaje(StringManager.UNEXPECTED_ERROR, responseCode);
                }

                @Override
                public void onResponseErrorServidor() {
                    mListener.OnInteractorMensaje(StringManager.SERVER_ERROR);
                }

                @Override
                public void onResponseSinConexion() {
                    mListener.OnInteractorMensaje(StringManager.NO_INTERNET);
                }

                @Override
                public void onResponseTiempoAgotado() {
                    mListener.OnInteractorMensaje(StringManager.TIMEOUT);
                }
            });
        }
}
