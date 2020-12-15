package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Encuesta;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.EncuestaResponseData;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

import java.util.ArrayList;

public class EncuestaPresenter implements EncuestaInteractor.OnEncuestaInteractorListener {

    private EncuestaView mView;

    private EncuestaInteractor mInteractor;

    public EncuestaPresenter(EncuestaView view, EncuestaInteractor interactor)
    {
        mInteractor = interactor;

        mView = view;
    }

    @Override
    public void OnInteractorServerError() {
        if(mView != null)
        {
            mView.OnViewEsconderLoader();
            mView.OnViewMostrarMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
        {
            mView.OnViewEsconderLoader();
            mView.OnViewMostrarMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
        {
            mView.OnViewEsconderLoader();
            mView.OnViewMostrarMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mView != null)
        {
            mView.OnViewEsconderLoader();
            mView.OnViewMostrarMensaje(DialogManager.EXPIRED_TOKEN);
        }
    }

    @Override
    public void OnInteractorLlamarEncuesta(int idObject) {
        if(mView != null)
        {
            mView.OnViewMostrarLoader();
            mInteractor.callEncuesta(idObject, SharedPreferencesUtil.getInstance().getTokenUsuario(), this);
        }
    }

    @Override
    public void OnInteractorMuestraMensaje(int tipoMensaje, int codigoRespuesta) {
        if(mView != null)
        {
            mView.OnViewEsconderLoader();
            mView.OnViewMostrarMensaje(tipoMensaje, codigoRespuesta);
        }
    }

    @Override
    public void OnInteractorMuestraEncuesta(EncuestaResponseData data) {
        if(mView != null)
        {
            mView.OnViewEsconderLoader();
            mView.OnViewPintaEncuesta(data);
        }
    }

    @Override
    public void OnInteractorAddEncuesta(int idObjeto, ArrayList<String> idPreguntas, ArrayList<String> idRespuestas, int aprobado) {
        if(mView != null)
        {
            mView.OnViewMostrarLoader();
            mInteractor.insertEncuesta(idObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario(), idRespuestas, idPreguntas, aprobado, this);
        }
    }

    @Override
    public void OnInteractorEncuestaSuccess() {
        if(mView  != null)
        {
            mView.OnViewEsconderLoader();
            mView.OnViewEncuestaAdded();
        }
    }

    public void onDeestroy()
    {
        mView = null;
        mInteractor = null;
    }
}