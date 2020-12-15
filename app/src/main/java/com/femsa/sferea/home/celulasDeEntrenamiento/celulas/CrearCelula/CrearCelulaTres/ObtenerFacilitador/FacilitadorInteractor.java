package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.ObtenerFacilitador;


import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.AreaDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Facilitador.ObtenerAreaRequest;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Facilitador.ObtenerFacilitadorPorAreaRequest;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.Area.AreaData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.FacilitadorData.FacilitadorData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

import java.util.ArrayList;

public class FacilitadorInteractor {

    private OnFacilitadorInteractorListener mListener;

    public interface OnFacilitadorInteractorListener
    {
        void OnInteractorTraeAreas(String token);

        void OnInteractorMuestraAreas(AreaDTO data);

        void OnInteractorTraeFacilitadores(ArrayList<Integer> areas, int idPaisReceptor, String token, String palabraClave);

        void OnInteractorMuestraFacilitadores(FacilitadorData facilitadores);

        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorNoAuth();

        void OnInteractorUnexpectedError(int codigoRespuesta);

        void OnInteractorNoContent();
    }

    public void CallArea(String token, OnFacilitadorInteractorListener listener)
        {
            mListener = listener;
            ObtenerAreaRequest request = new ObtenerAreaRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(token, new ObtenerAreaRequest.OnResponseObtenerArea() {
                @Override
                public void onResponseObtenerArea(AreaData data) {
                    mListener.OnInteractorMuestraAreas(data.getmAreas());
                }

                @Override
                public void onResponseTokenInvalido() {
                    mListener.OnInteractorNoAuth();
                }

                @Override
                public void onResponseErrorInesperado(int codigoRespuesta) {
                    mListener.OnInteractorUnexpectedError(codigoRespuesta);
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

    public void CallFacilitador(String token, ArrayList<Integer> idAreasArray, int idPaisReceptor, String palabraClave, OnFacilitadorInteractorListener listener)
        {
            mListener = listener;

            ObtenerFacilitadorPorAreaRequest request = new ObtenerFacilitadorPorAreaRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(token, idAreasArray, palabraClave, idPaisReceptor, new ObtenerFacilitadorPorAreaRequest.OnResponseFacilitadorPorArea() {
                @Override
                public void onResponseFacilitadorPorArea(FacilitadorData data) {
                    if(data.getFacilitadores().getListadoFacilitadores().size() < 1)
                        {
                            mListener.OnInteractorNoContent();
                        }
                    else
                        {
                            mListener.OnInteractorMuestraFacilitadores(data);
                        }
                }

                @Override
                public void onResponseTokenInvalido() {
                    mListener.OnInteractorNoAuth();
                }

                @Override
                public void onResponseErrorInesperado(int codigoRespuesta) {
                    mListener.OnInteractorUnexpectedError(codigoRespuesta);
                }

                @Override
                public void onResponseSinFacilitadores() {
                    mListener.OnInteractorNoContent();
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
