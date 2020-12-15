package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.FacilitadorDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.DetallePaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.TemaEspecificoDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.miCuenta.configuracion.Idiomas;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.ObtenerFacilitador.ObtenerAreaYFacilitadorActivity;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.HoraSpinnerDialog;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CrearCelulaTresActivity extends AppCompatActivity implements  TemaEspecificoAdapter.OnTemaEspecifico, HoraSpinnerDialog.OnHorasSelected, View.OnClickListener, PaisesView{

    public static final String TIPO_KEY = "Tipo" ;
    private Toolbar mtoolbar;
    private ImageButton btnFacilitador;
    private Button mBtnSiguinete;
    private RecyclerView mTemasRecycler;
    private int mIndiceTemas = -1;

    private ArrayList<String> mTitulosTemas, mSubtemas, mAreasProceso, mTiempoSugerido;
    private ArrayList<FacilitadorDTO> mFacilitadores;
    private TextView mTitulo, mPaisSolicitante;
    private CircleImageView mAgregarTemaButton;
    private TemaEspecificoAdapter mAdapterTema;
    private EditText mTemaAgregarET, mTemaGeneral;
    private CelulaDTO mCelulaDTO, mCelulaDTOEditable;
    private float horas;
    Bundle horasTotal = new Bundle();
    private LinearLayoutManager LayoutManager;
    private ImageButton mbtnDerecha,mbtnIzquierda;
    private TextView mIndicadorPagina, mPaginaTotal;
    private int pagina;
    private String mTipo;
    private ImageView mPaisSolicitanteBandera;
    private Spinner mPaisSpinner;
    private TextView mTitleInformacion;
    private ConstraintLayout mPaisReceptor;
    private TextView mtitlePaisSolicitante;
    private TextView mTitlePaisReceptor;
    private PaisesPresenter mPresenter;
    private ConstraintLayout mcontador;

    //private ImageView mEliminarFacilitador;
    //private ConstraintLayout mContainerAñadirPuesto;
    //private CardView mContainerFacilitador;
    //private TextView mFacilitadorNombre, mFacilitadorPuesto;

    
    
    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_celula_tres);
        mCelulaDTO = (CelulaDTO) getIntent().getSerializableExtra("CelulaDTO");
        mCelulaDTOEditable = (CelulaDTO) getIntent().getSerializableExtra("Editar");
        Intent intent = getIntent();
        mTipo = intent.getStringExtra(TIPO_KEY);
        mPresenter = new PaisesPresenter(this, new PaisesInteractor());
        mPresenter.onInteractorCallPaises(SharedPreferencesUtil.getInstance().getTokenUsuario());
        mPresenter.onInteractorCallDetallePais(SharedPreferencesUtil.getInstance().getTokenUsuario(), SharedPreferencesUtil.getInstance().getIDPais());
        bindviews();
        bindListeners();
         if(mTipo.equals("Inducción")){
            mPaisSpinner.setVisibility(View.GONE);
            mTitlePaisReceptor.setVisibility(View.GONE);
            mPaisReceptor.setVisibility(View.GONE);
            mtitlePaisSolicitante.setVisibility(View.GONE);
        }
        mTitulo.setText(mTipo.equals(getResources().getString(R.string.Celula)) ? getResources().getString(R.string.title_celulas): mTipo);
        mTitleInformacion.setText(getResources().getString(R.string.label_informacion_celula, mTipo.toLowerCase()));
        mTemaAgregarET.setHint(R.string.tema_especifico);
        if (mTitulosTemas.size() <= 0){
            mcontador.setVisibility(View.GONE);
            mbtnDerecha.setVisibility(View.INVISIBLE);
            mbtnIzquierda.setVisibility(View.INVISIBLE);
            mBtnSiguinete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPaginaTotal.setText(String.valueOf(mAdapterTema.getItemCount()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    private void bindviews(){
        mtoolbar = findViewById(R.id.toolbar_celulas3);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
            if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        mBtnSiguinete = findViewById(R.id.btn_siguiente);
        mTemasRecycler = findViewById(R.id.crear_celula_tema_especifico_rv);
        mAgregarTemaButton = findViewById(R.id.agregar_tema_especifico_celula_tres_activity);
        mTemaAgregarET = findViewById(R.id.editext_titulo_nuevo_tema);
        mTemaGeneral = findViewById(R.id.celula_trex_tema_general_et);
        mbtnIzquierda = findViewById(R.id.left_button);
        mbtnDerecha = findViewById(R.id.right_button);
        mIndicadorPagina = findViewById(R.id.indicador_temas);
        mPaginaTotal = findViewById(R.id.total_cardview_temas);
        mTitulo = findViewById(R.id.Titulo_Celula_tres);
        mPaisSolicitante = findViewById(R.id.celula_tres_pais_solicitante_tv);
        mcontador = findViewById(R.id.contadorConstrain);
        mPaisSolicitanteBandera = findViewById(R.id.imageView19);
        mPaisSpinner = findViewById(R.id.spinner_pais_receptor);
        mTitleInformacion = findViewById(R.id.title_informacion);
        mtitlePaisSolicitante = findViewById(R.id.title_pais_receptor);
        mPaisReceptor = findViewById(R.id.contenedor_pais_Solicitante);
        mTitlePaisReceptor = findViewById(R.id.title_paisReceptor);
    }

    /**
     * Método para configurar el spinner de los paises que se van a mostrar al momento de crear las células.
     * @param data listado de paises junto con su imagen correspondiente.
     * */
    private void configurarSpinner(PaisesResponseData data)
    {
        ArrayList<Idiomas> mPaisesListado = new ArrayList<>();
        for(int i = 0; i < data.getmPaises().getListadoPaises().size(); i++)
            {
                String pais = data.getmPaises().getListadoPaises().get(i).getNombrePais();
                int bandera = -1;
                mPaisesListado.add(
                        new Idiomas(
                                pais,
                                bandera,
                                data.getmPaises().getListadoPaises().get(i).getIdPais(),
                                data.getmPaises().getListadoPaises().get(i).getRutaImagen()
                        )
                );
            }

        CelulasSpinnerAdapter adapter = new CelulasSpinnerAdapter(this, R.layout.item_element_spinner, mPaisesListado);

        mPaisSpinner.setAdapter(adapter);

        mPaisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = ((TextView)view.findViewById(R.id.item_spinner_nombre_idioma)).getText().toString();
                mCelulaDTO.setPaisSolicitanteId(SharedPreferencesUtil.getInstance().getIDPais());
                mCelulaDTO.setPaisReceptorId(view.getId());
                mCelulaDTO.setPaisReceptorBandera(RequestManager.IMAGEN_CUADRADA_PAIS + "/" + mPaisesListado.get(pos).getRutaImagen());
                mCelulaDTO.setPaisReceptor(item);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void bindListeners() {
        mTitulosTemas = new ArrayList<>();
        mFacilitadores = new ArrayList<>();
        mSubtemas = new ArrayList<>();
        mAreasProceso = new ArrayList<>();
        mTiempoSugerido = new ArrayList<>();
        horas = 0;
        mBtnSiguinete.setOnClickListener(v-> onSiguiente());
        mbtnIzquierda.setOnClickListener(this);
        mbtnDerecha.setOnClickListener(this);

        mAgregarTemaButton.setOnClickListener(v->
                onAgregarTemaEspecifico()
        );

        mAdapterTema = new TemaEspecificoAdapter(this);
            mAdapterTema.setListener(this);


            if(mCelulaDTOEditable != null)
                {
                        for(int i = 0; i < mCelulaDTOEditable.getListadoTemasEspecificos().size(); i++)
                            {
                                mAdapterTema.addItem(
                                        mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getTituloTemaEspecifico(),
                                        mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getFacilitador(),
                                        mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getTiempoSugerido(),
                                        mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getAreaProceso(),
                                        mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getSubtemaEspecifico()
                                );
                                mTitulosTemas.add(mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getTituloTemaEspecifico());
                                mSubtemas.add(mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getSubtemaEspecifico());
                                mAreasProceso.add(mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getAreaProceso());
                                mTiempoSugerido.add(mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getTiempoSugerido());
                                mFacilitadores.add(mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getFacilitador());
                            }
                        mCelulaDTO = mCelulaDTOEditable;
                        mTemaGeneral.setText(mCelulaDTOEditable.getTemaGeneral());
                }


        LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTemasRecycler.setAdapter(mAdapterTema);
        mTemasRecycler.setLayoutManager(LayoutManager);
        mTemasRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                obtenerpagina();
                mIndicadorPagina.setText(String.valueOf(pagina+1));

            }
        });
    }

    private void obtenerpagina(){
        pagina = LayoutManager.findFirstVisibleItemPosition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Método que se manda a llamar cuando se presiona el botónde siguiente una vez terminado de llenar los campos
     * */
    private void onSiguiente() {
        if(mCelulaDTOEditable != null)
            {
                if(!mTitulosTemas.isEmpty())
                    {
                        if(onValidarTemasEspecificos())
                            {
                                for(int i = 0; i < mSubtemas.size(); i++){
                                    mAdapterTema.addArea(mAreasProceso.get(i), i);
                                    mAdapterTema.addSubtema(mSubtemas.get(i), i);
                                }
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("CelulaDTOEditable", mCelulaDTO);
                                onCompletarDTOCelula();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        else
                            {
                                DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.campos_necesarios), getResources().getString(R.string.femsa));
                            }
                    }
                else
                    {
                        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.agregar_tema_especifico_vacio));
                    }
            }
        else
            {
                if (!mTemaGeneral.getText().toString().isEmpty())
                    {
                        if(!mTitulosTemas.isEmpty())
                            {
                                if(onValidarTemasEspecificos())
                                    {
                                        horas = (float) 0;
                                        sumahoras();
                                        onCompletarDTOCelula();
                                        FragmentManager fm = getSupportFragmentManager();
                                        RangoFechasFragment rangofecha = new RangoFechasFragment();
                                        rangofecha.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);
                                        rangofecha.setCancelable(false);
                                        rangofecha.setArguments(horasTotal);
                                        rangofecha.setDTO(mCelulaDTO);
                                        rangofecha.show(fm, "dialog");
                                    }
                                else
                                    {
                                        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.campos_necesarios), getResources().getString(R.string.femsa));
                                    }
                            }
                        else
                            {
                                DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.agregar_tema_especifico_vacio));
                            }
                    }
                else
                    {
                        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.agregar_tema));
                    }
            }
    }

    /**
     * Método que suma las horas de todos los temas específicos para indicar al usuario la duración total
     * de la solicitud.
     * */
    private void sumahoras() {
        for (int i = 0; i<mTiempoSugerido.size(); i++){
            horas = horas + Float.valueOf(mTiempoSugerido.get(i).trim());
        }
        horasTotal.putFloat(RangoFechasFragment.HORAST_KEY, horas);
        horasTotal.putString(RangoFechasFragment.TIPO_KEY, mTipo);
    }

    /**
     * Método que guarda en el DTO local de la solicitud toda la información agregada por el usuario para pdoer enviarla
     * a la siguiente actividad, la de confirmar.
     * */
    private void onCompletarDTOCelula() {

        ArrayList<TemaEspecificoDTO> mListadoTemas = new ArrayList<>();
        for (int i = 0; i < mAdapterTema.getTituloTemas().size(); i++)
            {
                mListadoTemas.add(
                        new TemaEspecificoDTO(mAdapterTema.getTituloTemas().get(i),
                                mAdapterTema.getAreaProceso().get(i),
                                mAdapterTema.getSubtemaEspecifico().get(i),
                                mAdapterTema.getFacilitadores().get(i),
                                mAdapterTema.getHoras().get(i)));

            }
        mCelulaDTO.setTemaGeneral(mTemaGeneral.getText().toString());
        mCelulaDTO.setListadoTemasEspecificos(mListadoTemas);
    }

    /**
     * Método que agrega un tema específico, creando cada elemento necesario e inicialziandolos con valores vacíos
     * */
    private void onAgregarTemaEspecifico()
        {
            if (!mTemaAgregarET.getText().toString().isEmpty())
                {
                    mTitulosTemas.add(mTemaAgregarET.getText().toString());
                    mFacilitadores.add(null);
                    mAreasProceso.add("");
                    mSubtemas.add("");
                    mTiempoSugerido.add("0");
                    mAdapterTema.addItem(mTemaAgregarET.getText().toString(), null, "", "", "");
                    mTemasRecycler.scrollToPosition(mTitulosTemas.size()-1);
                    mPaginaTotal.setText(String.valueOf(mAdapterTema.getItemCount()));
                    mTemaAgregarET.getText().clear();

                }
            else
                {
                    DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.tema_especifico_sin_titulo));
                }
            if (mTitulosTemas.size() > 0){
                mTemaAgregarET.setHint(R.string.otrotema);
                mcontador.setVisibility(View.VISIBLE);
                mbtnDerecha.setVisibility(View.VISIBLE);
                mbtnIzquierda.setVisibility(View.VISIBLE);
                mBtnSiguinete.setVisibility(View.VISIBLE);
            }


        }

    /**
     * <p>Metoro que te permiete recorrer la lista a la derecha</p>
     * @param v parametro de la vista que contiene el elemento.
     */
    public void toRight(View v) {
        int pos = LayoutManager.findLastVisibleItemPosition() + 1;
        mTemasRecycler.scrollToPosition(pos >= mAdapterTema.getItemCount()? mAdapterTema.getItemCount()-1: pos);
        if(pos>= mAdapterTema.getItemCount()){

        }
        obtenerpagina();
        mIndicadorPagina.setText(String.valueOf(pagina+1));
    }

    /**
     * <p>Metodo que te permite recorrer la lista a la izquierda</p>
     * @param v
     */

    public void toLeft(View v) {
        int pos = LayoutManager.findFirstVisibleItemPosition() - 1;
        mTemasRecycler.scrollToPosition(pos < 0? 0: pos);
        if (pos<0) {

        }
        obtenerpagina();
        mIndicadorPagina.setText(String.valueOf(pagina+1));
    }

    private boolean onValidarTemasEspecificos()
        {
            if (mSubtemas == null){
                return false;
            }
            for(String subtema : mSubtemas)
                {
                    if(subtema.equals(""))
                        {
                            return false;
                        }
                }
            for(String areaproceso : mAreasProceso)
            {
                if(areaproceso.equals(""))
                {
                    return false;
                }
            }
            for(FacilitadorDTO facilitadorDTO : mFacilitadores)
            {
                if(facilitadorDTO == null || facilitadorDTO.getNombreFacilitador().equals(""))
                {
                    return false;
                }
            }
            for(String tiempos : mTiempoSugerido)
            {
                if(tiempos.equals("0"))
                {
                    return false;
                }
            }
            return  true;
        }

    /**
     * Método que cierra el Softkeyboard si se hace click afuera de él.
     * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Método que trae de vuelva un FacilitadorDTO el cual es asignado al listado de Facilitadores y asignado en el RecyclerView
     * en el elemento seleccionado.
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                FacilitadorDTO facilitador = (FacilitadorDTO) data.getSerializableExtra("Facilitador");
                mFacilitadores.set(mIndiceTemas, facilitador);
                mAdapterTema.addFacilitador(facilitador, mIndiceTemas);
                mAdapterTema.addArea(mAreasProceso.get(mIndiceTemas), mIndiceTemas);
                mAdapterTema.addSubtema(mSubtemas.get(mIndiceTemas), mIndiceTemas);
                mAdapterTema.addTema(mTitulosTemas.get(mIndiceTemas), mIndiceTemas);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    /**
     * Método implementado de TemarioEspecificoAdapter que llama al WebService y onActivityResult para traer un Facilitador y asignarlo a la
     * vista y al listado.
     * */
    @Override
    public void onTraerFacilitadores(int indice) {
        if(mTipo.equals("Inducción"))
            {
                mCelulaDTO.setPaisReceptorId(SharedPreferencesUtil.getInstance().getIDPais());
            }
        if(mCelulaDTO.getPaisReceptorId() < 1)
            {
                DialogManager.displaySnack(getSupportFragmentManager(), R.string.agrega_pais_receptor);
            }
        else
            {
                mIndiceTemas = indice;
                Intent intent = new Intent(this, ObtenerAreaYFacilitadorActivity.class);
                intent.putExtra("celula", mCelulaDTO);
                startActivityForResult(intent, 1);
            }
    }

    /**
     * Método implementado de TemarioEspecificoAdapter que elimina de cada elemento el facilitador asignado,
     * borrandolo de la vista del RecyclerView y del listado.
     * */
    @Override
    public void onEliminarFacilitador(int indice) {
        mFacilitadores.set(indice, null);
    }

    /**
     * Método implementado del TemaEspecificoAdapter que manda a llamar el DialogFragment con el spinner selector de horas
     * y le indica el indice donde se va a colocar ese valor.
     * */
    @Override
    public void onAgregarHora(int indice) {
        mIndiceTemas = indice;
        HoraSpinnerDialog horas = HoraSpinnerDialog.newInstance();
        horas.setListener(this);
        horas.show(getSupportFragmentManager(), "horas");
    }

    /***
     *  Método implementado de TemaEspecificoAdapter que agrega el subtema especifico al RecyclerView y al listado en el
     *  índice del RecyclewView correspondiente.
     */
    @Override
    public void onAgregarSubtema(int indice, String subtema) {
        if(indice < mSubtemas.size())
            {
                mSubtemas.set(indice, subtema);
            }
    }

    /***
     *  Método implementado de TemaEspecificoAdapter que agrega el Area/Proceso especifico al RecyclerView y al listado en el
     *  índice del RecyclewView correspondiente.
     */
    @Override
    public void onAgregarAreaProceso(int indice, String areaProceso) {
        if(indice < mAreasProceso.size())
            {
                mAreasProceso.set(indice, areaProceso);
            }
    }

    @Override
    public void onAgregarTemaEspecifico(int indice, String tema) {
        if(indice < mTitulosTemas.size())
        {
            mTitulosTemas.set(indice, tema);
        }
    }

    /**
     * Método implementado de TemaEspecificoAdapter que elimina un tema específico completo y todos sus elementos.
     * */
    @Override
    public void onEliminarTema(int indice) {
        mAdapterTema.removeTema(indice);
        mTitulosTemas.remove(indice);
        mSubtemas.remove(indice);
        mAreasProceso.remove(indice);
        mTiempoSugerido.remove(indice);
        mFacilitadores.remove(indice);
        if (mTitulosTemas.size() < 1){
            mTemaAgregarET.setHint(R.string.tema_especifico);
        }
    }

    @Override
    public void onPosicion(int posicion) {
        //mIndicadorPagina.setText(String.valueOf(posicion));
    }

    /**
     * Método implementado del Spinner selector de horas que agrega al recycler la cantidad de horas seleccionadas
     * en el indice indicado.
     * */
    @Override
    public void onHorasSelected(String horas) {
        horas = horas.trim().replace("H","");
        mAdapterTema.addHoras(horas, mIndiceTemas);
        mAdapterTema.addSubtema(mSubtemas.get(mIndiceTemas), mIndiceTemas);
        mAdapterTema.addArea(mAreasProceso.get(mIndiceTemas), mIndiceTemas);
        mAdapterTema.addTema(mTitulosTemas.get(mIndiceTemas), mIndiceTemas);
        mTiempoSugerido.set(mIndiceTemas, horas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_button:
                toLeft(v);
                break;
            case R.id.right_button:
                toRight(v);
                break;
        }
    }

    @Override
    public void onViewShowLoader() {

    }

    @Override
    public void onViewHideLoader() {

    }

    /**
     * Método implementado de PaisesView que se ejecuta cuando la petición para obtener paises se ejecutó con éxito.
     * @param data listado de paises traidos del WS.
     * */
    @Override
    public void onViewMuestraListadoPaises(PaisesResponseData data) {
        configurarSpinner(data);
    }

    @Override
    public void onViewMuestraDetallePais(DetallePaisesResponseData data) {
        String imagen = "", nombrePais = "";
        for(int i = 0; i < data.getmPais().getListadoPaises().size(); i++){
            if(data.getmPais().getListadoPaises().get(i).getIdPais() == SharedPreferencesUtil.getInstance().getIDPais()){
                    nombrePais = data.getmPais().getListadoPaises().get(i).getNombrePais();
                    imagen = data.getmPais().getListadoPaises().get(i).getRutaImagen();
                    break;
            }
        }
        String fullPath = RequestManager.IMAGEN_CUADRADA_PAIS + "/" + imagen;
        mCelulaDTO.setPaisSolicitanteBandera(fullPath);
        GlideApp.with(getApplicationContext()).load(fullPath).error(R.drawable.sin_imagen).into(mPaisSolicitanteBandera);
        mPaisSolicitante.setText(nombrePais);
        mCelulaDTO.setPaisSolicitante(nombrePais);
    }

    @Override
    public void onViewMuestraMensaje(int tipo, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipo, codigo);
    }

    @Override
    public void onViewNoAuth() {
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onViewMuestraMensaje(int tipo) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipo);
    }
}
