package com.femsa.requestmanager.DTO.User.Home;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesInnerData
{
    private static final String ID_CATEGORIES_KEY = "idCategorias";
    private static final String ID_LANGUAGE_KEY = "idIdiomas";
    private static final String CATEGORY_KEY = "categoria";

    private ArrayList<String> mAllCategories;
    private ArrayList<Integer> mAllIDLanguaje;
    private ArrayList<Integer> mAllIDCategories;


    public ArrayList<String> getAllCategories() {
        return mAllCategories;
    }

    public ArrayList<Integer> getAllIDLanguaje() {
        return mAllIDLanguaje;
    }

    public ArrayList<Integer> getAllIDCategories() {
        return mAllIDCategories;
    }

    public CategoriesInnerData(JSONObject data)
    {
        JSONArray allCats = data.optJSONArray("categorias");
        mAllCategories = new ArrayList<>();
        mAllIDCategories = new ArrayList<>();
        mAllIDLanguaje = new ArrayList<>();

        for(int i = 0; i < allCats.length(); i++)
            {
                JSONObject subdata = allCats.optJSONObject(i);
                mAllIDCategories.add(subdata.optInt(ID_CATEGORIES_KEY));
                mAllIDLanguaje.add(subdata.optInt(ID_LANGUAGE_KEY));
                mAllCategories.add(subdata.optString(CATEGORY_KEY));
            }

    }
}
