package com.femsa.sferea.Utilities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;

import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.DataGusanosEscaleras;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadManager extends AsyncTask<ObjetoAprendizajeActivity.JuegosTaskParams, String, String> {

    private ProgressDialog progressDialog;
    private String fileName;
    private String folder;
    private boolean isOffLine;
    private static final String NOMBRE_CARPETA_KEY = "MyKOFLearning";
    private static final String FILE_EXTENSION = ".zip";
    private static final String FULL_PATH = "Android/data/com.femsa.sferea/files/Download/";


    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(ObjetoAprendizajeActivity.JuegosTaskParams... juego) {
        int count;
        try {
           /* URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length*/
            //int lengthOfFile = connection.getContentLength();


            // input stream to read file - with 8k buffer
            int lengthOfFile = juego[0].getBuffer();
            InputStream input = juego[0].getZip();
            isOffLine = juego[0].isOffLine();
            //String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.getDefault()).format(new Date());

            //Append timestamp to file name
            fileName = "juego"; //juego[0].getNombreJuego();

            //External directory path to save file
            String outputFile;
            if(FileManager.isSDCardPresent(juego[0].getmContext())){
                folder = Environment.getExternalStorageDirectory() + File.separator + NOMBRE_CARPETA_KEY + File.separator;
                outputFile = Environment.getExternalStorageDirectory() + File.separator + FULL_PATH;
            }
            else{
                folder = AppTalentoRHApplication.getApplication().getFilesDir().getAbsolutePath() + File.separator + NOMBRE_CARPETA_KEY + File.separator;
                outputFile = AppTalentoRHApplication.getApplication().getFilesDir().getAbsolutePath() + File.separator + FULL_PATH;
            }

            //Create androiddeft folder if it does not exist
            File directory = new File(folder);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Output stream to write file
            OutputStream output = new FileOutputStream(folder + fileName  + FILE_EXTENSION);

            byte[] data = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lengthOfFile));
                LogManager.d("Descarga", "Progress: " + (int) ((total * 100) / lengthOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            unzip(folder + fileName + FILE_EXTENSION, outputFile, juego[0].getIdObjeto(), juego[0].getListenerJuego(), juego[0].getVersion(), juego[0].getDataGE());
            LogManager.d("Descargadas", "Descargado en " + folder + fileName + FILE_EXTENSION);
            return "Downloaded at: " + folder + fileName;

        } catch (Exception e) {
            return  e.getMessage();
        }
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
//|        progressDialog.setProgress(Integer.parseInt(progress[0]));
    }


    @Override
    protected void onPostExecute(String message) {
        // dismiss the dialog after the file was downloaded
        // Display File path after downloading
        //Toast.makeText(AppTalentoRHApplication.getApplication(), "Descargado y descomprimido...", Toast.LENGTH_LONG).show();
    }

    private void unzip(String zip, String destino, int idObjeto,
                       FileManager.onDescargaJuego listener, int version,
                       DataGusanosEscaleras data) throws IOException {
        FileManager.unzip(new File(zip), new File(destino), idObjeto, isOffLine, listener, version, data);
    }
}