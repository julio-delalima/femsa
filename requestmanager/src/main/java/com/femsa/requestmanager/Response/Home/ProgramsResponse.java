package com.femsa.requestmanager.Response.Home;

import com.femsa.requestmanager.DTO.User.Home.ProgramsInnerData;

import org.json.JSONObject;

public class ProgramsResponse {

    private String mError;

    private ProgramsInnerData mPrograms;

    public ProgramsResponse(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mPrograms = new ProgramsInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public ProgramsInnerData getPrograms() {
        return mPrograms;
    }
}


