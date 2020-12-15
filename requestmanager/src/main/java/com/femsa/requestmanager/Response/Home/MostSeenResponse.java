package com.femsa.requestmanager.Response.Home;

import com.femsa.requestmanager.DTO.User.Home.MostSeenInnerData;

import org.json.JSONObject;

public class MostSeenResponse {
    private static final String ERROR_KEY = "error";
    private static final String DATA_KEY = "data";


    private String mError;
    private MostSeenInnerData mMostSeen;

    public MostSeenResponse(JSONObject data, boolean esSub) {
        // mError = data.optString(ERROR_KEY);
        mMostSeen = new MostSeenInnerData(data, esSub);
    }

    public String getError() {
        return mError;
    }

    public MostSeenInnerData getCategories() {
        return mMostSeen;
    }


}
