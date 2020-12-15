package com.femsa.requestmanager.DTO.User.Home;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubcategoriesInnerData {

    private ArrayList<String> mCategoryNames;
    private ArrayList<Integer> mCategoryID;
    private ArrayList<Integer> mSubCategoryID;
    private ArrayList<Integer> mLanguageID;

    private static final String SUBCATEGORY_ARRAY_KEY = "subCategorias";
    private static final String CATEGORY_ID_KEY = "idCategorias";
    private static final String SUBCATEGORY_ID_KEY = "idSubCategoria";
    private static final String LANGUAGE_ID_KEY = "idIdiomas";
    private static final String SUBCATEGORY_KEY = "subCategoria";

    public ArrayList<String> getCategoryNames() {
        return mCategoryNames;
    }

    public ArrayList<Integer> getCategoryID() {
        return mCategoryID;
    }

    public ArrayList<Integer> getSubCategoryID() {
        return mSubCategoryID;
    }

    public ArrayList<Integer> getLanguageID() {
        return mLanguageID;
    }

    public SubcategoriesInnerData(JSONObject data)
    {
        mCategoryNames = new ArrayList<>();
        mCategoryID = new ArrayList<>();
        mSubCategoryID = new ArrayList<>();
        mLanguageID = new ArrayList<>();

        JSONArray subcategory = data.optJSONArray(SUBCATEGORY_ARRAY_KEY);
        for(int i = 0; i < subcategory.length(); i++)
        {
            JSONObject currentSubcategory = subcategory.optJSONObject(i);
            mCategoryNames.add(currentSubcategory.optString(SUBCATEGORY_KEY));
            mCategoryID.add(currentSubcategory.optInt(CATEGORY_ID_KEY));
            mSubCategoryID.add(currentSubcategory.optInt(SUBCATEGORY_ID_KEY));
            mLanguageID.add(currentSubcategory.optInt(LANGUAGE_ID_KEY));
        }
    }
}
