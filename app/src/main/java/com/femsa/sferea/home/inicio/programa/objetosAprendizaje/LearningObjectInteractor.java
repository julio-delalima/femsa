package com.femsa.sferea.home.inicio.programa.objetosAprendizaje;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.DetalleObjeto.ObjectDetailRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.DetalleObjeto.ObjetoMarkAsReadRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.DetalleObjeto.ObtenerBonusObjetoRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.DescargaZipRequest;
import com.femsa.requestmanager.Request.Program.LikeRequest;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.io.InputStream;

public class LearningObjectInteractor {

    private OnObjectInteractorListener mListener;

    public interface OnObjectInteractorListener
    {
        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorNoAuth();

        void OnInteractorCallObjectDetail(int idObject);

        void OnInteractorObjectSuccess(ObjectDetailResponseData data);

        void OnInteractorSuccessLike();

        void OnInteractorErrorInesperado(int codigoError);

        void OnInteractorCallBonus(int idObjeto, String token);

        void OnInteractorBonusSuccess();

        void OnInteractorMarkAsRead(int idObjeto, String token);

        void OnInteractorMarkAsReadExitoso();

        /**
         * Llamada que hace la petición para dar like a un objeto
         * */
        void OnInteractorLike(int idObject);

        void OnInteractorCallJuego(int idObjeto);

        void OnInteractorJuegoRecibido(InputStream zip, int buffer);

    }

    public void OnCallObjectDetail(int idObject, OnObjectInteractorListener listener)
    {
        mListener = listener;

        ObjectDetailRequest object = new ObjectDetailRequest(AppTalentoRHApplication.getApplication());
        object.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), idObject, new ObjectDetailRequest.ObjectDetailResponseContract() {
            @Override
            public void OnDetailSuccess(ObjectDetailResponseData data) {
                mListener.OnInteractorObjectSuccess(data);
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


    public void OnLikeProgram(int id, String token, OnObjectInteractorListener listener)
    {
        mListener = listener;
        /**
         * Método para realizar la petición de los likes
         * @param id ID del objeto o del programa
         * @param token token del usuario
         * @param listener listener de respuestas
         * @param likeType tipo de like que se va a dar, si es 0: Detalle Programa, 1: Objeto Aprendizaje
         * */
        LikeRequest like = new LikeRequest(AppTalentoRHApplication.getApplication());
        like.makeRequest(id, token, new LikeRequest.OnLikeResponseContract() {
            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnLikeSuccess() {
                mListener.OnInteractorSuccessLike();
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
        }, 1);
    }

    public void CallBonus(int idObjeto, String token, OnObjectInteractorListener listener)
    {
        mListener = listener;
        ObtenerBonusObjetoRequest bonusObjetoRequest = new ObtenerBonusObjetoRequest(AppTalentoRHApplication.getApplication());
        bonusObjetoRequest.makeRequest(idObjeto, token, new ObtenerBonusObjetoRequest.ObtenerBonusObjetoResponseContract() {
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

    public void callMarkAsRead(int idObjeto, String token, OnObjectInteractorListener listener)
        {
            mListener = listener;
            ObjetoMarkAsReadRequest readRequest = new ObjetoMarkAsReadRequest(AppTalentoRHApplication.getApplication());
            readRequest.makeRequest(idObjeto, token, new ObjetoMarkAsReadRequest.ObjetoMarkAsReadResponseContract() {
                @Override
                public void onResponseBonusCorrecto() {
                    mListener.OnInteractorMarkAsReadExitoso();
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

    public void callJuego(int idObjeto, String token, OnObjectInteractorListener listener) {
        mListener = listener;
        DescargaZipRequest requ = new DescargaZipRequest(AppTalentoRHApplication.getApplication());
        requ.makeRequest(token, idObjeto, new DescargaZipRequest.DescargaZipResponseContract() {
            @Override
            public void OnDescargado(InputStream zip, int buffer) {
                mListener.OnInteractorJuegoRecibido(zip, buffer);
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

}
