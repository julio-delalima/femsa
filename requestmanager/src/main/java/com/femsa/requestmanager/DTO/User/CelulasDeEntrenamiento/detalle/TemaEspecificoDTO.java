package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.FechaDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Insomnia: temaEspecifico
 */
public class TemaEspecificoDTO implements Serializable {

    private static final String NOMBRE_TEMA_KEY = "nombreTema"; //Tema específico
    private static final String FOTO_PERFIL = "fotoPerfil";
    private static final String AUTORIZACION_KEY = "autorizacion";
    private static final String NOMBRE_FACILITADOR_KEY = "nombreFacilitador";
    private static final String DESCRIPCION_POSICION_KEY = "descPosicion";
    private static final String SUBTEMA_ESPECIFICO_KEY = "subTemaEspecifico";
    private static final String ID_PAIS_KEY = "idPais";
    private static final String FECHAS_ARRAY_KEY = "fechas";
    private static final String ID_FACILITADOR_KEY = "idFacilitador";

    private String mNombreTema;
    private String mFotoPerfilFacilitador;
    private boolean mAutorizacionFacilitador;
    private String mNombreFacilitador;
    private String mDescripcionPosicion;
    private ArrayList<FechaDTO> mListaFechas;
    private int mIdPais;
    private int mIdFcailitador;
    private ArrayList<SubtemaEspecificoDTO> mListSubtemas;

    public TemaEspecificoDTO(JSONObject data){
        if (data!=null){
            mListSubtemas = new ArrayList<>();
            mNombreTema = data.optString(NOMBRE_TEMA_KEY);
            mFotoPerfilFacilitador = data.optString(FOTO_PERFIL);
            mAutorizacionFacilitador = data.optBoolean(AUTORIZACION_KEY);
            mNombreFacilitador = data.optString(NOMBRE_FACILITADOR_KEY);
            mDescripcionPosicion = data.optString(DESCRIPCION_POSICION_KEY);
            mIdPais = data.optInt(ID_PAIS_KEY);
            mListaFechas = new ArrayList<>();
            obtenerListaSubtemas(data.optJSONArray(SUBTEMA_ESPECIFICO_KEY));
            obtenerListaFechas(data.optJSONArray(FECHAS_ARRAY_KEY));
            mIdFcailitador = data.optInt(ID_FACILITADOR_KEY);
        }
    }

    /**
     * <p>Método que permite obtener el listado de subtemas específicos que pertenezcan a un subterma.</p>
     * @param array Lista de subtemas específicos.
     */
    private void obtenerListaSubtemas(JSONArray array){
        if (array!=null){
            for (int i=0; i<array.length(); i++){
                mListSubtemas.add(new SubtemaEspecificoDTO(array.optJSONObject(i)));
            }
        }
    }


    /**
     * <p>Método que crea el listado de fechas de los temas específicos a partir un arreglo de JSONs.</p>
     * @param array
     */
    private void obtenerListaFechas(JSONArray array){
        if (array!=null){
            if(array.length() == 0)
                {
                    mListaFechas.add(new FechaDTO("", ""));
                }
            for (int i=0; i<array.length(); i++){
                mListaFechas.add(new FechaDTO(array.optJSONObject(i)));
            }
        }
    }

    public String getNombreTema() {
        return mNombreTema;
    }

    public String getFotoPerfilFacilitador(){
        return mFotoPerfilFacilitador;
    }

    public boolean isAutorizacionFacilitador(){
        return mAutorizacionFacilitador;
    }

    public String getNombreFacilitador() {
        return mNombreFacilitador;
    }

    public String getDescripcionPosicion() {
        return mDescripcionPosicion;
    }

    public int getIdPais(){
        return mIdPais;
    }

    public ArrayList<SubtemaEspecificoDTO> getListSubtemas() {
        return mListSubtemas;
    }

    public ArrayList<FechaDTO> getmListaFechas() {
        return mListaFechas;
    }

    public int getIdFcailitador (){
        return mIdFcailitador;
    }
}
