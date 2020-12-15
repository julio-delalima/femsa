package com.femsa.requestmanager.DTO.User.Ranking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProgramRankingInnerData {

    private static final String RANKING_ACTIVE_MAIN_KEY =  "rankingProgramasActivo";
    private static final String RANKING_HISTORY_MAIN_KEY = "rankingProgramasHistorial";
    private static final String PROGRAM_NAME_KEY = "nombrePrograma";
    private static final String PROGRAM_BONUS_KEY = "bonusPorProgra";
    private static final String ID_PROGRAM_KEY = "idPrograma";
    private static final String ID_OBJECT_KEY = "idObj";
    private static final String POSITION_KEY = "posicion";
    private static final String NUM_OPPONENTS_KEY = "numOponentes";
    private static final String PROGRAM_IMG_KEY = "imgPrograma";
    private static final String GROUP_ID_KEY = "idGrupo";
    private static final String TOTAL_BONUS_KEY = "bonusTotalPorProgra";
    private static final String KOF_MAIN_KEY = "kof";
    private static final String MIND_KEY = "mentalidad";
    private static final String LIGHTBULB_LEY = "foco";
    private static final String EXCELLENCE_KEY = "excelencia";
    private static final String DECISION_KEY = "decisionesAgiles";
    private static final String PEOPLE_KEY = "gente";


    private ArrayList<ProgramRankingData> mActivePrograms;
    private ArrayList<ProgramRankingData> mHistoryPrograms;
    private int mMentalidad, mFoco, mExcelencia, mDesiciones, mGente;

    public ProgramRankingInnerData(JSONObject data) throws JSONException {
        mActivePrograms = new ArrayList<>();
        mHistoryPrograms = new ArrayList<>();
        JSONArray active = data.optJSONArray(RANKING_ACTIVE_MAIN_KEY);
        mActivePrograms.addAll(parseData(active));
        JSONArray history = data.optJSONArray(RANKING_HISTORY_MAIN_KEY);
        mHistoryPrograms.addAll(parseData(history));
        getKOFData(data.getJSONObject(KOF_MAIN_KEY));
    }

    private ArrayList<ProgramRankingData> parseData(JSONArray main) throws JSONException
        {
            ArrayList<ProgramRankingData> tempData = new ArrayList<>();
            if(main == null)
            {
                return new ArrayList<>();
            }
            for(int i = 0; i < main.length(); i++)
                {
                    JSONObject tempJsonObj = main.getJSONObject(i);
                    tempData.add(
                            new ProgramRankingData(
                                    tempJsonObj.optString(PROGRAM_NAME_KEY),
                                    tempJsonObj.optInt(PROGRAM_BONUS_KEY),
                                    tempJsonObj.optInt(ID_PROGRAM_KEY),
                                    tempJsonObj.optInt(ID_OBJECT_KEY),
                                    tempJsonObj.optInt(POSITION_KEY),
                                    tempJsonObj.optInt(NUM_OPPONENTS_KEY),
                                    tempJsonObj.optString(PROGRAM_IMG_KEY),
                                    tempJsonObj.optInt(GROUP_ID_KEY),
                                    tempJsonObj.optInt(TOTAL_BONUS_KEY)
                    ));
                }

            return tempData;
        }

    private void getKOFData(JSONObject kof)
    {
        mMentalidad = kof.optInt(MIND_KEY);
        mFoco = kof.optInt(LIGHTBULB_LEY);
        mExcelencia = kof.optInt(EXCELLENCE_KEY);
        mDesiciones = kof.optInt(DECISION_KEY);
        mGente = kof.optInt(PEOPLE_KEY);
    }
    public ArrayList<ProgramRankingData> getActivePrograms() {
        return mActivePrograms;
    }

    public ArrayList<ProgramRankingData> getHistoryPrograms() {
        return mHistoryPrograms;
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

    public int getDesiciones() {
        return mDesiciones;
    }

    public int getGente() {
        return mGente;
    }
}
