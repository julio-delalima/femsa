package com.femsa.requestmanager.Response.CelulasDeEntrenamiento.FacilitadorData;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.FacilitadorInnerData;

import org.json.JSONObject;

import java.io.Serializable;

public class FacilitadorData implements Serializable {

    private String mError;

    private FacilitadorInnerData mFacilitadores;

    public FacilitadorData (JSONObject data){
        mFacilitadores = new FacilitadorInnerData(data);
    }

    public FacilitadorInnerData getFacilitadores(){
        return mFacilitadores;
    }

    public String getError() {
        return mError;
    }
}
