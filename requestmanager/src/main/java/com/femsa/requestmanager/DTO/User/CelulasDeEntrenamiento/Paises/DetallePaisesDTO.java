package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetallePaisesDTO {

    private static final String ID_PAIS_KEY = "idPais";
    private static final String PAIS_NOMBRE_KEY = "pais";
    private static final String PAIS_IMG_KEY = "imgPais";
    private static final String LISTADO_PAIS_KEY = "listaPaises";

    private ArrayList<PaisData> mListadoPaises = new ArrayList<>();

    public ArrayList<PaisData> getListadoPaises() {
        return mListadoPaises;
    }

    public DetallePaisesDTO(JSONObject data){
        {
            JSONArray mListado = data.optJSONArray(LISTADO_PAIS_KEY);
            for(int i = 0; i < mListado.length(); i++){
                JSONObject paisActual = mListado.optJSONObject(i);
                mListadoPaises.add(
                        new PaisData(
                                paisActual.optInt(ID_PAIS_KEY),
                                paisActual.optString(PAIS_NOMBRE_KEY),
                                paisActual.optString(PAIS_IMG_KEY)
                        ));
            }
        }
    }

    public static class PaisData
        {
            int idPais;
            String nombrePais;
            String rutaImagen;

            public PaisData(int idPais, String nombrePais, String rutaImagen) {
                this.idPais = idPais;
                this.nombrePais = nombrePais;
                this.rutaImagen = rutaImagen;
            }

            public int getIdPais() {
                return idPais;
            }

            public String getNombrePais() {
                return nombrePais;
            }

            public String getRutaImagen() {
                return rutaImagen;
            }
        }
}
