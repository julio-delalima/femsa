package com.femsa.sferea.home.miCuenta.miRanking;

import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingTierlistResponseData;

import java.util.ArrayList;

public interface RankingView {

    void onShowLoader();

    void onHideLoader();

    void onRankingSuccess(RankingResponseData data);

    void onProgramRankingSuccess(ProgramRankingResponseData data);

    void onShowMessage(int messageType);

    void onShowMessage(int messageType, int codigo);

    void onRankingTierListSuccess(RankingTierlistResponseData data);

    void onRankingLikesuccess();

    /**Cuando el token del usuario expire se mandará a llamar este método*/
    void onRankingNoAuth();

    void onRankingNoInternet();

    void onViewDesplegarlistadoPaises(PaisesResponseData data);
}
