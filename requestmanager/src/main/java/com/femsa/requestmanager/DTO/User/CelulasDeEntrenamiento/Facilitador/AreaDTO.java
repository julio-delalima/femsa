package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class AreaDTO implements Serializable {

    private static final String ID_AREA_FUNCIONAL_KEY = "idAreasFun";
    private static final String NOMBRE_AREA_FUNCIONAL_KEY = "nombreAreaFunc";
    private static final String ID_IDIOMA_KEY = "dIdioma";
    private static final String AREAS_ARRAY_KEY = "areas";

    private int IdAreaFuncional;
    private String nombreArea;
    private int IdIdioma;
    private ArrayList<AreaDTO> mAreas;

    public AreaDTO(int idAreaFuncional, String nombreArea, int idIdioma) {
        IdAreaFuncional = idAreaFuncional;
        this.nombreArea = nombreArea;
        IdIdioma = idIdioma;
    }

    public AreaDTO (JSONObject object){
        mAreas = new ArrayList<>();
        JSONArray arregloAreas = object.optJSONArray(AREAS_ARRAY_KEY);
        for(int i = 0; i < arregloAreas.length(); i++)
            {
                JSONObject areaActual = arregloAreas.optJSONObject(i);
                IdAreaFuncional = areaActual.optInt(ID_AREA_FUNCIONAL_KEY);
                nombreArea = areaActual.optString(NOMBRE_AREA_FUNCIONAL_KEY);
                IdIdioma = areaActual.optInt(ID_IDIOMA_KEY);
                mAreas.add(new AreaDTO(IdAreaFuncional, nombreArea, IdIdioma));
            }
    }

    public int getIdAreaFuncional() {
        return IdAreaFuncional;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public int getIdIdioma() {
        return IdIdioma;
    }

    public ArrayList<AreaDTO> getAreas() {
        return mAreas;
    }
}
