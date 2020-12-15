package com.femsa.requestmanager.Response.ObjetosAprendizaje;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Evaluacion.EvaluacionInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class EvaluacionResponseData  {

    private String mError;
    private EvaluacionInnerData mChecklist;

    public EvaluacionResponseData(JSONObject data) throws JSONException {
        // mError = data.optString(ERROR_KEY);
        mChecklist = new EvaluacionInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public EvaluacionInnerData getEvaluacion() {
        return mChecklist;
    }
}
