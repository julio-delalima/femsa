package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class FacilitadorInnerData implements Serializable {

    private ArrayList<FacilitadorDTO> mFacilitadorArray;

    private static final String ARRAY_MAIN_KEY = "facilitadores";
    private static final String ID_FACILITADOR_KEY = "idFacilitador";
    private static final String NOMBRE_FACILITADOR_KEY = "nombreFacilitador";
    private static final String AREA_FUNCIONAL_KEY = "areaFuncional";
    private static final String DESC_POSICION_KEY = "descPosicion";
    private static final String CORREO_KEY = "correo";

    public FacilitadorInnerData (JSONObject object) {
        mFacilitadorArray = new ArrayList<>();
        JSONArray mArregloFacilitadores = object.optJSONArray(ARRAY_MAIN_KEY);

        for(int i = 0; i< mArregloFacilitadores.length(); i++)
            {
                try
                    {
                        JSONObject mFacilitadorActual = mArregloFacilitadores.getJSONObject(i);
                        mFacilitadorArray.add(new FacilitadorDTO(
                            mFacilitadorActual.optInt(ID_FACILITADOR_KEY),
                            mFacilitadorActual.optString(NOMBRE_FACILITADOR_KEY),
                            mFacilitadorActual.optString(AREA_FUNCIONAL_KEY),
                            mFacilitadorActual.optString(DESC_POSICION_KEY),
                            mFacilitadorActual.optString(CORREO_KEY)
                        ));
                    }
                catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
            }

    }

    public ArrayList<FacilitadorDTO> getListadoFacilitadores() {
        return mFacilitadorArray;
    }
}