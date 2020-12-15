package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Encuesta;

import java.util.ArrayList;

public class PreguntasEncuesta {
    private int idPregunta;
    private String textoPregunta;
    private String tipoPregunta;
    private String respuestaAbiertaTexto;
    private ArrayList<RespuestasEncuesta> respuestas;

    public PreguntasEncuesta(int idPregunta, String textoPregunta, String tipoPregunta, String respuestaTexto, ArrayList<RespuestasEncuesta> respuestas) {
        this.idPregunta = idPregunta;
        this.textoPregunta = textoPregunta;
        this.tipoPregunta = tipoPregunta;
        this.respuestaAbiertaTexto = respuestaTexto;
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

    public String getRespuestaAbiertaTexto() {
        return respuestaAbiertaTexto;
    }

    public ArrayList<RespuestasEncuesta> getRespuestas() {
        return respuestas;
    }
}
