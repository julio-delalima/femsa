package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.delegarSolicitud;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.facilitador.ActualizarFacilitadorRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class ActualizarFacilitadorInteractor {

    private OnInteractorActualizarFacilitador mListener;

    public interface OnInteractorActualizarFacilitador{
        void onInteractorFacilitadorActualizado();
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorTiempoEsperaAgotado();
        void onInteractorErrorServidor();
        void onInteractorSinFacilitador();
    }

    /**
     * <p>Método para hacer la petición en la que no se es responsable de la solicitud.</p>
     * @param idSolicitud Id de la solcitud.
     * @param idFacilitador Id del antiguo facilitador.
     * @param responsable Bandera para indicar que no es el responsable.
     * @param nombre Nombre del nuevo facilitador.
     * @param listener Listener para manejar las respuestas.
     */
    public void peticionNoResponsable(String idSolicitud, String idFacilitador, boolean responsable,
                                      String nombre, OnInteractorActualizarFacilitador listener){
        mListener = listener;
        ActualizarFacilitadorRequest request = new ActualizarFacilitadorRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest("", idSolicitud, idFacilitador, responsable, false, nombre, SharedPreferencesUtil.getInstance().getTokenUsuario(), new ActualizarFacilitadorRequest.OnResponseActualizarFacilitador() {
            @Override
            public void onResponseFacilitadorActualizado() {
                mListener.onInteractorFacilitadorActualizado();
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.onInteractorTokenInvalido();
            }

            @Override
            public void onResponseNoHayFacilitador() {
                mListener.onInteractorSinFacilitador();
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
     * <p>Método para actualizar una solicitud cuando no se está disponible.</p>
     * @param correo Correo del nuevo facilitador.
     * @param idSolicitud Id de la solicitud.
     * @param idFacilitador Id del antiguo facilitador.
     * @param disponible Bandera para indicar que no está disponible.
     * @param listener Listener para manejar las respuestas.
     */
    public void peticionNoDisponible(String correo, String idSolicitud, String idFacilitador, boolean disponible, OnInteractorActualizarFacilitador listener){
        mListener = listener;
        ActualizarFacilitadorRequest request = new ActualizarFacilitadorRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(correo, idSolicitud, idFacilitador, false, disponible, "", SharedPreferencesUtil.getInstance().getTokenUsuario(), new ActualizarFacilitadorRequest.OnResponseActualizarFacilitador() {
            @Override
            public void onResponseFacilitadorActualizado() {
                mListener.onInteractorFacilitadorActualizado();
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.onInteractorTokenInvalido();
            }

            @Override
            public void onResponseNoHayFacilitador() {
                mListener.onInteractorSinFacilitador();
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
