package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.jefeDirecto;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.autorizador.AutorizarSolicitudDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.autorizador.ParticipanteDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.autorizador.AutorizarSolicitudRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

public class PorAutorizarJefeInteractor {

    private OnInteractorObtenerAutorizacionJefe mListener;

    public interface OnInteractorObtenerAutorizacionJefe{
        void onInteractorAutorizacionConcedida();
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
    }

    /**
     * <p>Método que realiza la petición para obtener la autorización del jefe.</p>
     * @param idSolicitud Id de la solicitud que se va a autorizar.
     * @param listener Listener para manejar las respuestas de la petición.
     */
    public void peticionObtenerAutorizacionJefe(int idSolicitud, int idEmpleado, ArrayList<ParticipanteDTO> list, OnInteractorObtenerAutorizacionJefe listener){
        mListener = listener;
        AutorizarSolicitudRequest request = new AutorizarSolicitudRequest(AppTalentoRHApplication.getApplication());
        AutorizarSolicitudDTO autorizarSolicitudDTO = new AutorizarSolicitudDTO(idSolicitud, idEmpleado, list);
        request.makeRequest(autorizarSolicitudDTO, SharedPreferencesUtil.getInstance().getTokenUsuario(),  new AutorizarSolicitudRequest.OnResponseAutorizarSolicitud() {
            @Override
            public void onResponseAutorizacionConcedida() {
                mListener.onInteractorAutorizacionConcedida();
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
