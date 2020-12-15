package com.femsa.requestmanager.Response.MiActividad;

import com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros.MiActividadInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class MiActividadResponseData {
    private String mError;
    private MiActividadInnerData mProgram;

    public MiActividadResponseData(JSONObject data) throws JSONException {
        // mError = data.optString(ERROR_KEY);
        mProgram = new MiActividadInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public MiActividadInnerData getMiActividad() {
        return mProgram;
    }

}
