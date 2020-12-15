package com.femsa.sferea.home.inicio.componentes.fragment;

import com.femsa.requestmanager.Request.Home.BrowserRequest;
import com.femsa.requestmanager.Request.Home.CategoriesRequest;
import com.femsa.requestmanager.Request.Home.FilterSubcategoryProgramsRequest;
import com.femsa.requestmanager.Request.Home.especiales.LoMasVistoRequest;
import com.femsa.requestmanager.Request.Home.especiales.LoMasVistoSubcategoriaRequest;
import com.femsa.requestmanager.Request.Home.especiales.PorqueVisteRequest;
import com.femsa.requestmanager.Request.Home.ProgramsRequest;
import com.femsa.requestmanager.Request.Home.SubcategoriesRequest;
import com.femsa.requestmanager.Request.Home.especiales.PorqueVisteSubcategoriaRequest;
import com.femsa.requestmanager.Request.Login.UpdatePasswordRequest;
import com.femsa.requestmanager.Request.Logout.LogOutRequest;
import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.requestmanager.Response.Home.CuzYouSawResponse;
import com.femsa.requestmanager.Response.Home.MostSeenResponse;
import com.femsa.requestmanager.Response.Home.ProgramsResponse;
import com.femsa.requestmanager.Response.Home.SubcategoriesResponse;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

public class FragmentHomeInteractor {

    private FragmentHomeInteractor.OnFragmentHomeInteractorListener mListener;

    public interface OnFragmentHomeInteractorListener
    {
        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorRequestFailure();

        void OnInteractorNoAuth();

        /***
         * Success
         */

        void OnInteractorSuccessfulLogout();

        void OnInteractorMostSeenSuccess(MostSeenResponse data);

        void OnInteractorCuzYouSawSuccess(CuzYouSawResponse data);

        void OnInteractorAllProgramsSuccess(ProgramsResponse data);

        void OnInteractorSuccesCredential();

        void OnInteractorSuccessfullBrowser(ProgramsResponse data);

        void OnInteractorSubcategoriesSuccess(SubcategoriesResponse data);

        void OnInteractorProgramPersubcategorySuccess(ProgramsResponse data);

        void OnInteractorCategoriesSuccess(CategoriesResponse data);

        void OnInteractorNoContent();

        void OnInteractorUnexpectedError(int codigoRespuesta);

        /**
         * Callers
         * */

        void OnInteractorCallChange(String password, String token, String mail);

        void OnInteractorCallMostSeen(String token);

        void OnInteractorCallCuzYouSaw(String token);


        void OnInteractorCallMasVistosSub(String token, int idSubcategoria);

        void OnInteractorCallPorqueVisteSub(String token, int idSubcategoria);


        void OnInteractorCallPrograms(String token);

        void OnInteractorCallSubCategories(int idCategory, String token);

        void OnInteractorCallProgramsPersubcategory(int idSubcategory);

        void OnInteractorCallCategories(String token);

        void OnInteractorNoHayProgramas();

        void OnInteractorPorqueVisteVacio();

        /**
         * Método para mandar a llamar la petición del buscador
         * */
        void OnInteractorCallBrowser(String token, String word);
    }


    public void OnCallLogOut(String tokenUser, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        LogOutRequest out = new LogOutRequest(AppTalentoRHApplication.getApplication());
        out.makeRequest(tokenUser, new LogOutRequest.LogoutResponseContract() {
            /**
             * <p>Método que indica que el servidor tiene un error y no fue posible obtener la respuesta.</p>
             */
            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
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
            public void onResponseLogOutCorrecto() {
                mListener.OnInteractorSuccessfulLogout();
            }

            @Override
            public void onResponseLogOutError() {
                mListener.OnInteractorRequestFailure();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
                mListener.OnInteractorUnexpectedError(codigoRespuesta);
            }
        });
    }


    public void OnCallFeaturedMostSeen(String token, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        LoMasVistoRequest most = new LoMasVistoRequest(AppTalentoRHApplication.getApplication());
        most.makeRequest(token, new LoMasVistoRequest.OnMostSeenResponseContract() {
            @Override
            public void OnSeenSuccess(MostSeenResponse data) {
                mListener.OnInteractorMostSeenSuccess(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnNoContent() {

            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
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

    public void OnCallMasVistosSub(String token, int idSubcategoria, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        LoMasVistoSubcategoriaRequest most = new LoMasVistoSubcategoriaRequest(AppTalentoRHApplication.getApplication());
        most.makeRequest(token, idSubcategoria, new LoMasVistoSubcategoriaRequest.OnMostSeenResponseContract() {
            @Override
            public void OnSeenSuccess(MostSeenResponse data) {
                mListener.OnInteractorMostSeenSuccess(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnNoContent() {

            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
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

    public void OnCallCuzYouSaw(String token, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        PorqueVisteRequest request = new PorqueVisteRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, new PorqueVisteRequest.CuzYouSawRequestContract() {

            @Override
            public void OnCuzYouSawSuccess(CuzYouSawResponse data) {
                mListener.OnInteractorCuzYouSawSuccess(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnNoContent() {
                mListener.OnInteractorPorqueVisteVacio();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
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

    public void OnCallPorqueVisteSub(String token, int idSubcategoria, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        PorqueVisteSubcategoriaRequest request = new PorqueVisteSubcategoriaRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, idSubcategoria, new PorqueVisteSubcategoriaRequest.CuzYouSawRequestContract() {

            @Override
            public void OnCuzYouSawSuccess(CuzYouSawResponse data) {
                mListener.OnInteractorCuzYouSawSuccess(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnNoContent() {
                mListener.OnInteractorPorqueVisteVacio();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
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

    public void OnCallAllPrograms(String token, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        ProgramsRequest allPrograms = new ProgramsRequest(AppTalentoRHApplication.getApplication());
        allPrograms.makeRequest(token, new ProgramsRequest.ProgramsRequestResponseContract() {
            @Override
            public void OnProgramsSuccess(ProgramsResponse data) {
                mListener.OnInteractorAllProgramsSuccess(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnProgramsError() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void OnNoContent() {
                mListener.OnInteractorNoHayProgramas();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
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

    public void OnCallChangeCredential(String password, String token, String mail, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        UpdatePasswordRequest passReq = new UpdatePasswordRequest(AppTalentoRHApplication.getApplication());
        passReq.makeRequest(token, mail, password, new UpdatePasswordRequest.UpdatePasswordResponseContract() {
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

            @Override
            public void OnPasswordsuccess() {
                mListener.OnInteractorSuccesCredential();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
                mListener.OnInteractorUnexpectedError(codigoRespuesta);
            }
        });
    }

    public void OnCallBrowser(String word, String token, OnFragmentHomeInteractorListener listener )
    {
        BrowserRequest request = new BrowserRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, word, new BrowserRequest.BrowserRequestResponseContract() {
            @Override
            public void OnBrowserSuccess(ProgramsResponse data) {
                mListener.OnInteractorSuccessfullBrowser(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnProgramsError() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void OnBrowserNoContent() {
                mListener.OnInteractorNoContent();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
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

    public void OnCallSubCategories(int idCategry, String token, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        SubcategoriesRequest subcatsRequest = new SubcategoriesRequest(AppTalentoRHApplication.getApplication());
        subcatsRequest.makeRequest(token, idCategry, new SubcategoriesRequest.SubcategoriesResponseContract() {
            @Override
            public void OnSubcategoriesSuccess(SubcategoriesResponse data) {
                mListener.OnInteractorSubcategoriesSuccess(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnSubcategoriesError() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void OnNoContent() {
                mListener.OnInteractorNoContent();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
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

    public void OnCallProgramSubcategory(int idCategory, String token, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        FilterSubcategoryProgramsRequest filterRequest = new FilterSubcategoryProgramsRequest(AppTalentoRHApplication.getApplication());
        filterRequest.makeRequest(idCategory, token, new FilterSubcategoryProgramsRequest.FilterSubcategoryResponseContract() {
            @Override
            public void OnProgramsSuccess(ProgramsResponse data) {
                mListener.OnInteractorProgramPersubcategorySuccess(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnProgramsError() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void OnNoContent() {
                mListener.OnInteractorNoContent();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
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


    public void OnCallCategories(String tokenUser, FragmentHomeInteractor.OnFragmentHomeInteractorListener listener)
    {
        mListener = listener;
        CategoriesRequest request = new CategoriesRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(tokenUser, new CategoriesRequest.CategoriesRequestContract() {
            /**
             * <p>Método que indica que el servidor tiene un error y no fue posible obtener la respuesta.</p>
             */
            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
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
            public void OnRequestError() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void OnCategoriesSuccess(CategoriesResponse data) {
                mListener.OnInteractorCategoriesSuccess(data);
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
                mListener.OnInteractorUnexpectedError(codigoRespuesta);
            }

            @Override
            public void OnNoContent() {
                mListener.OnInteractorNoContent();
            }
        });
    }


}
