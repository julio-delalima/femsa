package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle;

import org.json.JSONObject;

import java.io.Serializable;

public class ColaboradorDTO implements Serializable {

    private static final String NOMBRE_PARTICIPANTE_KEY = "nombreParticipante";
    private static final String DESC_POSICION_KEY = "descPosicion";

    private String mNombreColaborador;
    private String mPosicionColaborador;

    public ColaboradorDTO(JSONObject data){
        if (data!=null){
            mNombreColaborador = data.optString(NOMBRE_PARTICIPANTE_KEY);
            mPosicionColaborador = data.optString(DESC_POSICION_KEY);
        }
    }

    public String getNombreColaborador() {
        return mNombreColaborador;
    }

    public String getPosicionColaborador() {
        return mPosicionColaborador;
    }
}
