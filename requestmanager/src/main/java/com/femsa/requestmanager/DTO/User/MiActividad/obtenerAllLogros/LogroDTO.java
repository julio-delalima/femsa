package com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros;

import org.json.JSONObject;

import java.io.Serializable;

public class LogroDTO implements Serializable {

    private static final String ID_LOGRO_KEY = "idLogro";
    private static final String IMG_LOGRO_KEY = "imgLogro";
    private static final String DESCRIPCION_LOGRO_KEY = "descLogro";
    private static final String FECHA_ALCANZADO_KEY = "fechaAlcanzado";
    private static final String OBTENIDA_KEY = "obtenida";
    private static final String PROCESADA_KEY = "procesada";
    private static final String NOMBRE_LOGRO_KEY = "nombreLogro";

    private int mIdLogro;
    private String mImgLogro;
    private String mDescripcionLogro;
    private Long mFechaAlcanzado;
    private boolean mObtenida;
    private boolean mProcesada;
    private String mNombreLogro;

    public LogroDTO(JSONObject data){
        mIdLogro = data.optInt(ID_LOGRO_KEY);
        mImgLogro = data.optString(IMG_LOGRO_KEY);
        mDescripcionLogro = data.optString(DESCRIPCION_LOGRO_KEY);
        mFechaAlcanzado = data.optLong(FECHA_ALCANZADO_KEY);
        mObtenida = data.optBoolean(OBTENIDA_KEY);
        mProcesada = data.optBoolean(PROCESADA_KEY);
        mNombreLogro = data.optString(NOMBRE_LOGRO_KEY);
    }

    public int getIdLogro() {
        return mIdLogro;
    }

    public String getImgLogro() {
        return mImgLogro;
    }

    public String getDescripcionLogro() {
        return mDescripcionLogro;
    }

    public Long getFechaAlcanzado() {
        return mFechaAlcanzado;
    }

    public boolean isObtenida() {
        return mObtenida;
    }

    public boolean isProcesada() {
        return mProcesada;
    }

    public String getNombreLogro() {
        return mNombreLogro;
    }
}
