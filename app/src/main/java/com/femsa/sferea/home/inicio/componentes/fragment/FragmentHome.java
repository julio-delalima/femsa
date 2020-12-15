package com.femsa.sferea.home.inicio.componentes.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.femsa.requestmanager.DTO.User.Home.DataPrograms;
import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.requestmanager.Response.Home.CuzYouSawResponse;
import com.femsa.requestmanager.Response.Home.MostSeenResponse;
import com.femsa.requestmanager.Response.Home.ProgramsResponse;
import com.femsa.requestmanager.Response.Home.SubcategoriesResponse;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.inicio.componentes.activity.HomeView;
import com.femsa.sferea.home.inicio.componentes.adapters.CarrouselAdapter;
import com.femsa.sferea.home.inicio.componentes.adapters.FilterAdapter;
import com.femsa.sferea.home.inicio.componentes.adapters.ProgramasContainerAdapter;
import com.femsa.sferea.home.inicio.componentes.adapters.SeccionHeaderAdapter;
import com.femsa.sferea.home.inicio.componentes.dialogFragments.FilterDialog;
import com.femsa.sferea.home.inicio.componentes.dialogFragments.FirstLoginDialog;
import com.femsa.sferea.home.inicio.componentes.dialogFragments.SinContenidoDialog;
import com.femsa.sferea.home.inicio.componentes.dialogFragments.UpdateDataDialog;
import com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity;
import com.rd.PageIndicatorView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class FragmentHome extends Fragment implements ProgramasContainerAdapter.OnHomeListener,
        FragmentHomeView, UpdateDataDialog.OnPasswordSuccessDialog, HomeView, SeccionHeaderAdapter.OnSeccionHeaderListener
        , FilterAdapter.OnHomeFilterListener, CarrouselAdapter.OnCarruselClick, SwipeRefreshLayout.OnRefreshListener{

    private View mView;

    private ViewPager mViewPager;

    private CarrouselAdapter mCarrouselAdapter;

    private RecyclerView mHomeRecycler, mMasVistoRecycler, mPorqueVisteRecycler;

    private SwipeRefreshLayout mRefreshLayout;

    private ArrayList<String> mTitles;

    private ArrayList<DataPrograms> mPrograms, mFeaturedPrograms, masVistosListado, porqueVisteListado;

    private FragmentHomePresenter mPresenter;

    private NestedScrollView mScroll;

    private OnHomeHideToolbar mListener;

    Loader loader;

    private PageIndicatorView mPageIndicator;

    /**
     * Bandera que controla el comportamiento del buscador, permitiendo que solo salga una ves sí y una vez no.
     */
    private int mBrowserJustShown = -1;

    private boolean canShowBrowser = false;

    private boolean isUsuarioNuevo = false;

    private EditText mBuscador;

    private ViewTreeObserver.OnScrollChangedListener mScrollListener;

    private ConstraintLayout mBrowserContainer;

    private RecyclerView mRecyclerHeader;

    /**
     * Bandera usada para permitir cambios en la toolbar y buscador únicamente si se ha hecho scroll vertical,
     * si detecta un scroll horizontal mantiene los componentes en su mRutaGuardado
     */
    private boolean mHasVerticallyScrolled = false;

    private ProgramasContainerAdapter mAdapter, mAdapterMasVisto, mAdapterPorqueViste;

    private ConstraintLayout mFilterContainer;

    private SubcategoriesResponse mSubcategoriasFiltroDialog;

    private TextView mFiltroCategoriaTexto, mFiltroSubcategoriaTexto;

    private CategoriesResponse mCategoriesData;

    private boolean categoryFilterCalledBrowser = false;

    private Toolbar mToolbar;

    private TextView mSinProgramasTexto;

    private ImageView mSinProgramasImageView;

    // Texto que reemplazará al anterior que se tenga en el filtro en la parte nombre de categoria y solo cambiará si la categoria tiene subcategoría
    private String mCategoriaFiltroNuevoTexto = "";

    private LinearLayout mProgramasContainer;

    /**
     * Bandera que permite bloquear la toolbar para que esta no desaparezca cuando se tienen resultados del buscador.
     * */
    private boolean mToolbackLocked = false;

    //Bandera que revisa el último mebnsaje mostrando, evitando mostrar el mismo mensaje repetidas ocasiones
    private int lastMessageShown = -1;

    /**
     * Método que se ejecuta cuando se llama o se termina de arrastrar el SwipeRevealLayout,
     * utilizado aquí para mostrar el buscador únicamente cuando se tiene activo el FragmentHome.
     * */
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        if(mListener.isActiveFragmentIntanceOfFragmentHome()) {
            mBrowserContainer.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Interfaz de Home utilizada para controlar el comportamiento.
     * */
    public interface OnHomeHideToolbar
    {
        /**
         * Método para ocultar la toolbar completa.
         * */
        void hideToolbar();

        /**
         * Método para volver a mostrar la toolbar.
         * */
        void showToolbar();

        /**
         * Método para inicializar el aspecto visual al cargar el fragment de home.
         * */
        void setHomeInMainView();

        /**
         * Método para verificar si el fragmento activo o en pantalla es una instancia de FragmentHome
         * Retorna true si es instancia de Fragment home, de lo contrario retorna false.
         * */
        boolean isActiveFragmentIntanceOfFragmentHome();

        /**
         * Método que controla el aspecto de home al utilizar el buscador
         * */
        void setBrowserView();

        /**
         * Método utilizado cuando se limpia el buscador
         * */
        void hideBrowserView();

        /**
         * Cambia la bandera de la contraseña una vez que ha sido actualizada
         * */
        void updatedPasswordChangeFlag();

        /**
         * Método para volver  allamar el menú de home
         * */
        void recallHome();

        /**
         * función utilizada para que cuando no se tengan fragments en la pila, al hacer back en el botón físico,
         * en vez de llamar el dialog de cerrar aplicación, mejor te regrese a home.
         * */
        void browserWasCalled();
    }

    /**
     * Asignación del listener que ejecutará los método de la Interfaz OnHomeHideToolbar.
     * */
    public void setListener(OnHomeHideToolbar listener)
    {
        mListener = listener;
    }

    /**
     * Inicialización del Fragment.
     * */
    public static FragmentHome newInstance() {
        Bundle args = new Bundle();
        FragmentHome fragment = new FragmentHome();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Método del ciclo de vida de los Fragments
     * */
    @Override
    public void onStart() {
        super.onStart();
        mScroll.getViewTreeObserver().addOnScrollChangedListener(mScrollListener);
        //initData();
    }

    /**
     * Método exclusivo de Fragments que asigna la vista e inicializa las vistas.
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mPresenter = new FragmentHomePresenter(this, new FragmentHomeInteractor());
        if(getArguments() != null)
        {
            isUsuarioNuevo = getArguments().getBoolean("new");
        }
        bindViews();
        if(mListener != null)
        {
            mListener.setHomeInMainView();
        }
        initData();
        return mView;
    }

    /**
     * Método que inicializa los datos de los arraylist que poblarán cada Recycler View.
     * */
    private void initData()
    {
        mTitles = new ArrayList<>();

        mPrograms = new ArrayList<>();
        masVistosListado = new ArrayList<>();
        porqueVisteListado = new ArrayList<>();

        mFeaturedPrograms = new ArrayList<>();

        callData();

        setDataIntoProgramasRV();

        setDataIntoCarrouselRV();
    }

    /**
     * Método que llama a los Web service que traerán los datos de las Categorías y los Programas.
     * */
    private void callData()
    {
        mPresenter.OnInteractorCallCategories(SharedPreferencesUtil.getInstance().getTokenUsuario());
        LogManager.d("Categorias", "Call categorias");
        mPresenter.OnInteractorCallPrograms(SharedPreferencesUtil.getInstance().getTokenUsuario());
        LogManager.d("Categorias", "call programs");
        OnMostrarProgramasDeSiempre();
    }

    /**
     * Método que llama a los Web Service que traerán los datos de los programas pertenecientes a las secciones "Lo más visto" y "Porque viste"
     * */
    private void OnMostrarProgramasDeSiempre()
    {
        mPresenter.OnInteractorCallCuzYouSaw(SharedPreferencesUtil.getInstance().getTokenUsuario());
        LogManager.d("Categorias", "call porque viste");
        mPresenter.OnInteractorCallMostSeen(SharedPreferencesUtil.getInstance().getTokenUsuario());
        LogManager.d("Categorias", "call mas visto");
    }

    /**
     * Método para inicializar cada elemento de las vistas.
     * */
    @SuppressLint("ClickableViewAccessibility")
    private void bindViews()
    {
        mToolbackLocked = false;

        mToolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);

        mPageIndicator = mView.findViewById(R.id.pageIndicatorView);

        loader = Loader.newInstance();

        mScrollListener = this::onBrowserBehaviourController;

        mScroll = mView.findViewById(R.id.fragment_home_scroll);

        mBuscador = getActivity().findViewById(R.id.home_buscador);
        mBuscador.setText("");
        mBuscador.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        OnBuscadorHacerBusqueda();
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });


        ImageView mBrowserClose = getActivity().findViewById(R.id.homeactivity_broswer_close_iv);
        mBrowserClose.setOnClickListener(v->mBuscador.setText(""));

        mBrowserContainer = getActivity().findViewById(R.id.browser_container);
        mBrowserContainer.setVisibility(View.GONE);

        Button mBrowserCancelButton = getActivity().findViewById(R.id.buscador_cancelar_boton_btn);
        mBrowserCancelButton.setOnClickListener(v->onCloseBrowser());


        mViewPager = mView.findViewById(R.id.viewPager);
        mViewPager.setPageMargin(0);

        mHomeRecycler = mView.findViewById(R.id.mainRecycler);

        mPorqueVisteRecycler = mView.findViewById(R.id.recycler_porque_viste);

        mMasVistoRecycler = mView.findViewById(R.id.recycler_mas_vistos);

        mFilterContainer = getActivity().findViewById(R.id.filter_container);
        mFilterContainer.setVisibility(View.GONE);

        mFiltroCategoriaTexto = getActivity().findViewById(R.id.filter_main_category_tv);
        mFiltroCategoriaTexto.setOnClickListener(v->onFilterCategoryClick());

        ImageView mSubcategoriaFlecha = getActivity().findViewById(R.id.subcategoryArrow);
        mSubcategoriaFlecha.setOnClickListener(v->onFilterSubcategoryClick());

        ImageView mCategoriaFlecha = getActivity().findViewById(R.id.categoryArrow);
        mCategoriaFlecha.setOnClickListener(v->onFilterCategoryClick());

        mRecyclerHeader = getActivity().findViewById(R.id.homeActivity_recyclerViewHeader_categories);

        mFiltroSubcategoriaTexto = getActivity().findViewById(R.id.filter_subcategory_tv);
        mFiltroSubcategoriaTexto.setOnClickListener(v->onFilterSubcategoryClick());

        if(isUsuarioNuevo)
        {
            FirstLoginDialog mDialog =  FirstLoginDialog.newInstance();
            FragmentManager fm = getChildFragmentManager();
            mDialog.setCancelable(false);
            mDialog.setListener(this);
            mDialog.show(fm, "NewDialog");
        }

        mSinProgramasImageView = mView.findViewById(R.id.no_programs_fragment_gome_image);

        mSinProgramasTexto = mView.findViewById(R.id.no_programs_fragment_home_text);

        mProgramasContainer = mView.findViewById(R.id.linear_fragment_home);

        onQuitarCategoriasPrincipales();

        mRefreshLayout = getActivity().findViewById(R.id.swipe_home_controller);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.femsa_red_b));
        mRefreshLayout.setProgressBackgroundColorSchemeColor(Color.TRANSPARENT);
        try {
            Field f = mRefreshLayout.getClass().getDeclaredField("mCircleView");
            f.setAccessible(true);
            ImageView img = (ImageView)f.get(mRefreshLayout);
            assert img != null;
            img.setImageResource(0);
            img.setElevation(0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que inicializa los recycler view que contendrán a los programas y al carrusel principal.
     * */
    private void setDataIntoProgramasRV()
    {
        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager LayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager LayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new ProgramasContainerAdapter(mTitles, mPrograms, getContext(), width, true);
        mAdapter.setOnHomeListener(this);
        mHomeRecycler.setLayoutManager(LayoutManager);
        mHomeRecycler.setAdapter(mAdapter);

        mAdapterMasVisto = new ProgramasContainerAdapter(new ArrayList<>(Collections.singletonList(getResources().getString(R.string.lomasvisto))), masVistosListado, getContext(), width, false);
        mAdapterMasVisto.setOnHomeListener(this);
        mMasVistoRecycler.setLayoutManager(LayoutManager2);
        mMasVistoRecycler.setAdapter(mAdapterMasVisto);
        mAdapterPorqueViste = new ProgramasContainerAdapter(new ArrayList<>(Collections.singletonList(getResources().getString(R.string.porqueviste))), porqueVisteListado, getContext(), width, false);
        mAdapterPorqueViste.setOnHomeListener(this);
        mPorqueVisteRecycler.setLayoutManager(LayoutManager3);
        mPorqueVisteRecycler.setAdapter(mAdapterPorqueViste);

    }

    /**
     * <p>Método para inicializar los datos del RV para el carrusel principal de programas destacados.</p>
     * */
    private void setDataIntoCarrouselRV()
    {
        mCarrouselAdapter = new CarrouselAdapter(getContext(), mFeaturedPrograms);
        mCarrouselAdapter.setListener(this);
        mViewPager.setAdapter(mCarrouselAdapter);
    }

    /**
     * Método que inicializa los datos que irán de categorías en la parte superior.
     * */
    private void setDataIntoHeaderRV(CategoriesResponse data)
    {
        LinearLayoutManager LayoutMan = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        SeccionHeaderAdapter adapter = new SeccionHeaderAdapter(data, this);
        if(mRecyclerHeader != null) {
            mRecyclerHeader.setLayoutManager(LayoutMan);
            mRecyclerHeader.setAdapter(adapter);
        }
    }


    /**
     * Método que maneja el funcionamiento del buscador.
     * */
    private void OnBuscadorHacerBusqueda()
    {
        categoryFilterCalledBrowser = false;
        String tempWord = mBuscador.getText().toString();
        if(!tempWord.isEmpty())
        {
            mPresenter.OnInteractorCallBrowser(SharedPreferencesUtil.getInstance().getTokenUsuario(), tempWord);
        }
    }

    /**
     * Método que se ejecuta cuando se hace click sobre el botón cancelar del buscador, regresando al usuario al home.
     * */
    private void onCloseBrowser()
    {
        mListener.recallHome();
    }


    /**
     * Método que controla el comportamiento del buscador, en donde se muestra una vez sí y una vez no cada que se alcanza la parte superior
     * o 0 del scroll.
     *
     * ---- Deprecado el 15/07/2019, día que descubrí el SwipeRefreshLayout, no lo borro porque, como dice la regla de oro:
     * "Si ya sirve, ni le muevas"
     * */
    private void onBrowserBehaviourController()
    {
        if (mScroll != null && mListener != null && mListener.isActiveFragmentIntanceOfFragmentHome())
        {

            if (mScroll.getScrollY() == 0)
            {
                if(!mToolbackLocked)
                {
                    mListener.showToolbar();
                }
                if(mHasVerticallyScrolled)
                {
                    if(mBrowserJustShown == 1)
                    {
                        // mBrowserContainer.setVisibility(View.VISIBLE);
                        mBrowserJustShown = 0;
                    }
                    else if(mBrowserJustShown == 0)
                    {
                        mBrowserJustShown = 1;
                        if(mToolbackLocked)
                        {
                            //   mBrowserContainer.setVisibility(View.VISIBLE);
                        }
                        else {
                            mBrowserContainer.setVisibility(View.GONE);
                        }

                    }
                    mHasVerticallyScrolled = false;
                }
            }
            else
            {
                mHasVerticallyScrolled = true;
                if(mBrowserJustShown == -1)
                {
                    mBrowserJustShown = 0;
                }
                mListener.hideToolbar();
                mBrowserContainer.setVisibility(View.GONE);
            }
        }
    }


    /**
     * Método que manda a llamar el cuadro de dialogo que incluye todas las subcategorias asociadas a la categoria
     * seleccionada en el filtro.
     * */
    private void onFilterSubcategoryClick()
    {
        FragmentManager fm = getChildFragmentManager();
        FilterDialog mDialogFilter = new FilterDialog();
        mDialogFilter.setData(mSubcategoriasFiltroDialog, this, 0);
        mDialogFilter.show(fm, "Loader");
    }

    /**
     * Método que manda a llamar el cuadro de diálogo con todas las categorías una vez que se activó el filtro.
     * */
    private void onFilterCategoryClick()
    {
        FragmentManager fm = getChildFragmentManager();
        FilterDialog mDialogFilter = new FilterDialog();
        mDialogFilter.setData(mCategoriesData, this, 1);
        mDialogFilter.show(fm, "Filter");
    }


    /**
     * @deprecated
     * Método que ordena las categorías de forma que Lo más visto siempre aparezca primero y porqueViste siempre sea el segundo en aparecer. [OBSOLETO]
     * */
    private void onOrdenarCategorias()
    {
       /* for(int i = 0; i < mTitles.size(); i++)
            {
                if(mTitles.get(i).equals(MAS_VISTOS_KEY) && i != 0)
                    {
                        Collections.swap(mTitles, i, 0);
                    }
                if(mTitles.get(i).equals(PORQUE_VISTE_KEY) && i != 1)
                {
                    Collections.swap(mTitles, i, 1);
                }
            }
        mAdapter.notifyDataSetChanged();*/
    }

    /***
     * Método que nos envia al detalle de cada programa.
     * @param id: el id de el programa seleccionado.
     */
    @Override
    public void onResponseListener(int id) {
        onShowLoader();
        Intent sendTo = new Intent(getActivity(), DetalleProgramaActivity.class);
        sendTo.putExtra("id", id);
        startActivity(sendTo);
        onHideLoader();
    }

    /**
     * Método que nos envia al detalle de cada programa.
     * @param id: el id de el programa seleccionado.
     */
    @Override
    public void OnCarruselClicked(int id) {
        Intent sendTo = new Intent(getActivity(), DetalleProgramaActivity.class);
        sendTo.putExtra("id", id);
        startActivity(sendTo);
    }

    /**
     * Método de cierre de sesión forzado tras recibir un
     * código de respuesta 401.
     * */
    @Override
    public void onLogOutSuccess() {
        GlobalActions g = new GlobalActions(getActivity());
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }


    /**
     * Método que trae la información del WS de Los Más Vistos.
     * */
    @Override
    public void getMostSeenSuccess(MostSeenResponse data) {
        LogManager.d("Categorias", "mas vistos success " + data.getCategories().getAllData().toString());
        if(data != null){
            mMasVistoRecycler.setVisibility(View.VISIBLE);
            masVistosListado.addAll(data.getCategories().getAllData());
            mAdapterMasVisto.notifyDataSetChanged();
            setDataIntoProgramasRV();
        }
    }

    /**
     * <p>Método que trae la información del WS de Porque Viste.</p>
     * @param data listado de programas de la sección "Porque viste".
     * */
    @Override
    public void getCuzYouSawSuccess(CuzYouSawResponse data) {
        LogManager.d("Categorias", "get porque viste success " + data.getCategories().getAllData().toString());
        if(data != null){
            mPorqueVisteRecycler.setVisibility(
                    (data.getCategories().getAllData().size() > 0 )
                            ? View.VISIBLE
                            : View.GONE
            );
            porqueVisteListado.addAll(data.getCategories().getAllData());
            mAdapterPorqueViste.notifyDataSetChanged();
            setDataIntoProgramasRV();
        }
    }

    /**
     * <p>Método que trae el listado de programas del WS de Programas. y rellena los listado
     * de programas y de destacados.</p>
     * @param data listado de programas.
     * */
    @Override
    public void getAllProgramsSuccess(ProgramsResponse data) {
        LogManager.d("Categorias", "get todos los programas " + data.getPrograms().getAllData().toString());
        if(data != null){
            for(int i = 0; i < data.getPrograms().getAllData().size(); i++){
                if(data.getPrograms().getAllData().get(i).isDestacado()){
                    mFeaturedPrograms.add(data.getPrograms().getAllData().get(i));
                }
            }
            mPageIndicator.setCount(mFeaturedPrograms.size());
            mTitles.addAll(data.getPrograms().getAllCategories());
            mPrograms.addAll(data.getPrograms().getAllData());
            mAdapter.notifyDataSetChanged();
            setDataIntoProgramasRV();
            mCarrouselAdapter.notifyDataSetChanged();
            if(mFeaturedPrograms.size() < 1){
                onNoExistenDestacados();
            }
            onOrdenarCategorias();
        }
    }

    /**
     * Método para mostrar el loader.
     * */
    @Override
    public void onShowLoader() {
        FragmentManager fm = getChildFragmentManager();
        if (!loader.isAdded())
        {
            loader.show(fm, "Loader");
        }
    }

    /**
     * Método para ocultar el loader, se mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    @Override
    public void onHideLoader() {
        try
        {
            if(loader != null)
            {
                loader.dismiss();
            }
        }
        catch (IllegalStateException ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }


    @Override
    public void onPasswordSuccess() {
    }

    @Override
    public void OnPasswordFail() {

    }

    /**
     * Método que se ejecuta cuando se obtienen resultados del buscador de home
     * @param data los programas obtenidos durante la búsqueda.
     */
    @Override
    public void OnBrowserSuccess(ProgramsResponse data) {
        if(data != null){
            onLimpiarDataProgramas();
            onQuitarCategoriasPrincipales();
            mListener.browserWasCalled();
            mFilterContainer.setVisibility(View.GONE);
            mRecyclerHeader.setVisibility(View.VISIBLE);
            mBrowserContainer.setBackgroundColor(getResources().getColor(R.color.femsa_red_b));
            if(!categoryFilterCalledBrowser)
            {
                mViewPager.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mViewPager.setVisibility(View.INVISIBLE);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mViewPager.getWidth(), mBrowserContainer.getHeight());
                                mViewPager.setLayoutParams(params);
                                mListener.setBrowserView();
                            }
                        });
                mPageIndicator.setVisibility(View.INVISIBLE);
                mRecyclerHeader.setVisibility(View.VISIBLE);
                mBrowserContainer.setVisibility(View.VISIBLE);
                mToolbar.setVisibility(View.GONE);
                mToolbackLocked = true;
            }
            else
            {
                mBrowserContainer.setVisibility(View.GONE);
                mRecyclerHeader.setVisibility(View.GONE);
                mFilterContainer.setVisibility(View.VISIBLE);
                mBrowserContainer.setBackground(null);
            }

            if(data != null && data.getPrograms().getAllData().size() > 0)
            {
                mTitles.clear();
                mPrograms.clear();
                mTitles.addAll(data.getPrograms().getAllCategories());
                mPrograms.addAll(data.getPrograms().getAllData());
                setDataIntoProgramasRV();
            }
        }
    }

    /**
     * Método para mostrar mensajes.
     * */
    @Override
    public void onShowMessage(int tipoMensaje) {
        if(tipoMensaje == DialogManager.EMPTY_QUERY && !isUsuarioNuevo)
        {
            SinContenidoDialog sinContenidoDialog = SinContenidoDialog.newInstance();
            FragmentManager fm = getChildFragmentManager();
            sinContenidoDialog.show(fm, "sinconenido");
        }
        else
        {
            if(!isUsuarioNuevo) //Solo se muestran mensaje de error o sin contenido si el usuario no es nuevo
            {
                if(tipoMensaje != lastMessageShown) //Solo se muestran mensaje de error o sin contenido si el usuario no es nuevo
                {
                    DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje);
                    lastMessageShown = tipoMensaje;
                }
            }
        }

    }

    /**
     * Método para mostrar mensajes con código de error.
     * */
    @Override
    public void onShowMessage(int tipoMensaje, int codigoRespuesta) {
        if(codigoRespuesta == DialogManager.EMPTY_QUERY && !isUsuarioNuevo)
        {
            SinContenidoDialog sinContenidoDialog = SinContenidoDialog.newInstance();
            FragmentManager fm = getChildFragmentManager();
            sinContenidoDialog.show(fm, "sincontenido");
        }
        else
        {
            if(!isUsuarioNuevo && tipoMensaje != lastMessageShown) //Solo se muestran mensaje de error o sin contenido si el usuario no es nuevo
            {
                DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje, codigoRespuesta);
                lastMessageShown = tipoMensaje;
            }
        }
    }
    /**
     * Método para mostrar mensajes con código de error.
     * */
    @Override
    public void onMostrarMensajeInesperado(int tipoMensaje, int codigoRespuesta) {
        if(!isUsuarioNuevo) //Solo se muestran mensaje de error o sin contenido si el usuario no es nuevo
        {
            if(tipoMensaje != lastMessageShown)
            {
                DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje, codigoRespuesta);
                lastMessageShown = tipoMensaje;
            }
        }
    }

    /**
     * Método que le indica a la actividad principal que el cambio de contraseña durante el registro fue exitoso y cierra el cuadro de diálogo.
     * */
    @Override
    public void successfulPassword() {
        DialogManager.displaySnack(getFragmentManager(), getResources().getString(R.string.password_success));
        //Toast.makeText(getContext(), R.string.password_success, Toast.LENGTH_SHORT).show();
        getArguments().clear();
        isUsuarioNuevo = false;
        mListener.updatedPasswordChangeFlag();
    }

    /**
     * Método del ciclo de vida de los Fragment
     * */
    @Override
    public void onPause() {
        super.onPause();
        mScroll.getViewTreeObserver().removeOnScrollChangedListener(mScrollListener);
    }


    /**
     * Método que te retorna todas las subcategorias asociadas a una categoría y se mostrarán dentro del dialog fragment del filtro.
     * @param data todas las subcategorías asociadas a una categoría.
     * */
    @Override
    public void getSubcategoriesSuccess(SubcategoriesResponse data) {
        if(data != null){
            mFilterContainer.setVisibility(View.VISIBLE);
            mFiltroCategoriaTexto.setText(mCategoriaFiltroNuevoTexto);
            categoryFilterCalledBrowser = true;
            mRecyclerHeader.setVisibility(View.GONE);
            mFiltroCategoriaTexto.setText(mCategoriaFiltroNuevoTexto);
            mFiltroSubcategoriaTexto.setText(R.string.all_programs_button);//
            onLimpiarDataProgramas();
            mSubcategoriasFiltroDialog = null;
            mSubcategoriasFiltroDialog = data;
        }
    }

    /**
     * Método que se ejecuta cuando se hace click sobre una de las categorías mostradas en el header, activando
     * el filtro por cada categoría.
     * @param filtro el nombre de la categoría sobre la que se hizo click.
     * @param CategoryID el ID de dicha categoría.
     * */
    @Override
    public void OnClickSeccionHeader(String filtro, int CategoryID) {
        mCategoriaFiltroNuevoTexto = filtro;
        mPresenter.OnInteractorCallBrowser(SharedPreferencesUtil.getInstance().getTokenUsuario(), mCategoriaFiltroNuevoTexto);
        mPresenter.OnInteractorCallSubCategories(CategoryID, SharedPreferencesUtil.getInstance().getTokenUsuario());
        categoryFilterCalledBrowser = true;
    }

    /**
     * Método que se ejecuta cuando se hace click sobre la subcategoria en el filtro.
     * @param idSubcategoria el id de la subcategoria que se va a consultar.
     * @param textoSubcategoria el nombre de la subcategoria.
     * */
    @Override
    public void onSubCategoriaFiltroClick(int idSubcategoria, String textoSubcategoria) {
        mPresenter.OnInteractorCallProgramsPersubcategory(idSubcategoria);
        mFiltroSubcategoriaTexto.setText(textoSubcategoria);
        mPresenter.OnInteractorCallMasVistosSub(SharedPreferencesUtil.getInstance().getTokenUsuario(), idSubcategoria);
        mPresenter.OnInteractorCallPorqueVisteSub(SharedPreferencesUtil.getInstance().getTokenUsuario(), idSubcategoria);
    }

    /**
     * Método que se ejecuta cuando se hace click sobre una categoría en el filtro.
     * @param textoCategoria el nombre de la categoría.
     * @param idCategoria el id de la categoría.
     * */
    @Override
    public void onCategoriaFiltroHeaderClick(String textoCategoria, int idCategoria) {
        mCategoriaFiltroNuevoTexto = textoCategoria;
        mPresenter.OnInteractorCallSubCategories(idCategoria, SharedPreferencesUtil.getInstance().getTokenUsuario());
        mPresenter.OnInteractorCallBrowser(SharedPreferencesUtil.getInstance().getTokenUsuario(), textoCategoria);
    }

    /**
     * Método para limpiar los datos de los programas cada que se utiliza el filtro o buscador.
     * */
    private void onLimpiarDataProgramas()
    {
        mTitles.clear();
        mPrograms.clear();
        mAdapter.notifyDataSetChanged();
//        onQuitarCategoriasPrincipales();
    }


    /**
     * WS que llena el menú superior del recycler view
     * */
    @Override
    public void getCategoriesSuccess(CategoriesResponse data) {
        if(data != null){
            mCategoriesData = data;
            setDataIntoHeaderRV(data);
        }
    }

    /**
     * Método que muestra la imagen y texto de "No Hay programas"
     * */
    @Override
    public void onNoHayProgramas() {
        onNoExistenProgramas();
    }

    /**
     * Métodos para ocultar la sección de Porque Viste.
     * */
    @Override
    public void onPorqueVisteVacío() {
        //mTitles.remove(PORQUE_VISTE_KEY);
        mPorqueVisteRecycler.setVisibility(View.GONE);
        //mAdapter.notifyDataSetChanged();
    }

    private void onQuitarCategoriasPrincipales()
    {
        mPorqueVisteRecycler.setVisibility(View.GONE);
        mMasVistoRecycler.setVisibility(View.GONE);
    }


    /**
     * Método que muestra todos los programas asociados a una subcategoría
     * @param data todos los programas incluidos en esa subcategoría.
     * */
    @Override
    public void getProgramsSubCategorySuccess(ProgramsResponse data) {
        if(data != null){
            // onQuitarCategoriasPrincipales();
            onLimpiarDataProgramas();
            mTitles.clear();
            mPrograms.clear();
            mPrograms.addAll(data.getPrograms().getAllData());
            mTitles.addAll(data.getPrograms().getAllCategories());
            mAdapter.addAll(data.getPrograms().getAllData(), data.getPrograms().getAllCategories());
            //mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * <p>Método que muestra la imagen y texto de "No Hay programas"</p>
     * */
    private void onNoExistenProgramas()
    {
//            mSinProgramasTexto.setVisibility(View.VISIBLE);
//            mSinProgramasImageView.setVisibility(View.VISIBLE);
        mProgramasContainer.setVisibility(View.GONE);
    }

    /**
     * <p>Método que muestra el Mensaje "No hay programas" en el carrusel de destacados.
     * Como este metodo se ejecuta cuando la vista aún no existe, se crea un ViewTreeObserver que cambiará
     * las medidas cuando la vista ya haya sido dibujada y luego se eliminará el ViewTreeObserver.</p>
     * */
    private void onNoExistenDestacados(){
        mSinProgramasTexto.setVisibility(View.VISIBLE);
        mSinProgramasImageView.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
//        final ViewTreeObserver observer= mSinProgramasImageView.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ViewGroup.LayoutParams p = mViewPager.getLayoutParams();
//                p.height = mSinProgramasImageView.getHeight();
//                mViewPager.setLayoutParams(p);
//                observer.removeOnGlobalLayoutListener(this);
//            }
//        });

    }

    /**
     * Método del ciclo de vida de los Fragment
     * */
    @Override
    public void onResume() {
        //mListener.setHomeInMainView();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null){
            mPresenter.onDestroy();
        }
        mToolbackLocked = false;
        super.onDestroy();
    }

}
