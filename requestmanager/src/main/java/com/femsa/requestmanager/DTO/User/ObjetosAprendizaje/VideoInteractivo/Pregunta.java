package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VideoInteractivo;

import java.util.ArrayList;

public class Pregunta {

    private String mPreguntaCompleta;

    private ArrayList<Respuesta> mPosiblesRespuestas;

    public Pregunta(String preguntaCompleta, ArrayList<Respuesta> posiblesRespuestas) {
        mPreguntaCompleta = preguntaCompleta;
        mPosiblesRespuestas = posiblesRespuestas;
    }

    public String getPreguntaCompleta() {
        return mPreguntaCompleta;
    }

    public ArrayList<Respuesta> getPosiblesRespuestas() {
        return mPosiblesRespuestas;
    }

    @Override
    public String toString() {
        return "Pregunta{" +
                "mPreguntaCompleta='" + mPreguntaCompleta + '\'' +
                ", mPosiblesRespuestas=" + mPosiblesRespuestas +
                '}';
    }
}
