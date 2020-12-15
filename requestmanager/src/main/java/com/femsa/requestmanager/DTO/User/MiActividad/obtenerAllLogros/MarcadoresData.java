package com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros;

import org.json.JSONObject;

import java.io.Serializable;

public class MarcadoresData implements Serializable {

    private static final String MENTALIDAD_KEY = "mentalidad";
    private static final String FOCO_KEY = "foco";
    private static final String EXCELENCIA_KEY = "excelencia";
    private static final String DECISIONES_AGILES_KEY = "decisionesAgiles";
    private static final String GENTE_KEY = "gente";

    private int mMentalidad;
    private int mFoco;
    private int mExcelencia;
    private int mDecisionesAgiles;
    private int mGente;

    public MarcadoresData(JSONObject data){
        mMentalidad = data.optInt(MENTALIDAD_KEY);
        mFoco = data.optInt(FOCO_KEY);
        mExcelencia = data.optInt(EXCELENCIA_KEY);
        mDecisionesAgiles = data.optInt(DECISIONES_AGILES_KEY);
        mGente = data.optInt(GENTE_KEY);
    }

    public int getMentalidad() {
        return mMentalidad;
    }

    public int getFoco() {
        return mFoco;
    }

    public int getExcelencia() {
        return mExcelencia;
    }

    public int getDecisionesAgiles() {
        return mDecisionesAgiles;
    }

    public int getGente() {
        return mGente;
    }
}
