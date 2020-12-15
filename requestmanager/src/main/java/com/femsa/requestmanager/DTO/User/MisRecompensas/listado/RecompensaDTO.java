package com.femsa.requestmanager.DTO.User.MisRecompensas.listado;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * <p>Archivo que representa un elemento en el listado de recompensas.</p>
 */
public class RecompensaDTO implements Serializable {

    private static final String ID_RECOMPENSAS_KEY = "idRecompensas";
    private static final String TITULO_RECOMPENSA_KEY = "tituloRec";
    private static final String VALOR_RECOMPENSA_KEY = "valorRec";
    private static final String NOMBRE_IMAGEN_RECOMPENSA = "nombreImgRec";

    private int mIdRecompensa;
    private String mTituloRecompensa;
    private int mValorRecompensa;
    private String mNombreImagenRecompensa;

    public RecompensaDTO(JSONObject data){
        if (data!=null){
            mIdRecompensa = data.optInt(ID_RECOMPENSAS_KEY);
            mTituloRecompensa = data.optString(TITULO_RECOMPENSA_KEY);
            mValorRecompensa = data.optInt(VALOR_RECOMPENSA_KEY);
            mNombreImagenRecompensa = data.optString(NOMBRE_IMAGEN_RECOMPENSA);
        }
    }

    public int getIdRecompensa() {
        return mIdRecompensa;
    }

    public String getTituloRecompensa() {
        return mTituloRecompensa;
    }

    public int getValorRecompensa() {
        return mValorRecompensa;
    }

    public String getNombreImagenRecompensa() {
        return mNombreImagenRecompensa;
    }
}
