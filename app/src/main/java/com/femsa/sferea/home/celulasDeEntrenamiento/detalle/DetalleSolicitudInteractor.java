package com.femsa.sferea.home.celulasDeEntrenamiento.detalle;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudData;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Paises.DetallePaisRequest;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Paises.PaisesRequest;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.ObtenerDetalleSolicitudRequest;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

public class DetalleSolicitudInteractor {

    private OnInteractorObtenerDetalleSolicitud mListener;

    public interface OnInteractorObtenerDetalleSolicitud{
        void onInteractorObtenerDetalleSolicitud(DetalleSolicitudData data);
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
        void OnInteractorMuestraMensaje(int mensaje, int codigo);
        void OnInteractorSinPaisData();
        void onInteractorDetallePais(DetallePaisesResponseData data);
        void onInteractorTraerPaises(String token);
        void onInteractorErrorInesperado(int codigo);
        void onInteractorDesplegarPaises(PaisesResponseData data);
    }

    /**
     * <p>Método que realiza la petición para obtener el detalle de una solicitud.</p>
     * @param idSolicitud Id de la solicitud.
     * @param listener Listener de las distintas respuestas.
     */
    public void peticionObtenerDetalleSolicitud(int idSolicitud, OnInteractorObtenerDetalleSolicitud listener){
        mListener = listener;
        ObtenerDetalleSolicitudRequest request = new ObtenerDetalleSolicitudRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(idSolicitud, SharedPreferencesUtil.getInstance().getTokenUsuario(), new ObtenerDetalleSolicitudRequest.OnResponseObtenerDetalleSolicitud() {
            @Override
            public void onResponseObtenerDetalleSolicitud(DetalleSolicitudData data) {
                mListener.onInteractorObtenerDetalleSolicitud(data);
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

    public void obtenerDetallePais(int idPais, OnInteractorObtenerDetalleSolicitud listener, String token)
        {
            mListener = listener;
            DetallePaisRequest paisitos = new DetallePaisRequest(AppTalentoRHApplication.getApplication());
            paisitos.makeRequest(token, idPais, new DetallePaisRequest.OnResponseObtenerListadoPaises() {
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
                public void onResponseObtenerDetallePais(DetallePaisesResponseData data) {
                    mListener.onInteractorDetallePais(data);
                }

                @Override
                public void onResponseTokenInvalido() {
                    mListener.onInteractorTokenInvalido();
                }

                @Override
                public void onResponseMuestraMensaje(int codigoRespuesta) {
                    mListener.OnInteractorMuestraMensaje(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
                }

                @Override
                public void onResponseSinPaises() {
                    mListener.OnInteractorSinPaisData();
                }
            });
        }


    public void callListadoPaises(String token, OnInteractorObtenerDetalleSolicitud listener)
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
