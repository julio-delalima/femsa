package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Insomnia:
 * mostrarHorario
 */
public class MostrarHorarioData implements Serializable {

    private static final String AUTORIZACION_KEY = "autorizacion";
    private static final String DATOS_KEY = "datos";

    private String mAutorizacion;
    private DatosDTO mDatos;

    public MostrarHorarioData(JSONObject data){
        if (data!=null){
            mAutorizacion = data.optString(AUTORIZACION_KEY);
            mDatos = new DatosDTO(data.optJSONObject(DATOS_KEY));
        }
    }

    public String getAutorizacion() {
        return mAutorizacion;
    }

    public DatosDTO getDatos() {
        return mDatos;
    }
}
