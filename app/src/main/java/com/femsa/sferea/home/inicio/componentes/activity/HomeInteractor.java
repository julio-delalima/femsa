package com.femsa.sferea.home.inicio.componentes.activity;

import com.femsa.requestmanager.Request.Home.SetTokenFirebaseRequest;
import com.femsa.requestmanager.Request.Logout.LogOutRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

public class HomeInteractor {

    OnHomeInteractorListener mListener;

    public interface OnHomeInteractorListener
    {
        void OnInteractorLogOut(String token);

        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorRequestFailure();

        void OnInteractorNoAuth();

        /***
         * Success
         */

        void OnInteractorSuccessfulLogout();

        void OnInteractorMensajeInesperado(int codigoRespuesta);

        void OnInteractorSetTokenFirebase(String tokenFirebase, String tipoDispositivo);

        void OnInteractorTokenFirebaseDefinido();
    }

    public void OnCallLogOut(String tokenUser, OnHomeInteractorListener listener)
    {
        mListener = listener;
        LogOutRequest out = new LogOutRequest(AppTalentoRHApplication.getApplication());
        out.makeRequest(tokenUser, new LogOutRequest.LogoutResponseContract() {
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
            public void onResponseLogOutCorrecto() {
                mListener.OnInteractorSuccessfulLogout();
            }

            @Override
            public void onResponseLogOutError() {
                mListener.OnInteractorRequestFailure();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
                mListener.OnInteractorMensajeInesperado(codigoRespuesta);
            }
        });
    }


    public void callTokenFirebase(String tokenfirebase, String tokenUsuario, String tipoDispositivo, OnHomeInteractorListener listener)
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
                        mListener.OnInteractorMensajeInesperado(codigoRespuesta);
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
