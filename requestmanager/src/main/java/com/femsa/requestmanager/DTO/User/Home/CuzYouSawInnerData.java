package com.femsa.requestmanager.DTO.User.Home;

import com.femsa.requestmanager.Utilities.RemoveDuplicates;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CuzYouSawInnerData {

    private static final String MAIN_YOU_SAW_KEY = "porqueViste"; //ARRAY
    private static final String PORQUE_VISTE_SUB_KEY = "porqueVisteSub"; //ARRAY
    private static final String ID_PROGRAM_KEY = "idPrograma";
    private static final String PROGRAM_IMAGE_KEY = "imagenPrograma";
    private static final String IMAGE_TITLE_KEY = "tituloImagen";
    private static final String CATEGORY_NAME_KEY = "nombreCategoria";

    private ArrayList<Integer> mAllIDPrograms;
    private ArrayList<String> mAllImagesPrograms, mAllImageTitles, mAllCategoryNames;

    public ArrayList<Integer> getAllIDPrograms() {
        return mAllIDPrograms;
    }

    public ArrayList<String> getAllImagesPrograms() {
        return mAllImagesPrograms;
    }

    public ArrayList<String> getAllImageTitles() {
        return mAllImageTitles;
    }

    public ArrayList<String> getAllCategoryNames() {
        return mAllCategoryNames;
    }

    private ArrayList<DataPrograms> allData;

    public CuzYouSawInnerData(JSONObject data, boolean esSubcategoria)
    {
        mAllCategoryNames = new ArrayList<>();
        mAllIDPrograms = new ArrayList<>();
        mAllImagesPrograms = new ArrayList<>();
        mAllImageTitles = new ArrayList<>();
        allData = new ArrayList<>();

        JSONArray allYouSaw = data.optJSONArray(esSubcategoria ? PORQUE_VISTE_SUB_KEY : MAIN_YOU_SAW_KEY);

        for(int i = 0; i< allYouSaw.length(); i++)
        {
            JSONObject subdata = allYouSaw.optJSONObject(i);
            mAllCategoryNames.add("Porque viste");//subdata.optString(CATEGORY_NAME_KEY));
            mAllIDPrograms.add(subdata.optInt(ID_PROGRAM_KEY));
            mAllImagesPrograms.add(subdata.optString(PROGRAM_IMAGE_KEY));
            mAllImageTitles.add(subdata.optString(IMAGE_TITLE_KEY));
            allData.add(new DataPrograms(
                    subdata.optInt(ID_PROGRAM_KEY),
                    subdata.optString(IMAGE_TITLE_KEY),
                    subdata.optString(PROGRAM_IMAGE_KEY),
                    "Porque viste", false)
            );
        }
        mAllCategoryNames = RemoveDuplicates.removeDuplicates(mAllCategoryNames);

    }

    public ArrayList<DataPrograms> getAllData() {
        return allData;
    }

}
