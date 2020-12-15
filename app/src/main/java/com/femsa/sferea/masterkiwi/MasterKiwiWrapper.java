package com.femsa.sferea.masterkiwi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.inicio.componentes.activity.HomeActivity;
import com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.DataGusanosEscaleras;
import com.femsa.sferea.masterkiwi.GusanosEscaleras.GusanosEscalerasInteractor;
import com.femsa.sferea.masterkiwi.GusanosEscaleras.GusanosEscalerasPresenter;
import com.femsa.sferea.masterkiwi.GusanosEscaleras.GusanosEscalerasView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.GameLoadingActivity.RECEIVING_GAME_TYPE;

/**
 * <p>Clase que permita la comunicación con MasterKiwi.</p>
 */
public class MasterKiwiWrapper implements GusanosEscalerasView
{
    /**
     * <p>Bandera que indica si ya se cargo la librería nativa de MasterKiwi.</p>
     */
    protected boolean loaded;
    /**
     * <p>Bandera que activa o desactiva los logs de este componente.</p>
     */
    protected boolean debugActivated = true;
    /**
     * <p>ID del proyecto de MasterKiwi que se va a cargar.</p>
     */
    protected int currentGame;
    /**
     * <p>ID del tipo de proyecto que se va a abrir.</p>
     */
    public int gameType;
    /**
     * <p>Nombre del usuario con el que se accederá a MasterKiwi.</p>
     */
    protected String currentUser;
    /**
     * <p>Contraseña del usuario con el que se accederá a MasterKiwi.</p>
     */
    protected String currentPassword;

    protected  boolean useLocalData;

    private GusanosEscalerasPresenter mPresenter;

    private DataGusanosEscaleras mData, mDataRecibida;

    public String naviText = "";

    public String loadingBackgroundPath;
    /**
     * <p>Bandera que indica si los archivos del juego vienen embebidos en la aplicación.</p>
     */
    protected boolean embebedFiles = false;
    /**
     * <p>Los ayudadores que existen para mover los archivos actuales.</p>
     */
    protected List<MoveFileHelper> helpers;
    /**
     * <p>Lista de todos los listeners que esperan noticias de este objeto.</p>
     */
    protected List<MasterKiwiListener> listeners;
    /**
     * <p>Bandera que indica si la aplicación esta en background.</p>
     */
    public boolean isAppOnBackground;
    /**
     * <p>La actividad que creo este objeto</p>
     */
    public Activity currentActivity;
    /**
     * <p>El controlador de todas las peticiones que hace MasterKiwi.</p>
     */
    public MasterKiwiNetworkManager networkManager;
    /**
     * <p>Nombre del juego que se cargará</p>
     */
    public String gameName;
    /**
     * <p>Función encargada de mostrar los logs.</p>
     *
     * @param message Mensaje que se imprimirá en el log.
     */
    public void showDebug(String message)
    {
        if(debugActivated)
            {
            //Log.d("MasterKiwi", message);
            }
    }

    /**
     * <p>Instancia de este objeto para que funcione como singleton.</p>
     */
    protected static MasterKiwiWrapper mkWrapper;
    /**
     * <p>Función que regresa la única instancia que debe existir de este objeto.</p>
     *
     * @return Instancia única de este objeto
     */
    public static MasterKiwiWrapper GetInstance()
        {
        if(mkWrapper == null)
            {
            mkWrapper = new MasterKiwiWrapper();
            mkWrapper.networkManager = new MasterKiwiNetworkManager();
            }

        return mkWrapper;
        }

    /**
     * Constructor de la clase. Este carga las librerías nativas.
     */
    protected MasterKiwiWrapper()
        {
        loaded = false;
        listeners = new ArrayList<MasterKiwiListener>();
        helpers = new ArrayList<MoveFileHelper>();
        try
            {
            loadLibraries();
            loaded = true;
            }
        catch(UnsatisfiedLinkError e)
            {
            System.err.println(e.getMessage());
            }
        catch(Exception e)
            {
            System.err.println(e.getMessage());
            }
        }
    /**
     * <p>Función que asigna la actividad que creo a este objeto.</p>
     *
     * @param activity Actividad desde la cuál se controlará este objeto.
     */
    public void setActivity(Activity activity)
        {
             networkManager.currentActivity = currentActivity = activity;
        }

    /**
     * <p>Método que le pasa los datos del Multijugador al Wrapper para enviarlos al juego</p>
     * */
    public void setDataMultijugador(DataGusanosEscaleras data){
        mData = data;
        mDataRecibida = data;
        LogManager.d("Gusanos", "MKWrapper.setDataMultijugador(); data " + mData.getIdEmpleadoRetado() + " en partida " + mData.getIdPartida());
        mPresenter = new GusanosEscalerasPresenter(this, new GusanosEscalerasInteractor());
    }

    /**
     * <p>Función que carga las librerías nativas de MAsterKiwi.</p>
     */
    protected void loadLibraries()
        {
        System.loadLibrary("main");
        }
    /**
     * <p>Función que destruye la instancia, esta también manda a destruir MastereKiwi.</p>
     */
    public void Destroy()
        {
        networkManager.cancelDownloads();
        nativeWrapperStop();
        listeners.clear();
        }
    /**
     * <p>Función para registrarse a escuchar los eventos.</p>
     *
     * @param nListener Nuevo objeto que le interesá escuchar los eventos de MasterKiwi.
     */
    public void registerForEvents(MasterKiwiListener nListener)
        {
        if(listeners.indexOf(nListener) < 0)
            {
            listeners.add(nListener);
            }
        }
    /**
     * <p>Función que desregistrarse de los eventos.</p>
     *
     * @param nListener Escucha que ya no quiere estar registrado.
     */
    public void unResgisterForEvents(MasterKiwiListener nListener)
        {
        listeners.remove(nListener);
        }
    /**
     * <p>Función que manda a llamar el Login a MasterKiwi.</p>
     *
     * @param user Usuario para hacer login.
     * @param password Contraseña para hacerr login.
     */
    public void loginToMasterKiwi(String user, String password,boolean checkLocal)
        {
        currentUser = user;
        currentPassword = password;
        networkManager.canDownload = true;
        useLocalData = checkLocal;
        if(loaded)
            {
            LoginAsync asyncLogin = new LoginAsync();
            asyncLogin.execute(this);
            }
        }
    /**
     * <p>Función que manda a preguntar si existen nuevos datos del juego en el portal de MasterKiwi.</p>
     *
     * @param gameID ID del juego en cuestión.
     */
    public void askDataDate(int gameID)
        {
        currentGame = gameID;
        networkManager.canDownload = true;
        masterKiwiAskDataDate(gameID);
        }
    /**
     * <p>Función para saber si existen datos del juego en el dispositivo.</p>
     *
     * @param gameID ID del juego en cuestión.
     * @return
     */
    public boolean hasGameData(int gameID)
        {
            return masterKiwiHasGameData(gameID);
        }

    /**
     * <p>Función para saber si existen datos del juego en el dispositivo.</p>
     *
     * @param gameID ID del juego en cuestión.
     * @param path la ubicacion a buscar
     * @return
     */
    public boolean hasGameData(int gameID, String path)
    {
        return masterKiwiHasGameDataPath(gameID, path);
    }


    public void startearGame(int gameID){
        currentGame = gameID;
        //LogManager.d("MasterKiwisito","run");
        runGame(gameID);
    }



    /**
     * <p>Función que dará inicio a MasterKiwi.</p>
     *
     * @param gameID ID del juego en cuestión.
     * @param forceUpdate Bandera para forzar actualización de datos.
     */

    public void startGame(int gameID, boolean forceUpdate)
        {
        //showDebug("StartingGame!!!");
        currentGame = gameID;
        if(embebedFiles && forceUpdate)
            {
            moveFiles();
            }
        else if(!isAppOnBackground)
            {
            if(forceUpdate)
                {
                MasterKiwiArgs args = new MasterKiwiArgs();
                for(int i = listeners.size()-1; i >= 0; i--)
                    {
                    listeners.get(i).onInitiatingCompresure(this, args);
                    }
                CompressAtlasAsync asyncCompress = new CompressAtlasAsync();
                asyncCompress.execute(this);
                }
            else
                {
                    //showDebug("runningGame");
                    if(mData != null){
                        initDataGusanosYEscaleras(
                                mData.getUsado(),
                                mData.getPosJ1(),
                                mData.getPosJ2(),
                                mData.getMapa(),
                                mData.getTurno()
                        );
                    }
                 runGame(gameID);
                }

            }
        }
    /**
     * <p>Función que mueve los archivos descargados a la carpeta que necesita MasterKiwi.</p>
     */
    protected void moveFiles()
        {
        String path = "interface/data";
        String[] list;
        try
            {
            list = currentActivity.getAssets().list(path);
            if(list.length > 0)
                {
                for(String file : list)
                    {
                    helpers.add(new MoveFileHelper(file, getDownloadDirectory(), getStoragePath()));
                    }
                moveFileAsync asyncDownload = new moveFileAsync();
                asyncDownload.execute(this);
                }
            }
        catch(IOException e)
            {
            showDebug(e.getMessage());
            }
        }


    /**
     * <p>Función que mueve los archivos descargados a la carpeta que necesita MasterKiwi.</p>
     */
    protected void moveFiles(String rutaDescargado)
    {
        String path = "interface/data";
        String[] list;
        try
        {
            list = currentActivity.getAssets().list(path);
            if(list.length > 0)
            {
                for(String file : list)
                {
                    helpers.add(new MoveFileHelper(file, rutaDescargado, getStoragePath()));
                }
                moveFileAsync asyncDownload = new moveFileAsync();
                asyncDownload.execute(this);
            }
        }
        catch(IOException e)
        {
            showDebug(e.getMessage());
        }
    }

    /**
     * <p>Función que da inicio al juego dentro de MasterKiwi.</p>
     *
     * @param gameID ID del juego en cuestión.
     */
    protected void runGame(int gameID)
        {
            LogManager.d("MasterKiwisito","rungame");
           // showDebug("Entered runnig");
            masterKiwiSetGame(gameID);
            currentGame = 0;
            nativeWrapperStop();
            Intent intent = new Intent(currentActivity, SDLActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(RECEIVING_GAME_TYPE, gameType);
            currentActivity.startActivity(intent);
        }

    /**
     * <p>Función que inicia el canal de comunicación con MasterKiwi.</p>
     */
    public void initializeWrapper()
        {
            //Log.d("MKWrapper", "antes del native");
            nativeWrapperInit();
        }
    /**
     * <p>Declaración de función nativa para iniciar canal de comunicación.</p>
     */


    public void jsonSQData(String json)
    {
      /* MasterKiwiArgs args = new MasterKiwiArgs();
        for(int i = listeners.size()-1; i >= 0; i--)
        {
            listeners.get(i).jsonSQData(this, args);
        }*/
        //Log.d("***JSON***", "en el wrapper de java - " + json);
        mkWrapper.jsonToSquirrel(json);//no se que hace aqui solo recibe un JSON pero no hace nada.
    }

    /**
     * <p>Método que manda al núcleo de MasterKiwi los datos para iniciar un juego de Gusanos
     * y Escaleras multijugador</p>
     * @param usado toma valores 0 o 1, sirve para saber si esta en el juego o no, si carga la partida o no.
     * @param posJ1 posición del jugador 1 después de la última partida, debe ser mayor a 0.
     * @param posJ2 posición del jugador 2 después de la última partida, debe ser mayor a 0.
     * @param mapa toma valores de 1 - 3 y sirve para indicar al juego cual de los 3 posibles mapas debe cargar
     * @param turno toma valores 0 o 1, 1 significa que es mi turno de jugar, 0 que es turno de mi rival.
     * */
    void initDataGusanosYEscaleras(int usado, int posJ1, int posJ2, int mapa, int turno){
        LogManager.d("Gusanos", "Enviando: " + usado + " - " + posJ1 + " - " + posJ2 + " - " + mapa + " - " + turno);
        mkWrapper.sendDatosGusnaosYEscaleras(usado, posJ1, posJ2, mapa, turno);
    }

    private native void nativeWrapperInit();
    /**          
     * <p>Declaración de función nativa para terminar canal de comunicación.</p>
     */
    private native void nativeWrapperStop();
    /**
     * <p>Declaración de función nativa para asignar el ID del juego a MasterKiwi.</p>
     */
    private native void masterKiwiSetGame(int gameID);
    /**
     * <p>Declaración de función nativa para preguntar a MasterKiwi por datos de un juego.</p>
     */
    private native boolean masterKiwiHasGameData(int gameID);

    /**
     * <p>Declaración de función nativa para preguntar a MasterKiwi por datos de un juego en determinada ruta.</p>
     */
    private native boolean masterKiwiHasGameDataPath(int gameID, String path);
    /**
     * <p>Declaración de función nativa para preguntar a MasterKiwi por actualización de datos.</p>
     */
    private native void masterKiwiAskDataDate(int gameID);
    /**
     * <p>Declaración de función nativa para pedir a MasterKiwi que mueva los archivos.</p>
     */
    public native void moveFile(String from, String to);
    /**
     * <p>Declaración de función nativa para pedir a MasterKiwi que cambie el estado del audio del juego actual.</p>
     */

    public native void jsonToSquirrel(String json);

    public native void sendDatosGusnaosYEscaleras(int usado, int posJ1, int posJ2, int mapa, int turno);

    public native void changeAudioState();
    /**
     * <p>Declaración de función nativa para preguntar a MasterKiwi por el estado actual del audio.</p>
     */
    public native boolean isAudioActivated();
    /**
     * Funcion mandada a llamar desde el engine
     */
    private static void startSDL(int download)
        {
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        if(download == 1)
            {
            wrap.startGame(wrap.currentGame, true);
            }
        else
            {
            wrap.startGame(wrap.currentGame, false);
            }
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private static void onUsersDataReceived(String uData)
        {
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        wrap.showDebug("Llamando callback" + uData);


            if (uData.length() > 4000) {
                int chunkCount = uData.length() / 4000;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= uData.length()) {
                        Log.v("yeison", "chunk " + i + " of " + chunkCount + ":" + uData.substring(4000 * i));
                    } else {
                        Log.v("yeison", "chunk " + i + " of " + chunkCount + ":" + uData.substring(4000 * i, max));
                    }
                }
            }



        MasterKiwiArgs args = new MasterKiwiArgs(uData);
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onLogin(wrap, args);
            }
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void onUserLogOut()
        {
            LogManager.d("MasterKiwisito", "logout");
        MasterKiwiArgs args = new MasterKiwiArgs();
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onLogout(this, args);
            }
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private String getStoragePath()
        {
            //return currentActivity.getFilesDir().getAbsolutePath();
            String path = "";
            if(Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED))
                path = currentActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            else
                path = currentActivity.getFilesDir().getAbsolutePath();
            return path;
        }

    private String getDownloadDirectory()
        {
        String path = "";
        if(Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED))
            path = currentActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        else
            path = currentActivity.getFilesDir().getAbsolutePath();
        return path;
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void netErrorReported(String errorMsg, String masterKiwiMsg)
        {
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        MasterKiwiArgs args = new MasterKiwiArgs(errorMsg, masterKiwiMsg);
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onNetError(this, args);
            }
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void onAtlasCompressed()
        {
        if(loaded)
            {
            MasterKiwiArgs args = new MasterKiwiArgs();
            MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
            for(int i = wrap.listeners.size()-1; i >= 0; i--)
                {
                wrap.listeners.get(i).onInitiatingTextures(this, args);
                }
            PrepareTexturesAsync asyncTextures = new PrepareTexturesAsync();
            asyncTextures.execute(this);
            }
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void onTexturesReady()
        {
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        if(wrap.loaded)
            {
            MasterKiwiArgs args = new MasterKiwiArgs();
            for(int i = wrap.listeners.size()-1; i >= 0; i--)
                {
                wrap.listeners.get(i).onInitiatingAsstesList(this, args);
                }
            DownloadAssetsAsync asyncDownload = new DownloadAssetsAsync();
            asyncDownload.execute(this);
            }
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void assetsToDownload(int count)
        {
        //Empezará a descarga en automático
        MasterKiwiArgs args = new MasterKiwiArgs(count, count);
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onInitiatingDownload(this, args);
            }
        wrap.showDebug(String.valueOf(count));
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void assetDownloaded(int remaining)
        {
        MasterKiwiArgs args = new MasterKiwiArgs(remaining, -1);
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onAssetsDownload(this, args);
            }
        wrap.showDebug("Faltan por descargar" + remaining);
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void allAssetsDownloaded()
        {
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        wrap.showDebug("Termino la descarga");
        MasterKiwiArgs args = new MasterKiwiArgs();
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onDownloadFinished(this, args);
            }
        if(!wrap.isAppOnBackground)
            {

            wrap.runGame(currentGame);
            }
        else
            {
            wrap.currentActivity.finish();
            }
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    public String readGameDataJSON()
        {
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        String result = "";
        BufferedReader reader = null;
        try
            {
            reader = new BufferedReader(new InputStreamReader(wrap.currentActivity.getAssets().open("interface/sqripts")));
            String mLine;
            StringBuilder strBuilder = new StringBuilder(131072);
            while((mLine = reader.readLine()) != null)
                {
                strBuilder.append(mLine);
                }
            result = strBuilder.toString();
            }
        catch(IOException e)
            {
            wrap.showDebug(e.toString());
            }
        finally
            {
            if(reader != null)
                {
                try
                    {
                    reader.close();
                    }
                catch(IOException e)
                    {
                    wrap.showDebug(e.toString());
                    }
                }
            }
        return result;
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void startingGame()
        {
        MasterKiwiArgs args = new MasterKiwiArgs();
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onSatrtingGame(this, args);
            }
        }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void gameOver()
        {
            LogManager.d("MasterKiwisito", "Over");

        MasterKiwiArgs args = new MasterKiwiArgs();
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onGameOver(this, args);
            }
        Intent intent = new Intent();
        wrap.currentActivity.setResult(Activity.RESULT_OK, intent);
        //SDLActivity.mSingleton.finish();
            //
            Context mContext = AppTalentoRHApplication.getApplication();
            Intent casa = new Intent(mContext, HomeActivity.class);
            casa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppTalentoRHApplication.getApplication().startActivity(casa);
            //currentActivity.finish();
        }

    /**
     * <p>Método mandado llamar desde el </p>
     * */
    private void mandarDatos(int usado, int posJ1, int posJ2, int mapa, int turno){
        LogManager.d("Gusanos", "En el Wrapper: mandarDatos() " + mData);
        if(mData != null){
            if(mData.getIdEmpleadoRetado() == 1){
                LogManager.d("Gusanos", "En el Wrapper: mandarDatos() if");
                mPresenter.onMandaDatos(usado, posJ1, posJ2, mapa, turno,  SharedPreferencesUtil.getInstance().getTokenUsuario(), mData.getIdPartida());
            }
            else{
                LogManager.d("Gusanos", "En el Wrapper: mandarDatos() else");
                mPresenter.onMandaDatos(usado, posJ2, posJ1, mapa, turno,  SharedPreferencesUtil.getInstance().getTokenUsuario(), mData.getIdPartida());
            }
        }
    }
    /**
     * Funcion mandada a llamar desde el engine*
     */
    private void exitGame()
        {
            LogManager.d("MasterKiwisito", "exit");
        /*MasterKiwiArgs args = new MasterKiwiArgs();
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onExitGame(this, args);
            }
        //Intent intent = new Intent();
        //wrap.currentActivity.setResult(Activity.RESULT_OK, intent);*/
       // SDLActivity.mSingleton.finish();
            currentActivity.finish();
            LogManager.d("MasterKiwisito", "exit3");
        }
    /**
     * Funcion mandada a llamar desde el engine
     */
    private void onNotLoggedIn()
        {
        MasterKiwiArgs args = new MasterKiwiArgs();
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onNoneUserLogged(this, args);
            }
        }
    /**
     * Funcion mandada a llamar desde el engine
     */
    private void onNotGameData()
        {
            LogManager.d("MasterKiwisito", "No game data");
        MasterKiwiArgs args = new MasterKiwiArgs();
        MasterKiwiWrapper wrap = MasterKiwiWrapper.GetInstance();
        for(int i = wrap.listeners.size()-1; i >= 0; i--)
            {
            wrap.listeners.get(i).onNoGameData(this, args);
            }
        }
    /**
     * <p>Función que asigna los datos del juego que se cargará.</p>
     *
     * @param gameType Tipo del juego que se cargará.
     * @param nombre Nombre del juego que se cargará.
     */
    public void setGameType(int gameType, String nombre)
        {
        this.gameType = gameType;
        this.gameName = nombre;
        }

    @Override
    public void onDatosGEenviados() {
        LogManager.d("Gusanos", "onDatosGEenviados");
    }

    @Override
    public void onMuestraMensaje(int error) {
        LogManager.d("Gusanos", "onMuestraMensaje " + error);
    }

    @Override
    public void onMuestraMensaje(int error, int codigo) {
        LogManager.d("Gusanos", "onMuestraMensaje " + error + " - " + codigo);
    }

    @Override
    public void onNoAuth() {
        LogManager.d("Gusanos", "onNoAuth");
        Intent listado = new Intent(currentActivity.getApplicationContext(), DetalleProgramaActivity.class);
        listado.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        currentActivity.startActivity(listado);
    }

    @Override
    public void onCargaLoader() {
        LogManager.d("Gusanos", "onCargaLoader");
    }

    @Override
    public void onOcultaLoader() {
        LogManager.d("Gusanos", "onOcultaLoader");
    }

}

/**
 * <p>Clase que ayuda a MasterKiwi a mover los archivos descargados.</p>
 */
class MoveFileHelper
{
    /**
     * <p>Ruta final dentro de MasterKiwi a donde se movera el archivo.</p>
     */
    public String assetsPath;
    /**
     * <p>Ruta en la que se descargará el archivo de manera temporal.</p>
     */
    public String nameToStorage;
    /**
     * <p>Ruta final dentro del dispositivo a donde se moverá el archivo.</p>
     */
    public String newName;
    /**
     * <p>Constructor del objeto que almacen los movimientos del archivo.</p>
     *
     * @param assetName Nombre del archivo.
     * @param source Ruta en donde se descargará el archivo
     * @param target Ruta a la que se moverá el archivo.
     */
    public MoveFileHelper(String assetName, String source, String target)
        {
        assetsPath = "interface/data/" + assetName;
        nameToStorage = source + "/" + assetName;
        newName = target + "/" + assetsPath;
        }
    }

/**
 * <p>Clase que permite mover los archivos de manera asincrona.</p>
 */
class moveFileAsync extends AsyncTask<MasterKiwiWrapper, Void, MasterKiwiWrapper>
    {
    /**
     * <p>Función que se ejecuta en otro hilo.</p>
     *
     * @param wrappers El objeto que sirve para comunicarse con MasterKiwi
     * @return objeto con el que se comunico con MasterKiwi.
     */
    @Override
    protected MasterKiwiWrapper doInBackground(MasterKiwiWrapper... wrappers)
        {
        for(int i = 0; i < wrappers[0].helpers.size(); i++)
            {
            String from = wrappers[0].helpers.get(i).nameToStorage;
            File f = new File(from);
            try
                {
                InputStream is = wrappers[0].currentActivity.getAssets().open(wrappers[0].helpers.get(i).assetsPath);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(buffer);
                fos.close();
                }
            catch(Exception e)
                {
                throw new RuntimeException(e);
                }
            wrappers[0].moveFile(from, wrappers[0].helpers.get(i).newName);
            }
        return wrappers[0];
        }

    /**
     * <p>Función que se manda a llamar tras haber acabado el proceso asincrono</p>
     *
     * @param wrapper El objeto con el que se comunica con MasterKiwi.
     */
    @Override
    protected void onPostExecute(MasterKiwiWrapper wrapper)
        {
        wrapper.startGame(wrapper.currentGame, false);
        }
    }

/**
 * <p>Clase que permite hacer login de manera asincrona.</p>
 */
class LoginAsync extends AsyncTask<MasterKiwiWrapper, Void, Void>
    {
    /**
     * <p>Función que se ejecuta en otro hilo.</p>
     *
     * @param wrapper El objeto que sirve para comunicarse con MasterKiwi
     */
    @Override
    protected Void doInBackground(MasterKiwiWrapper... wrapper)
        {
            //Log.d("MKWrapper", wrapper[0].currentUser + "");
            wrapper[0].initializeWrapper();
            wrapper[0].networkManager.wrapperLogin(wrapper[0].currentUser, wrapper[0].currentPassword,wrapper[0].useLocalData);
            return null;
        }
    }

/**
 * <p>Clase que permite comprimir los atlas de manera asincrona.</p>
 */
class CompressAtlasAsync extends AsyncTask<MasterKiwiWrapper, Void, Void>
    {
    /**
     * <p>Función que se ejecuta en otro hilo.</p>
     *
     * @param wrapper El objeto que sirve para comunicarse con MasterKiwi
     */
    @Override
    protected Void doInBackground(MasterKiwiWrapper... wrapper)
        {
        wrapper[0].initializeWrapper();
        wrapper[0].networkManager.wrapperCompressAtlas(wrapper[0].currentGame);
        return null;
        }
    }

/**
 * <p>Clase que permite hacer un listado de los assets de manera asincrona.</p>
 */
class PrepareTexturesAsync extends AsyncTask<MasterKiwiWrapper, Void, Void>
    {
    /**
     * <p>Función que se ejecuta en otro hilo.</p>
     *
     * @param wrapper El objeto que sirve para comunicarse con MasterKiwi
     */
    @Override
    protected Void doInBackground(MasterKiwiWrapper... wrapper)
        {
        wrapper[0].networkManager.wrapperTexturesToRTTTEXT();
        return null;
        }
    }

/**
 * <p>Clase que permite descargar los assets de manera asincrona.</p>
 */
class DownloadAssetsAsync extends AsyncTask<MasterKiwiWrapper, Void, Void>
    {
    /**
     * <p>Función que se ejecuta en otro hilo.</p>
     *
     * @param wrapper El objeto que sirve para comunicarse con MasterKiwi
     */
    @Override
    protected Void doInBackground(MasterKiwiWrapper... wrapper)
        {
        wrapper[0].networkManager.wrapperDonwloadAssets();
        return null;
        }
    }