package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante;

import org.json.JSONObject;

import java.io.Serializable;

public class ParticipanteData implements Serializable {

    private static final String PARTICIPANTE_KEY = "participante";


    private ParticipanteDTO mParticipante;

    public ParticipanteData (JSONObject data){
        mParticipante = new ParticipanteDTO(data.optJSONObject(PARTICIPANTE_KEY));
    }

    public ParticipanteDTO getmParticipante() {
        return mParticipante;
    }
}
