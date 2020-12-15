package com.femsa.requestmanager.DTO.User.Configuracion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class IdiomaInnerData {

    private static final String IDIOMA_ARRAY_KEY =  "idiomas";
    private static final String ID_IDIOMA_KEY = "idIdiomas";
    private static final String NOMBRE_IDIOMA_KEY = "traduccion";
    private static final String ISO_KEY = "iso";
    private static final String ID_IDIOMA_ACTUAL_KEY = "idiomaActual";

    private ArrayList<IdiomaDTO> mListadoIdiomas;

    public IdiomaInnerData(JSONObject data) {
            mListadoIdiomas = new ArrayList<>();
            JSONArray mListadoIdiomasArray = data.optJSONArray(IDIOMA_ARRAY_KEY);
            for(int i = 0; i < mListadoIdiomasArray.length(); i++)
                {
                    JSONObject idiomaActual = mListadoIdiomasArray.optJSONObject(i);
                    mListadoIdiomas.add(new IdiomaDTO(
                        idiomaActual.optInt(ID_IDIOMA_KEY),
                        idiomaActual.optString(ISO_KEY),
                        idiomaActual.optString(NOMBRE_IDIOMA_KEY),
                        idiomaActual.optInt(ID_IDIOMA_ACTUAL_KEY)
                    ));
                }
        }

    public ArrayList<IdiomaDTO> getListadoIdiomas() {
        return mListadoIdiomas;
    }
}
