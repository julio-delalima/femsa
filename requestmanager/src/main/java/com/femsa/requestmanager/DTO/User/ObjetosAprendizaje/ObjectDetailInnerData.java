package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje;

import com.femsa.requestmanager.Utilities.RemoveDuplicates;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ObjectDetailInnerData implements Serializable {

    private int mIdProgram;
    private int mIdObject;
    private String mObjectName;
    private String mImageObject;
    private String mType;
    private String mContent;
    private String mImageDetail;
    private String mDetailContent;
    private String mDetailName;
    private String mEstimatedTime;
    private int mModuleID;
    private int mLanguageID;
    private int mBonus;
    private int mLike;
    private String mFilename;
    private String mFiletype;
    private String mModule;
    private Boolean mDescargas;
    private boolean mSeriado;
    private int version;
    private boolean mObligatorio;
    private boolean mStatusSeriado;
    private boolean mStatusBonus;
    private String mURLEnlace;

    private int mGEUsado;
    private int mGEPosJ1;
    private int mGEPosJ2;
    private int mGEMapa;
    private int mGETurno;
    private int mGEIDPartida;
    private int mUltimoTiro;
    private boolean mRetador;
    private boolean mOponente;
    private String mNombreRetador;
    private int mIdRetador;

    private static final String ID_PROG_KEY =  "idPrograma";
    private static final String ID_OBJECT_KEY = "idObjeto";
    private static final String OBJECT_NAME_KEY = "nombreObjeto";
    private static final String IMAGE_KEY =  "imagenObjeto";
    private static final String TYPE_KEY =  "tipoObjeto";
    private static final String CONTENT_KEY =  "contenidoP";
    private static final String DETAIL_IMAGE_KEY = "imagenDetalle";
    private static final String DETAIL_NAME_KEY = "nombreDetalle";
    private static final String CONTENT_DETAIL_KEY = "contenidoDetalle";
    private static final String TIME_KEY = "tiempoEstimado";
    private static final String MODULE_ID_KEY = "idModulo";
    private static final String ID_LANGUAGE_KEY = "idIdioma";
    private static final String BONUS_KEY = "bonus";
    private static final String LIKE_KEY = "like";
    private static final String FILENAME_KEY = "nombreArchivo";
    private static final String FILETYPE_KEY = "tipoArchivo";
    private static final String MODULE_NAME_KEY = "nombreModulo";
    private static final String DESCARGAS_KEY = "descargas";
    private static final String OBLIGATORIO_KEY = "objetoObligatorio";
    private static final String SERIADO_KEY = "objetoSeriado";
    private static final String STATUS_OBJETO_KEY = "statusObjeto";
    private static final String STATUS_BONUS_OBJETO = "bonusStatus";
    private static final String VERSION_JUEGO_KEY = "version";

    private static final String GE_USADO_KEY = "usado";
    private static final String GE_POSJ1_KEY = "posJ1";
    private static final String GE_POSJ2_KEY = "posJ2";
    private static final String GE_MAPA_KEY = "mapa";
    private static final String GE_TURNO_KEY = "turno";
    private static final String GE_ID_PARTIDA_KEY = "idPartida";
    private static final String GE_ULTIMO_TIRO_KEY = "ultimoTiro";
    private static final String GE_RETADOR_KEY = "retador";
    private static final String GE_CONTRINCANTE_KEY = "oponente";
    private static final String GE_RETADOR_NOMBRE_KEY = "nombreOponente";
    private static final String GE_ID_RETADOR_KEY = "";
    private static final String URL_ENLACE_KEY = "url";


    private static final String ARRAY_KEY = "detalleObjetoAprendizaje";

    private ArrayList<ObjectData> mAllObjects;

    private ArrayList<String> mAllTitlesRepeated, mAllTitles;

    public ObjectDetailInnerData(JSONObject array)
    {
        JSONArray arrayData = array.optJSONArray(ARRAY_KEY);
        mAllObjects = new ArrayList<>();
        mAllTitlesRepeated = new ArrayList<>();
        mAllTitles = new ArrayList<>();
        for(int i = 0; i < arrayData.length(); i++)
            {
                JSONObject data = arrayData.optJSONObject(i);
                mIdProgram = data.optInt(ID_PROG_KEY);
                mIdObject = data.optInt(ID_OBJECT_KEY);
                mObjectName = data.optString(OBJECT_NAME_KEY);
                mImageObject = data.optString(IMAGE_KEY);
                mType = data.optString(TYPE_KEY);
                mContent = data.optString(CONTENT_KEY);
                mImageDetail = data.optString(DETAIL_IMAGE_KEY);
                mDetailContent = data.optString(CONTENT_DETAIL_KEY);
                mDetailName = data.optString(DETAIL_NAME_KEY);
                mEstimatedTime = data.optString(TIME_KEY);
                mModuleID = data.optInt(MODULE_ID_KEY);
                mLanguageID = data.optInt(ID_LANGUAGE_KEY);
                mBonus = data.optInt(BONUS_KEY);
                mLike = data.optInt(LIKE_KEY);
                mFilename = data.optString(FILENAME_KEY);
                mFiletype = data.optString(FILETYPE_KEY);
                mModule = data.optString(MODULE_NAME_KEY);
                mSeriado = data.optBoolean(SERIADO_KEY);
                mObligatorio = data.optBoolean(OBLIGATORIO_KEY);
                mStatusSeriado = data.optBoolean(STATUS_OBJETO_KEY);
                mDescargas = data.optBoolean(DESCARGAS_KEY);
                mStatusBonus = data.optBoolean(STATUS_BONUS_OBJETO);
                mAllObjects.add(new ObjectData(mIdProgram, mIdObject, mObjectName, mImageObject, mType, mContent,
                        mImageDetail, mDetailContent, mDetailName, mEstimatedTime, mModuleID, mLanguageID, mBonus,
                        mLike, mFilename, mFiletype, mModule, mDescargas, mSeriado, mObligatorio, mStatusSeriado, mStatusBonus));
                mAllTitlesRepeated.add(mModule);
                version = data.optInt(VERSION_JUEGO_KEY);
                mGEIDPartida = data.optInt(GE_ID_PARTIDA_KEY);

                //GusanosyEscaleras
                mGEPosJ1 = data.optInt(GE_POSJ1_KEY);
                mGEPosJ2 = data.optInt(GE_POSJ2_KEY);
                mGETurno = data.optInt(GE_TURNO_KEY);
                mGEMapa = data.optInt(GE_MAPA_KEY);
                mGEUsado = data.optInt(GE_USADO_KEY);
                mUltimoTiro = data.optInt(GE_ULTIMO_TIRO_KEY);
                mRetador = data.optBoolean(GE_RETADOR_KEY);
                mOponente = data.optBoolean(GE_CONTRINCANTE_KEY);
                mNombreRetador = data.optString(GE_RETADOR_NOMBRE_KEY);
                mIdRetador = data.optInt(GE_ID_RETADOR_KEY);
                mURLEnlace = data.optString(URL_ENLACE_KEY);
            }
        mAllTitles = RemoveDuplicates.removeDuplicates(mAllTitlesRepeated);
    }



    public int getmIdProgram() {
        return mIdProgram;
    }

    public int getmIdObject() {
        return mIdObject;
    }

    public String getmObjectName() {
        return mObjectName;
    }

    public String getmImageObject() {
        return mImageObject;
    }

    public String getmType() {
        return mType;
    }

    public String getmContent() {
        return mContent;
    }

    public String getmImageDetail() {
        return mImageDetail;
    }

    public String getmDetailContent() {
        return mDetailContent;
    }

    public String getmDetailName() {
        return mDetailName;
    }

    public String getmEstimatedTime() {
        return mEstimatedTime;
    }

    public int getmModuleID() {
        return mModuleID;
    }

    public int getmLanguageID() {
        return mLanguageID;
    }

    public int getmBonus() {
        return mBonus;
    }

    public int getmLike() {
        return mLike;
    }

    public ArrayList<ObjectData> getAllObjects() {
        return mAllObjects;
    }

    public ArrayList<String> getAllTitles() {
        return mAllTitles;
    }

    public String getFilename() {
        return mFilename;
    }

    public Boolean getDescargas() {
        return mDescargas;
    }

    public boolean isSeriado() {
        return mSeriado;
    }

    public boolean isObligatorio() {
        return mObligatorio;
    }

    public boolean isStatusSeriado() {
        return mStatusSeriado;
    }

    public boolean ismStatusBonus() {return  mStatusBonus;}

    public int getVersion() {
        return version;
    }

    public int getGEUsado() {
        return mGEUsado;
    }

    public int getGEPosJ1() {
        return mGEPosJ1;
    }

    public int getGEPosJ2() {
        return mGEPosJ2;
    }

    public int getGEMapa() {
        return mGEMapa;
    }

    public int getGETurno() {
        return mGETurno;
    }

    public int getGEIDPartida() {
        return mGEIDPartida;
    }

    public int getUltimoTiro() {
        return mUltimoTiro;
    }

    public boolean isRetador() {
        return mRetador;
    }

    public boolean isOponente() {
        return mOponente;
    }

    public String getNombreRetador() {
        return mNombreRetador;
    }

    public int getIdRetador() {
        return mIdRetador;
    }

    public String getmURLEnlace() {
        return mURLEnlace;
    }
}
