package com.femsa.sferea.home.miCuenta.miRanking;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Paises.PaisesRequest;
import com.femsa.requestmanager.Request.Ranking.LikeRankingRequest;
import com.femsa.requestmanager.Request.Ranking.ProgramRankingRequest;
import com.femsa.requestmanager.Request.Ranking.RankingRequest;
import com.femsa.requestmanager.Request.Ranking.RankingTierRequest;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingTierlistResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class RankingInteractor {

    private OnRankingInteractorListener mListener;

    public interface OnRankingInteractorListener
    {
        void OnInteractorSuccessFullRanking(RankingResponseData data);

        void OnInteractorSuccessfulProgramRanking(ProgramRankingResponseData data);

        void OnInteractorSuccesfulTierList(RankingTierlistResponseData data);

        void OnInteractorsuccesfulLike();

        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorCallRanking();

        void OnInteractorCallProgramRanking();

        void OnInteractorCallRankingList(int idProgram);

        void OnInteractorNoAuth();

        void OnInteractorCallLike(int idProgram, int idemployee);

        void onInteractorTraerPaises(String token);

        void onInteractorErrorInesperado(int codigo);

        void onInteractorDesplegarPaises(PaisesResponseData data);
    }

    public void CallRanking(OnRankingInteractorListener listener)
    {
        mListener = listener;
        RankingRequest ranking = new RankingRequest(AppTalentoRHApplication.getApplication());
        ranking.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), new RankingRequest.RankingResponseContract() {
            @Override
            public void OnRankingSuccess(RankingResponseData data) {
                mListener.OnInteractorSuccessFullRanking(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }
        });
    }

    public void callProgramsRanking(OnRankingInteractorListener listener)
    {
        mListener = listener;
        ProgramRankingRequest rankingRequest = new ProgramRankingRequest(AppTalentoRHApplication.getApplication());
        rankingRequest.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), new ProgramRankingRequest.ProgramRankingResponseContract() {
            @Override
            public void OnRankingSuccess(ProgramRankingResponseData data) {
                mListener.OnInteractorSuccessfulProgramRanking(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }
        });
    }

    public void callRankingList(OnRankingInteractorListener listener, int idProgram)
    {
        mListener = listener;
        RankingTierRequest tierRequest = new RankingTierRequest(AppTalentoRHApplication.getApplication());
        tierRequest.makeRequest(idProgram, SharedPreferencesUtil.getInstance().getTokenUsuario(), new RankingTierRequest.RankingTierlistResponseContract() {
            @Override
            public void OnRankingSuccess(RankingTierlistResponseData data) {
                mListener.OnInteractorSuccesfulTierList(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }
        });
    }

    public void callLike(OnRankingInteractorListener listener, int idProgram, int idemployee)
    {
        mListener = listener;
        LikeRankingRequest like = new LikeRankingRequest(AppTalentoRHApplication.getApplication());
        like.makeRequest(idProgram, idemployee, SharedPreferencesUtil.getInstance().getTokenUsuario(), new LikeRankingRequest.OnLikeResponseContract() {
            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnLikeSuccess() {
                mListener.OnInteractorsuccesfulLike();
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }
        });
    }


    public void callListadoPaises(String token, OnRankingInteractorListener listener)
    {
        mListener = listener;
        PaisesRequest listaPaises = new PaisesRequest(AppTalentoRHApplication.getApplication());
        listaPaises.makeRequest(token, new PaisesRequest.OnResponseObtenerListadoPaises(){
            /**
             * <p>Método que indica que el servidor tiene un error y no fue posible obtener la respuesta.</p>
             */
            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorNoInternet();
            }

            /**
             * <p>Método que indica que el dispositivo no cuenta con conexión a Internet.</p>
             */
            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            /**
             * <p>Método que indica que el tiempo de espera que tiene la configuración del servicio se ha
             * agotado.</p>
             */
            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }

            @Override
            public void onResponseObtenerListadoPaises(PaisesResponseData data) {
                mListener.onInteractorDesplegarPaises(data);
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void onResponseMuestraMensaje(int codigoRespuesta) {
                mListener.onInteractorErrorInesperado(codigoRespuesta);
            }

            @Override
            public void onResponseSinPaises() {
                mListener.onInteractorErrorInesperado(RequestManager.CODIGO_SERVIDOR.NO_CONTENT);
            }
        });
    }

}
