package com.femsa.requestmanager.DTO.User.Home;

import com.femsa.requestmanager.Utilities.RemoveDuplicates;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProgramsInnerData {

    private static final String MAIN_KEY = "programas";
    private static final String ID_PROGRAM_KEY = "idPrograma";
    private static final String PROGRAM_IMAGE_KEY = "imagenPrograma";
    private static final String IMAGE_TITLE_KEY = "tituloImagen";
    private static final String CATEGORY_NAME_KEY = "nombreCategoria";
    private static final String DESTACADO_NAME_KEY = "destacado";

    private ArrayList<String> allCategories;

    private ArrayList<DataPrograms> allData;

    public ProgramsInnerData(JSONObject data)
    {
        ArrayList<String> allCatsRepeated = new ArrayList<>();
        allCategories = new ArrayList<>();
        allData = new ArrayList<>();
        JSONArray allPrograms = data.optJSONArray(MAIN_KEY);
        for(int i = 0; i < allPrograms.length(); i++)
        {
            JSONObject subdata = allPrograms.optJSONObject(i);
            allCatsRepeated.add(subdata.optString(CATEGORY_NAME_KEY));
            allData.add(new DataPrograms(
                            subdata.optInt(ID_PROGRAM_KEY),
                            subdata.optString(IMAGE_TITLE_KEY),
                            subdata.optString(PROGRAM_IMAGE_KEY),
                            subdata.optString(CATEGORY_NAME_KEY),
                            subdata.optBoolean(DESTACADO_NAME_KEY))
                        );
        }
        allCategories = RemoveDuplicates.removeDuplicates(allCatsRepeated);
    }

    public ArrayList<String> getAllCategories() {
        return allCategories;
    }

    public ArrayList<DataPrograms> getAllData() {
        return allData;
    }
}
