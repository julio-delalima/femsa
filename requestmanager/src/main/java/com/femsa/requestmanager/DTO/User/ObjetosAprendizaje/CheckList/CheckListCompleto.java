package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.CheckList;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckListCompleto implements Serializable {
    private int idObjeto;
    private ArrayList<CheckListPregunta> mPreguntas;

    public CheckListCompleto(int idObjeto, ArrayList<CheckListPregunta> preguntas) {
        this.idObjeto = idObjeto;
        mPreguntas = preguntas;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public ArrayList<CheckListPregunta> getPreguntas() {
        return mPreguntas;
    }
}
