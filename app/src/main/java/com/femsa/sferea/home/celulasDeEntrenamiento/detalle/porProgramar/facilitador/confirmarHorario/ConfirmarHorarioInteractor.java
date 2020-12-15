package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.FechaDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.MostrarHorarioData;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.ProgramarSolicitudDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.facilitador.MostrarHorarioRequest;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.facilitador.ProgramarSolicitudRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

public class ConfirmarHorarioInteractor {

    private OnInteractorProgramarSolicitud mListener;

    public interface OnInteractorProgramarSolicitud{
        void onViewMostrarHorario(MostrarHorarioData data);
        void onInteractorSolicitudProgramada();
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorTiempoEsperaAgotado();
        void onInteractorErrorServidor();
    }

    public void peticionProgramarSolicitud(int idFacilitador, int idSolicitud, ArrayList<FechaDTO> list, OnInteractorProgramarSolicitud listener){
        mListener = listener;
        ProgramarSolicitudRequest programarSolicitudRequest = new ProgramarSolicitudRequest(AppTalentoRHApplication.getApplication());
        ProgramarSolicitudDTO programarSolicitudDTO = new ProgramarSolicitudDTO(idFacilitador, idSolicitud, list);
        programarSolicitudRequest.makeRequest(programarSolicitudDTO, SharedPreferencesUtil.getInstance().getTokenUsuario(), new ProgramarSolicitudRequest.OnResponseProgramarSolicitud() {
            @Override
            public void onResponseSolicitudProgramada() {
                mListener.onInteractorSolicitudProgramada();
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

    /**
     * <p>Método que reúne los datos para llevar a cabo la petición para programar una solicitud.</p>
     * @param idSolicitud Id de la solicitud de la que se quiere obtener la información.
     * @param idFacilitador Id del facilitador que va a programar la solicitud.
     * @param listener Interface para manejar las posibles respuestas de la petición.
     */
    public void peticionMostrarHorario(int idSolicitud, int idFacilitador, OnInteractorProgramarSolicitud listener){
        mListener = listener;
        MostrarHorarioRequest request = new MostrarHorarioRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(idSolicitud, idFacilitador, SharedPreferencesUtil.getInstance().getTokenUsuario(), new MostrarHorarioRequest.OnResponseMostrarHorario() {
            @Override
            public void onResponseMostrarHorario(MostrarHorarioData data) {
                mListener.onViewMostrarHorario(data);
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
