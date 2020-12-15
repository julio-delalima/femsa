package com.femsa.sferea.Utilities;

import android.content.Context;
import android.content.Intent;
import android.net.SSLCertificateSocketFactory;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.core.content.FileProvider;


import com.femsa.sferea.BuildConfig;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.femsa.sferea.Utilities.FileManager.RUTA_COMPLETA_KEY;
import static com.femsa.sferea.Utilities.StringManager.EXCEL_MIME_TYPE;
import static com.femsa.sferea.Utilities.StringManager.EXCEL_TYPE;
import static com.femsa.sferea.Utilities.StringManager.POWERPOINT_MIME_TYPE;
import static com.femsa.sferea.Utilities.StringManager.POWERPOINT_TYPE;
import static com.femsa.sferea.Utilities.StringManager.WORD_MIME_TYPE;
import static com.femsa.sferea.Utilities.StringManager.WORD_TYPE;

public class  DownloadManagerURL extends AsyncTask<String, String, String>{

    private String extencion;
    public String mRutaGuardado;
    private int mIDObjeto;
    private String mObjeto;
    private boolean isOffline;
    private Context mContext;

    public DownloadManagerURL(String extencion, int mIDOBjeto, String objeto, Context context, boolean isOffline) {
        this.extencion = extencion;
        this.mIDObjeto = mIDOBjeto;
        this.isOffline = isOffline;
        mObjeto = objeto;
        mContext = context;
    }

    public interface ONDownloadStatus{
        void onDownloadURLBegin();
        void onDownloadURLEnd();
        void onDownloadURLError(String error);
        void onDownloadURLEndForOffline(String ruta);
    }

    private ONDownloadStatus mListener;

    public void setListener(ONDownloadStatus listener) {
        mListener = listener;
    }

    @Override
    protected String doInBackground(String... f_url) {
        mRutaGuardado = RUTA_COMPLETA_KEY + mIDObjeto + extencion;
        mListener.onDownloadURLBegin();
        if(!new File(mRutaGuardado).exists())
            {
                try {
                    if(!new File(RUTA_COMPLETA_KEY).exists()){
                        new File(RUTA_COMPLETA_KEY).mkdirs();
                    }
                    new File(mRutaGuardado).createNewFile();
                    URL url = new URL(f_url[0]);
                    InputStream is = null;
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if (urlConnection instanceof HttpsURLConnection) {
                        HttpsURLConnection httpsConn = (HttpsURLConnection) urlConnection;
                        httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                        httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
                    }
                    if (urlConnection.getResponseCode() == 200) {
                        is = new BufferedInputStream(urlConnection.getInputStream());
                    }

                        if(is != null){
                        DataInputStream dis = new DataInputStream(is);
                        byte[] buffer = new byte[1024];
                        int length;
                        FileOutputStream fos = new FileOutputStream(new File(mRutaGuardado));
                        while ((length = dis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }

                } catch (Exception ex) {
                    mListener.onDownloadURLEnd();
                    mListener.onDownloadURLError(ex.getMessage());
                }
            }

        mListener.onDownloadURLEnd();
        if(isOffline){
            mListener.onDownloadURLEndForOffline(mRutaGuardado);
        }
        else
            {
                abrirMicrosoft(mRutaGuardado, mObjeto);
            }
        return mRutaGuardado;
    }



    public void abrirMicrosoft(String url, String objeto){
        //Uri uri = Uri.parse("file://"+file.getAbsolutePath());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String mimeType = "";
        switch(objeto){
            case WORD_TYPE:
                mimeType = WORD_MIME_TYPE;
            break;
            case EXCEL_TYPE:
                mimeType = EXCEL_MIME_TYPE;
            break;
            case POWERPOINT_TYPE:
                mimeType = POWERPOINT_MIME_TYPE;
            break;
            default:
        }
        Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", new File(url));
        intent.setDataAndType(uri, mimeType);
        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        //progressDialog.setProgress(Integer.parseInt(progress[0]));
    }


    @Override
    public void onPostExecute(String message) {
        // dismiss the dialog after the file was downloaded
        //this.progressDialog.dismiss();
        // Display File path after downloading
    }
}
