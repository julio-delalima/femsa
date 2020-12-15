package com.femsa.requestmanager.Response.Ranking;

import com.femsa.requestmanager.DTO.User.Ranking.ProgramRankingInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class ProgramRankingResponseData {

    private String mError;
    private ProgramRankingInnerData mProgram;

    public ProgramRankingResponseData(JSONObject data) throws JSONException {
        // mError = data.optString(ERROR_KEY);
        mProgram = new ProgramRankingInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public ProgramRankingInnerData getProgramsRanking() {
        return mProgram;
    }

}
