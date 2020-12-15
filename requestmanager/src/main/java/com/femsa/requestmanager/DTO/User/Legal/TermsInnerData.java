package com.femsa.requestmanager.DTO.User.Legal;

import org.json.JSONObject;

public class TermsInnerData {

    private static final String TERMS_MAIN_KEY = "terminosCondiciones";
    private static final String ID_TERMS_KEY= "idTerminosCondiciones";
    private static final String ID_LANGUAGE_KEY = "idIdiomas";
    private static final String TERMS_KEY = "terminosTraducido";
    private static final String COUNTRY_KEY = "pais";

    private int mIdTerms;
    private int mIdLanguage;
    private String mTerms;
    private String mCountry;

    public TermsInnerData(JSONObject data) {
        JSONObject subdata = data.optJSONObject(TERMS_MAIN_KEY);
        mIdTerms = subdata.optInt(ID_TERMS_KEY);
        mIdLanguage = subdata.optInt(ID_LANGUAGE_KEY);
        mTerms = subdata.optString(TERMS_KEY);
        mCountry = subdata.optString(COUNTRY_KEY);
    }

    public int getIdTerms()
        {
            return mIdTerms;
        }

    public int getIdLanguage()
        {
            return mIdLanguage;
        }

    public String getTerms()
        {
            return mTerms;
        }

    public String getCountry()
        {
            return mCountry;
        }
}
