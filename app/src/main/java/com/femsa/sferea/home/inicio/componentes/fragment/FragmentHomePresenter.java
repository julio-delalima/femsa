package com.femsa.sferea.home.inicio.componentes.fragment;

import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.requestmanager.Response.Home.CuzYouSawResponse;
import com.femsa.requestmanager.Response.Home.MostSeenResponse;
import com.femsa.requestmanager.Response.Home.ProgramsResponse;
import com.femsa.requestmanager.Response.Home.SubcategoriesResponse;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

public class FragmentHomePresenter implements  FragmentHomeInteractor.OnFragmentHomeInteractorListener{

    private FragmentHomeView mHomeView;

    private FragmentHomeInteractor mInteractor;

    public FragmentHomePresenter(FragmentHomeView homeView, FragmentHomeInteractor interactor) {
        mHomeView = homeView;
        mInteractor = interactor;
    }

    /**
     * Llamado cuando se actualiza la contraseña del login exitosamente por primera vez
     * */
    @Override
    public void OnInteractorSuccesCredential() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onPasswordSuccess();
        }

    }

    @Override
    public void OnInteractorSuccessfullBrowser(ProgramsResponse data) {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.OnBrowserSuccess(data);
        }
    }

    @Override
    public void OnInteractorCallChange(String password, String token, String mail) {
        if(mHomeView != null) {
            mHomeView.onShowLoader();
            mInteractor.OnCallChangeCredential(password, token, mail, this);
        }
    }


    @Override
    public void OnInteractorSuccessfulLogout() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onLogOutSuccess();
        }
    }

    @Override
    public void OnInteractorServerError() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.SERVER_ERROR);
        }
    }

    @Override
    public void OnInteractorTimeout() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.TIMEOUT);
        }
    }

    @Override
    public void OnInteractorNoInternet() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.NO_INTERNET);
            mHomeView.onNoHayProgramas();
        }
    }

    @Override
    public void OnInteractorRequestFailure() {
        if(mHomeView != null) {
            mHomeView.onHideLoader();
        }

    }

    @Override
    public void OnInteractorNoAuth() {
        if (mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.EXPIRED_TOKEN);
            mHomeView.onLogOutSuccess();
        }

    }

    @Override
    public void OnInteractorMostSeenSuccess(MostSeenResponse data) {
        if(mHomeView != null)
        {
            mHomeView.onHideLoader();
            mHomeView.getMostSeenSuccess(data);
        }

    }

    @Override
    public void OnInteractorCuzYouSawSuccess(CuzYouSawResponse data) {
        if(mHomeView != null)
        {
            mHomeView.getCuzYouSawSuccess(data);
        }
    }

    @Override
    public void OnInteractorAllProgramsSuccess(ProgramsResponse data) {
        if(mHomeView != null)
        {
            mHomeView.getAllProgramsSuccess(data);
        }
    }


    @Override
    public void OnInteractorCallMostSeen(String token) {
        if(mHomeView != null)
        {
            mInteractor.OnCallFeaturedMostSeen(token, this);
        }
    }

    @Override
    public void OnInteractorCallCuzYouSaw(String token) {
        if(mHomeView != null) {
            mInteractor.OnCallCuzYouSaw(token, this);
        }
    }

    @Override
    public void OnInteractorCallMasVistosSub(String token, int idSubcategoria) {
        if(mHomeView != null) {
            mInteractor.OnCallMasVistosSub(token, idSubcategoria, this);
        }
    }

    @Override
    public void OnInteractorCallPorqueVisteSub(String token, int idSubcategoria) {
        if(mHomeView != null) {
            mInteractor.OnCallPorqueVisteSub(token, idSubcategoria,this);
        }
    }

    @Override
    public void OnInteractorCallPrograms(String token) {
        if(mHomeView != null) {
            mInteractor.OnCallAllPrograms(token, this);
        }
    }

    /**
     * Método para mandar a llamar la petición del buscador
     *
     * @param token
     * @param word
     */
    @Override
    public void OnInteractorCallBrowser(String token, String word) {
        if(mHomeView != null)
        {
            mHomeView.onShowLoader();
            mInteractor.OnCallBrowser(word, token, this);
        }
    }


    /**
     * Método que te retorna todas las subcategorias asociadas a una categoría.
     * @param data todas las subcategorías asociadas a una categoría.
     * */
    @Override
    public void OnInteractorSubcategoriesSuccess(SubcategoriesResponse data) {
        if(mHomeView != null)
        {
            mHomeView.getSubcategoriesSuccess(data);
        }
    }

    /**
     * Método que muestra todos los programas asociados a una subcategoría
     * @param data todos los programas incluidos en esa subcategoría.
     * */
    @Override
    public void OnInteractorProgramPersubcategorySuccess(ProgramsResponse data) {
        if(mHomeView != null)
        {
            mHomeView.onHideLoader();
            mHomeView.getProgramsSubCategorySuccess(data);
        }
    }


    @Override
    public void OnInteractorCallCategories(String token) {
        if(mHomeView != null)
        {
            mHomeView.onShowLoader();
            mInteractor.OnCallCategories(token, this);
        }
    }

    @Override
    public void OnInteractorNoHayProgramas() {
        if(mHomeView != null)
        {
            mHomeView.onNoHayProgramas();
        }
    }

    @Override
    public void OnInteractorPorqueVisteVacio() {
        if(mHomeView != null)
        {
            mHomeView.onPorqueVisteVacío();
        }
    }


    @Override
    public void OnInteractorCallSubCategories(int idCategory, String token) {
        if(mHomeView != null)
        {
            mInteractor.OnCallSubCategories(idCategory, token, this);
        }
    }

    @Override
    public void OnInteractorCallProgramsPersubcategory(int idSubcategory) {
        if(mHomeView != null)
        {
            mHomeView.onShowLoader();
            mInteractor.OnCallProgramSubcategory(idSubcategory, SharedPreferencesUtil.getInstance().getTokenUsuario(), this);
        }
    }

    @Override
    public void OnInteractorCategoriesSuccess(CategoriesResponse data) {
        if(mHomeView != null)
        {
            mHomeView.onHideLoader();
            mHomeView.getCategoriesSuccess(data);
        }
    }

    @Override
    public void OnInteractorNoContent() {
        if (mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onShowMessage(DialogManager.EMPTY_QUERY);
        }
    }

    @Override
    public void OnInteractorUnexpectedError(int codigoRespuesta) {
        if (mHomeView != null) {
            mHomeView.onHideLoader();
            mHomeView.onMostrarMensajeInesperado(DialogManager.UNEXPECTED_ERROR, codigoRespuesta);
        }
    }

    public void onDestroy(){
        mHomeView = null;
    }

}
