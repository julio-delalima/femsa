package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Checklist;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.CheckListResponseData;

public interface ChecklistView {

    void OnViewMostrarLoader();

    void OnViewEsconderLoader();

    void OnViewMostrarMensaje(int tipoMensaje);

    void OnViewMostrarMensaje(int tipoMensaje, int codigoRespuesta);

    void OnViewPintaCheckList(CheckListResponseData data);

    void OnViewChecklistAdded();
}
