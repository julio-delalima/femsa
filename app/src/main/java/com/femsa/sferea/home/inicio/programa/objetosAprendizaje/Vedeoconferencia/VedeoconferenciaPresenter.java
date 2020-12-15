package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Vedeoconferencia;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.VedeoconferenciaResponseData;
import com.femsa.sferea.Utilities.DialogManager;

public class VedeoconferenciaPresenter implements VedeoconferenciaInteractor.OnVedeoconferenciaInteractorListener{

    private VedeoconferenciaView mVedeoView;

    private VedeoconferenciaInteractor mInteractor;

    public VedeoconferenciaPresenter(VedeoconferenciaView vedeoView, VedeoconferenciaInteractor interactor) {
        mVedeoView = vedeoView;
        mInteractor = interactor;
    }


    @Override
    public void OnInteractorServerError() {
        if(mVedeoView != null)
            {
                mVedeoView.OnVedeoconferenciaOcultaLoader();
                mVedeoView.OnVedeoconferenciaMensaje(DialogManager.SERVER_ERROR);
            }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mVedeoView != null)
            {
                mVedeoView.OnVedeoconferenciaOcultaLoader();
                mVedeoView.OnVedeoconferenciaMensaje(DialogManager.TIMEOUT);
            }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mVedeoView != null)
            {
                mVedeoView.OnVedeoconferenciaOcultaLoader();
                mVedeoView.OnVedeoconferenciaMensaje(DialogManager.NO_INTERNET);
            }
    }

    @Override
    public void OnInteractorTraeVedeoconferencia(int idObjeto, String token) {

        if(mVedeoView != null)
            {
                mVedeoView.OnVedeoconferenciaMuestraLoader();
                mInteractor.CallVedeoconferencia(idObjeto, token, this);
            }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mVedeoView != null)
            {
                mVedeoView.OnVedeoconferenciaOcultaLoader();
                mVedeoView.OnVedeoconferenciaMensaje(DialogManager.EXPIRED_TOKEN);
            }
    }

    @Override
    public void OnInteractorMensaje(int mensaje) {
        if(mVedeoView != null)
            {
                mVedeoView.OnVedeoconferenciaOcultaLoader();
                mVedeoView.OnVedeoconferenciaMensaje(mensaje);
            }
    }

    @Override
    public void OnInteractorMensaje(int mensaje, int codigo) {
        if(mVedeoView != null)
            {
                mVedeoView.OnVedeoconferenciaOcultaLoader();
                mVedeoView.OnVedeoconferenciaMensaje(DialogManager.UNEXPECTED_ERROR, codigo);
            }
    }

    @Override
    public void OnInteractorVedeoconferencia(VedeoconferenciaResponseData data) {
        if(mVedeoView != null)
            {
                mVedeoView.OnVedeoconferenciaOcultaLoader();
                mVedeoView.OnVedeoconferenciaAcomodaData(data);
            }
    }
}
