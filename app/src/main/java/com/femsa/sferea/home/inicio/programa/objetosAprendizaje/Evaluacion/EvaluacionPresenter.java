package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Evaluacion;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.EvaluacionResponseData;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

import java.util.ArrayList;

public class EvaluacionPresenter implements EvaluacionInteractor.OnEvaluacionInteractorListener {

    private EvaluacionView mView;

    private EvaluacionInteractor mInteractor;

    public EvaluacionPresenter(EvaluacionView view, EvaluacionInteractor interactor)
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
    public void OnInteractorLlamarEvaluacion(int idObject) {
        if(mView != null)
        {
            mView.OnViewMostrarLoader();
            mInteractor.callEvaluacion(idObject, SharedPreferencesUtil.getInstance().getTokenUsuario(), this);
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
    public void OnInteractorMuestraEvaluacion(EvaluacionResponseData data) {
        if(mView != null)
        {
            mView.OnViewEsconderLoader();
            mView.OnViewPintaEvaluacion(data);
        }
    }

    @Override
    public void OnInteractorAddEvaluacion(int idObjeto, ArrayList<String> idPreguntas, ArrayList<String> idRespuestas, int aprobado, int calif) {
        if(mView != null)
        {
            mView.OnViewMostrarLoader();
            mInteractor.insertEvaluacion(idObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario(), idRespuestas, idPreguntas, aprobado, calif,this);
        }
    }

    @Override
    public void OnInteractorEvaluacionSuccess() {
        if(mView  != null)
        {
            mView.OnViewEsconderLoader();
           // mView.OnViewChecklistAdded();
        }
    }

    public void onDeestroy()
    {
        mView = null;
        mInteractor = null;
    }
}
