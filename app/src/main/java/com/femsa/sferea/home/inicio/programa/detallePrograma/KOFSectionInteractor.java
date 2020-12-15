package com.femsa.sferea.home.inicio.programa.detallePrograma;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.ObjectRequest;
import com.femsa.requestmanager.Request.Program.InscripcionProgramaRequest;
import com.femsa.requestmanager.Request.Program.LikeRequest;
import com.femsa.requestmanager.Request.Program.ProgramDetailRequest;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectResponseData;
import com.femsa.requestmanager.Response.Program.ProgramDetailResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

public class KOFSectionInteractor {

    private OnKOFInteractorListener mlistener;

    public interface OnKOFInteractorListener
    {
        void OnInteractorNoContent();

        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorUnexpectedError(int errorCode);

        void OnInteractorNoAuth();

        /***
         * Success
         */
        void OnInteractorProgramDetailSucess(ProgramDetailResponseData data);

        /**
         * Callers
         * */
        void OnInteractorCallProgramDetail(int idProgram, String token);

        /**
         * Llamada que hace la petición para dar like a un programa
         * */
        void OnInteractorLike(int idProgram, String token);

        /**
         * Llamada en caso de que la petición de dar like sea exitosa
         * */
        void OnInteractorSuccessLike();

        /**
         *Método que nos trae todos los objetos de aprendizaje de cada programa recibiendo únicamente el id del programa*/
        void OnInteractorAllObjectsSuccess(ObjectResponseData data);

        /**
         * Método para mandar a traer todos los objetos de aprendizaje
         * */
        void OnInteractorCallLearningObject(int idProgram, String token);

        /**
         * Método pára inscribirte a un programa.
         * @param idPrograma ID del programa al que se quiere inscribir.
         * */
        void OnInteractorHacerInscripcion(int idPrograma);

        /**
         * Método que se ejecuta cuando se logra realizar una inscripción a programa exitosa.
         * */
        void OnInteractorInscripcionExitosa();
    }

    public void OnCallProgramDetail(int idProgram, String token, OnKOFInteractorListener listener)
    {
        mlistener = listener;
        ProgramDetailRequest pdRequest = new ProgramDetailRequest(AppTalentoRHApplication.getApplication());
        pdRequest.makeRequest(token, idProgram, new ProgramDetailRequest.ProgramDetailResponseContract() {

            @Override
            public void onResponseErrorServidor() {
                mlistener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mlistener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mlistener.OnInteractorTimeout();
            }

            @Override
            public void OnDetailSuccess(ProgramDetailResponseData data) {
                mlistener.OnInteractorProgramDetailSucess(data);
            }

            @Override
            public void OnNoAuth() {
                mlistener.OnInteractorNoAuth();
            }

            @Override
            public void OnNoContent() {
                mlistener.OnInteractorNoContent();
            }

            @Override
            public void OnUnexpectedError(int errorCode) {
                mlistener.OnInteractorUnexpectedError(errorCode);
            }
        });

    }

    public void OnLikeProgram(int id, String token, OnKOFInteractorListener listener)
    {
        mlistener = listener;
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
                    mlistener.OnInteractorNoAuth();
                }

                @Override
                public void OnLikeSuccess() {
                    mlistener.OnInteractorSuccessLike();
                }

                @Override
                public void onResponseErrorServidor() {
                    mlistener.OnInteractorServerError();
                }

                @Override
                public void onResponseSinConexion() {
                    mlistener.OnInteractorNoInternet();
                }

                @Override
                public void onResponseTiempoAgotado() {
                    mlistener.OnInteractorTimeout();
                }
            }, 0);
    }

    public void OnCallObjects(int idProgram, String token, OnKOFInteractorListener listener)
    {
        mlistener = listener;

        ObjectRequest request = new ObjectRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idProgram, new ObjectRequest.ObjectResponseContract() {
            @Override
            public void OnObjectSuccess(ObjectResponseData data) {
                mlistener.OnInteractorAllObjectsSuccess(data);
            }

            @Override
            public void OnNoAuth() {
                mlistener.OnInteractorNoAuth();
            }

            @Override
            public void OnNoContent() {
                mlistener.OnInteractorNoContent();
            }

            @Override
            public void OnUnexpectedError(int errorCode) {
                mlistener.OnInteractorUnexpectedError(errorCode);
            }

            @Override
            public void onResponseErrorServidor() {
                mlistener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mlistener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mlistener.OnInteractorTimeout();
            }
        });
    }

    public void callInscripcion(int idPrograma, String token, OnKOFInteractorListener listener)
    {
        mlistener = listener;
        InscripcionProgramaRequest request = new InscripcionProgramaRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(idPrograma, token, new InscripcionProgramaRequest.OnInscripcionProgramaContract() {
            @Override
            public void OnNoAuth() {
                mlistener.OnInteractorNoAuth();
            }

            @Override
            public void OnInscripcionSuccess() {
                mlistener.OnInteractorInscripcionExitosa();
            }

            @Override
            public void OnErrorInesperado(int codigoRespuesta) {
                mlistener.OnInteractorUnexpectedError(codigoRespuesta);
            }

            @Override
            public void onResponseErrorServidor() {
                mlistener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mlistener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mlistener.OnInteractorTimeout();
            }
        });
    }
}
