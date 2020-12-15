package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.Multijugador.AceptarRetoRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.Multijugador.GetListadoRetadoresRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.Multijugador.RetarJugadorRequest;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.Juegos.RetadorResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.masterkiwi.GusanosEscaleras.GusanosEscalerasInteractor;

public class MultijugadorInteractor {

    private OnMultiPlayerInteractorListener mListener;

    public interface OnMultiPlayerInteractorListener{
        void onTraeListado(int idPrograma, String token);

        void onMuestraMensaje(int tipoMensaje, int codigoError);

        void onMuestraMensaje(int tipoMensaje);

        void onCargaListadoRetadores(RetadorResponseData data);

        void onSinRetadores();

        void onNoAuth();

        void onCallEnviarReto(String token, int idEmpleado, int idObjeto, int idPrograma);

        void onRetoEnviado();

        void onCallAceptarReto(int idEmpleado, int idObjeto);

        void onRetoAceptado();
    }

    void callListadoRetadores(String token, int idPrograma, OnMultiPlayerInteractorListener listener){
        mListener = listener;
        GetListadoRetadoresRequest request = new GetListadoRetadoresRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(idPrograma, token, new GetListadoRetadoresRequest.GetListadoRetadoresResponseContract() {
            @Override
            public void onGetListadoRetadores(RetadorResponseData data) {
                mListener.onCargaListadoRetadores(data);
            }

            @Override
            public void onNoAuth() {
                mListener.onNoAuth();
            }

            @Override
            public void onUnexpectedError(int errorcode) {
                mListener.onMuestraMensaje(StringManager.UNEXPECTED_ERROR, errorcode);
            }

            @Override
            public void onSinRetadores() {
                mListener.onSinRetadores();
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


    void callEnviarReto(String token, int idEmpleado, int idObjeto, int idPrograma, OnMultiPlayerInteractorListener listener){
        mListener = listener;
        RetarJugadorRequest request = new RetarJugadorRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(idEmpleado, idObjeto, idPrograma, token, new RetarJugadorRequest.RetarResponseContract() {
            @Override
            public void onResponseRetoEnviado() {
                mListener.onRetoEnviado();
            }

            @Override
            public void onNoAuth() {
                mListener.onNoAuth();
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

    void onCallAceptarReto(String token, int idObjeto, int idEmpleado, OnMultiPlayerInteractorListener listener){
        mListener = listener;
        AceptarRetoRequest retoRequest = new AceptarRetoRequest(AppTalentoRHApplication.getApplication());
        retoRequest.makeRequest(idEmpleado, idObjeto, token, new AceptarRetoRequest.RetarResponseContract() {
            @Override
            public void onResponseRetoAceptado() {
                mListener.onRetoAceptado();
            }

            @Override
            public void onNoAuth() {
                mListener.onNoAuth();
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
