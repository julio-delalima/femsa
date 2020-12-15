package com.femsa.requestmanager.DTO.User.Ranking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RankingTierlistInnerData {

    private static final String RANKING_LIST_KEY = "rankingLista";
    private static final String TOTAL_BONUS_KEY = "bonusTotales";
    private static final String ID_EMPLOYE_KEY = "idEmpleado";
    private static final String ID_GROUP_KEY = "idGrupo";
    private static final String NAME_KEY = "nombre";
    private static final String ID_LANG_KEY = "idIdioma";
    private static final String PLACEMENT_KEY = "lugar";
    private static final String PHOTO_KEY = "fotoPerfil";
    private static final String TOTAL_LIKES_KEY = "cantMeGusta";
    private static final String HAS_LIKE_KEY = "tengoLike";

    private ArrayList<RankingTierlistData> mTierlist;

    public RankingTierlistInnerData(JSONObject data) throws JSONException {
        mTierlist = new ArrayList<>();
        mTierlist.addAll(parseData(data.optJSONArray(RANKING_LIST_KEY)));
    }

    private ArrayList<RankingTierlistData> parseData(JSONArray main) throws JSONException
        {
            ArrayList<RankingTierlistData> tempData = new ArrayList<>();
            for(int i = 0; i < main.length(); i++)
                {
                    JSONObject tempJsonObj = main.getJSONObject(i);
                    tempData.add(
                            new RankingTierlistData(
                                    tempJsonObj.optInt(TOTAL_BONUS_KEY),
                                    tempJsonObj.optInt(ID_EMPLOYE_KEY),
                                    tempJsonObj.optInt(ID_GROUP_KEY),
                                    tempJsonObj.optString(NAME_KEY),
                                    tempJsonObj.optInt(ID_LANG_KEY),
                                    tempJsonObj.optInt(PLACEMENT_KEY),
                                    tempJsonObj.optString(PHOTO_KEY),
                                    tempJsonObj.optInt(TOTAL_LIKES_KEY),
                                    tempJsonObj.optBoolean(HAS_LIKE_KEY)
                    ));
                }

            return tempData;
        }

    public ArrayList<RankingTierlistData> getTierlist() {
        return mTierlist;
    }
}
