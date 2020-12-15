package com.femsa.sferea.masterkiwi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.femsa.sferea.Utilities.WebUtil;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.GameLoadingActivity;
import com.femsa.sferea.UtilsMK.dialog.NoInternetDialog;
//import com.femsa.sferea.activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * <p>Clase que ayuda con la comunicación con la red a MasterKiwi.</p>
 */
public class MasterKiwiNetworkManager
    {
    /**
     * <p>Indice del archivo que se esta descargando.</p>
     */
    public int downloadIndex;
    /**
     * <p>Actividad que esta corriendo en la aplicación.</p>
     */
    public Activity currentActivity;
    /**
     * <p>URL actual que se mando a llamar.</p>
     */
    public String currentUrl;
    /**
     * <p>Request que se hizo a la red.</p>
     */
    public RequestQueue queue;
    /**
     * <p>Arreglo de bytes que se trajo la petcición web.</p>
     */
    public static byte[] masterkiwiNetRequestData;
    /**
     * <p>Bandera que le permite hacer descarga y peticiones.</p>
     */
    public Boolean canDownload = true;
    /**
     * <p>Tarea asincrona para descargar imagenes.</p>
     */
    protected ImageDownload asyncTextures;
    /**
     * <p>Tarea asincrona para descargar sonidos.</p>
     */
    protected AudioDownload asyncAudio;

    /**
     * <p>Declaración de función nativa para reportar lo que regreso la peticion.</p>
     */
    private native void networkResponse(String result);

    /**
     * <p>Declaración de función nativa para reportar que se completo la descarga.</p>
     */
    public native void networkDownloadComplete(String fileDownloaded);

    /**
     * <p>Declaración de función nativa para solicitar un login.</p>
     */
    private native void masterKiwiLoginWithData(String user, String password,boolean checkLocal);

    /**
     * <p>Declaración de función nativa para pedir que se compriman los atlas.</p>
     */
    private native void masterKiwiCompressAtlas(int gameID);

    /**
     * <p>Declaración de función nativa para pedir que se compriman las texturas.</p>
     */
    private native void masterKiwiCompressToRTTEXT();

    /**
     * <p>Declaración de función nativa para generar la lista de archivos.</p>
     */
    private native void masterKiwiGetAssetsList();

    /**
     * <p>Declaración de función nativa para solicitar la descarga de datos del juego.</p>
     */
    private native void masterKiwiDownloadGameData();

    /**
     * <p>Función que pide a MasterKiwi hacer login.</p>
     *
     * @param user     Nombre del usuario.
     * @param password Contraseña del usuario.
     */
    public void wrapperLogin(String user, String password,boolean checkLocal)
        {
        downloadIndex = 0;
        masterKiwiLoginWithData(user, password,checkLocal);
        }

    /**
     * <p>Función que manda a comprimir los atlas.</p>
     *
     * @param currentGame Juego sobre el que se pide trabajar.
     */
    public void wrapperCompressAtlas(int currentGame)
        {
        masterKiwiCompressAtlas(currentGame);
        }

    /**
     * <p>Función que manda a comprimir las texturas.</p>
     */
    public void wrapperTexturesToRTTTEXT()
        {
        masterKiwiCompressToRTTEXT();
        }

    /**
     * <p>Función que manda a descargar los archivos.</p>
     */
    public void wrapperDonwloadAssets()
        {
        masterKiwiGetAssetsList();
        }

    /**
     * Funcion mandada a llamar desde el engine
     */
    protected void requestString(String url)
    {
        currentUrl = url;
        if(WebUtil.isNetworkAvailable(currentActivity.getApplicationContext()))
        {
            makeStringRequest();
        }
        else
        {
            if(currentUrl.contains("a=1"))//Viene de login TODO: cambiar esta verificacion
            {
                NoInternetDialog dialog = NoInternetDialog.newInstance("", "Por favor, verifica tu conexión a internet. No hay registro previo de esta cuenta.");
             /*   dialog.setOnDismissListener((MainActivity)currentActivity);
                dialog.setCancelable(false);
                ((MainActivity)currentActivity).getSupportFragmentManager().beginTransaction().add(dialog, "VoteDialog").commitAllowingStateLoss();*/
            }
            else
            {
                NoInternetDialog dialog = NoInternetDialog.newInstance("", "Por favor, verifica tu conexión a internet");
                dialog.setOnDismissListener((GameLoadingActivity)currentActivity);
                dialog.setCancelable(false);
                ((GameLoadingActivity)currentActivity).getSupportFragmentManager().beginTransaction().add(dialog, "VoteDialog").commitAllowingStateLoss();
            }
        }
    }

    protected void makeStringRequest()
    {
        if(queue == null)
        {
            queue = Volley.newRequestQueue(currentActivity.getBaseContext());
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET,currentUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if(canDownload)
                        {
                            networkResponse(response);
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                cancelDownloads();
                currentActivity.finish();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(this);
        queue.add(stringRequest);
    }

    /**
     * Funcion mandada a llamar desde el engine
     */
    protected void requestImageDownload(String url)
        {
        currentUrl = url;
        downloadIndex++;
        asyncTextures = new ImageDownload();
        asyncTextures.execute(this);
        }

    /**
     * Funcion mandada a llamar desde el engine*
     */
    protected void requestAudioDownload(String url)
        {
        currentUrl = url;
        downloadIndex++;
        asyncAudio = new AudioDownload();
        asyncAudio.execute(this);
        }

    /**
     * Funcion mandada a llamar desde el engine*
     */
    protected void uploadStatsData(String url)
    {
        requestString(url);
    }

    /**
     * <p>Función que cancela cualquier proceso con la red.</p>
     */
    public void cancelDownloads()
        {
        canDownload = false;
        if(queue != null)
        {
            queue.cancelAll(this);
        }
        if(asyncTextures != null)
            {
            asyncTextures.cancel(true);
            }
        if(asyncAudio != null)
            {
            asyncAudio.cancel(true);
            }
        }
    }

/**
 * <p>Clase que hace la petición asincrona de la descarga de una imagen</p>
 */
class ImageDownload extends AsyncTask<MasterKiwiNetworkManager, Void, Void>
    {
    /**
     * <p>Función que se ejecuta en otro hilo.</p>
     *
     * @param wrapper El objeto que sirve para comunicarse con MasterKiwi
     */
    @Override
    protected Void doInBackground(MasterKiwiNetworkManager... wrapper)
        {
        if(isCancelled())
            {
            return null;
            }
        String imageUrl = wrapper[0].currentUrl;
        try
            {
            java.net.URL url = new java.net.URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            String absolutePath;
            if(Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED))
                absolutePath = wrapper[0].currentActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            else
                absolutePath = wrapper[0].currentActivity.getFilesDir().getAbsolutePath();
            File image = new File(absolutePath, String.format("%s.mktx", "TestAli" + wrapper[0].downloadIndex));
            imageUrl = image.getAbsolutePath();
            if(wrapper[0].canDownload)
                {
                FileOutputStream fileOutputStream = new FileOutputStream(imageUrl);
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                bitmap.recycle();
                fileOutputStream.close();
                wrapper[0].networkDownloadComplete(image.getAbsolutePath());
                }
            }
        catch(IOException e)
            {
            e.printStackTrace();
            wrapper[0].cancelDownloads();
            wrapper[0].currentActivity.finish();
            }
        return null;
        }
    }

/**
 * <p>Clase que hace la petición asincrona de la descarga de un sonido</p>
 */
class AudioDownload extends AsyncTask<MasterKiwiNetworkManager, Void, Void>
    {
    /**
     * <p>Función que se ejecuta en otro hilo.</p>
     *
     * @param wrapper El objeto que sirve para comunicarse con MasterKiwi
     */
    @Override
    protected Void doInBackground(MasterKiwiNetworkManager... wrapper)
        {
        if(isCancelled())
            {
            return null;
            }
        String audioUrl = wrapper[0].currentUrl;
        try
            {
            java.net.URL url = new java.net.URL(audioUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            String absolutePath;
            if(Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED))
                absolutePath = wrapper[0].currentActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            else
                absolutePath = wrapper[0].currentActivity.getFilesDir().getAbsolutePath();
            File audio = new File(absolutePath, String.format("%s.wav", "TestAli" + wrapper[0].downloadIndex));
            audioUrl = audio.getAbsolutePath();
            if(wrapper[0].canDownload)
                {
                FileOutputStream fileOutputStream = new FileOutputStream(audioUrl);
                byte[] buffer = new byte[4 * 1024];
                int read;
                while((read = input.read(buffer)) != -1)
                    {
                    fileOutputStream.write(buffer, 0, read);
                    }
                fileOutputStream.flush();
                fileOutputStream.close();
                wrapper[0].networkDownloadComplete(audio.getAbsolutePath());
                }
            }
        catch(IOException e)
            {
            e.printStackTrace();
            wrapper[0].cancelDownloads();
            wrapper[0].currentActivity.finish();
            }
        return null;
        }
    }