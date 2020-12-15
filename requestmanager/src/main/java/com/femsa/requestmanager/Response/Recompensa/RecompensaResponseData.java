package com.femsa.requestmanager.Response.Recompensa;

import com.femsa.requestmanager.DTO.User.Recompensa.RecompensaInnerData;

import org.json.JSONObject;

public class RecompensaResponseData {

    private String mError;
    private RecompensaInnerData mProgram;

    public RecompensaResponseData(JSONObject data)
    {
        // mError = data.optString(ERROR_KEY);
        mProgram = new RecompensaInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public RecompensaInnerData getRecompensa() {
        return mProgram;
    }

}
