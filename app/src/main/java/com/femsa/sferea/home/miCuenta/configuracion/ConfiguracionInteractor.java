package com.femsa.sferea.home.miCuenta.configuracion;

import com.femsa.requestmanager.Request.Configuracion.CambiarIdiomaRequest;
import com.femsa.requestmanager.Request.Configuracion.DescargaRequest;
import com.femsa.requestmanager.Request.Configuracion.HabilitarNotificacionesRequest;
import com.femsa.requestmanager.Request.Configuracion.ListadoIdiomasRequest;
import com.femsa.requestmanager.Request.Home.SetTokenFirebaseRequest;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Configuracion.IdiomaResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class ConfiguracionInteractor {

    private OnConfiguracionInteractorListener mListener;

    public interface OnConfiguracionInteractorListener
        {
            void OnInteractorDescargasCambiada();

            void OnInteractorNotificacionesCambiadas();

            void OnInteractorIdiomaCambiado();

            void OnInteractorCambiarIdioma(int idioma, String token);

            void OnInteractorCambiarDescarga(String token, boolean descarga);

            void OnInteractorCambiarNotificaciones(String token, boolean notif);

            void OnInteractorTraeIdiomas(int idIdiomaActual);

            void OnInteractorCargaIdiomas(IdiomaResponseData data);

            void OnInteractorServerError();

            void OnInteractorTimeout();

            void OnInteractorNoInternet();

            void OnInteractorUnexpectedError(int errorCode);

            void OnInteractorTokenFirebaseDefinido();

            void OnInteractorNoAuth();

            void OnInteractorSetTokenFirebase(String tokenFirebase, String tipoDispositivo);
        }

    public void callTraeIdiomas(int idIdiomaActual, OnConfiguracionInteractorListener listener)
    {
        mListener = listener;
        ListadoIdiomasRequest request = new ListadoIdiomasRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(idIdiomaActual, SharedPreferencesUtil.getInstance().getTokenUsuario(),new ListadoIdiomasRequest.OnResponseObtenerListadoIdiomas() {
            @Override
            public void onResponseObtenerListadoIdiomas(IdiomaResponseData data) {
                mListener.OnInteractorCargaIdiomas(data);
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
            public void onResponseSinIdiomas() {
                mListener.OnInteractorUnexpectedError(RequestManager.CODIGO_SERVIDOR.NO_CONTENT);
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

    public void CallCambiarIdioma(int idIdioma, String token, OnConfiguracionInteractorListener listener)
        {
            mListener = listener;

            CambiarIdiomaRequest request = new CambiarIdiomaRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(idIdioma, token, new CambiarIdiomaRequest.CambiarIdiomaResponseContract() {
                @Override
                public void OnIdiomaCambiado() {
                    mListener.OnInteractorIdiomaCambiado();
                }

                @Override
                public void OnNoAuth() {
                    mListener.OnInteractorNoAuth();
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

    public void CallCambiarNotificaciones(String token, boolean notificaciones, OnConfiguracionInteractorListener listener)
        {
            mListener = listener;

            HabilitarNotificacionesRequest request = new HabilitarNotificacionesRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(token, notificaciones, new HabilitarNotificacionesRequest.HabilitarNotificacionesContract() {
                @Override
                public void OnNotificacionCambiada() {
                    mListener.OnInteractorNotificacionesCambiadas();
                }

                @Override
                public void OnNoAuth() {
                    mListener.OnInteractorNoAuth();
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

    public void CallCambiarDescarga(String token, boolean wifi, OnConfiguracionInteractorListener listener)
        {
            mListener = listener;
            DescargaRequest request = new DescargaRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(token, wifi, new DescargaRequest.DescargaContract() {
                @Override
                public void OnDescargaCambiada() {
                    mListener.OnInteractorDescargasCambiada();
                }

                @Override
                public void OnNoAuth() {
                    mListener.OnInteractorNoAuth();
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


    public void callTokenFirebase(String tokenfirebase, String tokenUsuario, String tipoDispositivo, OnConfiguracionInteractorListener listener)
    {
        mListener = listener;
        SetTokenFirebaseRequest request = new SetTokenFirebaseRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(tokenfirebase, tipoDispositivo, tokenUsuario, new SetTokenFirebaseRequest.setTokenFirebaseResponseContract() {
            @Override
            public void OnTokenDefinido() {
                mListener.OnInteractorTokenFirebaseDefinido();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
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
