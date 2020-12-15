package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Encuesta;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Encuesta.AddEncuestaRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Encuesta.TraeEncuestaRequest;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.EncuestaResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.DialogManager;

import java.util.ArrayList;

public class EncuestaInteractor {

    private OnEncuestaInteractorListener mListener;

    public interface OnEncuestaInteractorListener
    {
        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorNoAuth();

        void OnInteractorLlamarEncuesta(int idObject);

        void OnInteractorMuestraMensaje(int tipoMensaje, int codigoRespuesta);

        void OnInteractorMuestraEncuesta(EncuestaResponseData data);

        void OnInteractorAddEncuesta(int idObjeto, ArrayList<String> idPreguntas, ArrayList<String> idRespuestas, int aprobado);

        void OnInteractorEncuestaSuccess();
    }

    public void callEncuesta(int idObjeto, String token, OnEncuestaInteractorListener listener)
    {
        mListener = listener;
        TraeEncuestaRequest request = new TraeEncuestaRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idObjeto, new TraeEncuestaRequest.EncuestaResponseContract() {
            @Override
            public void OnEncuestaSuccess(EncuestaResponseData data) {
                mListener.OnInteractorMuestraEncuesta(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnUnexpectedError(int responseCode) {
                mListener.OnInteractorMuestraMensaje(DialogManager.UNEXPECTED_ERROR, responseCode);
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

    public void insertEncuesta(int idObjeto, String token, ArrayList<String> idRespuestas, ArrayList<String> idPreguntas, int aprobado, OnEncuestaInteractorListener listener)
    {
        mListener = listener;
        AddEncuestaRequest request = new AddEncuestaRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idObjeto, idRespuestas, idPreguntas, new AddEncuestaRequest.EncuestaResponseContract() {
            @Override
            public void OnCheckSuccess() {
                mListener.OnInteractorEncuestaSuccess();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnUnexpectedError(int responseCode) {
                mListener.OnInteractorMuestraMensaje(DialogManager.UNEXPECTED_ERROR, responseCode);
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
}
