package com.femsa.requestmanager.Response.ObjetosAprendizaje.Juegos;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Juegos.RetadorDTO;

import org.json.JSONException;
import org.json.JSONObject;

public class RetadorResponseData {
    private String mError;
    private RetadorDTO mRetador;

    public RetadorResponseData(JSONObject data) throws JSONException {
        mRetador = new RetadorDTO(data);
    }

    public String getError() {
        return mError;
    }

    public RetadorDTO getRetador() {
        return mRetador;
    }

}
