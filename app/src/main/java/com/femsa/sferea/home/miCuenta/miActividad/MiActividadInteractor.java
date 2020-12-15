package com.femsa.sferea.home.miCuenta.miActividad;

import com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros.ObtenerAllLogrosData;
import com.femsa.requestmanager.Request.MyActivity.MiActividadRequest;
import com.femsa.requestmanager.Request.MyActivity.MyActivityRequest;
import com.femsa.requestmanager.Request.MyActivity.ObtenerAllLogrosRequest;
import com.femsa.requestmanager.Request.Ranking.ProgramRankingRequest;
import com.femsa.requestmanager.Response.MiActividad.MiActividadResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class MiActividadInteractor {

    private OnInteractorObtenerAllLogros mListener;

    public interface OnInteractorObtenerAllLogros{
        void onInteractorObtenerAllLogros(ObtenerAllLogrosData datos);
        void onInteractorObtenerDatos(RankingResponseData data);
        void onInteractorObtenerProgramasProgreso(ProgramRankingResponseData data);
        void onInteractorLlamarDatosActividad();
        void onInteractorLlamarProgresoProgramas();
        void onInteractorSesionInvalida();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
        void onInteractorSinConexion();
        void onInteractorMiActividadData(MiActividadResponseData data);
        void onInteractorMostrarMensaje(int error);
        void onInteractorCallMiActividad();
    }

    public void peticionObtenerMiActividadData(OnInteractorObtenerAllLogros listener)
    {
        mListener = listener;
        MiActividadRequest actividadRequest = new MiActividadRequest(AppTalentoRHApplication.getApplication());
        actividadRequest.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), new MiActividadRequest.ActivityResponseContract() {
            @Override
            public void OnActivitySuccess(MiActividadResponseData data) {
                mListener.onInteractorMiActividadData(data);
            }

            @Override
            public void onResponseUnexpectedError(int error) {
                mListener.onInteractorMostrarMensaje(error);
            }

            @Override
            public void OnNoAuth() {
                mListener.onInteractorSesionInvalida();
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.onInteractorErrorServidor();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.onInteractorSinConexion();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.onInteractorTiempoEsperaAgotado();
            }
        });
    }

    public void peticionObtenerAllLogros(OnInteractorObtenerAllLogros listener){
        mListener = listener;
        ObtenerAllLogrosRequest request = new ObtenerAllLogrosRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), new ObtenerAllLogrosRequest.OnResponseObtenerAllLogros() {
            @Override
            public void onResponseObtenerAllLogros(ObtenerAllLogrosData data) {
                mListener.onInteractorObtenerAllLogros(data);
            }

            @Override
            public void onResponseTokenInvalido() {
                mListener.onInteractorSesionInvalida();
            }

            @Override
            public void onResponseSinLogros() {

            }

            @Override
            public void onResponseErrorServidor() {
                mListener.onInteractorErrorServidor();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.onInteractorSinConexion();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.onInteractorTiempoEsperaAgotado();
            }
        });
    }


    public void peticionObtenerDatosActividad(OnInteractorObtenerAllLogros listener)
    {
        mListener = listener;
        MyActivityRequest peticionActividad = new MyActivityRequest(AppTalentoRHApplication.getApplication());
        peticionActividad.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), new MyActivityRequest.ActivityResponseContract() {
            /**
             * <p>Método que indica que el servidor tiene un error y no fue posible obtener la respuesta.</p>
             */
            @Override
            public void onResponseErrorServidor() {
                mListener.onInteractorErrorServidor();
            }

            /**
             * <p>Método que indica que el dispositivo no cuenta con conexión a Internet.</p>
             */
            @Override
            public void onResponseSinConexion() {
                mListener.onInteractorSinConexion();
            }

            /**
             * <p>Método que indica que el tiempo de espera que tiene la configuración del servicio se ha
             * agotado.</p>
             */
            @Override
            public void onResponseTiempoAgotado() {
                mListener.onInteractorTiempoEsperaAgotado();
            }

            @Override
            public void OnActivitySuccess(RankingResponseData data) {
                mListener.onInteractorObtenerDatos(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.onInteractorSesionInvalida();
            }
        });
    }

    /**
     * Petición para traer los valores de progreso en los medidores de Mi Actividad y para mostrar todos los programas
     * activos y en el historial, hace petición a Ranking ya que obtiene los mismos datos.
     * */
    public void peticionObtenerMedidoresProgreso_Y_Programas(OnInteractorObtenerAllLogros listener)
    {
        mListener = listener;
        ProgramRankingRequest peticionRanking = new ProgramRankingRequest(AppTalentoRHApplication.getApplication());
        peticionRanking.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), new ProgramRankingRequest.ProgramRankingResponseContract() {
            @Override
            public void OnRankingSuccess(ProgramRankingResponseData data) {
                mListener.onInteractorObtenerProgramasProgreso(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.onInteractorSesionInvalida();
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.onInteractorErrorServidor();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.onInteractorSinConexion();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.onInteractorTiempoEsperaAgotado();
            }
        });
    }
}
