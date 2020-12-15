package com.femsa.requestmanager.DTO.User.MisRecompensas.detalle;

import org.json.JSONObject;

import java.io.Serializable;

public class DetalleRecompensaData implements Serializable {

    private static final String DETALLE_RECOMPENSA_KEY = "detallePrograma";

    private DetalleRecompensaDTO mDetalle;

    public DetalleRecompensaData(JSONObject data){
        if (data!=null){
            mDetalle = new DetalleRecompensaDTO(data.optJSONObject(DETALLE_RECOMPENSA_KEY));
        }
    }

    public DetalleRecompensaDTO getDetalle() {
        return mDetalle;
    }
}
