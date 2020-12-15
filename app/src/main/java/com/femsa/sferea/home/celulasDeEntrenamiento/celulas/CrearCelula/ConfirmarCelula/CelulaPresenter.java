package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.ConfirmarCelula;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.sferea.Utilities.DialogManager;

public class CelulaPresenter implements CelulaInteractor.OnCelulaInteractorListener{

    private CelulaView mView;

    private CelulaInteractor mInteractor;

    public CelulaPresenter(CelulaView view, CelulaInteractor interactor)
        {
            mView = view;
            mInteractor = interactor;
        }


    @Override
    public void OnInteractorLlamarCelula(CelulaDTO dto, String tipo, int idSolicitante, String token) {
        if(mView != null)
            {
                mView.OnViewMostrarLoader();
                mInteractor.CallCelula(dto,tipo, idSolicitante, token, this);
            }
    }

    @Override
    public void OnInteractorCelulaExitosa() {
        if(mView != null)
        {
            mView.OnViewOcultarLoader();
            mView.OnViewMostrarSolicitudExitosa();
        }
    }

    @Override
    public void OnInteractorServerError() {
        if(mView != null)
        {
            mView.OnViewOcultarLoader();
            mView.OnViewMostrarMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
        {
            mView.OnViewOcultarLoader();
            mView.OnViewMostrarMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
        {
            mView.OnViewOcultarLoader();
            mView.OnViewMostrarMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void OnInteractorUnexpectedError(int errorCode) {
        if(mView != null)
        {
            mView.OnViewOcultarLoader();
            mView.OnViewMostrarMensaje(DialogManager.UNEXPECTED_ERROR, errorCode);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mView != null)
        {
            mView.OnViewOcultarLoader();
            mView.OnViewMostrarMensaje(DialogManager.EXPIRED_TOKEN);
            mView.OnViewSesionInvalida();
        }
    }

    @Override
    public void onViewDesplegarlistadoPaises(PaisesResponseData data) {
        mView.onViewDesplegarlistadoPaises(data);
    }

    @Override
    public void onInteractorTraerPaises(String token) {
        mInteractor.callListadoPaises(token, this);
    }
}
