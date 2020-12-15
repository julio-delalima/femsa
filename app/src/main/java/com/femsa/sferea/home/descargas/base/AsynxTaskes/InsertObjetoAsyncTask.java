package com.femsa.sferea.home.descargas.base.AsynxTaskes;

import android.os.AsyncTask;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.home.descargas.base.DBManager;

public class InsertObjetoAsyncTask extends AsyncTask<DBManager.ObjetoAprendizaje, String, String> {

    private ObjectDetailResponseData mData;
    private String rutaArchivo, rutaImagen, mNombrePrograma;

    public interface OnBDStatus{
        void OnInicioQuery();
        void OnFinQuery();
        void OnErrorQuery(String error);
    }

    private OnBDStatus mListener;

    public void setListener(OnBDStatus listener) {
        mListener = listener;
    }

    public void setRutas(String archivo, String imagen, String nombre){
        rutaArchivo = archivo;
        rutaImagen = imagen;
        mNombrePrograma = nombre;
    }

    public void setData(ObjectDetailResponseData data) {
        mData = data;
    }

    @Override
        protected String doInBackground(DBManager.ObjetoAprendizaje... bd) {
            mListener.OnInicioQuery();
            try {
                bd[0].setmData(mData);
                //bd[0].dropTables();
                bd[0].insertarObjetoEnBD(rutaImagen, rutaArchivo, mNombrePrograma);
            }
            catch (Exception ex){
                mListener.OnErrorQuery(AppTalentoRHApplication.getApplication().getResources().getString(R.string.descarga_fallida));
            }


            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        public void onPostExecute(String message) {
            mListener.OnFinQuery();
        }
    }

