package com.femsa.requestmanager.Response.Legal;

import com.femsa.requestmanager.DTO.User.Legal.TermsInnerData;

import org.json.JSONObject;

import java.io.Serializable;

public class TermsAndConditionsResponseData implements Serializable {
    private static final String ERROR_KEY = "error";
    private static final String DATA_KEY = "data";


    private String mError;
    private TermsInnerData mTerms;

    public TermsAndConditionsResponseData(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mTerms = new TermsInnerData(data);
    }

    public String getmError() {
        return mError;
    }

    public TermsInnerData getmTerms() {
        return mTerms;
    }
}
