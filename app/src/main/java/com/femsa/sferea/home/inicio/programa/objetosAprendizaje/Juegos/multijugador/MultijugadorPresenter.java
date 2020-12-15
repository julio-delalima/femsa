package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.Juegos.RetadorResponseData;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class MultijugadorPresenter implements MultijugadorInteractor.OnMultiPlayerInteractorListener {

    private MultiJugadorView mView;

    private MultijugadorInteractor mInteractor;

    public MultijugadorPresenter(MultiJugadorView view, MultijugadorInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onTraeListado(int idPrograma, String token) {
        if(mView != null){
            mView.onMuestraLoader();
            mInteractor.callListadoRetadores(token, idPrograma, this);
        }
    }

    @Override
    public void onMuestraMensaje(int tipoMensaje, int codigoError) {
        if(mView != null){
            mView.onQuitaLoader();
            mView.onMuestraMensaje(tipoMensaje, codigoError);
        }
    }

    @Override
    public void onMuestraMensaje(int tipoMensaje) {
        if(mView != null){
            mView.onQuitaLoader();
            mView.onMuestraMensaje(tipoMensaje);
        }
    }

    @Override
    public void onCargaListadoRetadores(RetadorResponseData data) {
        if(mView != null){
            mView.onCargaListadoRetadores(data);
            mView.onQuitaLoader();
        }
    }

    @Override
    public void onSinRetadores() {
        if(mView != null){
            mView.onQuitaLoader();
            mView.onSinRetadores();
        }
    }

    @Override
    public void onNoAuth() {
        if(mView != null){
            mView.onQuitaLoader();
            mView.onNoAuth();
        }
    }

    @Override
    public void onCallEnviarReto(String token, int idEmpleado, int idObjeto, int idPrograma) {
        if(mView != null){
            mView.onMuestraLoader();
            mInteractor.callEnviarReto(token, idEmpleado, idObjeto,idPrograma, this);
        }
    }

    @Override
    public void onRetoEnviado() {
        if(mView != null){
            mView.onQuitaLoader();
            mView.OnRetoEnviado();
        }
    }

    @Override
    public void onCallAceptarReto(int idEmpleado, int idObjeto) {
        if(mView != null){
            mView.onMuestraLoader();
            mInteractor.onCallAceptarReto(SharedPreferencesUtil.getInstance().getTokenUsuario(), idObjeto, idEmpleado,  this);
        }
    }

    @Override
    public void onRetoAceptado() {
        if(mView != null){
            mView.onQuitaLoader();
            mView.OnRetoAceptado();
        }
    }

}
