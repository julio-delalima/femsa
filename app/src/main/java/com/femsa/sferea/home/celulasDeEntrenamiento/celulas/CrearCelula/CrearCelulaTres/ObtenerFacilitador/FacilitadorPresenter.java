package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.ObtenerFacilitador;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.AreaDTO;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.FacilitadorData.FacilitadorData;
import com.femsa.sferea.Utilities.DialogManager;

import java.util.ArrayList;

public class FacilitadorPresenter implements FacilitadorInteractor.OnFacilitadorInteractorListener{

    private FacilitadorView mView;

    private FacilitadorInteractor mInteractor;

    public FacilitadorPresenter(FacilitadorView view, FacilitadorInteractor interactor)
        {
            mView = view;
            mInteractor = interactor;
        }


    @Override
    public void OnInteractorTraeAreas(String token) {
        if(mView != null)
            {
                mView.onFacilitadorViewShowLoader();
                mInteractor.CallArea(token, this);
            }
    }

    @Override
    public void OnInteractorMuestraAreas(AreaDTO data) {
        if(mView != null)
        {
            mView.onFacilitadorViewHideLoader();
            mView.onFacilitadorViewMuestraAreas(data);
        }
    }

    @Override
    public void OnInteractorTraeFacilitadores(ArrayList<Integer> areas, int idPaisReceptor, String token, String palabraClave) {
        if(mView != null)
        {
            mView.onFacilitadorViewShowLoader();
            mInteractor.CallFacilitador(token, areas, idPaisReceptor, palabraClave,this);
        }
    }

    @Override
    public void OnInteractorMuestraFacilitadores(FacilitadorData facilitadores) {
        if(mView != null)
        {
            mView.onFacilitadorViewHideLoader();
            mView.onFacilitadorViewMuestraFacilitadores(facilitadores);
        }
    }

    @Override
    public void OnInteractorServerError() {
        if(mView != null)
        {
            mView.onFacilitadorViewHideLoader();
            mView.onFacilitadorViewMuestraMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
        {
            mView.onFacilitadorViewHideLoader();
            mView.onFacilitadorViewMuestraMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
        {
            mView.onFacilitadorViewHideLoader();
            mView.onFacilitadorViewMuestraMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mView != null)
        {
            mView.onFacilitadorViewHideLoader();
            mView.onFacilitadorViewMuestraMensaje(DialogManager.EXPIRED_TOKEN);
            mView.onFacilitadorViewSesionInvalida();
        }
    }

    @Override
    public void OnInteractorUnexpectedError(int codigoRespuesta) {
        if(mView != null)
        {
            mView.onFacilitadorViewHideLoader();
            mView.onFacilitadorViewMuestraMensaje(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
        }
    }

    @Override
    public void OnInteractorNoContent() {
        if(mView != null)
        {
            mView.onFacilitadorViewHideLoader();
            mView.onFacilitadorViewSinFacilitador();
        }
    }
}
