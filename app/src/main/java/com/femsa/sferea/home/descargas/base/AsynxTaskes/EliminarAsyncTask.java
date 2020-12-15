package com.femsa.sferea.home.descargas.base.AsynxTaskes;

import android.os.AsyncTask;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.home.descargas.base.DBManager;
import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;
import com.femsa.sferea.home.descargas.base.entity.ProgramaEntity;

import java.io.File;

public class EliminarAsyncTask extends AsyncTask<DBManager.ObjetoAprendizaje, String, String> {

    private ObjetoAprendizajeEntity mObjeto;

    public interface OnEliminarInterface{
        void onInitQuery();
        void onQueryDeletesuccess();
        void onQueryError(String message);
    }

    private OnEliminarInterface mListener;

    public void setListener(OnEliminarInterface listener) {
        mListener = listener;
    }

    public void setObjeto(ObjetoAprendizajeEntity objeto) {
        mObjeto = objeto;
    }

    @Override
    protected String doInBackground(DBManager.ObjetoAprendizaje... bd) {
        try {
            mListener.onInitQuery();
            bd[0].deleteObjeto(mObjeto);
            if(bd[0].cuentaProgramasPorId(mObjeto.getIdPrograma()) < 1){
                bd[0].deleteProgramaById(mObjeto.getIdPrograma());
            }
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
        deleteFiles();
        mListener.onQueryDeletesuccess();
    }

    private void deleteFiles(){
        File imagen = new File(mObjeto.getRutaImagen());
        File archivo = new File(mObjeto.getRutaArchivo());
        if(imagen.exists()){
            imagen.delete();
        }
        if(archivo.exists()){
            archivo.delete();
        }
    }
}

