package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaDos;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteData;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Participante.ObtenerParticpante;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

public class ParticipanteInteractor {

    private OnInteractorObtenerParticipante mListener;

    public interface  OnInteractorObtenerParticipante{
        void onInteractorObtenerParticipnates(ParticipanteData data);
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
        void onInteractorMensajeInesperado(int codigoRespuesta);
    }

    public void peticionObtenerParticipante(String token, int numEmpleado, OnInteractorObtenerParticipante listener){
        mListener = listener;
        ObtenerParticpante request = new ObtenerParticpante(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, numEmpleado, new ObtenerParticpante.OnResponseObtenerParticipante() {
            @Override
            public void onResponseObtenerParticipante(ParticipanteData data) {
                mListener.onInteractorObtenerParticipnates(data);
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.onInteractorTokenInvalido();
            }

            @Override
            public void onResponseMensajeInesperado(int codigoRespuesta) {
                mListener.onInteractorMensajeInesperado(codigoRespuesta);
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.onInteractorErrorServidor();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.onInteractorSinConexion();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.onInteractorTiempoEsperaAgotado();
            }
        });
    }
}
