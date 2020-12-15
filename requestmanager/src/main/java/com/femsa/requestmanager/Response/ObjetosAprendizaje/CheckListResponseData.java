package com.femsa.requestmanager.Response.ObjetosAprendizaje;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.CheckList.CheckListInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckListResponseData  {

    private String mError;
    private CheckListInnerData mChecklist;

    public CheckListResponseData(JSONObject data) throws JSONException {
        // mError = data.optString(ERROR_KEY);
        mChecklist = new CheckListInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public CheckListInnerData getCheckList() {
            return mChecklist;
        }
}
