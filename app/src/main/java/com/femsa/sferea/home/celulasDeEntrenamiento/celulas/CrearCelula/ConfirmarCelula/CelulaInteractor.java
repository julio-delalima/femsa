package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.ConfirmarCelula;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.CrearSolicitudRequest;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Paises.PaisesRequest;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

public class CelulaInteractor {

    private OnCelulaInteractorListener mListener;

    public interface OnCelulaInteractorListener
    {
        void OnInteractorLlamarCelula(CelulaDTO dto, String tipo, int idSolicitante, String token);

        void OnInteractorCelulaExitosa();

        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorUnexpectedError(int errorCode);

        void OnInteractorNoAuth();

        void onViewDesplegarlistadoPaises(PaisesResponseData data);

        void onInteractorTraerPaises(String token);
    }

    public void CallCelula(CelulaDTO dto,String tipo,  int idSolicitante, String token, OnCelulaInteractorListener listener)
        {
            mListener = listener;

            CrearSolicitudRequest request = new CrearSolicitudRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(tipo, token, dto, idSolicitante, new CrearSolicitudRequest.OnResponseNuevaSolicitud() {
                @Override
                public void onResponseSolicitudCreada() {
                    mListener.OnInteractorCelulaExitosa();
                }

                @Override
                public void onResponseTokenInvalido() {
                    mListener.OnInteractorNoAuth();
                }

                @Override
                public void onResponseErrorInesperado(int codigoRespuesta) {
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


    public void callListadoPaises(String token, OnCelulaInteractorListener listener)
    {
        mListener = listener;
        PaisesRequest listaPaises = new PaisesRequest(AppTalentoRHApplication.getApplication());
        listaPaises.makeRequest(token, new PaisesRequest.OnResponseObtenerListadoPaises(){
            /**
             * <p>Método que indica que el servidor tiene un error y no fue posible obtener la respuesta.</p>
             */
            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            /**
             * <p>Método que indica que el dispositivo no cuenta con conexión a Internet.</p>
             */
            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            /**
             * <p>Método que indica que el tiempo de espera que tiene la configuración del servicio se ha
             * agotado.</p>
             */
            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }

            @Override
            public void onResponseObtenerListadoPaises(PaisesResponseData data) {
                mListener.onViewDesplegarlistadoPaises(data);
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void onResponseMuestraMensaje(int codigoRespuesta) {
                mListener.OnInteractorUnexpectedError(codigoRespuesta);
            }

            @Override
            public void onResponseSinPaises() {
                mListener.OnInteractorUnexpectedError(RequestManager.CODIGO_SERVIDOR.NO_CONTENT);
            }
        });
    }
}
