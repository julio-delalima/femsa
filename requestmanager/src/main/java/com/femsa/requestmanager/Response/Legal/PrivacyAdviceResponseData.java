package com.femsa.requestmanager.Response.Legal;

import com.femsa.requestmanager.DTO.User.Legal.PrivacyInnerData;

import org.json.JSONObject;

public class PrivacyAdviceResponseData {

    private static final String ERROR_KEY = "error";
    private static final String DATA_KEY = "data";


    private String mError;
    private PrivacyInnerData mTerms;

    public PrivacyAdviceResponseData(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mTerms = new PrivacyInnerData(data);
    }

    public String getmError() {
        return mError;
    }

    public PrivacyInnerData getAdvice() {
        return mTerms;
    }
}