package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.ActualizaSesionRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.StringManager;

public class JuegosInteractor {

    private OnJuegosInteractorListener mListener;

    interface OnJuegosInteractorListener{
        void onCallActualizaSesion(String token);

        void onMuestraMensaje(int tipoMensaje, int codigoError);

        void onMuestraMensaje(int tipoMensaje);

        void onSesionActualizada();
    }

    /**
     * <p>Método que hace la petición para actualizar la sesión del usuario
     * y no se cierre mientras etsá jugando.</p>
     * @param tokenUsuario token actual del usuario.
     * */
    void callActualizaSesion(String tokenUsuario, OnJuegosInteractorListener listener){
        mListener = listener;
        ActualizaSesionRequest mRequest = new ActualizaSesionRequest(AppTalentoRHApplication.getApplication());
            mRequest.makeRequest(tokenUsuario, new ActualizaSesionRequest.SesionResponseContract() {
                @Override
                public void onResponseSesionActivada() {
                    mListener.onSesionActualizada();
                }

                @Override
                public void onNoAuth() {
                    mListener.onMuestraMensaje(StringManager.EXPIRED_TOKEN);
                }

                @Override
                public void onUnexpectedError(int code) {
                    mListener.onMuestraMensaje(StringManager.UNEXPECTED_ERROR, code);
                }

                @Override
                public void onResponseErrorServidor() {
                    mListener.onMuestraMensaje(StringManager.SERVER_ERROR);
                }

                @Override
                public void onResponseSinConexion() {
                    mListener.onMuestraMensaje(StringManager.NO_INTERNET);
                }

                @Override
                public void onResponseTiempoAgotado() {
                    mListener.onMuestraMensaje(StringManager.TIMEOUT);
                }
            });
    }
}
