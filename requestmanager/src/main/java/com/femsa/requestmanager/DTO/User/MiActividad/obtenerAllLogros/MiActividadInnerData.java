package com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros;

import com.femsa.requestmanager.DTO.User.Ranking.ProgramRankingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MiActividadInnerData {

    /**
     * Llaves para la parte de los marcadores, donde kof trae el progreso actual  kofsiguientenivel indica el total a alcanzar
     * kof / kofsiguientenivel
     * */
    private static final String KOF_SIGUIENTE_NIVEL_KEY = "kofSiguienteNivel";
    private static final String KOF_KEY = "kof";

    /**
     * Llaves para la parte de los logros
     * */
    private static final String LISTA_LOGROS_KEY = "listaLogros";
    private static final String MI_LOGRO_ID = "idLogro";
    private static final String MI_LOGRO_NOMBRE = "nombreLogro";
    private static final String MI_LOGRO_IMAGEN_NOMBRE = "nombreImgLogro";
    private static final String MI_LOGRO_MAIN_KEY = "MiLogro";

    /**
     * lLAVES PARA LOS DATOS DE PROGRAMAS
     * */
    private static final String MAIN_RANKING_KEY = "ranking";
    private static final String COMPLETED_PROGRAMS_KEY = "prograCompletados";
    private static final String INCOMPLETE_PROGRAMS_KEY = "prograIncompletos";
    private static final String CORCHOLATAS_KEY = "corcholatas";

    /**
     * Llaves para el historial y programas activos
     * */
    private static final String RANKING_ACTIVE_MAIN_KEY =  "rankingProgramasActivo";
    private static final String RANKING_HISTORY_MAIN_KEY = "rankingProgramasHistorial";
    private static final String PROGRAM_NAME_KEY = "nombrePrograma";
    private static final String PROGRAM_BONUS_KEY = "bonusPorProgra";
    private static final String ID_PROGRAM_KEY = "idPrograma";
    private static final String ID_OBJECT_KEY = "idObj";
    private static final String POSITION_KEY = "posicion";
    private static final String NUM_OPPONENTS_KEY = "numOponentes";
    private static final String PROGRAM_IMG_KEY = "imgPrograma";
    private static final String GROUP_ID_KEY = "idGrupo";
    private static final String TOTAL_BONUS_KEY = "bonusTotalPorProgra";

    /**
     * Variables que contienen los valores actual y maximo de los marcadores
     * */
    private MarcadoresData mMarcadorMaximo;
    private MarcadoresData mMarcadorActual;

    /**
     * Variable para retornar el listado de logros
     * */
    private ArrayList<LogroDTO>  mListadoLogros;
    private int miLogroID;
    private String nombreMiLogro;
    private String nombreImagenMiLogro;


    /**
     * Variables para los datos de los programas
     * */
    private int mCompletedPrograms;
    private int mIncompletePrograms;
    private int mCorcholatas;

    /**
     * Variables para el historial de programas y los programas activos
     * */
    private ArrayList<ProgramRankingData> mActivePrograms;
    private ArrayList<ProgramRankingData> mHistoryPrograms;


    public MiActividadInnerData(JSONObject data) throws JSONException {
        getProgramasData(data);
        getLogrosData(data);
        getMarcadoresData(data);
        getHistorialYProgramasActivos(data);
        getMiLogroData(data);
    }

    private void getProgramasData(JSONObject data)
    {
        JSONObject subdata = data.optJSONObject(MAIN_RANKING_KEY);
        mCompletedPrograms = subdata.optInt(COMPLETED_PROGRAMS_KEY);
        mIncompletePrograms = subdata.optInt(INCOMPLETE_PROGRAMS_KEY);
        mCorcholatas = subdata.optInt(CORCHOLATAS_KEY);
    }

    /**
     * Ciclo para iterar entre todos los logros y almacenarlos en el Arraylist
     * */
    private void getLogrosData(JSONObject data)
    {
        if (data!=null){
            mListadoLogros = new ArrayList<>();
            obtenerListadoLogros(data.optJSONArray(LISTA_LOGROS_KEY));
        }
    }

    /**
     * <p>Método que permite agregar al ArrayList la lista de logros.</p>
     * @param array el arreglo de logros contenido en el JSONObject 'listaLogros'
     */
    private void obtenerListadoLogros(JSONArray array){
        for (int i=0; i<array.length(); i++){
            mListadoLogros.add(new LogroDTO(array.optJSONObject(i)));
        }
    }

    /**
     * Metodo para meter toda la información de los marcadores
     * @param data el JSONObject 'data'
     * */
    private void getMarcadoresData(JSONObject data)
    {
        mMarcadorActual = new MarcadoresData(data.optJSONObject(KOF_KEY));
        mMarcadorMaximo = new MarcadoresData(data.optJSONObject(KOF_SIGUIENTE_NIVEL_KEY));
    }


    /**
     * Método para llenar los ArrayList de programas activos e historial
     * */
    private void getHistorialYProgramasActivos(JSONObject data) throws JSONException {
        mActivePrograms = new ArrayList<>();
        mHistoryPrograms = new ArrayList<>();
        JSONArray active = data.optJSONArray(RANKING_ACTIVE_MAIN_KEY);
        mActivePrograms.addAll(parseData(active));
        JSONArray history = data.optJSONArray(RANKING_HISTORY_MAIN_KEY);
        mHistoryPrograms.addAll(parseData(history));
    }

    /**
     * Metodo para llenar el ArrayList que irá a programas activos e historial
     * @param main el JSONArray de 'rankingProgramasActivo' o 'rankingProgramasHistorial';
     * @return ArrayList<ProgramRankingData> arreglo con todos los elementos d eprogramas activos e historial
     * */
    private ArrayList<ProgramRankingData> parseData(JSONArray main) throws JSONException
    {
        ArrayList<ProgramRankingData> tempData = new ArrayList<>();
        if(main == null)
        {
            return new ArrayList<>();
        }
        for(int i = 0; i < main.length(); i++)
        {
            JSONObject tempJsonObj = main.getJSONObject(i);
            tempData.add(
                    new ProgramRankingData(
                            tempJsonObj.optString(PROGRAM_NAME_KEY),
                            tempJsonObj.optInt(PROGRAM_BONUS_KEY),
                            tempJsonObj.optInt(ID_PROGRAM_KEY),
                            tempJsonObj.optInt(ID_OBJECT_KEY),
                            tempJsonObj.optInt(POSITION_KEY),
                            tempJsonObj.optInt(NUM_OPPONENTS_KEY),
                            tempJsonObj.optString(PROGRAM_IMG_KEY),
                            tempJsonObj.optInt(GROUP_ID_KEY),
                            tempJsonObj.optInt(TOTAL_BONUS_KEY)
                    ));
        }

        return tempData;
    }

    /**
     * Método para llenar la información de Mi logro
     * */
    private void getMiLogroData(JSONObject data)
    {
        JSONObject miLogro = data.optJSONObject(MI_LOGRO_MAIN_KEY);
        miLogroID = miLogro.optInt(MI_LOGRO_ID);
        nombreMiLogro = miLogro.optString(MI_LOGRO_NOMBRE);
        nombreImagenMiLogro = miLogro.optString(MI_LOGRO_IMAGEN_NOMBRE);
    }

    public MarcadoresData getMarcadorMaximo() {
        return mMarcadorMaximo;
    }

    public MarcadoresData getMarcadorActual() {
        return mMarcadorActual;
    }

    public ArrayList<LogroDTO> getListadoLogros() {
        return mListadoLogros;
    }

    public int getMiLogroID() {
        return miLogroID;
    }

    public String getNombreMiLogro() {
        return nombreMiLogro;
    }

    public String getNombreImagenMiLogro() {
        return nombreImagenMiLogro;
    }

    public int getCompletedPrograms() {
        return mCompletedPrograms;
    }

    public int getIncompletePrograms() {
        return mIncompletePrograms;
    }

    public int getCorcholatas() {
        return mCorcholatas;
    }

    public ArrayList<ProgramRankingData> getActivePrograms() {
        return mActivePrograms;
    }

    public ArrayList<ProgramRankingData> getHistoryPrograms() {
        return mHistoryPrograms;
    }
}
