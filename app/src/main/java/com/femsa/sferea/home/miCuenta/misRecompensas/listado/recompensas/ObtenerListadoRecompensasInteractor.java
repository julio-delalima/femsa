package com.femsa.sferea.home.miCuenta.misRecompensas.listado.recompensas;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoRecompensasData;
import com.femsa.requestmanager.Request.MisRecompensas.listado.ObtenerListadoRecompensasRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class ObtenerListadoRecompensasInteractor {

    private OnInteractorObtenerListadoRecompensas mListener;

    public interface OnInteractorObtenerListadoRecompensas{
        void onInteractorObtenerListadoRecompensas(ObtenerListadoRecompensasData data);
        void onInteractorTokenInvalido();
        void onInteractorSinRecompensas();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
    }

    /**
     * <p>Método que permite realizar la petición para obtener el listado de recompensas.</p>
     * @param listener Listener que contiene los métodos para definir las respuestas de la petición.
     */
    public void peticionObtenerListadoRecompensas(OnInteractorObtenerListadoRecompensas listener){
        mListener = listener;
        ObtenerListadoRecompensasRequest request = new ObtenerListadoRecompensasRequest(AppTalentoRHApplication.getApplication());
        request.makeRequestListado(SharedPreferencesUtil.getInstance().getTokenUsuario(), new ObtenerListadoRecompensasRequest.OnResponseObtenerListadoRecompensas() {
            @Override
            public void onResponseObtenerListadoRecompensas(ObtenerListadoRecompensasData data) {
                mListener.onInteractorObtenerListadoRecompensas(data);
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.onInteractorTokenInvalido();
            }

            @Override
            public void onResponseSinRecompensas() {
                mListener.onInteractorSinRecompensas();
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
