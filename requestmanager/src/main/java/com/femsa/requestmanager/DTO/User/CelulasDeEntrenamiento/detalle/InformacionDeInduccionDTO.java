package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.FechaDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Insomnia: informacionList[]
 */
public class InformacionDeInduccionDTO implements Serializable {

    private static final String NOMBRE_TEMA_KEY = "nombreTema";
    private static final String TEMA_ESPECIFICO_KEY = "temaEspecifico";

    private String mNombreTema;
    private ArrayList<TemaEspecificoDTO> mListaTemasEspecificos;

    public InformacionDeInduccionDTO(JSONObject data){
        if (data!=null){
            mListaTemasEspecificos = new ArrayList<>();
            mNombreTema = data.optString(NOMBRE_TEMA_KEY);
            obtenerListaTemasEspecificos(data.optJSONArray(TEMA_ESPECIFICO_KEY));
        }
    }

    /**
     * <p>Método que crea el listado de temas específicos a partir un arreglo de JSONs.</p>
     * @param array
     */
    private void obtenerListaTemasEspecificos(JSONArray array){
        if (array!=null){
            for (int i=0; i<array.length(); i++){
                mListaTemasEspecificos.add(new TemaEspecificoDTO(array.optJSONObject(i)));
            }
        }
    }


    public String getNombreTema() {
        return mNombreTema;
    }

    public ArrayList<TemaEspecificoDTO> getListaTemasEspecificos() {
        return mListaTemasEspecificos;
    }

}
