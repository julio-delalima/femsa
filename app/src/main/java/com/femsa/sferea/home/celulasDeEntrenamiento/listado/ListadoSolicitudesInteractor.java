package com.femsa.sferea.home.celulasDeEntrenamiento.listado;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado.ListaSolicitudesData;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.EliminarSolicitudRequest;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Paises.PaisesRequest;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.listado.ObtenerListadoSolicitudesRequest;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class ListadoSolicitudesInteractor {

    private OnInteractorObtenerListadoSolicitudes mListener;

    public interface OnInteractorObtenerListadoSolicitudes{
        void onInteractorObtenerListadoSolicitudes(ListaSolicitudesData data);
        void onInteractorTokenInvalido();
        void onInteractorSinSolicitudes();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
        void onInteractorEliminarSolicitud(int idSolicitud);
        void onInteractorSolicitudeliminada();
        void onInteractorErrorInesperado(int codigoRespuesta);
        void onInteractorTraerPaises(String token);
        void onInteractorDesplegarPaises(PaisesResponseData data);
    }

    public void peticionObtenerListadoSolicitudes(OnInteractorObtenerListadoSolicitudes listener){
        mListener = listener;
        ObtenerListadoSolicitudesRequest request = new ObtenerListadoSolicitudesRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), new ObtenerListadoSolicitudesRequest.OnResponseObtenerListadoSolicitudes() {
            @Override
            public void onResponseObtenerListadoSolicitudes(ListaSolicitudesData data) {
                mListener.onInteractorObtenerListadoSolicitudes(data);
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.onInteractorTokenInvalido();
            }

            @Override
            public void onResponseSinSolicitudes() {
                mListener.onInteractorSinSolicitudes();
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

    public void callEliminarSolicitud(String token, int idSolicitud, OnInteractorObtenerListadoSolicitudes listener)
        {
            mListener = listener;
            EliminarSolicitudRequest eliminar = new EliminarSolicitudRequest(AppTalentoRHApplication.getApplication());
            eliminar.makeRequest(token, idSolicitud, new EliminarSolicitudRequest.OnResponseEliminarSolicitud() {
                @Override
                public void onResponseSolicitudEliminada() {
                    mListener.onInteractorSolicitudeliminada();
                }

                @Override
                public void onResponseTokenInvalido() {
                    mListener.onInteractorTokenInvalido();
                }

                @Override
                public void onResponseErrorInesperado(int codigoRespuesta) {
                    mListener.onInteractorErrorInesperado(codigoRespuesta);
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

    public void callListadoPaises(String token, OnInteractorObtenerListadoSolicitudes listener)
        {
            mListener = listener;
            PaisesRequest listaPaises = new PaisesRequest(AppTalentoRHApplication.getApplication());
            listaPaises.makeRequest(token, new PaisesRequest.OnResponseObtenerListadoPaises(){
                /**
                 * <p>Método que indica que el servidor tiene un error y no fue posible obtener la respuesta.</p>
                 */
                @Override
                public void onResponseErrorServidor() {
                    mListener.onInteractorErrorServidor();
                }

                /**
                 * <p>Método que indica que el dispositivo no cuenta con conexión a Internet.</p>
                 */
                @Override
                public void onResponseSinConexion() {
                    mListener.onInteractorSinConexion();
                }

                /**
                 * <p>Método que indica que el tiempo de espera que tiene la configuración del servicio se ha
                 * agotado.</p>
                 */
                @Override
                public void onResponseTiempoAgotado() {
                    mListener.onInteractorTiempoEsperaAgotado();
                }

                @Override
                public void onResponseObtenerListadoPaises(PaisesResponseData data) {
                    mListener.onInteractorDesplegarPaises(data);
                }

                @Override
                public void onResponseTokenInvalido() {
                    mListener.onInteractorTokenInvalido();
                }

                @Override
                public void onResponseMuestraMensaje(int codigoRespuesta) {
                    mListener.onInteractorErrorInesperado(codigoRespuesta);
                }

                @Override
                public void onResponseSinPaises() {
                    mListener.onInteractorErrorInesperado(RequestManager.CODIGO_SERVIDOR.NO_CONTENT);
                }
            });
        }
}
