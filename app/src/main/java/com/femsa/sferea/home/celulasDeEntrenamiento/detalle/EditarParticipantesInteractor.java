package com.femsa.sferea.home.celulasDeEntrenamiento.detalle;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.EditarParticipanteRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

public class EditarParticipantesInteractor {

    private OnInteractorEditarParticipantes mListener;

    public interface OnInteractorEditarParticipantes{
        void onInteractorParticipanteAgregado();
        void onInteractorParticipanteEliminado();
        void onInteractorTokenInvalido();
        void onInteractorSinConexion();
        void onInteractorErrorServidor();
        void onInteractorTiempoEsperaAgotado();
    }

    public void peticionAgregarParticipantes(String idSolicitud, ArrayList<Integer> empleados, boolean accion,
                                            OnInteractorEditarParticipantes listener){
        mListener = listener;
        EditarParticipanteRequest request = new EditarParticipanteRequest(AppTalentoRHApplication.getApplication());
        request.makeRequestAgregarParticipante(idSolicitud, empleados, accion, SharedPreferencesUtil.getInstance().getTokenUsuario(),
                new EditarParticipanteRequest.OnResponseEditarParticipante() {
                    @Override
                    public void onResponseParticipantesEditados() {
                        mListener.onInteractorParticipanteAgregado();
                    }

                    @Override
                    public void onResponseTokenInvalido() {
                        mListener.onInteractorTokenInvalido();
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

    public void peticionEliminarParticipantes(String idSolicitud, ArrayList<Integer> empleados, boolean accion,
                                             OnInteractorEditarParticipantes listener){
        mListener = listener;
        EditarParticipanteRequest request = new EditarParticipanteRequest(AppTalentoRHApplication.getApplication());
        request.makeRequestEliminarParticipante(idSolicitud, empleados, accion, SharedPreferencesUtil.getInstance().getTokenUsuario(),
                new EditarParticipanteRequest.OnResponseEditarParticipante() {
                    @Override
                    public void onResponseParticipantesEditados() {
                        mListener.onInteractorParticipanteEliminado();
                    }

                    @Override
                    public void onResponseTokenInvalido() {
                        mListener.onInteractorTokenInvalido();
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
