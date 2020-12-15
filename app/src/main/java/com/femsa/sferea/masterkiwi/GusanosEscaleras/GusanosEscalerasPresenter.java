package com.femsa.sferea.masterkiwi.GusanosEscaleras;

import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.StringManager;

public class GusanosEscalerasPresenter implements GusanosEscalerasInteractor.OnGusanosResponseListener {

    private GusanosEscalerasView mView;

    private GusanosEscalerasInteractor mInteractor;

    public GusanosEscalerasPresenter(GusanosEscalerasView view, GusanosEscalerasInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onMandaDatos(int usado, int posJ1, int posJ2, int mapa, int turno, String token, int idPartida) {
        LogManager.d("Gusanos", "Presenter preview");
        if(mView != null){
            LogManager.d("Gusanos", "Presenter");
            mView.onCargaLoader();
            mInteractor.onCallEnviarDatos(usado, posJ1, posJ2, mapa, turno, token, idPartida, this);
        }
    }

    @Override
    public void onDatosEnviados() {
        if(mView != null) {
            mView.onOcultaLoader();
            mView.onDatosGEenviados();
        }
    }

    @Override
    public void onMuestraMensaje(int mensaje) {
        if(mView != null){
            mView.onOcultaLoader();
            mView.onMuestraMensaje(mensaje);
        }
    }

    @Override
    public void onMuestraMensaje(int mensaje, int codigo) {
        if(mView != null){
            mView.onOcultaLoader();
            mView.onMuestraMensaje(mensaje, codigo);
        }
    }

    @Override
    public void OnServerError() {
        if(mView != null){
            mView.onOcultaLoader();
            mView.onMuestraMensaje(StringManager.SERVER_ERROR);
        }
    }

    @Override
    public void onTimeout() {
        if(mView != null){
            mView.onOcultaLoader();
            mView.onMuestraMensaje(StringManager.TIMEOUT);
        }
    }

    @Override
    public void onNoInternet() {
        if(mView != null){
            mView.onOcultaLoader();
            mView.onMuestraMensaje(StringManager.NO_INTERNET);
        }
    }


    @Override
    public void onNoAuth() {
        if(mView != null){
            mView.onNoAuth();
        }
    }

    /**
     * <p>Método que indica que el servidor tiene un error y no fue posible obtener la respuesta.</p>
     */
    @Override
    public void onResponseErrorServidor() {
        if(mView != null){
            mView.onOcultaLoader();
            mView.onMuestraMensaje(StringManager.SERVER_ERROR);
        }
    }

    /**
     * <p>Método que indica que el dispositivo no cuenta con conexión a Internet.</p>
     */
    @Override
    public void onResponseSinConexion() {
        if(mView != null){
            mView.onOcultaLoader();
            mView.onMuestraMensaje(StringManager.NO_INTERNET);
        }
    }

    /**
     * <p>Método que indica que el tiempo de espera que tiene la configuración del servicio se ha
     * agotado.</p>
     */
    @Override
    public void onResponseTiempoAgotado() {
        if(mView != null){
            mView.onOcultaLoader();
            mView.onMuestraMensaje(StringManager.TIMEOUT);
        }
    }
}
