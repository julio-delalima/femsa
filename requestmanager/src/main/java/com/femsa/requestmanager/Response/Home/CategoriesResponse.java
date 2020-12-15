package com.femsa.requestmanager.Response.Home;

import com.femsa.requestmanager.DTO.User.Home.CategoriesInnerData;

import org.json.JSONObject;

public class CategoriesResponse {

    private String mError;

    private CategoriesInnerData mTerms;

    public CategoriesResponse(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mTerms = new CategoriesInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public CategoriesInnerData getCategories() {
        return mTerms;
    }
}
