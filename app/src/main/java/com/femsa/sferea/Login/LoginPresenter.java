package com.femsa.sferea.Login;

import com.femsa.requestmanager.Response.Login.UserResponseData;

public class LoginPresenter implements LoginInteractor.OnLoginInteractorListener {

    private LoginView mView;

    private LoginInteractor mInteractor;

    public LoginPresenter(LoginView view, LoginInteractor interactor)
    {
        mInteractor = interactor;
        mView = view;
    }

    public void onDestroy()
    {
        mView = null;
    }


    @Override
    public void OnInteractorActiveSession() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnActiveSession();
            }
    }

    @Override
    public void OnInteractorWrongCredetials() {
        if(mView != null)
        {
            mView.OnHideLoader();
            mView.OnWrongCredentials();
        }
    }

    @Override
    public void OnInteractorSuccesfulLogin(UserResponseData usuarioDTO) {
        if(mView != null)
        {
            mView.OnHideLoader();
            mView.OnSuccessFulLogin(usuarioDTO);
        }
    }


    @Override
    public void OnInteractorServerError() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnShowMessage(2);
            }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnShowMessage(3);
            }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnShowMessage(4);
            }
    }

    @Override
    public void OnInteractorDoLogin(String employee, String password) {
        if(mView != null)
            {
                mView.OnShowLoader();
                mInteractor.doLogin(employee, password, this);
            }
    }

    @Override
    public void OnInteractorSuccessMail() {
        if(mView != null)
        {
            mView.OnHideLoader();
            mView.OnSuccessMail();
        }
    }

    @Override
    public void OnInteractorWrongMail() {
        if(mView != null)
        {
            mView.OnHideLoader();
            mView.OnFailMail();
        }
    }

    @Override
    public void OnInteractorDoMail(String mail) {
        if(mView != null)
        {
            mView.OnShowLoader();
            mInteractor.sendEmail(mail, this);
        }
    }

    @Override
    public void OnInteractorBlocked() {
        if(mView != null)
        {
            mView.OnHideLoader();
            mView.OnBlocked();
        }
    }
}
