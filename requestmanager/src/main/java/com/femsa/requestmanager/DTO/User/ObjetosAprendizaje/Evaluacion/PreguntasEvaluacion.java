package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Evaluacion;

import java.util.ArrayList;

public class PreguntasEvaluacion {
    private int idPregunta;
    private String textoPregunta;
    private String tipoPregunta;
    private ArrayList<RespuestasEvaluacion> respuestas;

    public PreguntasEvaluacion(int idPregunta, String textoPregunta, String tipoPregunta, ArrayList<RespuestasEvaluacion> respuestas) {
        this.idPregunta = idPregunta;
        this.textoPregunta = textoPregunta;
        this.tipoPregunta = tipoPregunta;
        this.respuestas = respuestas;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public String getTipoPregunta() {
        return tipoPregunta;
    }

    public ArrayList<RespuestasEvaluacion> getRespuestas() {
        return respuestas;
    }
}
