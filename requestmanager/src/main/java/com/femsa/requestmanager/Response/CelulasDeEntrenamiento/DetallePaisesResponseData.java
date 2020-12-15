package com.femsa.requestmanager.Response.CelulasDeEntrenamiento;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.DetallePaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;

import org.json.JSONObject;

public class DetallePaisesResponseData {

    private DetallePaisesDTO mPaises;

    public DetallePaisesResponseData(JSONObject data){
        mPaises = new DetallePaisesDTO(data);
    }

    public DetallePaisesDTO getmPais(){
        return mPaises;
    }

}
