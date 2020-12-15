package com.femsa.requestmanager.Response.Configuracion;

import com.femsa.requestmanager.DTO.User.Configuracion.IdiomaInnerData;

import org.json.JSONObject;

public class IdiomaResponseData{

    private static final String ERROR_KEY = "error";
    private static final String DATA_KEY = "data";


    private String mError;
    private IdiomaInnerData mIdiomas;

    public IdiomaResponseData(JSONObject data)
    {
        mIdiomas = new IdiomaInnerData(data);
    }

    public IdiomaResponseData(String error, int errornum)
    {
        mError = error;
    }

    public String getmError() {
        return mError;
    }

    public IdiomaInnerData getIdiomaDTO() {
        return mIdiomas;
    }
}
