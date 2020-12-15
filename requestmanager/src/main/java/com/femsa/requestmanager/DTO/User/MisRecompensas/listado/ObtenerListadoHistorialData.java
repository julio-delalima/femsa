package com.femsa.requestmanager.DTO.User.MisRecompensas.listado;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Insomnia: obtenerRecompensaHistorica
 */
public class ObtenerListadoHistorialData implements Serializable {

    private static final String HISTORIAL_KEY = "recompensaHistorial";

    private ArrayList<RecompensaDTO> mListHistorial;

    public ObtenerListadoHistorialData(JSONObject data){
        if (data!=null){
            mListHistorial = new ArrayList<>();
            obtenerListadoHistorial(data.optJSONArray(HISTORIAL_KEY));
        }
    }

    /**
     * <p>Método que permite agregar a mListHistorial el listado de recompensas de la petición.</p>
     * @param array
     */
    private void obtenerListadoHistorial(JSONArray array){
        if (array!=null){
            for (int i=0; i<array.length(); i++){
                mListHistorial.add(new RecompensaDTO(array.optJSONObject(i)));
            }
        }
    }

    public ArrayList<RecompensaDTO> getmListHistorial() {
        return mListHistorial;
    }
}
