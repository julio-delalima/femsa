package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Insomnia: obtenerListaSolicitudes
 */
public class ListaSolicitudesData implements Serializable {

    private static final String SOLICITUDES_KEY = "solicitudes";

    private ArrayList<SolicitudDTO> mListadoSolicitudes;

    public ListaSolicitudesData(JSONObject data){
        if (data!=null){
            mListadoSolicitudes = new ArrayList<>();
            obtenerListadoSolicitudes(data.optJSONArray(SOLICITUDES_KEY));
        }
    }

    /**
     * <p>Método que permite agregar al ArrayList el listado de Solicitudes.</p>
     * @param array
     */
    private void obtenerListadoSolicitudes(JSONArray array){
        for (int i=0; i<array.length(); i++){
            mListadoSolicitudes.add(new SolicitudDTO(array.optJSONObject(i)));
        }
    }

    public ArrayList<SolicitudDTO> getListadoSolicitudes() {
        return mListadoSolicitudes;
    }
}
