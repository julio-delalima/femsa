package com.femsa.sferea.home.miCuenta.miPerfil;

import com.femsa.requestmanager.DTO.User.Image.ImageResponseData;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

public class ProfilePresenter implements ProfileInteractor.OnProfileInteractorListener{

    private ProfileView mView;

    private ProfileInteractor mInteractor;

    public ProfilePresenter(ProfileView view, ProfileInteractor interactor)
    {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void OnInteractorSuccesfulChange(ImageResponseData data) {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnSuccessfulChange(data.getmUser().getNewPic());
                mView.OnMostrarToast(R.string.image_actualizada);
            }
    }

    @Override
    public void OnInteractorServerError() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnMostrarMensaje(DialogManager.SERVER_ERROR);
            }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnMostrarMensaje(DialogManager.TIMEOUT);
            }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnMostrarMensaje(DialogManager.NO_INTERNET);
            }
    }

    @Override
    public void OnInteractorChangeImage(String image) {
        if(mView != null)
            {
                mView.OnShowLoader();
                mInteractor.changeImage(image, this);
            }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnNoAuth();
            }
    }

    @Override
    public void OnInteractorActualizaCorreo(String correo) {
        if(mView != null)
        {
            mView.OnShowLoader();
            mInteractor.callActualizaCorreo(correo, SharedPreferencesUtil.getInstance().getTokenUsuario(), this);
        }
    }

    @Override
    public void OnInteractorActualizaCorreoExitoso() {
        if(mView != null)
        {
            mView.OnHideLoader();
            mView.OnCorreoActualizado();
        }
    }

    @Override
    public void OnInteractorErrorInesperado(int codigoRespuesta) {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnMostrarMensaje(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
            }
    }

    @Override
    public void OnInteractorCorreoYaRegistrado() {
        if(mView != null)
            {
                mView.OnHideLoader();
                mView.OnCorreoYaRegistrado();
            }
    }

    public void onDestroy()
    {
        mView = null;
    }
}
