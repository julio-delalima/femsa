package com.femsa.requestmanager.DTO.User.Ranking;

import org.json.JSONObject;

public class RankingInnerData {

    private static final String MAIN_RANKING_KEY = "ranking";
    private static final String COMPLETED_PROGRAMS_KEY = "prograCompletados";
    private static final String INCOMPLETE_PROGRAMS_KEY = "prograIncompletos";
    private static final String CORCHOLATAS_KEY = "corcholatas";

    private int mCompletedPrograms;
    private int mIncompletePrograms;
    private int mCorcholatas;

    public RankingInnerData(JSONObject data)
    {
        JSONObject subdata = data.optJSONObject(MAIN_RANKING_KEY);
        mCompletedPrograms = subdata.optInt(COMPLETED_PROGRAMS_KEY);
        mIncompletePrograms = subdata.optInt(INCOMPLETE_PROGRAMS_KEY);
        mCorcholatas = subdata.optInt(CORCHOLATAS_KEY);
    }

    public int getCompletedPrograms() {
        return mCompletedPrograms;
    }

    public int getIncompletePrograms() {
        return mIncompletePrograms;
    }

    public int getCorcholatas() {
        return mCorcholatas;
    }
}
