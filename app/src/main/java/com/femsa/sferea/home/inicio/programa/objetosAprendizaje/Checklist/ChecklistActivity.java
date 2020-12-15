package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Checklist;

import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.CheckListResponseData;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectInteractor;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectView;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.LearningObjectsPresenter;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;

import java.io.InputStream;
import java.util.ArrayList;

public class ChecklistActivity extends AppCompatActivity implements ChecklistView, ChecklistRespuestasAdapter.OnCheckRespondido, DialogFragmentManager.OnDialogManagerButtonActions {

    private ConstraintLayout mRootView;

    private RecyclerView mPreguntasRecycler;

    private Loader mLoader;

    private CheckListPresenter mPresenter;

    private Toolbar mToolbar;

    private ArrayList<String> mCheckRespuestas = new ArrayList<>();

    private Button mFinalizarButton;

    private int mIdObjeto = 0;

    private boolean mContestado = false;

    private LearningObjectsPresenter mPresenterObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        bindComponents();
    }

    private void bindComponents() {
        mRootView = findViewById(R.id.checklist_root_view);
            mRootView.setVisibility(View.INVISIBLE);

        mPreguntasRecycler = findViewById(R.id.checklist_preguntas_recycler);

        mFinalizarButton = findViewById(R.id.checklist_finalizar_boton);
            mFinalizarButton.setOnClickListener(v -> onChecklistTerminada());

        mLoader = Loader.newInstance();

        mToolbar = findViewById(R.id.checklist_toolbar);

        mPresenter = new CheckListPresenter(this, new CheckListInteractor());

        mIdObjeto = getIntent().getIntExtra(ObjetoAprendizajeActivity.ID_OBJECT_KEY, 0);

        initPreguntas();

        initializeToolbar();

        mPresenterObj = new LearningObjectsPresenter(new LearningObjectView() {
            @Override
            public void likeSuccess() {

            }

            @Override
            public void getObjectDetailSuccess(ObjectDetailResponseData data) {

            }

            @Override
            public void showLoader() {

            }

            @Override
            public void hideLoader() {

            }

            @Override
            public void muestraMensaje(int tipoMensaje) {

            }

            @Override
            public void OnMuestraMensajeInesperado(int tipoMensaje, int codigoRespuesta) {

            }

            @Override
            public void OnMarkAsReadExitoso() {

            }

            @Override
            public void OnBonusSuccess() {

            }

            @Override
            public void OnNoInternet() {

            }

            @Override
            public void OnJuegoListo(InputStream zip, int buffer) {

            }
        }, new LearningObjectInteractor());
    }


    /**
     * <p>Método que permite configurar la toolbar de la Activity.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * Método para inicializar el listado de preguntas
     */
    private void initPreguntas() {
        mPresenter.OnInteractorLlamarCheckList(mIdObjeto);
    }

    /**
     * Método para verificar que la Checklist haya sido completada y para enviar el resultado.
     */
    private void onChecklistTerminada() {
        if(mCheckRespuestas.size() < 1)
            {
                DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.total_respuestas_encoesta));
            }
        else
            {
                mPresenter.OnInteractorAddCheckList(mIdObjeto, mCheckRespuestas);
            }
    }

    @Override
    public void OnViewMostrarLoader() {
        mLoader.show(getSupportFragmentManager(), "loader");
    }

    @Override
    public void OnViewEsconderLoader() {
        try
        {
            if(mLoader != null && mLoader.isAdded())
            {
                mLoader.dismiss();
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
    public void OnViewMostrarMensaje(int tipoMensaje, int codigoRespuesta) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje, codigoRespuesta);
    }

    /**
     * Método implementado de ChecklistView que se manda a llamar cuando ya s eobtuvieron todos los datos del WS
     * que contienen la información del checklist.
     * @param data información sobre el checklist.
     * */
    @Override
    public void OnViewPintaCheckList(CheckListResponseData data) {
        guardaRespuestasChecked(data);
       /* for(int i = 0; i < data.getCheckList().getCheckList().getPreguntas().size(); i++)
        {
            for(int j = 0; j < data.getCheckList().getCheckList().getPreguntas().get(i).getRespuestas().size(); j++)
            {
                if(data.getCheckList().getCheckList().getPreguntas().get(i).getRespuestas().get(j).isStatus())
                {
                    mContestado = true;
                    break;
                }
            }
        }*/
        ChecklistAdapter mAdapter = new ChecklistAdapter(this, data, this, mContestado);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPreguntasRecycler.setAdapter(mAdapter);
        mPreguntasRecycler.setLayoutManager(manager);

        //mFinalizarButton.setVisibility(mContestado ? View.GONE : View.VISIBLE);
        mRootView.setVisibility(View.VISIBLE);
    }

    /**
     * Método para guardar en el listado de mCheckRespuestas auqellas que ya fueron contestadas anteriormente.
     * */
    private void guardaRespuestasChecked(CheckListResponseData data)
    {
        for(int i = 0; i < data.getCheckList().getCheckList().getPreguntas().size(); i++)
        {
            for(int j = 0; j < data.getCheckList().getCheckList().getPreguntas().get(i).getRespuestas().size(); j++)
            {
                if(data.getCheckList().getCheckList().getPreguntas().get(i).getRespuestas().get(j).isStatus())
                {
                    mCheckRespuestas.add(String.valueOf(data.getCheckList().getCheckList().getPreguntas().get(i).getRespuestas().get(j).getIdPreguntaCheck()));
                }
            }
        }
    }

    /**
     * Método implemenetado de CheckListView que se manda a llamar cuando se realizó con exíto la inserción de los datos del
     * checklist con el Web Service.
     * */
    @Override
    public void OnViewChecklistAdded() {
        DialogFragmentManager f = DialogFragmentManager.newInstance(
                false, false, true, getResources().getString(R.string.gracias),
                getResources().getString(R.string.informacion_aceptada), getResources().getString(R.string.aceptar), "", false);
        f.setListener(this);
        f.show(getSupportFragmentManager(), "check");
        mPresenterObj.OnInteractorMarkAsRead(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
        //mPresenterObj.OnInteractorCallBonus(mIdObjeto, SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Método implementado de CheckListAdapter que administra la lista de respuestas, agregando o elimninando
     * elementos de acuerdo a si se seleccionan o deseleccionan elementos del Check.
     * */
    @Override
    public void onCheckElegido(String idCheck, boolean checked) {
        if(checked)
            {
                mCheckRespuestas.add(idCheck);
            }
        else
            {
                mCheckRespuestas.remove(idCheck);
            }
    }

    @Override
    public void OnDialogAceptarClick() {
        finish();
    }

    @Override
    public void OnDialogCancelarClick() {

    }
}