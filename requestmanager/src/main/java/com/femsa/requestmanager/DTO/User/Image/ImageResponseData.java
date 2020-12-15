package com.femsa.requestmanager.DTO.User.Image;

import org.json.JSONObject;

import java.io.Serializable;

public class ImageResponseData implements Serializable

    {

        private static final String ERROR_KEY = "error";
        private static final String DATA_KEY = "data";


        private String mError;
        private ImageInnerData mNewPic;

        public ImageResponseData(JSONObject data)
        {
            mError = data.optString(ERROR_KEY);
            mNewPic = new ImageInnerData(data);
        }

        public String getmError() {
                return mError;
            }

        public ImageInnerData getmUser() {
                return mNewPic;
            }
    }
