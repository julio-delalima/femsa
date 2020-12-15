package com.femsa.requestmanager.Response.Home;

import com.femsa.requestmanager.DTO.User.Home.CuzYouSawInnerData;

import org.json.JSONObject;

public class CuzYouSawResponse {

    private String mError;
    private CuzYouSawInnerData mSeen;

    public CuzYouSawResponse(JSONObject data, boolean sub)
    {
        // mError = data.optString(ERROR_KEY);
        mSeen = new CuzYouSawInnerData(data, sub);
    }

    public String getError() {
        return mError;
    }

    public CuzYouSawInnerData getCategories() {
        return mSeen;
    }
}
