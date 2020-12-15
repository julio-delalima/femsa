package com.femsa.requestmanager.Response.ObjetosAprendizaje;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Encuesta.EncuestaInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class EncuestaResponseData {

    private String mError;
    private EncuestaInnerData mEncuesta;

    public EncuestaResponseData(JSONObject data) throws JSONException {
        // mError = data.optString(ERROR_KEY);
        mEncuesta = new EncuestaInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public EncuestaInnerData getEncuesta() {
        return mEncuesta;
    }
}
