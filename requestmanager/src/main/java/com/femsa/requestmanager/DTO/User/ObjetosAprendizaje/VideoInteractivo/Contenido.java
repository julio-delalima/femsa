package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VideoInteractivo;

import java.util.ArrayList;

public class Contenido {

    private String mVideoRuta;
    private String mTituloVideo;
    private int mIDVideo;
    private ArrayList<Pregunta> mPreguntas;

    public Contenido(String videoRuta, String tituloVideo, int IDVideo, ArrayList<Pregunta> preguntas) {
        mVideoRuta = videoRuta;
        mTituloVideo = tituloVideo;
        mIDVideo = IDVideo;
        mPreguntas = preguntas;
    }

    public String getVideoRuta() {
        return mVideoRuta;
    }

    public String getTituloVideo() {
        return mTituloVideo;
    }

    public int getIDVideo() {
        return mIDVideo;
    }

    public ArrayList<Pregunta> getPreguntas() {
        return mPreguntas;
    }

    @Override
    public String toString() {
        return "Contenido{" +
                "mVideoRuta='" + mVideoRuta + '\'' +
                ", mTituloVideo='" + mTituloVideo + '\'' +
                ", mIDVideo=" + mIDVideo +
                ", mPreguntas=" + mPreguntas +
                '}';
    }
}
