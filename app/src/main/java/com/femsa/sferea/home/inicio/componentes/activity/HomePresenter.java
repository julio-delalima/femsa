package com.femsa.sferea.home.inicio.componentes.activity;

import android.os.CountDownTimer;

import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

public class HomePresenter implements  HomeInteractor.OnHomeInteractorListener{

    private HomeView mHomeView;

    private HomeInteractor mInteractor;

    public HomePresenter(HomeView homeView, HomeInteractor interactor) {
        mHomeView = homeView;
        mInteractor = interactor;
    }


    @Override
    public void OnInteractorSuccessfulLogout() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onLogOutSuccess();
        }
    }

    @Override
    public void OnInteractorMensajeInesperado(int codigoRespuesta) {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
        }
    }

    @Override
    public void OnInteractorSetTokenFirebase(String tokenFirebase, String tipoDispositivo) {
        if(mHomeView != null) {
            mInteractor.callTokenFirebase(tokenFirebase, SharedPreferencesUtil.getInstance().getTokenUsuario(), tipoDispositivo, this);
        }
    }

    @Override
    public void OnInteractorTokenFirebaseDefinido() {

    }

    @Override
    public void OnInteractorLogOut(String userToken) {
        if(mHomeView != null) {
            mHomeView.onShowLoader();
            mInteractor.OnCallLogOut(userToken, this);
        }
    }

    @Override
    public void OnInteractorServerError() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void OnInteractorRequestFailure() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mHomeView != null)
            {
                mHomeView.onHideLoader();
                mHomeView.onShowMessage(DialogManager.EXPIRED_TOKEN);
                CountDownTimer c = new CountDownTimer(2000,1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        mHomeView.onLogOutSuccess();
                    }
                };
                c.start();
            }
    }

    public void onDestroy(){
        mHomeView = null;
    }

}
