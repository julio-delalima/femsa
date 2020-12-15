package com.femsa.requestmanager.Response.Login;

import com.femsa.requestmanager.DTO.User.Login.UserInnerData;

import org.json.JSONObject;

import java.io.Serializable;

public class UserResponseData implements Serializable {

        private static final String ERROR_KEY = "error";
        private static final String DATA_KEY = "data";


        private String mError;
        private UserInnerData mUser;

        public UserResponseData(JSONObject data)
            {
                mUser = new UserInnerData(data);
            }

        public UserResponseData(String error, int errornum)
            {
                mError = error;
            }

    public String getmError() {
        return mError;
    }

    public UserInnerData getmUser() {
        return mUser;
    }
}
