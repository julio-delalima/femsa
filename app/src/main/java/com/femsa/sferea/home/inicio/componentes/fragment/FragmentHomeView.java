package com.femsa.sferea.home.inicio.componentes.fragment;

import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.requestmanager.Response.Home.CuzYouSawResponse;
import com.femsa.requestmanager.Response.Home.MostSeenResponse;
import com.femsa.requestmanager.Response.Home.ProgramsResponse;
import com.femsa.requestmanager.Response.Home.SubcategoriesResponse;

public interface FragmentHomeView {

    void onLogOutSuccess();

    void getMostSeenSuccess(MostSeenResponse data);

    void getCuzYouSawSuccess(CuzYouSawResponse data);

    void getAllProgramsSuccess(ProgramsResponse data);

    void onShowLoader();

    void onHideLoader();

    void onPasswordSuccess();

    void OnPasswordFail();

    /**
     * Método que se ejecuta cuando se obtienen resultados del buscador de home
     * */
    void OnBrowserSuccess(ProgramsResponse data);

    void onShowMessage(int type);

    void onMostrarMensajeInesperado(int tipoMensaje, int codigoRespuesta);

    void getSubcategoriesSuccess(SubcategoriesResponse data);

    void getProgramsSubCategorySuccess(ProgramsResponse data);

    void getCategoriesSuccess(CategoriesResponse data);

    void onNoHayProgramas();

    void onPorqueVisteVacío();
}
