package com.femsa.sferea.home.miCuenta.misRecompensas.detalle;

import com.femsa.requestmanager.DTO.User.MisRecompensas.detalle.DetalleRecompensaData;
import com.femsa.requestmanager.Request.MisRecompensas.detalle.ObtenerDetalleRecompensaRequest;
import com.femsa.requestmanager.Request.MisRecompensas.detalle.ObtenerRecompensaRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class DetalleRecompensaInteractor {

    private OnInteractorObtenerDetalleRecompensa mListener;

    public interface OnInteractorObtenerDetalleRecompensa{
        void onInteractorObtenerDetalleRecompensa(DetalleRecompensaData data);
        void onInteractorRecompensaObtenida();
        void onInteractorRecompensaIncanjeable();
        void onInteractorInfuficiente();
        void onInteractorAgotado();
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
        void onInteractorMensajeInesperado(int codigo);
    }

    /**
     * <p>Método que realiza la petición para obtener el detalle de una recompensa.</p>
     * @param idRecompensa Id de la recompensa que se desea visualizar.
     * @param listener Listener que define los métodos para las respuestas de la petición.
     */
    public void peticionObtenerDetalleRecompensa(int idRecompensa, OnInteractorObtenerDetalleRecompensa listener){
        mListener = listener;
        ObtenerDetalleRecompensaRequest request = new ObtenerDetalleRecompensaRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(idRecompensa, SharedPreferencesUtil.getInstance().getTokenUsuario(), new ObtenerDetalleRecompensaRequest.OnResponseObtenerDetalleRecompensa() {
            @Override
            public void onResponseObtenerDetalleRecompensa(DetalleRecompensaData data) {
                mListener.onInteractorObtenerDetalleRecompensa(data);
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

    /**
     * <p>Método que realiza la petición para canjear una recompensa.</p>
     * @param idRecompensa Id de la recompensa que se desea canjear.
     * @param listener Listener para manejar las respuestas de la petición.
     */
    public void peticionObtenerRecompensa(int idRecompensa, OnInteractorObtenerDetalleRecompensa listener){
        mListener = listener;
        ObtenerRecompensaRequest request = new ObtenerRecompensaRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(idRecompensa, SharedPreferencesUtil.getInstance().getTokenUsuario(), new ObtenerRecompensaRequest.OnResponseObtenerRecompensa() {
            @Override
            public void onResponseRecompensaObtenida() {
                mListener.onInteractorRecompensaObtenida();
            }

            @Override
            public void onResponseYaCanjeada() {
                mListener.onInteractorRecompensaIncanjeable();
            }

            @Override
            public void onRespnseInsuficientes() {
                mListener.onInteractorInfuficiente();
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.onInteractorTokenInvalido();
            }

            @Override
            public void onResponseUnexpectedError(int error) {
                mListener.onInteractorMensajeInesperado(error);
            }

            @Override
            public void onResponseRecompensaAgotada() {
                mListener.onInteractorAgotado();
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
