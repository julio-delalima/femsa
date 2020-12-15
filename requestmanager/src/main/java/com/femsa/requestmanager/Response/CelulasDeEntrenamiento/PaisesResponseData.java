package com.femsa.requestmanager.Response.CelulasDeEntrenamiento;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;

import org.json.JSONObject;

public class PaisesResponseData {

    private PaisesDTO mPaises;

    public PaisesResponseData (JSONObject data){
        mPaises = new PaisesDTO(data);
    }

    public PaisesDTO getmPaises(){
        return mPaises;
    }

}
