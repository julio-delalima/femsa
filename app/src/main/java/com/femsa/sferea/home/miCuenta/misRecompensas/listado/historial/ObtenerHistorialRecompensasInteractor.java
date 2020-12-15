package com.femsa.sferea.home.miCuenta.misRecompensas.listado.historial;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoHistorialData;
import com.femsa.requestmanager.Request.MisRecompensas.listado.ObtenerListadoHistorialRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class ObtenerHistorialRecompensasInteractor {

    private OnInteractorObtenerHistorialRecompensas mListener;

    public interface OnInteractorObtenerHistorialRecompensas{
        void onInteractorObtenerHistorialRecompensas(ObtenerListadoHistorialData data);
        void onInteractorTokenInvalido();
        void onInteractorSinRecompensas();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
    }

    /**
     * <p>Método que permite realizar la petición para obtener el historial. De igual forma maneja las respuestas con la vista.</p>
     * @param listener
     */
    public void peticionObtenerHistorialRecompensas(OnInteractorObtenerHistorialRecompensas listener){
        mListener = listener;
        ObtenerListadoHistorialRequest request = new ObtenerListadoHistorialRequest(AppTalentoRHApplication.getApplication());
        request.makeRequestHistorialRecompensas(SharedPreferencesUtil.getInstance().getTokenUsuario(), new ObtenerListadoHistorialRequest.OnResponseObtenerHistorialRecompensas() {
            @Override
            public void onResponseObtenerListadoHistorial(ObtenerListadoHistorialData data) {
                mListener.onInteractorObtenerHistorialRecompensas(data);
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
