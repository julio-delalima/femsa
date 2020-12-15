package com.femsa.requestmanager.DTO.User.Legal;

import org.json.JSONObject;

public class PrivacyInnerData {

    private static final String ADVICE_MAIN_KEY = "avisoprivacidad";
    private static final String ID_PRIVACY_KEY = "idAvisoPrivacidad";
    private static final String COUNTRY_KEY = "pais";
    private static final String ID_LANGUAGE_KEY = "idIdioma";
    private static final String CONTENT_KEY = "contenido";

    private int mIdAdvice;
    private String mCountry;
    private int mIdLanguage;
    private String mContent;

    public PrivacyInnerData(JSONObject data)
    {
        if(data != null){
            JSONObject subdata = data.optJSONObject(ADVICE_MAIN_KEY);
            mIdAdvice = subdata.optInt(ID_PRIVACY_KEY);
            mCountry = subdata.optString(COUNTRY_KEY);
            mIdLanguage = subdata.optInt(ID_LANGUAGE_KEY);
            mContent = subdata.optString(CONTENT_KEY);
        }
    }

    public int getIdAdvice() {
        return mIdAdvice;
    }

    public String getCountry() {
        return mCountry;
    }

    public int getIdLanguage() {
        return mIdLanguage;
    }

    public String getContent() {
        return mContent;
    }
}
