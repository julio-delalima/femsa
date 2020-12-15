package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Checklist;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.CheckListResponseData;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

import java.util.ArrayList;

public class CheckListPresenter implements CheckListInteractor.OnCheckListInteractorListener {

    private ChecklistView mView;

    private CheckListInteractor mInteractor;

    public CheckListPresenter(ChecklistView view, CheckListInteractor interactor)
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
    public void OnInteractorLlamarCheckList(int idObject) {
        if(mView != null)
        {
            mView.OnViewMostrarLoader();
            mInteractor.callCheckList(idObject, SharedPreferencesUtil.getInstance().getTokenUsuario(), this);
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
    public void OnInteractorMuestraChecklist(CheckListResponseData data) {
        if(mView != null)
        {
            mView.OnViewEsconderLoader();
            mView.OnViewPintaCheckList(data);
        }
    }

    @Override
    public void OnInteractorAddCheckList(int idObjeto, ArrayList<String> data) {
        if(mView != null)
            {
                mView.OnViewMostrarLoader();
                mInteractor.insertCheckList(idObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario(), data, this);
            }
    }

    @Override
    public void OnInteractorCheckListSuccess() {
        if(mView  != null)
            {
                mView.OnViewEsconderLoader();
                mView.OnViewChecklistAdded();
            }
    }

    public void onDeestroy()
        {
            mView = null;
            mInteractor = null;
        }
}
