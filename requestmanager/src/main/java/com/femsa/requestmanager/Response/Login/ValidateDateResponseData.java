package com.femsa.requestmanager.Response.Login;

import com.femsa.requestmanager.DTO.User.Login.UserInnerData;

import org.json.JSONObject;

import java.io.Serializable;

public class ValidateDateResponseData implements Serializable {

        private static final String ERROR_KEY = "error";
        private static final String DATA_KEY = "data";


        private String mError;
        private UserInnerData mUser;

        public ValidateDateResponseData(JSONObject data)
        {
            mUser = new UserInnerData(data);
        }

        public ValidateDateResponseData(JSONObject error, int errornum)
        {
            mError = error.optString(ERROR_KEY);
        }

        public String getmError() {
            return mError;
        }

        public UserInnerData getmUser() {
            return mUser;
        }

}
