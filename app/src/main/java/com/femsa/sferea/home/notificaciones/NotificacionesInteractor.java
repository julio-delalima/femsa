package com.femsa.sferea.home.notificaciones;

import com.femsa.requestmanager.Request.Notificaciones.EliminarNotificacionesRequest;
import com.femsa.requestmanager.Request.Notificaciones.NotificacionesImportantesRequest;
import com.femsa.requestmanager.Request.Notificaciones.NotificacionesVistasRequest;
import com.femsa.requestmanager.Request.Notificaciones.ObtenerNotificacionesRequest;
import com.femsa.requestmanager.Response.NotificacionesResponse.NotificacionesResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

public class NotificacionesInteractor {

    private onNotificacionesInteractor mListener;

    public interface onNotificacionesInteractor {
        void onInteractorSinNotificaciones();

        void onInteractorTraerNotificaciones();

        void onInteractorMostrarNotificaciones(NotificacionesResponseData data);

        void onInteractorMarcarImportante(int idNotificacion);

        void onInteractorNotificacionMarcada();

        void onInteractorMarcarVista(int idNotificacion);

        void onInteractorNotificacionVista();

        void onInteractorEliminarNotificacion(int idNotificacion);

        void onInteractorNotificacionEliminada();

        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorUnexpectedError(int codigoRespuesta);

        void OnInteractorNoAuth();
    }

    public void llamarNotificaciones(String token, onNotificacionesInteractor listener) {
        mListener = listener;
        ObtenerNotificacionesRequest traerNotificacionesRequest = new ObtenerNotificacionesRequest(AppTalentoRHApplication.getApplication());
        traerNotificacionesRequest.makeRequest(token, new ObtenerNotificacionesRequest.OnNotificacionesResponseContract() {
            @Override
            public void OnNoHayNotificaciones() {
                mListener.onInteractorSinNotificaciones();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnTodasLasNotificaciones(NotificacionesResponseData data) {
                mListener.onInteractorMostrarNotificaciones(data);
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

    public void MarcarImportanteNotificacion(String token, int idNotificacion, onNotificacionesInteractor listener) {
        mListener = listener;
        NotificacionesImportantesRequest notificacionesImportantesRequest = new NotificacionesImportantesRequest(AppTalentoRHApplication.getApplication());
        notificacionesImportantesRequest.makeRequest(token, idNotificacion, new NotificacionesImportantesRequest.MarcarNotificacionesResponseContract() {
            @Override
            public void OnNoHayNotificaciones() {
                mListener.onInteractorSinNotificaciones();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnNotificacionMarcada() {
                mListener.onInteractorNotificacionMarcada();
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


    public void marcarNotificacionVista(String token, int idNotificacion, onNotificacionesInteractor listener)
    {
        mListener = listener;
        NotificacionesVistasRequest vistaRequest = new NotificacionesVistasRequest(AppTalentoRHApplication.getApplication());
        vistaRequest.makeRequest(token, idNotificacion, new NotificacionesVistasRequest.NotificacionesVistasResponseContract() {
            @Override
            public void OnNoHayNotificaciones() {
                mListener.onInteractorSinNotificaciones();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnNotificacionVista() {
                mListener.onInteractorNotificacionVista();
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

    public void eliminarNotificacion(String token, int idNotificacion, onNotificacionesInteractor listener)
    {
        mListener = listener;
        EliminarNotificacionesRequest eliminarRequest = new EliminarNotificacionesRequest(AppTalentoRHApplication.getApplication());
        eliminarRequest.makeRequest(token, idNotificacion, new EliminarNotificacionesRequest.EliminarNotificacionesResponseContract() {
            @Override
            public void OnNoHayNotificaciones() {
                mListener.onInteractorSinNotificaciones();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnNotificacionEliminada() {
                mListener.onInteractorNotificacionEliminada();
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

}
