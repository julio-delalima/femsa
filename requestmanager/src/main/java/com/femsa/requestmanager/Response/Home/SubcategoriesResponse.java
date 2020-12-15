package com.femsa.requestmanager.Response.Home;

import com.femsa.requestmanager.DTO.User.Home.SubcategoriesInnerData;

import org.json.JSONObject;

public class SubcategoriesResponse {

    private String mError;

    private SubcategoriesInnerData mSubcats;

    public SubcategoriesResponse(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mSubcats = new SubcategoriesInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public SubcategoriesInnerData getSubcategories() {
        return mSubcats;
    }
}
