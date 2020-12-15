package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.DetalleObjeto.ObjetoMarkAsReadRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.VideoInteractivo.VideoInteractivoRequest;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.VideoInteractivoResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class VideoInteractivoInteractor {

    private OnInteractorListener mListener;

    public interface OnInteractorListener
    {
        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorNoAuth();

        void OnInteractorCallVideoInteractivo(int idObject);

        void OnInteractorGetVideoInteractivo(VideoInteractivoResponseData data);

        void OnInteractorErrorInesperado(int codigoRespuesta);

        void OnInteractorCallBonus(int idObjeto, String token);

        void OnInteractorBonusSuccess();
    }

    public void callVideoInteractivo(int idObject, OnInteractorListener listener)
    {
        mListener = listener;

        VideoInteractivoRequest videoRequest = new VideoInteractivoRequest(AppTalentoRHApplication.getApplication());
            videoRequest.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), idObject, new VideoInteractivoRequest.VideoInteractivoResponseContract() {
                @Override
                public void OnVideoInteractivoSuccess(VideoInteractivoResponseData data) {
                    mListener.OnInteractorGetVideoInteractivo(data);
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

    public void CallBonus(int idObjeto, String token,OnInteractorListener listener)
    {
        mListener = listener;
        ObjetoMarkAsReadRequest bonusObjetoRequest = new ObjetoMarkAsReadRequest(AppTalentoRHApplication.getApplication());
        bonusObjetoRequest.makeRequest(idObjeto, token, new ObjetoMarkAsReadRequest.ObjetoMarkAsReadResponseContract() {
            @Override
            public void onResponseBonusCorrecto() {
                mListener.OnInteractorBonusSuccess();
            }

            @Override
            public void onResponseNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void onResponseErrorInesperado(int codigoRespuesta) {
                mListener.OnInteractorErrorInesperado(codigoRespuesta);
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
}
