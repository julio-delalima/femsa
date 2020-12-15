package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Evaluacion;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Evaluacion.AddEvaluacionRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Evaluacion.EvaluacionRequest;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.EvaluacionResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.DialogManager;

import java.util.ArrayList;

public class EvaluacionInteractor {

    private OnEvaluacionInteractorListener mListener;

    public interface OnEvaluacionInteractorListener
    {
        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorNoAuth();

        void OnInteractorLlamarEvaluacion(int idObject);

        void OnInteractorMuestraMensaje(int tipoMensaje, int codigoRespuesta);

        void OnInteractorMuestraEvaluacion(EvaluacionResponseData data);

        void OnInteractorAddEvaluacion(int idObjeto, ArrayList<String> idPreguntas, ArrayList<String> idRespuestas, int aprobado, int calif);

        void OnInteractorEvaluacionSuccess();
    }

    public void callEvaluacion(int idObjeto, String token, OnEvaluacionInteractorListener listener)
    {
        mListener = listener;
        EvaluacionRequest request = new EvaluacionRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idObjeto, new EvaluacionRequest.EvaluacionResponseContract() {
            @Override
            public void OnEvaluacionSuccess(EvaluacionResponseData data) {
                mListener.OnInteractorMuestraEvaluacion(data);
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

    public void insertEvaluacion(int idObjeto, String token, ArrayList<String> idRespuestas, ArrayList<String> idPreguntas, int aprobado, int calif, OnEvaluacionInteractorListener listener)
    {
        mListener = listener;
        AddEvaluacionRequest request = new AddEvaluacionRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idObjeto, idRespuestas, idPreguntas, aprobado, calif, new AddEvaluacionRequest.EvaluacionResponseContract() {
            @Override
            public void OnCheckSuccess() {
                mListener.OnInteractorEvaluacionSuccess();
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
