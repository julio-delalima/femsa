package com.femsa.sferea.home.descargas.base.AsynxTaskes;

import android.os.AsyncTask;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.home.descargas.base.DBManager;
import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;
import com.femsa.sferea.home.descargas.base.entity.ProgramaEntity;

import java.util.ArrayList;
import java.util.List;

public class ObtenerListadoObjetosAsync extends AsyncTask<DBManager.ObjetoAprendizaje, String, String> {

    public interface OnListadoObjetos{
        void onInitQuery();
        void onQuerysuccess(List<ObjetoAprendizajeEntity> objs, List<ProgramaEntity> progs);
        void onQueryError(String message);
    }

    private OnListadoObjetos mListener;

    public void setListener(OnListadoObjetos listener) {
        mListener = listener;
    }

    private List<ProgramaEntity> mListadoProgramas;
    private List<ObjetoAprendizajeEntity> mListadoObjetos;
    @Override
    protected String doInBackground(DBManager.ObjetoAprendizaje... bd) {
        try {
            mListener.onInitQuery();
            mListadoObjetos = new ArrayList<>(bd[0].getListadoObjetosDescargados());
            mListadoProgramas = new ArrayList<>(bd[0].getListadoProgramas());
        }
        catch (Exception ex){
            mListener.onQueryError(AppTalentoRHApplication.getApplication().getResources().getString(R.string.descarga_fallida));
        }

        return null;
    }

    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        //progressDialog.setProgress(Integer.parseInt(progress[0]));
    }


    @Override
    public void onPostExecute(String message) {
        mListener.onQuerysuccess(mListadoObjetos, mListadoProgramas);
    }
}

