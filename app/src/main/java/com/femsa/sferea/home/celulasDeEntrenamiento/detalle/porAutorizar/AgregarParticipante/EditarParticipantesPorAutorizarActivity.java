package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porAutorizar.AgregarParticipante;

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

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteData;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.JefeDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.ParticipanteDTO;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.CelulaEntrenamiento;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarParticipantesPorAutorizarActivity extends AppCompatActivity implements View.OnClickListener, ParticipanteView, AdapterParticipantes.OnParticipanteAdapter{

    //Elemtos de la vista
    private Toolbar mtoolbar;
    private TextView mTvFecha;
    private Button mbtnSiguiente;
    public static final String TIPO_KEY = "Tipo";
    private EditText mEtNumEmpleado;
    private CircleImageView mBtnAgregarEmpleado;
    private ArrayList<ParticipanteDTO> listaParticipantes, mListadoNuevosparticipantes, mListaParticipanteseliminados; // listado para mostrar en el adapter
    private ParticipantePresenter mPresenter; //presentador para  realizar la peticion
    private AdapterParticipantes mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView mRvParticipantes;
    private ImageButton mStepRigth;
    private ImageButton mStepLeft;
    private TextView mPagina;
    private TextView mTotal;
    private int i;
    private TextView mTitulo;
    private ArrayList<String> mListAuxParticipantes;
    private ArrayList<JefeDTO> mListaJefes;
    private NestedScrollView mContenedor;
    Loader loader;
    private int pagina;
    private String mTipo;
    private TextView mEtiqueta;
    private ConstraintLayout ContenedorIndicador;
    private ArrayList<Integer> mListadoEliminar = new ArrayList<>();
    private String tipoSolicitud;
    private int idSolicitud = -1;
    private int idEliminarPosicion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_celula_dos);
        Bundle datos = this.getIntent().getExtras();
        mTipo = datos.getString(TIPO_KEY);
        tipoSolicitud =datos.getString("tipoSolicitud");
        listaParticipantes = (ArrayList<ParticipanteDTO>) getIntent().getSerializableExtra("PorAutorizar");
        mListaJefes = (ArrayList<JefeDTO>) getIntent().getSerializableExtra("jefes");


        for(int i = 0; i < listaParticipantes.size(); i++)
            {
                for(int j = 0; j < mListaJefes.size(); j++)
                    {
                        if(mListaJefes.get(j).getListColaboradores() != null){
                            for(int k = 0; k < mListaJefes.get(j).getListColaboradores().size(); k++)
                            {
                                if(mListaJefes.get(j).getListColaboradores().get(k).getNombreColaborador().equals(listaParticipantes.get(i).getNombreParticipante()))
                                {
                                    JefeDTO jefecito = mListaJefes.get(j);
                                    listaParticipantes.get(i).setNombreJefeDirecto(jefecito.getNombreJefe());
                                    listaParticipantes.get(i).setCorreoJefe(jefecito.getCorreoJefe());
                                    listaParticipantes.get(i).setNumeroJefeDirecto(jefecito.getNumeroEmpleadoSuperior());
                                    listaParticipantes.get(i).setPosicionJefeDirecto(jefecito.getPosicion());
                                    break;
                                }
                            }
                        }

                    }
            }

        mListAuxParticipantes = new ArrayList<>();
        idSolicitud = getIntent().getIntExtra("idSolicitud", 0);
        for(int i = 0; i < listaParticipantes.size(); i++) {
            mListAuxParticipantes.add(String.valueOf(listaParticipantes.get(i).getNumeroRed()));
        }
        bindbinds();
        obtenerFecha();
        bindListeners();
        imitAdapter();

        mContenedor.setVisibility(View.VISIBLE);
        ContenedorIndicador.setVisibility(View.VISIBLE);
        mStepLeft.setVisibility(View.VISIBLE);
        mStepRigth.setVisibility(View.VISIBLE);
        mbtnSiguiente.setVisibility(View.VISIBLE);

        //mTitulo.setText(mTipo);
        //mEtiqueta.setText(this.getResources().getString(R.string.Title_celulas, mTipo));
    }

    private void ponertotal() {
        mTotal.setText(String.valueOf(mAdapter.getItemCount()));
    }

    /**
     * <p>Permite Crear la vista del contenedor del elemeto de participantes</p>
     */
    private void imitAdapter() {
        mManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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

    private void obtenerpagina(){
        pagina = mManager.findFirstVisibleItemPosition();
    }

    private void peticion(int numEmpleado) {
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
        mTvFecha.setText(dia+"/"+mes + "/"+anio);
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
    }

    /**
     * Encuentra la refencia a la vista y sus elementos.
     */
    private void bindbinds() {
        mPresenter = new ParticipantePresenter(this, new ParticipanteInteractor());
        mtoolbar = findViewById(R.id.toolbar_celulas);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitulo = findViewById(R.id.Titilo_label);
        mEtiqueta = findViewById(R.id.LabelParticipantesRegistro);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (tipoSolicitud.equals(CelulaEntrenamiento.INDUCCION)){
                mTitulo.setText(CelulaEntrenamiento.obtenerTipoSolicitudCorto((CelulaEntrenamiento.INDUCCION)));
                mTipo = CelulaEntrenamiento.obtenerTipoSolicitudCorto((CelulaEntrenamiento.INDUCCION));
                mEtiqueta.setText(this.getResources().getString(R.string.Title_celulas, mTipo));
            } else {
                mTitulo.setText(CelulaEntrenamiento.obtenerTipoSolicitudCorto(CelulaEntrenamiento.CELULA));
                mTipo = CelulaEntrenamiento.obtenerTipoSolicitudCorto((CelulaEntrenamiento.CELULA));
                mEtiqueta.setText(this.getResources().getString(R.string.Title_celulas, mTipo));
            }
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
        ContenedorIndicador = findViewById(R.id.contador_constrain);
        mListadoNuevosparticipantes = new ArrayList<>();
        mListaParticipanteseliminados = new ArrayList<>();
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

                    if (listaParticipantes.size() > 0) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("PorAutorizar", mListadoNuevosparticipantes);
                        //returnIntent.putExtra("PorAutorizarEliminados", mListaParticipanteseliminados);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                    else
                        {
                            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.minimo_facilitadores));
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

    /**
     * <p>Respuesta de la petición cuando se agrega a un nuevo participante.</p>
     * @param data Información del participante que se va a agregar.
     */
    @Override
    public void OnViewMostrarParticipante(ParticipanteData data) {
        ParticipanteDTO participante = new ParticipanteDTO(
                data.getmParticipante().getIdParticipante(),
                data.getmParticipante().getNombreParticipante(),
                data.getmParticipante().getSubAreaProcesoParticipante(), //desc posinion
                data.getmParticipante().getSubAreaProcesoParticipante(),
                data.getmParticipante().getFotoPerfil(),
                0,
                data.getmParticipante().getNumeroParticipante(),
                data.getmParticipante().getNumeroJefeParticipante(),
                data.getmParticipante().getAreaFuncionalParticipante()
        );
        participante.setPosicionJefeDirecto(data.getmParticipante().getPosicionJefeParticipante());
        participante.setNumeroJefeDirecto(String.valueOf(data.getmParticipante().getNumeroJefeParticipante()));
        participante.setNombreJefeDirecto(data.getmParticipante().getNombreJefeParticipante());
        participante.setCorreoJefe(data.getmParticipante().getCorreoJefeParticipante());
        listaParticipantes.add(participante);
        mListadoNuevosparticipantes.add(participante);
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
    public void OnViewParticipanteEliminado() {
        mListadoNuevosparticipantes.remove(listaParticipantes.get(idEliminarPosicion));
        listaParticipantes.remove(idEliminarPosicion);
        mListAuxParticipantes.remove(idEliminarPosicion);
        mAdapter.notifyDataSetChanged();
        mTotal.setText(String.valueOf(mAdapter.getItemCount()));
        obtenerpagina();
        mPagina.setText(String.valueOf(pagina+1));
    }


    @Override
    public void OnParticipanteCambiado(int posicion) {
        // mPagina.setText(String.valueOf(posicion+1));
    }


    /**
     * Método para eliminar participantes del adaptet
     * */
    @Override
    public void OnEliminarParticipante(int posicion) {
        idEliminarPosicion = posicion;
        mListadoEliminar.add(listaParticipantes.get(posicion).getIdEmpleado());
        mPresenter.iniciarProcesoEliminarParticipantes(String.valueOf(idSolicitud), mListadoEliminar, false);
    }


}

