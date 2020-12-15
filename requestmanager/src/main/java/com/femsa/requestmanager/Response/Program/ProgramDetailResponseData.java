package com.femsa.requestmanager.Response.Program;

import com.femsa.requestmanager.DTO.User.Program.ProgramDetailInnerData;

import org.json.JSONObject;

public class ProgramDetailResponseData {
    private String mError;
    private ProgramDetailInnerData mProgram;

    public ProgramDetailResponseData(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mProgram = new ProgramDetailInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public ProgramDetailInnerData getPrograms() {
        return mProgram;
    }

}
