package com.femsa.requestmanager.Response.Ranking;

import com.femsa.requestmanager.DTO.User.Ranking.RankingInnerData;

import org.json.JSONObject;

public class RankingResponseData {

    private String mError;
    private RankingInnerData mProgram;

    public RankingResponseData(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mProgram = new RankingInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public RankingInnerData getRanking() {
        return mProgram;
    }

}
