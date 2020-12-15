package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.CharlaConExperto;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.VedeoCharlaConExpertoResponseData;
import com.femsa.sferea.Utilities.DialogManager;

public class VedeoCharlaConExpertoPresenter implements VedeoCharlaConExpertoInteractor.OnVedeoCharlaConExpertoInteractorListener{

    private VedeoCharlaConExpertoView mVedeoView;

    private VedeoCharlaConExpertoInteractor mInteractor;

    public VedeoCharlaConExpertoPresenter(VedeoCharlaConExpertoView vedeoView, VedeoCharlaConExpertoInteractor interactor) {
        mVedeoView = vedeoView;
        mInteractor = interactor;
    }


    @Override
    public void OnInteractorServerError() {
        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoOcultaLoader();
            mVedeoView.OnVedeoCharlaConExpertoMuestraMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoOcultaLoader();
            mVedeoView.OnVedeoCharlaConExpertoMuestraMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoOcultaLoader();
            mVedeoView.OnVedeoCharlaConExpertoMuestraMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void OnInteractorTraeVedeoCharlaConExperto(int idObjeto, String token) {

        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoCargaLoader();
            mInteractor.CallVedeoCharlaConExperto(idObjeto, token, this);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoOcultaLoader();
            mVedeoView.OnVedeoCharlaConExpertoMuestraMensaje(DialogManager.EXPIRED_TOKEN);
        }
    }

    @Override
    public void OnInteractorMensaje(int mensaje) {
        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoOcultaLoader();
            mVedeoView.OnVedeoCharlaConExpertoMuestraMensaje(mensaje);
        }
    }

    @Override
    public void OnInteractorMensaje(int mensaje, int codigo) {
        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoOcultaLoader();
            mVedeoView.OnVedeoCharlaConExpertoMuestraMensaje(DialogManager.UNEXPECTED_ERROR, codigo);
        }
    }

    @Override
    public void OnInteractorInsertarVedeoCharla(int idObjeto, int idHora, String token) {
        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoCargaLoader();
            mInteractor.InsertarVedeoCharlaConExperto(idObjeto, idHora, token, this);
        }
    }

    @Override
    public void OnInteractorVedeocharlaInsertadaBuenisimo() {
        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoOcultaLoader();
            mVedeoView.OnVedeoCharlaConExpertoInsertada();
        }
    }

    @Override
    public void OnInteractorVedeoCharlaConExperto(VedeoCharlaConExpertoResponseData data) {
        if(mVedeoView != null)
        {
            mVedeoView.OnVedeoCharlaConExpertoOcultaLoader();
            mVedeoView.OnVedeoCharlaConExpertoCargaDatos(data);
        }
    }

    @Override
    public void OnInteractorFechaInapartable() {
        if(mVedeoView != null){
            mVedeoView.OnVedeoCharlaConExpertoOcultaLoader();
            mVedeoView.OnVedeoCharlaConExpertoInapartable();
        }
    }
}
