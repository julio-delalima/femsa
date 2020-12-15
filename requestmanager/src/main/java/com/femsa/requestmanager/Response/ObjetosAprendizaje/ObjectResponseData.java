package com.femsa.requestmanager.Response.ObjetosAprendizaje;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.ObjectInnerData;

import org.json.JSONObject;

public class ObjectResponseData {
    private String mError;
    private ObjectInnerData mProgram;

    public ObjectResponseData(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mProgram = new ObjectInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public ObjectInnerData getCategories() {
        return mProgram;
    }

}
