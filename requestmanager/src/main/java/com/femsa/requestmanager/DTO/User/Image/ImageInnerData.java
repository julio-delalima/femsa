package com.femsa.requestmanager.DTO.User.Image;

import org.json.JSONObject;

public class ImageInnerData {

    /**
     * These are the keys used to catch variables from the JSON, names must match exactly
     **/
    private final String NEW_PIC_KEY = "fotoPerfil";

    private String mNewPic;

    public ImageInnerData(String newPic) {
        mNewPic = newPic;
    }

    public ImageInnerData(JSONObject data)
    {
        mNewPic = data.optString(NEW_PIC_KEY);
    }

    public String getNewPic() {
        return mNewPic;
    }

    public void setNewPic(String newPic) {
        mNewPic = newPic;
    }

}
