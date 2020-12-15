package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.autorizador;

import java.io.Serializable;

public class ParticipanteDTO implements Serializable {

    private int mIdParticipante;

    public ParticipanteDTO(int idParticipante){
        mIdParticipante = idParticipante;
    }

    public int getIdParticipante(){
        return mIdParticipante;
    }
}
