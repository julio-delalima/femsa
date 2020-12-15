package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje;

import com.femsa.requestmanager.Utilities.RemoveDuplicates;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ObjectInnerData {

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
    private int mIDModulo;
    private int mModuloPosicion;
    private String mFilename;
    private String mFiletype;
    private String mModule;
    private boolean mDescargas;
    private boolean mSeriado;
    private boolean mObligatorio;
    private boolean mStatusSeriado;
    private boolean mStatusBonus;

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
    private static final String ARRAY_KEY = "objetosAprendizaje";
    private static final String LISTA_OBJETO_KEY = "listaObjetos";
    private static final String ID_MODULO_KEY = "idModulo";
    private static final String MODULO_POSICION_KEY = "posicion";
    private static final String OBLIGATORIO_KEY = "objetoObligatorio";
    private static final String SERIADO_KEY = "objetoSeriado";
    private static final String STATUS_OBJETO_KEY = "statusObjeto";
    private static final String STATUS_BONUS_OBJETO = "bonusStatus";

    private ArrayList<ObjectData> mAllObjects;

    private ArrayList<String> mAllTitlesRepeated, mAllTitles;

    public ObjectInnerData(JSONObject array)
    {
        JSONArray arrayData = array.optJSONArray(ARRAY_KEY);
        mAllObjects = new ArrayList<>();
        mAllTitlesRepeated = new ArrayList<>();
        mAllTitles = new ArrayList<>();
        for(int i = 0; i < arrayData.length(); i++)
            {
                JSONObject modulos = arrayData.optJSONObject(i);
                mModule = modulos.optString(MODULE_NAME_KEY);
                mIDModulo = modulos.optInt(MODULE_ID_KEY);
                mModuloPosicion = modulos.optInt(MODULO_POSICION_KEY);
                JSONArray listaObjetos = modulos.optJSONArray(LISTA_OBJETO_KEY);
                if(listaObjetos != null)
                    {
                        for(int j = 0; j < listaObjetos.length(); j++)
                            {
                            JSONObject data = listaObjetos.optJSONObject(j);
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
                            mSeriado = data.optBoolean(SERIADO_KEY);
                            mObligatorio = data.optBoolean(OBLIGATORIO_KEY);
                            mStatusSeriado = data.optBoolean(STATUS_OBJETO_KEY);
                            mDescargas = data.optBoolean(DESCARGAS_KEY);
                            mStatusBonus = data.optBoolean(STATUS_BONUS_OBJETO);
                            mAllObjects.add(new ObjectData(mIdProgram, mIdObject, mObjectName, mImageObject, mType, mContent,
                                    mImageDetail, mDetailContent, mDetailName, mEstimatedTime, mModuleID, mLanguageID, mBonus,
                                    mLike, mFilename, mFiletype, mModule, mDescargas, mSeriado, mObligatorio, mStatusSeriado, mStatusBonus));
                            mAllTitlesRepeated.add(mModule);
                        }
                    }
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

}
