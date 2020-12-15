package com.femsa.requestmanager.DTO.User.MisRecompensas.detalle;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.RecompensaDTO;

import org.json.JSONObject;

public class DetalleRecompensaDTO extends RecompensaDTO {

    private static final String VIGENCIA_KEY = "vigencia";
    private static final String DESCRIPCION_KEY = "decRec";
    private static final String TERMINOS_KEY = "terminosRec";

    private String mVigencia;
    private String mDescripcion;
    private String mTerminos;

    public DetalleRecompensaDTO(JSONObject data) {
        super(data);
        if (data!=null){
            mVigencia = data.optString(VIGENCIA_KEY);
            mDescripcion = data.optString(DESCRIPCION_KEY);
            mTerminos = data.optString(TERMINOS_KEY);
        }
    }

    public String getVigencia() {
        return mVigencia;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public String getTerminos() {
        return mTerminos;
    }
}
