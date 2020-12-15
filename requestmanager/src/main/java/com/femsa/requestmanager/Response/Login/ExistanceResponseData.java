package com.femsa.requestmanager.Response.Login;

import com.femsa.requestmanager.DTO.User.Login.ExistanceInnerData;

import org.json.JSONObject;

public class ExistanceResponseData {

    private ExistanceInnerData mEmployee;

    public ExistanceResponseData(JSONObject data)
    {
        mEmployee = new ExistanceInnerData(data);
    }

    public ExistanceInnerData getEmployee() {
        return mEmployee;
    }
}

