package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos;

import com.femsa.sferea.Utilities.StringManager;

public class JuegosPresenter implements JuegosInteractor.OnJuegosInteractorListener {

    private JuegosView mView;

    private JuegosInteractor mJuegosInteractor;

    public JuegosPresenter(JuegosView view, JuegosInteractor juegosInteractor) {
        mView = view;
        mJuegosInteractor = juegosInteractor;
    }

    @Override
    public void onCallActualizaSesion(String token) {
        if(mView != null){
            mJuegosInteractor.callActualizaSesion(token, this);
        }
    }

    @Override
    public void onMuestraMensaje(int tipoMensaje, int codigoError) {
        if(mView != null){
            mView.onMuestraMensaje(tipoMensaje, codigoError);
        }
    }

    @Override
    public void onMuestraMensaje(int tipoMensaje) {
        if(mView != null){
            if(tipoMensaje == StringManager.EXPIRED_TOKEN){
                mView.onNoAuth();
            }
            else{
                mView.onMuestraMensaje(tipoMensaje);
            }
        }
    }

    @Override
    public void onSesionActualizada() {
        if(mView != null){
            mView.onSesionActualizada();
        }
    }
}
