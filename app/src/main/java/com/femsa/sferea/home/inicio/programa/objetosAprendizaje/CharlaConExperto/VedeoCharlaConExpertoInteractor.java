package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.CharlaConExperto;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.VedeoCharlaConExperto.InsertarVedeoCharlaConVedeoExpertoRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.VedeoCharlaConExperto.VedeoCharlaConExpertoRequest;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.VedeoCharlaConExpertoResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.StringManager;

public class VedeoCharlaConExpertoInteractor {

    private OnVedeoCharlaConExpertoInteractorListener mListener;

    public interface OnVedeoCharlaConExpertoInteractorListener
    {
        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorTraeVedeoCharlaConExperto(int idObjeto, String token);

        void OnInteractorNoAuth();

        void OnInteractorMensaje(int mensaje);

        void OnInteractorMensaje(int mensaje, int codigo);

        void OnInteractorInsertarVedeoCharla(int idObjeto, int idHora, String token);

        void OnInteractorVedeocharlaInsertadaBuenisimo();

        void OnInteractorVedeoCharlaConExperto(VedeoCharlaConExpertoResponseData data);

        void OnInteractorFechaInapartable();
    }

    public void CallVedeoCharlaConExperto(int idObjeto, String token, OnVedeoCharlaConExpertoInteractorListener listener)
    {
        mListener = listener;
        VedeoCharlaConExpertoRequest request = new VedeoCharlaConExpertoRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idObjeto, new VedeoCharlaConExpertoRequest.VedeoCharlaConExpertoResponseContract() {

            @Override
            public void OnVedeoCharlaConExpertoSuccess(VedeoCharlaConExpertoResponseData data) {
                mListener.OnInteractorVedeoCharlaConExperto(data);
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

    public void InsertarVedeoCharlaConExperto(int idObjeto, int idRespuesta, String token, OnVedeoCharlaConExpertoInteractorListener listener) {
        mListener = listener;
        InsertarVedeoCharlaConVedeoExpertoRequest request = new InsertarVedeoCharlaConVedeoExpertoRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idObjeto, idRespuesta, new InsertarVedeoCharlaConVedeoExpertoRequest.VedeoCharlaConExpertoResponseContract() {
            @Override
            public void OnInsertarSuccess() {
                mListener.OnInteractorVedeocharlaInsertadaBuenisimo();
            }

            @Override
            public void OnFechaInapartableNunca() {
                mListener.OnInteractorFechaInapartable();
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
