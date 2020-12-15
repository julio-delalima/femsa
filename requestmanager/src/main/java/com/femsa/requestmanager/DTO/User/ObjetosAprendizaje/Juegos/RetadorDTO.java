package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Juegos;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RetadorDTO {

    private static final String RETADORES_KEY = "jugadores";
    private static final String MENSAJE_KEY = "mensaje";
    private static final String ID_PARTICIPANTE_KEY = "idEmpleado";
    private static final String FOTO_PARTICIPANTE_KEY = "fotoPerfil";
    private static final String NOMBRE_PARTICIPANTE_KEY = "nombre";
    private static final String NOMBRE_PAIS_KEY = "fotoPais";
    private static final String AREA_PARTICIPANTE_KEY = "descArea";

    private ArrayList<ParticipanteDTO> mListadoJugadores;

    public RetadorDTO(JSONObject data) throws JSONException {
        mListadoJugadores = new ArrayList<>();
        JSONArray listadoJugadoresarray = data.optJSONArray(RETADORES_KEY);
        if(listadoJugadoresarray != null){
            for(int i = 0; i < listadoJugadoresarray.length(); i++){
                JSONObject participanteActual = listadoJugadoresarray.optJSONObject(i);
                mListadoJugadores.add(
                        new ParticipanteDTO(
                                participanteActual.optInt(ID_PARTICIPANTE_KEY),
                                participanteActual.optString(NOMBRE_PARTICIPANTE_KEY),
                                participanteActual.optString(AREA_PARTICIPANTE_KEY),
                                participanteActual.optString(FOTO_PARTICIPANTE_KEY),
                                participanteActual.optString(NOMBRE_PAIS_KEY))
                );
            }
        }
    }

    public ArrayList<ParticipanteDTO> getListadoJugadores() {
        return mListadoJugadores;
    }
}
