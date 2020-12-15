package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Paises.DetallePaisRequest;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Paises.PaisesRequest;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.DialogManager;

public class PaisesInteractor {
    private OnInteractorObtenerPaises mListener;

    public interface OnInteractorObtenerPaises{
        void onInteractorCallPaises(String token);
        void onInteractorCallDetallePais(String token, int idPais);
        void onInteractorObtenerPaises(PaisesResponseData data);
        void onInteractorObtenerDetallePais(DetallePaisesResponseData data);
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
        void onInteractorMensajeInesperado(int tipo, int codigoRespuesta);
        void onInteractorSinPaises();
    }

    public void CallPaises(String token, OnInteractorObtenerPaises listener)
        {
            mListener = listener;
            PaisesRequest request = new PaisesRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(token, new PaisesRequest.OnResponseObtenerListadoPaises() {
                @Override
                public void onResponseObtenerListadoPaises(PaisesResponseData data) {
                    mListener.onInteractorObtenerPaises(data);
                }

                @Override
                public void onResponseTokenInvalido() {
                    mListener.onInteractorTokenInvalido();
                }

                @Override
                public void onResponseMuestraMensaje(int codigoRespuesta) {
                    mListener.onInteractorMensajeInesperado(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
                }

                @Override
                public void onResponseSinPaises() {
                    mListener.onInteractorSinPaises();
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


    public void CallDetallePais(String token, int idPais, OnInteractorObtenerPaises listener)
    {
        mListener = listener;
        DetallePaisRequest request = new DetallePaisRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idPais, new DetallePaisRequest.OnResponseObtenerListadoPaises() {
            @Override
            public void onResponseObtenerDetallePais(DetallePaisesResponseData data) {
                mListener.onInteractorObtenerDetallePais(data);
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.onInteractorTokenInvalido();
            }

            @Override
            public void onResponseMuestraMensaje(int codigoRespuesta) {
                mListener.onInteractorMensajeInesperado(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
            }

            @Override
            public void onResponseSinPaises() {
                mListener.onInteractorSinPaises();
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
