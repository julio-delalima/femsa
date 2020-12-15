package com.femsa.sferea.home.miCuenta.miRanking;

import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingTierlistResponseData;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;

public class RankingPresenter implements RankingInteractor.OnRankingInteractorListener {

    private RankingView mView;

    private RankingInteractor mInteractor;

    public RankingPresenter(RankingView view, RankingInteractor interactor)
    {
        mView = view;

        mInteractor = interactor;
    }

    @Override
    public void OnInteractorSuccessFullRanking(RankingResponseData data) {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onRankingSuccess(data);
        }
    }

    @Override
    public void OnInteractorSuccessfulProgramRanking(ProgramRankingResponseData data) {
        if(mView != null)
            {
                mView.onHideLoader();
                mView.onProgramRankingSuccess(data);
            }
    }

    @Override
    public void OnInteractorSuccesfulTierList(RankingTierlistResponseData data) {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onRankingTierListSuccess(data);
        }
    }

    @Override
    public void OnInteractorsuccesfulLike() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onRankingLikesuccess();
        }
    }

    @Override
    public void OnInteractorServerError() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onShowMessage(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onShowMessage(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onShowMessage(DialogManager.NO_INTERNET);
            mView.onRankingNoInternet();
        }
    }

    @Override
    public void OnInteractorCallRanking() {
        if(mView != null)
        {
            mView.onShowLoader();
            mInteractor.CallRanking(this);
        }
    }

    @Override
    public void OnInteractorCallProgramRanking() {
        if(mView != null)
            {
                mInteractor.callProgramsRanking(this);
            }
    }

    @Override
    public void OnInteractorCallRankingList(int idProgram) {
        if(mView != null)
        {
            mView.onShowLoader();
            mInteractor.callRankingList(this, idProgram);
        }
    }

    @Override
    public void OnInteractorNoAuth() {
        if(mView != null)
        {
            mView.onHideLoader();
            mView.onShowMessage(DialogManager.EXPIRED_TOKEN);
            mView.onRankingNoAuth();
        }
    }

    @Override
    public void OnInteractorCallLike(int idProgram, int idEmployee) {
        if(mView != null)
        {
            mView.onShowLoader();
            mInteractor.callLike(this, idProgram, idEmployee);
        }
    }

    public void onDestroy()
    {
        mView = null;
    }

    @Override
    public void onInteractorTraerPaises(String token) {
        if(mView != null)
        {
            mInteractor.callListadoPaises(token, this);
        }
    }

    @Override
    public void onInteractorErrorInesperado(int codigo) {
        if(mView != null)
        {
            mView.onShowMessage(StringManager.UNEXPECTED_ERROR, codigo);
        }
    }

    @Override
    public void onInteractorDesplegarPaises(PaisesResponseData data) {
        if(mView != null)
            {
                mView.onViewDesplegarlistadoPaises(data);
            }
    }
}
