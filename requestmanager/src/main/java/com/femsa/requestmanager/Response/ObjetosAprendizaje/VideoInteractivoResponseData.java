package com.femsa.requestmanager.Response.ObjetosAprendizaje;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VideoInteractivo.VideoInteractivoInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoInteractivoResponseData {
    private String mError;
    private VideoInteractivoInnerData mVideo;

    public VideoInteractivoResponseData(JSONObject data) throws JSONException {
        // mError = data.optString(ERROR_KEY);
        mVideo = new VideoInteractivoInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public VideoInteractivoInnerData getVideo() {
        return mVideo;
    }

}
