package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Checklist;

import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Checklist.AddCheckListRequest;
import com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Checklist.ChecklistRequest;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.CheckListResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.DialogManager;

import java.util.ArrayList;

public class CheckListInteractor {

    private OnCheckListInteractorListener mListener;

    public interface OnCheckListInteractorListener
        {
            void OnInteractorServerError();

            void OnInteractorTimeout();

            void OnInteractorNoInternet();

            void OnInteractorNoAuth();

            void OnInteractorLlamarCheckList(int idObject);

            void OnInteractorMuestraMensaje(int tipoMensaje, int codigoRespuesta);

            void OnInteractorMuestraChecklist(CheckListResponseData data);

            void OnInteractorAddCheckList(int idObjeto, ArrayList<String> data);

            void OnInteractorCheckListSuccess();
        }

    public void callCheckList(int idObjeto, String token, OnCheckListInteractorListener listener)
        {
            mListener = listener;
            ChecklistRequest request = new ChecklistRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(token, idObjeto, new ChecklistRequest.CheckListResponseContract() {
                @Override
                public void OnChecklistInteractivoSuccess(CheckListResponseData data) {
                    mListener.OnInteractorMuestraChecklist(data);
                }

                @Override
                public void OnNoAuth() {
                    mListener.OnInteractorNoAuth();
                }

                @Override
                public void OnUnexpectedError(int responseCode) {
                    mListener.OnInteractorMuestraMensaje(DialogManager.UNEXPECTED_ERROR, responseCode);
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

    public void insertCheckList(int idObjeto, String token, ArrayList<String> idCheks, OnCheckListInteractorListener listener)
    {
        mListener = listener;
        AddCheckListRequest request = new AddCheckListRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idObjeto, idCheks, new AddCheckListRequest.AddChecklistResponseContract() {
            @Override
            public void OnCheckSuccess() {
                mListener.OnInteractorCheckListSuccess();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnUnexpectedError(int responseCode) {
                mListener.OnInteractorMuestraMensaje(DialogManager.UNEXPECTED_ERROR, responseCode);
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
