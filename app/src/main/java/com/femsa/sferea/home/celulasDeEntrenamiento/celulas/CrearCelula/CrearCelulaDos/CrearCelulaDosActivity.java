package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaDos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteData;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.CrearCelulaTresActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CrearCelulaDosActivity extends AppCompatActivity implements View.OnClickListener, ParticipanteView, AdapterParticipantes.OnParticipanteAdapter{
//Elemtos de la vista
private Toolbar mtoolbar;
private TextView mTvFecha;
private Button mbtnSiguiente;
private Boolean Participar;
public static final String PARTICIPAR_KEY = "partipar";
        public static final String TIPO_KEY = "Tipo";
private EditText mEtNumEmpleado;
private CircleImageView mBtnAgregarEmpleado;
private ArrayList<ParticipanteDTO> listaParticipantes; // listado para mostrar en el adapter
private ParticipantePresenter mPresenter; //presentador para  realizar la peticion
private AdapterParticipantes mAdapter;
private LinearLayoutManager mManager;
private RecyclerView mRvParticipantes;
private ImageButton mStepRigth;
private ImageButton mStepLeft;
private TextView mPagina;
private TextView mTotal;
private CelulaDTO mCelulaDTO, mCelulaDTOEditable;
private int i;
private TextView mTitulo;
private ArrayList<String> mListAuxParticipantes;
private NestedScrollView mContenedor;
Loader loader;
private int pagina;
private String mTipo;
private TextView mEtiqueta;
private ConstraintLayout ContenedorIndicador;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_crear_celula_dos);
    Bundle datos = this.getIntent().getExtras();
    Participar = datos.getBoolean(PARTICIPAR_KEY);
    mTipo = datos.getString(TIPO_KEY);
    listaParticipantes = new ArrayList<>();
    mListAuxParticipantes = new ArrayList<>();
    mCelulaDTOEditable = (CelulaDTO) getIntent().getSerializableExtra("Editar");
    bindbinds();
    obtenerFecha();
    bindListeners();
    mContenedor.setVisibility(View.GONE);
    if(mCelulaDTOEditable != null)
        {
            for(int i = 0; i < mCelulaDTOEditable.getListadoParticipantes().size(); i++)
            {
                mListAuxParticipantes.add(String.valueOf(mCelulaDTOEditable.getListadoParticipantes().get(i).getNumeroParticipante()));
            }
        }
    if (Participar){
        if (!mListAuxParticipantes.contains(SharedPreferencesUtil.getInstance().getNumEmpleado())){
            OnViewHideLoader();
            peticion(Integer.parseInt(SharedPreferencesUtil.getInstance().getNumEmpleado()));
            imitAdapter();
            mContenedor.setVisibility(View.VISIBLE);
            ContenedorIndicador.setVisibility(View.VISIBLE);
            mStepLeft.setVisibility(View.VISIBLE);
            mStepRigth.setVisibility(View.VISIBLE);
            mbtnSiguiente.setVisibility(View.VISIBLE);
        }
    }
    if(mListAuxParticipantes.size() <= 0){
        mContenedor.setVisibility(View.VISIBLE);
        ContenedorIndicador.setVisibility(View.GONE);
        mStepLeft.setVisibility(View.GONE);
        mStepRigth.setVisibility(View.GONE);
        mbtnSiguiente.setVisibility(View.GONE);
    }else {
        mContenedor.setVisibility(View.VISIBLE);
        ContenedorIndicador.setVisibility(View.VISIBLE);
        mStepLeft.setVisibility(View.VISIBLE);
        mStepRigth.setVisibility(View.VISIBLE);
        mbtnSiguiente.setVisibility(View.VISIBLE);
    }
    imitAdapter();
    mTitulo.setText(mTipo.equals(getResources().getString(R.string.Celula)) ? getResources().getString(R.string.title_celulas): mTipo);
    mEtiqueta.setText(this.getResources().getString(R.string.Title_celulas, mTipo.toLowerCase()));
}

/**
 * <p>Método que coloca el total de participantes dentro de la etiqueta de texto.</p>
 * */
private void ponertotal() {
    mTotal.setText(String.valueOf(mAdapter.getItemCount()));
}



/**
 * <p>Permite Crear la vista del contenedor del elemeto de participantes</p>
 */
private void imitAdapter() {
    mManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        if(mCelulaDTOEditable != null)
            {
                mCelulaDTO = mCelulaDTOEditable;
                listaParticipantes.addAll(mCelulaDTOEditable.getListadoParticipantes());
                
            }
    mAdapter = new AdapterParticipantes(this, listaParticipantes);
    mAdapter.setListener(this);
    mRvParticipantes.setLayoutManager(mManager);
    mRvParticipantes.setAdapter(mAdapter);
    mRvParticipantes.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            obtenerpagina();
            mPagina.setText(String.valueOf(pagina+1));
        }
    });
    ponertotal();
    mPagina.setText(String.valueOf(pagina+1));
}

/**
 * <p>Método que obtiene la página actual en la que se enuentra el RecyclerView</p>
 * */
private void obtenerpagina(){
    pagina = mManager.findFirstVisibleItemPosition();
}

/**
 * <p>Método que obtiene la información de un participante de acuerdo a su número de empleado</p>
 * @param numEmpleado Número de empleado
 * */
private void peticion(int numEmpleado) {
    mPresenter = new ParticipantePresenter(this, new ParticipanteInteractor());
    mPresenter.iniciarPeticionObtenerParticipante(SharedPreferencesUtil.getInstance().getTokenUsuario(),
            numEmpleado);

}

private void obtenerFecha() {
    Calendar fecha = Calendar.getInstance();
    String dia;
    int mesnum;
    String mes;
    String anio;
    dia = Integer.toString( fecha.get(Calendar.DAY_OF_MONTH));
    mesnum = fecha.get(Calendar.MONTH);
    mes = cambioMes(mesnum);
    anio = Integer.toString(fecha.get(Calendar.YEAR));
    SimpleDateFormat fechaFormato = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
    mTvFecha.setText(StringManager.parseFecha(dia+"/"+mes + "/"+anio, fechaFormato, SharedPreferencesUtil.getInstance().getIdioma()));

}

private String cambioMes(int mesnum)
{
    String result;
    switch (mesnum){
        case 0:
            result = "Ene";
            break;
        case 1:
            result = "Feb";
            break;
        case 2:
            result = "Mar";
            break;
        case 3:
            result = "Abr";
            break;
        case 4:
            result = "May";
            break;
        case 5:
            result = "Jun";
            break;
        case 6:
            result = "Jul";
            break;
        case 7:
            result = "Ago";
            break;
        case 8:
            result = "Sep";
            break;
        case 9:
            result = "Oct";
            break;
        case 10:
            result = "Nov";
            break;
        case 11:
            result = "Dic";
            break;
        default:
            result = Integer.toString(mesnum);
    }
    return result;
}


private void bindListeners() {
    mbtnSiguiente.setOnClickListener(this);
    mBtnAgregarEmpleado.setOnClickListener(this);
    mStepRigth.setOnClickListener(this);
    mStepLeft.setOnClickListener(this);
    mBtnAgregarEmpleado.setOnClickListener(this);
    mCelulaDTO = new CelulaDTO("", null, "", "", "", SharedPreferencesUtil.getInstance().getIDPais(), SharedPreferencesUtil.getInstance().getIDPais(), null, "", "");
}

/**
 * Encuentra la refencia a la vista y sus elementos.
 */
private void bindbinds() {
    mtoolbar = findViewById(R.id.toolbar_celulas);
    setSupportActionBar(mtoolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    if (getSupportActionBar() != null)
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    mTvFecha = findViewById(R.id.tv_fecha_solicitud);
    mEtNumEmpleado = findViewById(R.id.num_empleado);
    mBtnAgregarEmpleado = findViewById(R.id.add_empleado);
    mbtnSiguiente = findViewById(R.id.btn_siguiente);
    mRvParticipantes = findViewById(R.id.rv_participantes);
    mStepRigth = findViewById(R.id.rignt_step);
    mStepLeft = findViewById(R.id.left_step);
    mPagina = findViewById(R.id.indicador);
    mTotal = findViewById(R.id.total_cardview);
    mContenedor = findViewById(R.id.contenedor_padre);
    loader = Loader.newInstance();
    mTitulo = findViewById(R.id.Titilo_label);
    mEtiqueta = findViewById(R.id.LabelParticipantesRegistro);
    ContenedorIndicador = findViewById(R.id.contador_constrain);
}

/**
 * <p>Metoro que te permiete recorrer la lista a la derecha</p>
 * @param v parametro de la vista que contiene el elemento.
 */
public void toRight(View v) {
    int pos = mManager.findLastVisibleItemPosition() + 1;
    mRvParticipantes.scrollToPosition(pos >= mAdapter.getItemCount()? mAdapter.getItemCount()-1: pos);
    if(pos>= mAdapter.getItemCount()){
        //aqui puede ir un mensaje que diga que ya esta en el ultimo elemento
    }
    obtenerpagina();
    mPagina.setText(String.valueOf(pagina+1));
}

/**
 * <p>Metodo que te permite recorrer la lista a la izquierda</p>
 * @param v
 */

public void toLeft(View v) {
    int pos = mManager.findFirstVisibleItemPosition() - 1;
    mRvParticipantes.scrollToPosition(pos < 0? 0: pos);
    if (pos<0) {
        // aqui puede ir un mensaje que diga que ya esta en el primer elemento
    }
    obtenerpagina();
    mPagina.setText(String.valueOf(pagina+1));
}

@Override
public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
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


@Override
public void onClick(View v) {

    switch (v.getId()){
        case R.id.btn_siguiente:
            if(mCelulaDTOEditable != null)
                {
                    Intent returnIntent = new Intent();
                    onLlenarCelulaDTO();
                    returnIntent.putExtra("CelulaDTOEditable", mCelulaDTO);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            else
                {
                    if (listaParticipantes.size() > 0) {
                        Intent intent = new Intent(this, CrearCelulaTresActivity.class);
                        intent.putExtra(CrearCelulaTresActivity.TIPO_KEY, mTipo);
                        onLlenarCelulaDTO();
                        intent.putExtra("CelulaDTO", mCelulaDTO);
                        startActivity(intent);
                    }else{
                        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.minimo_facilitadores));
                    }
                }
            break;
        case R.id.rignt_step:
            toRight(v);
            break;
        case R.id.left_step:
            toLeft(v);
            break;
        case R.id.add_empleado:
            if (mEtNumEmpleado.getText() != null && !mEtNumEmpleado.getText().toString().isEmpty())
                {
                    String textoaux = String.valueOf(mEtNumEmpleado.getText());
                    if (mListAuxParticipantes.contains(textoaux))
                        {
                            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.participante_ya_agregado), getResources().getString(R.string.femsa));
                        }
                    else
                        {
                            peticion(Integer.parseInt(String.valueOf(mEtNumEmpleado.getText())));
                        }
                }
            mEtNumEmpleado.getText().clear();
    }
}



        @Override
public void OnViewMostrarParticipante(ParticipanteData data) {
    boolean puedeAgregar = true;
    if(mCelulaDTOEditable != null)
        {
            for(int i = 0; i < mCelulaDTOEditable.getListadoTemasEspecificos().size(); i++)
                {
                    if(mCelulaDTOEditable.getListadoTemasEspecificos().get(i).getFacilitador().getIdFacilitador() == data.getmParticipante().getIdParticipante())
                        {
                            puedeAgregar = false;
                            break;
                        }
                }
        }
    if(puedeAgregar)
        {
            listaParticipantes.add(data.getmParticipante());
            mAdapter.notifyDataSetChanged();
            mListAuxParticipantes.add(String.valueOf(data.getmParticipante().getNumeroParticipante()));
            ponertotal();
            mRvParticipantes.scrollToPosition(listaParticipantes.size()-1);
            pagina = listaParticipantes.size();
            mPagina.setText(String.valueOf(listaParticipantes.size()));
            mContenedor.setVisibility(View.VISIBLE);
            ContenedorIndicador.setVisibility(View.VISIBLE);
            mStepLeft.setVisibility(View.VISIBLE);
            mStepRigth.setVisibility(View.VISIBLE);
            mbtnSiguiente.setVisibility(View.VISIBLE);
        }
    else
        {
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.participante_ya_agregado_faciltador), getResources().getString(R.string.femsa));
        }
}

@Override
public void OnViewShowLoader() {
    FragmentManager fm = getSupportFragmentManager();
    if (!loader.isAdded()) {
        loader.show(fm, "Loader");
    }
}

@Override
public void OnViewHideLoader() {
    try
    {
        if(loader != null && loader.isAdded())
        {
            loader.dismiss();
        }
    }
    catch (IllegalStateException ignored)
    {
        // There's no way to avoid getting this if saveInstanceState has already been called.
    }
}

private void onLlenarCelulaDTO()
    {
        mCelulaDTO.setFechadeSolicitud(mTvFecha.getText().toString());
        mCelulaDTO.setListadoParticipantes(listaParticipantes);
    }

@Override
public void OnViewMostrarMensaje(int tipoMensaje) {
    DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
}

@Override
public void OnViewMostrarMensajeInesperado(int tipoMensaje, int codigoError) {
    DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje, codigoError);
}

@Override
public void OnViewSesionInvalida() {
    GlobalActions g = new GlobalActions(this);
    g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
}

@Override
public void OnViewParticipanteInexistente() {
    String message = getResources().getString(R.string.usuario_inexistente_empleado);
    DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, AppTalentoRHApplication.getApplication().getString(R.string.femsa),
            message, AppTalentoRHApplication.getApplication().getString(R.string.ok), "", true);
    fragment.show(getSupportFragmentManager(), "Error");
}


    @Override
public void OnParticipanteCambiado(int posicion) {
   // mPagina.setText(String.valueOf(posicion+1));
}

@Override
public void OnEliminarParticipante(int posicion) {
    listaParticipantes.remove(posicion);
    mListAuxParticipantes.remove(posicion);
    mAdapter.notifyDataSetChanged();
    mTotal.setText(String.valueOf(mAdapter.getItemCount()));
    obtenerpagina();
    mPagina.setText(String.valueOf(pagina+1));
}
}

