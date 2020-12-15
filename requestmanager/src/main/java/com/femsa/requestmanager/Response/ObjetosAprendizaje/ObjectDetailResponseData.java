package com.femsa.requestmanager.Response.ObjetosAprendizaje;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.ObjectDetailInnerData;

import org.json.JSONObject;

import java.io.Serializable;

public class ObjectDetailResponseData implements Serializable {
    private String mError;
    private ObjectDetailInnerData mProgram;

    public ObjectDetailResponseData(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mProgram = new ObjectDetailInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public ObjectDetailInnerData getCategories() {
        return mProgram;
    }

}
