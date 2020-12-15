package com.femsa.sferea.masterkiwi.GusanosEscaleras;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.Multijugador.InsertDataGusanosRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.Multijugador.RetarJugadorRequest;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.MultijugadorInteractor;

public class GusanosEscalerasInteractor {

    private OnGusanosResponseListener mListener;

    interface OnGusanosResponseListener extends ResponseContract{
        void onMandaDatos(int usado, int posJ1, int posJ2, int mapa, int turno, String token, int idempleado);
        void onDatosEnviados();
        void onMuestraMensaje(int mensaje);
        void onMuestraMensaje(int mensaje, int codigo);
        void OnServerError();
        void onTimeout();
        void onNoInternet();
        void onNoAuth();
    }

    void onCallEnviarDatos(int usado, int posJ1, int posJ2, int mapa, int turno, String token, int idEmpleado, OnGusanosResponseListener listener){
        mListener = listener;
        InsertDataGusanosRequest request = new InsertDataGusanosRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idEmpleado, usado, posJ1, posJ2, mapa, turno, new InsertDataGusanosRequest.GusanosResponseContract() {
            @Override
            public void OnAddDataSuccess() {
                mListener.onDatosEnviados();
            }

            @Override
            public void OnNoAuth() {
                mListener.onNoAuth();
            }

            @Override
            public void OnUnexpectedError(int errorcode) {
                mListener.onMuestraMensaje(StringManager.UNEXPECTED_ERROR, errorcode);
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.OnServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.onNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.onTimeout();
            }
        });
    }


}
