package com.femsa.requestmanager.DTO.User.Login;

import org.json.JSONObject;

public class ExistanceInnerData {

    private static final String STATUS_KEY = "status";

    private String mStatus;

    public ExistanceInnerData(JSONObject data)
        {
            mStatus = data.optString(STATUS_KEY);
        }

    public String getStatus() {
        return mStatus;
    }
}
