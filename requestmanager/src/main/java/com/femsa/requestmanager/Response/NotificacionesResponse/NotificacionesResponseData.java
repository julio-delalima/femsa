package com.femsa.requestmanager.Response.NotificacionesResponse;

import com.femsa.requestmanager.DTO.User.Notificaciones.NotificacionesInnerData;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificacionesResponseData {

    private String mError;
    private NotificacionesInnerData mNotificacion;

    public NotificacionesResponseData(JSONObject data) throws JSONException {
        // mError = data.optString(ERROR_KEY);
        mNotificacion = new NotificacionesInnerData(data);
    }

    public String getError() {
        return mError;
    }

    public NotificacionesInnerData getNotificaciones() {
            return mNotificacion;
        }
}
