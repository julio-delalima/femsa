package com.femsa.requestmanager.DTO.User.Recompensa;

import org.json.JSONObject;

public class RecompensaInnerData {

    private static final String CODIGO_MENSAJE_KEY = "numeroMensaje";
    private static final String RECOMPENSA_KEY = "recompensa";

    private int codigoMensaje;
    private String mensaje;

    public RecompensaInnerData(JSONObject data) {

        codigoMensaje = data.optInt(CODIGO_MENSAJE_KEY);
        mensaje = data.optString(RECOMPENSA_KEY);

    }

    public int getCodigoMensaje() {
        return codigoMensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
