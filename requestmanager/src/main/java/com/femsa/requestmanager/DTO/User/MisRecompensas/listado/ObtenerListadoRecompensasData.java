package com.femsa.requestmanager.DTO.User.MisRecompensas.listado;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Insomnia: obtenerRecompensas
 */
public class ObtenerListadoRecompensasData {

    private static final String RECOMPENSAS_KEY = "recompensas";

    private ArrayList<RecompensaDTO> mListadoRecompensas;

    public ObtenerListadoRecompensasData(JSONObject data){
        if (data!=null){
            mListadoRecompensas = new ArrayList<>();
            obtenerListadoRecompensas(data.optJSONArray(RECOMPENSAS_KEY));
        }
    }

    /**
     * <p>Método que permite agregar al ArrayList el listado de recompensas.</p>
     * @param array
     */
    private void obtenerListadoRecompensas(JSONArray array){
        if (array!=null){
            for (int i=0; i<array.length(); i++){
                mListadoRecompensas.add(new RecompensaDTO(array.optJSONObject(i)));
            }
        }
    }

    public ArrayList<RecompensaDTO> getListadoRecompensas() {
        return mListadoRecompensas;
    }
}
