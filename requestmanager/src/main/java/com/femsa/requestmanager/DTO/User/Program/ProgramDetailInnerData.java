package com.femsa.requestmanager.DTO.User.Program;

import android.util.Log;

import org.json.JSONObject;

public class ProgramDetailInnerData {
    private static final String MAIN_DETAIL_KEY = "detallePrograma";
    private static final String ID_PROGRAM_TRANSLATION_KEY = "idProgramaTraducido";
    private static final String ID_PROGRAM_KEY = "idPrograma";
    private static final String BEGIN_DATE_KEY = "fechaInicio";
    private static final String END_DATE_KEY = "fechaFin";
    private static final String ID_CATEGORY_KEY = "idCategoria";
    private static final String OPERATIVE_EXCEL_KEY = "excelnciaOperativa";
    private static final String LIGHTBULB_KEY = "foco";
    private static final String DESITION_KEY = "decisores";
    private static final String MIND_KEY = "mentalidad";
    private static final String ACTIVE_KEY = "activo";
    private static final String ERASED_LEY = "borrado";
    private static final String CONTENT_KEY = "contenido";
    private static final String PROGRAM_IMAGE = "imagenPrograma";
    private static final String IMAGE_TITLE_KEY = "tituloImagen";
    private static final String LIKE_KEY = "like";
    private static final String PEOPLE_KEY = "gente";
    private static final String CORCHOLATAS_key = "corcholatas";
    private static final String TIEMPO_ESTIMADO_KEY = "tiempoEstimado";
    private static final String INSCRITO_KEY = "inscrito";
    private static final String PERMANENTE_KEY = "permanente";

    private int mIdProgramaTraducido;
    private int mIdPrograma;
    private long mFechaInicio;
    private long mFechaFin;
    private int mIdCategoria;
    private boolean mExcelnciaOperativa;
    private boolean mFoco;
    private boolean mDecisores;
    private boolean mMentalidad;
    private boolean mActivo;
    private boolean mBorrado;
    private String mContenido;
    private String mImagenPrograma;
    private String mTituloImagen;
    private int mLike;
    private boolean mPeople;
    private int mCorcholatas;
    private String mTiempoEstimado;
    private boolean mInscrito;
    private boolean mPermanente;

    public ProgramDetailInnerData(JSONObject data)
    {
        JSONObject main = data.optJSONObject(MAIN_DETAIL_KEY);
            mIdProgramaTraducido = main.optInt(ID_PROGRAM_TRANSLATION_KEY);
            mIdPrograma = main.optInt(ID_PROGRAM_KEY);
            mFechaInicio = main.optLong(BEGIN_DATE_KEY);
            mFechaFin = main.optLong(END_DATE_KEY);
            mIdCategoria = main.optInt(ID_CATEGORY_KEY);
            mExcelnciaOperativa = main.optBoolean(OPERATIVE_EXCEL_KEY);
            mFoco = main.optBoolean(LIGHTBULB_KEY);
            mDecisores = main.optBoolean(DESITION_KEY);
            mMentalidad = main.optBoolean(MIND_KEY);
            mActivo = main.optBoolean(ACTIVE_KEY);
            mBorrado = main.optBoolean(ERASED_LEY);
            mContenido = main.optString(CONTENT_KEY);
            mImagenPrograma = main.optString(PROGRAM_IMAGE);
            mTituloImagen = main.optString(IMAGE_TITLE_KEY);
            mLike = main.optInt(LIKE_KEY);
            mPeople = main.optBoolean(PEOPLE_KEY);
            mCorcholatas = main.optInt(CORCHOLATAS_key);
            mTiempoEstimado = main.optString(TIEMPO_ESTIMADO_KEY);
            mInscrito = main.optBoolean(INSCRITO_KEY);
            mPermanente = main.optString(PERMANENTE_KEY).equals("Permanente");
        //Log.d("Negrete", mPermanente + " - " + main.optString(PERMANENTE_KEY));
    }

    public boolean isPermanente() {
        return mPermanente;
    }

    public int getIdProgramaTraducido() {
        return mIdProgramaTraducido;
    }

    public int getIdPrograma() {
        return mIdPrograma;
    }

    public long getFechaInicio() {
        return mFechaInicio;
    }

    public long getFechaFin() {
        return mFechaFin;
    }

    public int getIdCategoria() {
        return mIdCategoria;
    }

    public boolean isExcelnciaOperativa() {
        return mExcelnciaOperativa;
    }

    public boolean isFoco() {
        return mFoco;
    }

    public boolean isDecisores() {
        return mDecisores;
    }

    public boolean isMentalidad() {
        return mMentalidad;
    }

    public boolean isActivo() {
        return mActivo;
    }

    public boolean isBorrado() {
        return mBorrado;
    }

    public String getContenido() {
        return mContenido;
    }

    public String getImagenPrograma() {
        return mImagenPrograma;
    }

    public String getTituloImagen() {
        return mTituloImagen;
    }

    public int getLike(){return mLike;}

    public boolean isGente()
    {
        return mPeople;
    }

    public int getmCorcholatas() {
        return mCorcholatas;
    }

    public boolean isPeople() {
        return mPeople;
    }

    public int getCorcholatas() {
        return mCorcholatas;
    }

    public String getTiempoEstimado() {
        return mTiempoEstimado;
    }

    public boolean isInscrito() {
        return mInscrito;
    }
}
