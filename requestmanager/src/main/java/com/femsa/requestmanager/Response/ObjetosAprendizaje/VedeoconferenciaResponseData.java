package com.femsa.requestmanager.Response.ObjetosAprendizaje;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Vedeoconferencia.VedeoconferenciaInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class VedeoconferenciaResponseData {

        private static final String VEDEO_MENSAJE = "mensaje";
        private String mError;
        private VedeoconferenciaInnerData mVedeo;
        private String mensaje;

        public VedeoconferenciaResponseData(JSONObject data) throws JSONException {
            // mError = data.optString(ERROR_KEY);
            mensaje = data.optString(VEDEO_MENSAJE);
            if (mensaje.isEmpty()) {
                mVedeo = new VedeoconferenciaInnerData(data);
            }
        }

        public String getMensaje(){
            return mensaje;
        }

        public String getError() {
            return mError;
        }

        public VedeoconferenciaInnerData getVedeo() {
            return mVedeo;
        }

}
