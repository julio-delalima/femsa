package com.femsa.sferea.home.descargas;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.EliminarAsyncTask;
import com.femsa.sferea.home.descargas.base.AsynxTaskes.ObtenerListadoObjetosAsync;
import com.femsa.sferea.home.descargas.base.DBManager;
import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;
import com.femsa.sferea.home.descargas.base.entity.ProgramaEntity;
import com.femsa.sferea.home.descargas.listado.DescargasListadoTemasAdapter;
import com.femsa.sferea.R;
import com.femsa.sferea.home.descargas.listado.DescargasObjetosListadoAdapter;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Ebook.EbookActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.GIFPlayer.GIFActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.PDFViewer.PDFActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoPlayer.VideoPlayerMainActivity;

import java.util.LinkedList;
import java.util.List;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.DOC_PDF;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.DOC_WORD;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.EBOOK;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.EXCEL;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.GIF;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.JUEGO;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.POWER_POINT;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.VEDEO;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.IS_OFFLINE_FILE_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.OFFLINE_FILE_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.TIPO_OBJETO_KEY;

public class DescargasFragment extends Fragment implements DescargasObjetosListadoAdapter.OnDescargaClicked, ObtenerListadoObjetosAsync.OnListadoObjetos, EliminarAsyncTask.OnEliminarInterface {

    private View mView;

    private DescargasListadoTemasAdapter mAdapterTemas;

    private RecyclerView mListadoTemasRecycler;

    private OnDescargasInterface mListener;

    private Loader mLoader;
    private CardView mCVSinDescargas;
    private List<ObjetoAprendizajeEntity> mListadoObjetos = new LinkedList<>();
    private List<ProgramaEntity> mListadoProgramas = new LinkedList<>();
    private DBManager.ObjetoAprendizaje db;
    private ObtenerListadoObjetosAsync mAsync;

    public interface OnDescargasInterface
    {
        /**
         * Método para inicializar el aspecto visual al cargar el fragment de descargas.
         */
        void setDescargasInMainView();
    }

    public void setListener(OnDescargasInterface listener)
    {
        mListener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.descargas_main_activity, container, false);
        bindViews();
        bindAdapter();
        return mView;
    }

    private void bindAdapter(){
        db = new DBManager.ObjetoAprendizaje(getContext());
        mAsync = new ObtenerListadoObjetosAsync();
        mAsync.setListener(this);
        mAsync.execute(db);
    }

    /**
     * Método llamado al inicio de la ejecución del fragment para cargar todos los elementos visuales.
     * */
    private void bindViews()
        {
            mAdapterTemas = new DescargasListadoTemasAdapter(getContext(), this, mListadoProgramas, mListadoObjetos);
            if (mListener != null) {
                mListener.setDescargasInMainView();
            }
            mLoader = Loader.newInstance();
            LinearLayoutManager mLayourManager = new LinearLayoutManager(getContext());
            mLayourManager.setOrientation(LinearLayoutManager.VERTICAL);
            mListadoTemasRecycler = mView.findViewById(R.id.recycler_listado_descargas);
            mListadoTemasRecycler.setLayoutManager(mLayourManager);
            mListadoTemasRecycler.setAdapter(mAdapterTemas);
            mCVSinDescargas = mView.findViewById(R.id.cv_activos_descargas);
        }

    public void showLoader() {
        try
        {
            if(mLoader != null && !mLoader.isAdded())
            {
                mLoader.show(getChildFragmentManager(),"loader");
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    public void hideLoader() {
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
    public void onDescargaClicked(ObjetoAprendizajeEntity objeto) {
        int tipoObjeto = objeto.getTipo();
        Intent sendTo;
        switch(tipoObjeto)
        {
            case GIF:
                sendTo = new Intent(getContext(), GIFActivity.class);
                break;
            case DOC_PDF:
                sendTo = new Intent(getContext(), PDFActivity.class);
                break;
            case EBOOK:
                sendTo = new Intent(getContext(), EbookActivity.class);
                break;
            case VEDEO:
                sendTo = new Intent(getContext(), VideoPlayerMainActivity.class);
                break;
            case DOC_WORD:
            case JUEGO:
            case POWER_POINT:
            case EXCEL:
                sendTo = new Intent(getContext(), ObjetoAprendizajeActivity.class);
                break;
            default:
                sendTo = null;
        }
        if(sendTo!= null)
        {
            sendTo.putExtra(IS_OFFLINE_FILE_KEY, true);
            sendTo.putExtra(OFFLINE_FILE_KEY,  objeto);
            sendTo.putExtra(TIPO_OBJETO_KEY, objeto.getTipo());
            startActivity(sendTo);
        }
    }

    @Override
    public void onEliminarClicked(ObjetoAprendizajeEntity objeto) {
        EliminarAsyncTask eliminar = new EliminarAsyncTask();
        eliminar.setListener(this);
        eliminar.setObjeto(objeto);
        eliminar.execute(db);
    }

    @Override
    public void onInitQuery() {
        showLoader();
    }

    @Override
    public void onQueryDeletesuccess() {
        hideLoader();
        ObtenerListadoObjetosAsync mObjetoAsyncTemp = new ObtenerListadoObjetosAsync();
        mObjetoAsyncTemp.setListener(this);
        mObjetoAsyncTemp.execute(db);
    }

    @Override
    public void onQuerysuccess(List<ObjetoAprendizajeEntity> objs, List<ProgramaEntity> progs) {
        if(progs.size() < 1){
            mListadoTemasRecycler.setVisibility(View.GONE);
            mCVSinDescargas.setVisibility(View.VISIBLE);
        }
        else{
            mListadoObjetos.clear();
            mListadoProgramas.clear();
            mCVSinDescargas.setVisibility(View.GONE);
            mListadoObjetos.addAll(objs);
            mListadoProgramas.addAll(progs);
            mAdapterTemas.setListados(mListadoProgramas, mListadoObjetos);
        }
        hideLoader();
    }

    @Override
    public void onQueryError(String message) {
        hideLoader();
        DialogManager.displaySnack(getFragmentManager(), message);
    }
}
