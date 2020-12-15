package com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ObtenerAllLogrosData implements Serializable {

    private static final String KOF_SIGUIENTE_NIVEL_KEY = "kofSiguienteNivel";
    private static final String LISTA_LOGROS_KEY = "listaLogros";

    private MarcadoresData mKofSiguienteNivel;
    private ArrayList<LogroDTO>  mListadoLogros;

    public ObtenerAllLogrosData(JSONObject data){
        if (data!=null){
            mKofSiguienteNivel = new MarcadoresData(data.optJSONObject(KOF_SIGUIENTE_NIVEL_KEY));
            mListadoLogros = new ArrayList<>();
            obtenerListadoLogros(data.optJSONArray(LISTA_LOGROS_KEY));
        }
    }

    /**
     * <p>Método que permite agregar al ArrayList la lista de logros.</p>
     * @param array
     */
    private void obtenerListadoLogros(JSONArray array){
        for (int i=0; i<array.length(); i++){
            mListadoLogros.add(new LogroDTO(array.optJSONObject(i)));
        }
    }

    public MarcadoresData getKofSiguienteNivel() {
        return mKofSiguienteNivel;
    }

    public ArrayList<LogroDTO> getListadoLogros() {
        return mListadoLogros;
    }
}
