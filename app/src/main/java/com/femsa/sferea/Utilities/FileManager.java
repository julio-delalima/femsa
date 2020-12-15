package com.femsa.sferea.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.GameLoadingActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador.DataGusanosEscaleras;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment.IS_MULTIPLAYER_GAME;
import static com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment.MULTIPLAYER_GAME_DATA;
import static com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment.SENDING_GAME_ID;
import static com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment.SENDING_GAME_IMAGE;
import static com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment.SENDING_GAME_NAME;
import static com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment.SENDING_GAME_TYPE;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.GameLoadingActivity.RECEIVING_GAME_PATH;

public class FileManager {

    private Activity mCallingActivity;

    private Context mContext;

    private boolean isOffline = false;

    public static final String RUTA_COMPLETA_KEY = AppTalentoRHApplication.getApplication().getFilesDir().getAbsolutePath() + "/Android/data/com.femsa.sferea/files/Download/";

    public static final String VERSION = "version/";

    public FileManager(Context context) {
        mContext = context;
    }

    public interface onDescargaJuego{
        void onJuegoDescargadoOffline(String rutajuego);
    }

    /**
     * Este método es como los métodos de la función Miguel, no hacen nada.
     * */
    public static boolean isSDCardPresent(Context context) {
            /*File[] storages = ContextCompat.getExternalFilesDirs(context, null);
            if (storages.length > 1 && storages[0] != null && storages[1] != null)
                return true;
            else
                return false;
*/
            return false;
        }

    /**
     * Método estático que se encarga de extraer un archivo en una ruta específica.
     * @param zipFile el archivo .zip que se va a extraer.
     * @param targetDirectory la ruta o directorio donde se extraerá el archivo .zip.
     * */
    public static void unzip(File zipFile, File targetDirectory,
                             int idObjeto, boolean offline, onDescargaJuego listener,
                             int version, DataGusanosEscaleras data) throws IOException
    {
        clearInterface(targetDirectory.getAbsolutePath());

        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null)
                {
                    File file = new File(targetDirectory + "/" + idObjeto, ze.getName());
                    File dir = ze.isDirectory() ? file : file.getParentFile();
                    if (!dir.isDirectory() && !dir.mkdirs())
                        {
                            throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                        }
                    if (ze.isDirectory())
                        {
                            continue;
                        }
                    try (FileOutputStream fout = new FileOutputStream(file))
                        {
                            while ((count = zis.read(buffer)) != -1)
                                fout.write(buffer, 0, count);
                        }
                            /* if time should be restored as well
                            long time = ze.getTime();
                            if (time > 0)
                                file.setLastModified(time);
                            */
                        File versionFolder = new File(targetDirectory + "/" + idObjeto + "/" + VERSION);
                        versionFolder.mkdirs();
                        File versionFile = new File(targetDirectory + "/" + idObjeto + "/" + VERSION + version + ".txt");
                        versionFile.createNewFile();
                }
        }
        if(offline){
            listener.onJuegoDescargadoOffline(RUTA_COMPLETA_KEY + idObjeto);
        }
        else if(data != null && data.getIdEmpleadoRetado() > 0){
            iniciarJuegoYaDescargadoMultiJugador(idObjeto, data);
        }
        else{
            iniciarJuegoYaDescargado(idObjeto);
        }

    }

    public static void iniciarJuegoYaDescargado(int idJuego){
        clearInterface(RUTA_COMPLETA_KEY);
            File data = new File(RUTA_COMPLETA_KEY + idJuego + "/interface/data");
            File g = new File(RUTA_COMPLETA_KEY + idJuego + "/interface/g");
            if(data.exists() && data.isDirectory()){
                File[] files = data.listFiles();
                for (File file : files) {
                    try
                        {
                            copy(file, new File(RUTA_COMPLETA_KEY + "/interface/data/" + file.getName()));
                        }
                    catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                }
            }

            if(g.exists() && g.isDirectory()){
                File[] files = g.listFiles();
                for (File file : files) {
                    try
                    {
                        copy(file, new File(RUTA_COMPLETA_KEY + "/interface/g/" + file.getName()));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        iniciarJuego(new File(RUTA_COMPLETA_KEY));
    }

    public static void iniciarJuegoYaDescargadoMultiJugador(int idJuego, DataGusanosEscaleras mDataJuego){
        clearInterface(RUTA_COMPLETA_KEY);
        File data = new File(RUTA_COMPLETA_KEY + idJuego + "/interface/data");
        File g = new File(RUTA_COMPLETA_KEY + idJuego + "/interface/g");
        if(data.exists() && data.isDirectory()){
            File[] files = data.listFiles();
            for (File file : files) {
                try
                {
                    copy(file, new File(RUTA_COMPLETA_KEY + "/interface/data/" + file.getName()));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        if(g.exists() && g.isDirectory()){
            File[] files = g.listFiles();
            for (File file : files) {
                try
                {
                    copy(file, new File(RUTA_COMPLETA_KEY + "/interface/g/" + file.getName()));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        iniciarJuegoMultijugador(new File(RUTA_COMPLETA_KEY), mDataJuego);
    }

    public static void copy(File src, File dst) throws IOException {
        createDestino();
        if(!dst.exists()){
            boolean nuevo = dst.createNewFile();
            LogManager.d("Descargando", "Creando " + dst.getName() + " dio " + nuevo);
        }
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            catch (Exception ex){
                LogManager.d("Descargando", ex.getMessage());
            }
        }
        catch (Exception ex){
            LogManager.d("Descargando", ex.getMessage());
        }
    }

    /**
     * Método estático que inicia los activity para cargar el juego ya descomprimido.
     * @param targetDirectory la ruta donde fue extraído el juego.
     * */
    private static void iniciarJuego(File targetDirectory){
        Intent sendt = new Intent(AppTalentoRHApplication.getApplication(), GameLoadingActivity.class);
        sendt.putExtra(SENDING_GAME_ID, getGameID(targetDirectory + "/interface/data"));
        sendt.putExtra(SENDING_GAME_TYPE, getGameType(targetDirectory + "/interface/g"));
        File image = new File("/", String.format("%s.mktx", "Kiwi"));
        sendt.putExtra(SENDING_GAME_IMAGE,image.getAbsolutePath());
        sendt.putExtra(SENDING_GAME_NAME,"Asteroides");
        sendt.putExtra(RECEIVING_GAME_PATH, targetDirectory.getAbsolutePath());
        sendt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        sendt.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            AppTalentoRHApplication.getApplication().startActivity(sendt);
    }

    /**
     * Método estático que inicia los activity para cargar el juego ya descomprimido.
     * @param targetDirectory la ruta donde fue extraído el juego.
     * */
    private static void iniciarJuegoMultijugador(File targetDirectory, DataGusanosEscaleras dataJuego){
        Intent sendt = new Intent(AppTalentoRHApplication.getApplication(), GameLoadingActivity.class);
        sendt.putExtra(SENDING_GAME_ID, getGameID(targetDirectory + "/interface/data"));
        sendt.putExtra(SENDING_GAME_TYPE, getGameType(targetDirectory + "/interface/g"));
        File image = new File("/", String.format("%s.mktx", "Kiwi"));
        sendt.putExtra(SENDING_GAME_IMAGE,image.getAbsolutePath());
        sendt.putExtra(SENDING_GAME_NAME,"Asteroides");
        sendt.putExtra(IS_MULTIPLAYER_GAME, true);
        sendt.putExtra(MULTIPLAYER_GAME_DATA, dataJuego);
        sendt.putExtra(RECEIVING_GAME_PATH, targetDirectory.getAbsolutePath());
        sendt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        sendt.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        AppTalentoRHApplication.getApplication().startActivity(sendt);
    }

    /**
     * Método estático para borrar los archivos cada que se descomprima un juego nuevo.
     * */
    private static void clearInterface(String path){
        if(new File(path+"/interface").exists())
            {
                File data = new File(path + "/interface/data");
                File g = new File(path + "interface/g");
                if(data.exists() && data.isDirectory()){
                    File[] files = data.listFiles();
                    for (File file : files) {
                        file.delete();
                    }
                }
                if(g.exists() && g.isDirectory()){
                    File[] files = g.listFiles();
                    for (File file : files) {
                        file.delete();
                    }
                }
            }
    }



    public static boolean directoryExists(int idObjeto){
        File tempFile = new File(RUTA_COMPLETA_KEY + idObjeto);
        return tempFile.exists() && tempFile.isDirectory();
    }

    public static boolean isCurrentVersion(int idObjeto, int version){
        File versionFile = new File(RUTA_COMPLETA_KEY + idObjeto + "/" + VERSION + version + ".txt");
        return versionFile.exists();
    }

    private static void moveFiles(String path, String outputDirectory){
        InputStream in = null;
        OutputStream out = null;

        File dir = new File (outputDirectory);
        if (!dir.exists())
        {
            dir.mkdirs();
        }

        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            String tempName = files[i].getName();
            try
            {
                in = new FileInputStream(path + tempName);
                out = new FileOutputStream(outputDirectory + path);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1)
                    {
                        out.write(buffer, 0, read);
                    }
                in.close();
                // write the output file
                out.flush();
                out.close();
            }
            catch(Exception ex){

            }

        }
    }


    /**
     * Función estática que retorna el ID del juego, el cual es el nombre de archivo que se encuentra en
     * /interface/data/ID.txt
     * @param path ruta donde está el archivo con el ID.
     * @return el ID del juego en formato int.
     * */
    private static int getGameID(String path){
        File directory = new File(path);
        File[] files = directory.listFiles();
        if(files != null){
            for (File file : files) {
                String tempName = file.getName();
                if (tempName.length() == 8 && tempName.contains(".txt")) {
                    return Integer.parseInt(tempName.replace(".txt", ""));
                }
            }
        }
        return 0;
    }

    private static void createDestino(){
        File g = new File(RUTA_COMPLETA_KEY + "/interface/g");
        File data = new File(RUTA_COMPLETA_KEY + "/interface/data");
        if(!g.exists()){
            boolean createFile = g.mkdirs();
            LogManager.d("Descargando",  createFile+ "g creado" );
        }
        if(!data.exists()){
            boolean createFile = data.mkdirs();
            LogManager.d("Descargando",  createFile+ "data creado" );
        }
    }

    /**
     * Función estática que retorna el typeID del juego, el cual es el nombre de archivo que se encuentra en
     * /interface/g/ID.txt, dicho archivo también es el Script del juego.
     * @param path ruta donde está el archivo con el typeID.
     * @return el typeID del juego en formato int.
     * */
    private static int getGameType(String path){ //script
        File directory = new File(path);
        File[] files = directory.listFiles();
        if(files != null){
            for (File file : files) {
                String tempName = file.getName();
                if (tempName.length() == 8 && tempName.contains(".txt")) {
                    return Integer.parseInt(tempName.replace(".txt", ""));
                }
            }
        }

        return 0;
    }

}

