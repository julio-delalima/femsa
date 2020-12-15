package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VideoInteractivo;

public class Respuesta {

    private String mPreguntasTexto;

    private boolean mPreguntaCorrecta;

    private boolean mActivo;

    private int mIDRespuesta;



    public Respuesta(String preguntasTexto, boolean preguntaCorrecta, int idRespuesta) {
        mPreguntasTexto = preguntasTexto;
        mPreguntaCorrecta = preguntaCorrecta;
        mIDRespuesta = idRespuesta;
    }

    public int getmIDRespuesta() {
        return mIDRespuesta;
    }

    public String getPreguntasTexto() {
        return mPreguntasTexto;
    }

    public boolean isPreguntaCorrecta() {
        return mPreguntaCorrecta;
    }

    public void setActivo(boolean activo){
        mActivo = activo;
    }

    public boolean isActivo(){
        return mActivo;
    }

    @Override
    public String toString() {
        return "Respuesta{" +
                "mPreguntasTexto='" + mPreguntasTexto + '\'' +
                ", mPreguntaCorrecta=" + mPreguntaCorrecta +
                '}';
    }
}
