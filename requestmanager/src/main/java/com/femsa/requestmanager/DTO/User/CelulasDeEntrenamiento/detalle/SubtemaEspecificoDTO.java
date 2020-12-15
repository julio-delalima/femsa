package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Insominia: subTemaEspecifico{}
 */
public class SubtemaEspecificoDTO implements Serializable {

    private static final String NOMBRE_SUBTEMA_ESPECIFICO_KEY = "nombreSubTemaEspecifico";
    private static final String TIEMPO_CADENA_KEY = "tiempoCadena";
    private static final String SUBAREA_KEY = "subArea";

    private String mSubtema;
    private String mTiempo;
    private String mSubarea;

    public SubtemaEspecificoDTO(JSONObject data){
        if (data!=null){
            mSubtema = data.optString(NOMBRE_SUBTEMA_ESPECIFICO_KEY);
            mTiempo = data.optString(TIEMPO_CADENA_KEY);
            mSubarea = data.optString(SUBAREA_KEY);
        }
    }

    public String getSubtema() {
        return mSubtema;
    }

    public String getTiempo(){
        return mTiempo;
    }

    public String getSubarea(){
        return mSubarea;
    }
}
