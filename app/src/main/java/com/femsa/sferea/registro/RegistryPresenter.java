package com.femsa.sferea.registro;

import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.sferea.Utilities.DialogManager;

public class RegistryPresenter implements RegistryInteractor.OnRegistryInteractor {

    private RegistryView mView;

    private RegistryInteractor mInteractor;

    public RegistryPresenter(RegistryView view, RegistryInteractor interactor) {
        mInteractor = interactor;
        mView = view;
    }

    public void onDestroy() {
        mView = null;
    }


    @Override
    public void OnInteractorUserNotExists() {
        if (mView != null) {
            mView.OnHideLoader();
            mView.OnEmployeeNotExists();
        }
    }

    @Override
    public void OnInteractorUserExists(String employee) {
        if (mView != null) {
            mView.OnHideLoader();
            mView.OnEmployeeExists(employee);
        }
    }

    @Override
    public void OnInteractorServerError() {
        if (mView != null) {
            mView.OnHideLoader();
            mView.OnMostrarMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if (mView != null) {
            mView.OnHideLoader();
            mView.OnMostrarMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if (mView != null) {
            mView.OnHideLoader();
            mView.OnMostrarMensaje(DialogManager.NO_INTERNET);
        }
    }

    @Override
    public void OnInteractorCheckEmployeeExistance(String employee) {
        if (mView != null) {
            mView.OnShowLoader();
            mInteractor.checkEmployeeExistance(employee, this);
        }
    }

    @Override
    public void OnInteractorDoUniqueLogin(String employeeNumber) {
        if(mView != null)
        {
            mView.OnShowLoader();
            mInteractor.doUniqueLogin(employeeNumber, this);
        }
    }

    @Override
    public void OnInteractorSuccessfulLogin(UserResponseData data) {
        if(mView != null)
        {
            mView.OnHideLoader();
            mView.OnUniqueLoginSuccess(data);
        }
    }

    @Override
    public void OnInteractorActiveSession() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnNoAuth();
                mView.OnMostrarMensaje(DialogManager.EXPIRED_TOKEN);
            }
    }

    @Override
    public void OnInteractorCheckDate(String date, int id) {
        if(mView != null)
            {
                mView.OnShowLoader();
                mInteractor.validateDate(date, id, this);
            }
    }

    @Override
    public void OnInteractorsuccessfulDate() {
        if(mView != null)
        {
            mView.OnHideLoader();
            mView.OnDateSuccess();
        }
    }

    @Override
    public void OnInteractorDateFailure() {
        if(mView != null)
        {
            mView.OnHideLoader();
            mView.OnDateFailure();
        }
    }
}