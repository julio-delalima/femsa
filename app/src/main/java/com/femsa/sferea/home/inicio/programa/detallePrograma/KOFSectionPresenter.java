package com.femsa.sferea.home.inicio.programa.detallePrograma;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectResponseData;
import com.femsa.requestmanager.Response.Program.ProgramDetailResponseData;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

public class KOFSectionPresenter implements KOFSectionInteractor.OnKOFInteractorListener {

    private KOFSectionView mView;

    private KOFSectionInteractor mInteractor;

    public KOFSectionPresenter(KOFSectionView view, KOFSectionInteractor interactor)
    {
        mView = view;

        mInteractor = interactor;
    }

    @Override
    public void OnInteractorNoContent() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.onMostrarMensaje(DialogManager.EMPTY);
        }
    }

    @Override
    public void OnInteractorServerError() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.onMostrarMensaje(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.onMostrarMensaje(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.onMostrarMensaje(DialogManager.NO_INTERNET);
            mView.onNoInternet();
        }
    }

    @Override
    public void OnInteractorUnexpectedError(int errorCode) {
        if(mView != null)
        {
            mView.hideLoader();
            mView.onMostrarMensajeInesperado(DialogManager.UNEXPECTED_ERROR, errorCode);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mView != null)
        {
            mView.hideLoader();
            mView.onMostrarMensaje(DialogManager.EXPIRED_TOKEN);
            mView.onNoAuth();
        }
    }

    /***
     * Success
     * @param data
     */
    @Override
    public void OnInteractorProgramDetailSucess(ProgramDetailResponseData data) {
        if(mView != null)
        {
            mView.hideLoader();
            mView.getProgramDetailSuccess(data);
        }
    }

    /**
     * Callers
     *
     * @param idProgram
     * @param token
     */
    @Override
    public void OnInteractorCallProgramDetail(int idProgram, String token) {
        if(mView != null)
        {
            mView.showLoader();
            mInteractor.OnCallProgramDetail(idProgram, token, this);
        }
    }

    /**
     * Llamada que hace la petición para dar like a un programa
     *
     * @param idProgram
     */
    @Override
    public void OnInteractorLike(int idProgram, String token) {
        if(mView != null)
            {
                mView.showLoader();
                mInteractor.OnLikeProgram(idProgram, token, this);
            }
    }

    /**
     * Llamada en caso de que la petición de dar like sea exitosa
     */
    @Override
    public void OnInteractorSuccessLike() {
        if(mView != null)
            {
                mView.hideLoader();
                mView.likeSuccess();
            }
    }

    /**
     * Método que nos trae todos los objetos de aprendizaje de cada programa recibiendo únicamente el id del programa
     *
     * @param data
     */
    @Override
    public void OnInteractorAllObjectsSuccess(ObjectResponseData data) {
        if(mView != null)
            {
                mView.hideLoader();
                mView.getObjectsSuccess(data);
            }
    }

    /**
     * Método para mandar a traer todos los objetos de aprendizaje
     *
     * @param idProgram
     * @param token
     */
    @Override
    public void OnInteractorCallLearningObject(int idProgram, String token) {
        if(mView != null)
            {
                mView.showLoader();
                mInteractor.OnCallObjects(idProgram, token, this);
            }
        }

    /**
     * Método pára inscribirte a un programa.
     *
     * @param idPrograma ID del programa al que se quiere inscribir.
     */
    @Override
    public void OnInteractorHacerInscripcion(int idPrograma) {
        if(mView != null)
            {
                mView.showLoader();
                mInteractor.callInscripcion(idPrograma, SharedPreferencesUtil.getInstance().getTokenUsuario(), this);
            }
    }

    /**
     * Método que se ejecuta cuando se logra realizar una inscripción a programa exitosa.
     */
    @Override
    public void OnInteractorInscripcionExitosa() {
        if(mView != null)
            {
                mView.hideLoader();
                mView.onInscripcionExitosa();
            }
    }


    public void onDestroy()
    {
        mView = null;
    }
}
