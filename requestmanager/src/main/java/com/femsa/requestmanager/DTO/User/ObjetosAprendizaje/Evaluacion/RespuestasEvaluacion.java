package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Evaluacion;

public class RespuestasEvaluacion {
    private int idRespuesta;
    private String respuestaTexto;
    private boolean status;
    private boolean correcta;
    private int idPregunta;

    public RespuestasEvaluacion(int idRespuesta, String respuestaTexto, boolean status, boolean correcta, int idPregunta) {
        this.idRespuesta = idRespuesta;
        this.respuestaTexto = respuestaTexto;
        this.status = status;
        this.correcta = correcta;
        this.idPregunta = idPregunta;
    }

    public int getIdRespuesta() {
        return idRespuesta;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isCorrecta() {
        return correcta;
    }

    public int getIdPregunta() {
        return idPregunta;
    }
}
