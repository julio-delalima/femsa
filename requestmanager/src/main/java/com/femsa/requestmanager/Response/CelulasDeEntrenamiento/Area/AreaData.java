package com.femsa.requestmanager.Response.CelulasDeEntrenamiento.Area;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.AreaDTO;

import org.json.JSONObject;

import java.io.Serializable;

public class AreaData implements Serializable {

    private AreaDTO mAreas;

    public AreaData (JSONObject data){
        mAreas = new AreaDTO(data);
    }

    public AreaDTO getmAreas(){
        return  mAreas;
    }

}
