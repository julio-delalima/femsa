package com.femsa.requestmanager.Response.ObjetosAprendizaje;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VedeoCharlaConExperto.VedeoCharlaConExpertoInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class VedeoCharlaConExpertoResponseData {

    private String mError;
    private VedeoCharlaConExpertoInnerData mVedeo;

    public VedeoCharlaConExpertoResponseData(JSONObject data) throws JSONException {
        // mError = data.optString(ERROR_KEY);
        mVedeo = new VedeoCharlaConExpertoInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public VedeoCharlaConExpertoInnerData getVedeo() {
        return mVedeo;
    }

}
