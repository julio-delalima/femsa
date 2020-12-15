package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Encuesta;

public class RespuestasEncuesta {
    private int idRespuesta;
    private String respuestaTexto;
    private boolean status;
    private boolean correcta;
    private int idPregunta;
    private String mRespuestaGuardada;

    public RespuestasEncuesta(int idRespuesta, String respuestaTexto, boolean status, boolean correcta, int idPregunta, String respuestaGuardada) {
        this.idRespuesta = idRespuesta;
        this.respuestaTexto = respuestaTexto;
        this.status = status;
        this.correcta = correcta;
        this.idPregunta = idPregunta;
        mRespuestaGuardada = respuestaGuardada;
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

    public String getRespuestaGuardada() {
        return mRespuestaGuardada;
    }
}
