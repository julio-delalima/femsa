package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.contactarAdministrador;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.ContactarAdministradorRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class ContactarAdministradorInteractor {

    private OnInteractorContactarAdministrador mListener;

    public interface OnInteractorContactarAdministrador{
        void onInteractorMensajeEnviado();
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorTiempoEsperaAgotado();
        void onInteractorErrorServidor();
    }

    public void peticionContactarAdministrador(String mensaje, int idSolicitud, int tipomensaje, OnInteractorContactarAdministrador listener){
        mListener = listener;
        ContactarAdministradorRequest request = new ContactarAdministradorRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(mensaje, idSolicitud, SharedPreferencesUtil.getInstance().getTokenUsuario(), tipomensaje, new ContactarAdministradorRequest.OnResponseContactarAdministrador() {
            @Override
            public void onResponseMensajeEnviado() {
                mListener.onInteractorMensajeEnviado();
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
}
