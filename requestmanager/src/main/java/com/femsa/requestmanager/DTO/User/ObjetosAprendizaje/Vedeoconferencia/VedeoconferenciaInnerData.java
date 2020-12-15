package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Vedeoconferencia;

import org.json.JSONArray;
import org.json.JSONObject;

public class VedeoconferenciaInnerData {

    private static final String VEDEO_OBJECT_KEY = "video";
    private static final String VEDEO_HORA_INICIO_KEY = "horaInicio";
    private static final String VEDEO_HORA_FIN_KEY = "horaFinal";
    private static final String VEDEO_ID_KEY = "idObjeto";
    private static final String VEDEO_FECHA_KEY = "fecha";
    private static final String VEDEO_URL_KEY  = "urlSesion";
    private static final String VEDEO_ACTIVO_KEY = "activo";

    private String fecha, horanicio, horafinal, URL;
    private int id;
    private int mActivo;

    public VedeoconferenciaInnerData(JSONObject data) {
        JSONArray vedeoArray = data.optJSONArray(VEDEO_OBJECT_KEY);
        JSONObject vedeo = vedeoArray.optJSONObject(0);
        fecha = vedeo.optString(VEDEO_FECHA_KEY);
        horafinal = vedeo.optString(VEDEO_HORA_FIN_KEY);
        horanicio = vedeo.optString(VEDEO_HORA_INICIO_KEY);
        URL = vedeo.optString(VEDEO_URL_KEY);
        id = vedeo.optInt(VEDEO_ID_KEY);
        mActivo = vedeo.optInt(VEDEO_ACTIVO_KEY);
    }

    public String getFecha() {
        return fecha;
    }

    public String getHoranicio() {
        return horanicio;
    }

    public String getHorafinal() {
        return horafinal;
    }

    public String getURL() {
        return URL;
    }

    public int getId() {
        return id;
    }

    public int getActivo() {
        return mActivo;
    }
}
