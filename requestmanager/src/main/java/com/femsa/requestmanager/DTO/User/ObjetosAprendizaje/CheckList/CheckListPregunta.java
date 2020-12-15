package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.CheckList;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckListPregunta implements Serializable {

    private int idObjeto;
    private int idCheck;
    private int bloque;
    private String textoPregunta;
    private ArrayList<CheckListRespuesta> mRespuestas;

    public CheckListPregunta(int idObjeto, int idCheck, int bloque, String textoPregunta, ArrayList<CheckListRespuesta> respuestas) {
        this.idObjeto = idObjeto;
        this.idCheck = idCheck;
        this.bloque = bloque;
        this.textoPregunta = textoPregunta;
        mRespuestas = respuestas;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public int getIdCheck() {
        return idCheck;
    }

    public int getBloque() {
        return bloque;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public ArrayList<CheckListRespuesta> getRespuestas() {
        return mRespuestas;
    }
}
