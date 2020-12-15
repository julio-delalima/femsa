package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.CheckList;

import java.io.Serializable;

public class CheckListRespuesta implements Serializable {
    private int idPreguntaCheck;
    private String respuestaTextoCheck;
    private boolean status;
    private boolean correcta;

    public CheckListRespuesta(int idPreguntaCheck, String respuestaTextoCheck, boolean status, boolean correcta) {
        this.idPreguntaCheck = idPreguntaCheck;
        this.respuestaTextoCheck = respuestaTextoCheck;
        this.status = status;
        this.correcta = correcta;
    }

    public int getIdPreguntaCheck() {
        return idPreguntaCheck;
    }

    public String getRespuestaTextoCheck() {
        return respuestaTextoCheck;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isCorrecta() {
        return correcta;
    }
}
