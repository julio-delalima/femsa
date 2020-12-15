package com.femsa.requestmanager.Response.Ranking;

import com.femsa.requestmanager.DTO.User.Ranking.RankingTierlistInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class RankingTierlistResponseData {

    private String mError;
    private RankingTierlistInnerData mProgram;

    public RankingTierlistResponseData(JSONObject data) throws JSONException {
        // mError = data.optString(ERROR_KEY);
        mProgram = new RankingTierlistInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public RankingTierlistInnerData getProgramsRanking() {
        return mProgram;
    }

}
