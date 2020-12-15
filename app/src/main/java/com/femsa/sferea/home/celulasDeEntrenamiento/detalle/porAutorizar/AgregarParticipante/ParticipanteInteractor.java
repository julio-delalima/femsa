package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.AgregarParticipante;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteData;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Participante.ObtenerParticpante;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.EditarParticipanteRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

public class ParticipanteInteractor {

    private OnInteractorObtenerParticipante mListener;

    public interface  OnInteractorObtenerParticipante{
        void onInteractorObtenerParticipnates(ParticipanteData data);
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
        void onInteractorMensajeInesperado(int codigoRespuesta);
        void onInteractorParticipanteEliminado();
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

    public void peticionEliminarParticipantes(String idSolicitud, ArrayList<Integer> empleados, boolean accion,
                                             OnInteractorObtenerParticipante listener){
        mListener = listener;
        EditarParticipanteRequest request = new EditarParticipanteRequest(AppTalentoRHApplication.getApplication());
        request.makeRequestEliminarParticipante(idSolicitud, empleados, accion, SharedPreferencesUtil.getInstance().getTokenUsuario(),
                new EditarParticipanteRequest.OnResponseEditarParticipante() {
                    @Override
                    public void onResponseParticipantesEditados() {
                        mListener.onInteractorParticipanteEliminado();
                    }

                    @Override
                    public void onResponseTokenInvalido() {
                        mListener.onInteractorTokenInvalido();
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
