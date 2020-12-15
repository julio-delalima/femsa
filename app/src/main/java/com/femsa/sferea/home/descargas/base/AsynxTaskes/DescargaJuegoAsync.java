package com.femsa.sferea.home.descargas.base.AsynxTaskes;

import android.net.SSLCertificateSocketFactory;
import android.os.AsyncTask;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.femsa.sferea.Utilities.FileManager.RUTA_COMPLETA_KEY;

public class DescargaJuegoAsync  extends AsyncTask<DescargasParams, String, String> {

    private static final String CARPETA_ARCHIVOS = "Descargados/";
    private static final String CARPETA_OBJETOS = "Objetos/";
    private static final String CARPETA_IMG = "Imagenes/";
    public static final int IMAGEN = 0;
    public static final int ARCHIVO = 1;
    private int tipo;
    private String rutaGuardado;

    public interface OnStatusDescargaJuego{
        void onDownloadBegin();
        void onDownloadEnd(String uri, int tipo);
        void onDownloadError(String error);
    }

    private OnStatusDescargaJuego mListener;

    public void setListener(OnStatusDescargaJuego listener) {
        mListener = listener;
    }

    @Override
    protected String doInBackground(DescargasParams... parametros) {

        tipo = parametros[0].getTipo();
        String nombreArchivo = parametros[0].getNombreArchivo();
        String URLCompleta = parametros[0].URLCompleta;


        rutaGuardado = RUTA_COMPLETA_KEY + CARPETA_ARCHIVOS + (tipo == IMAGEN ? CARPETA_IMG : CARPETA_OBJETOS) + nombreArchivo;

        mListener.onDownloadBegin();

        if(!new File(rutaGuardado).exists())
        {
            try {
                if(!new File(RUTA_COMPLETA_KEY + CARPETA_ARCHIVOS + (tipo == IMAGEN ? CARPETA_IMG : CARPETA_OBJETOS)).exists()){
                    new File(RUTA_COMPLETA_KEY + CARPETA_ARCHIVOS + (tipo == IMAGEN ? CARPETA_IMG : CARPETA_OBJETOS)).mkdirs();
                }
                new File(rutaGuardado).createNewFile();
                URL url = new URL(URLCompleta);
                InputStream is = null;
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    is = new BufferedInputStream(urlConnection.getInputStream());
                }

                if(is != null){
                    DataInputStream dis = new DataInputStream(is);
                    byte[] buffer = new byte[1024];
                    int length;
                    FileOutputStream fos = new FileOutputStream(new File(rutaGuardado));
                    while ((length = dis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }

            } catch (Exception ex) {
                mListener.onDownloadError(AppTalentoRHApplication.getApplication().getResources().getString(R.string.descarga_fallida));
            }
        }


        return rutaGuardado;
    }

    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        //progressDialog.setProgress(Integer.parseInt(progress[0]));
    }


    @Override
    public void onPostExecute(String message) {
        mListener.onDownloadEnd(rutaGuardado, tipo);
    }
}
