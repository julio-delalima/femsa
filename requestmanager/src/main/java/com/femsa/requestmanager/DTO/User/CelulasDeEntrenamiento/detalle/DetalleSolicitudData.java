package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Insomnia: obtenerDetalleSolicitud
 */
public class DetalleSolicitudData implements Serializable {

    private static final String DETALLE_SOLICITUD_KEY = "detalleSolicitud";

    private DetalleSolicitudDTO mDetalleSolicitud;

    public DetalleSolicitudData(JSONObject data){
        if (data!=null){
            mDetalleSolicitud = new DetalleSolicitudDTO(data.optJSONObject(DETALLE_SOLICITUD_KEY));
        }
    }

    public DetalleSolicitudDTO getDetalleSolicitud() {
        return mDetalleSolicitud;
    }
}
