package com.femsa.requestmanager.DTO.User.Notificaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificacionesInnerData {

    public static final String MAIN_ARRAY_KEY = "facilitadores";
    public static final String ID_NOTIFICACION_KEY = "idNotificacion";
    public static final String ID_EMPLEADO_SOLICITANTE_KEY = "idEmpleadoSolicitante";
    public static final String ID_JEFE_DIRECTO_KEY = "idJefeDirecto";
    public static final String ID_FACILITADOR_KEY = "idFacilitador";
    public static final String ID_SOLICITUD_KEY = "idSolicitud";
    public static final String TIITULO_NOTIFICACION_KEY = "tituloNotificacion";
    public static final String STATUS_NOTIFICACION_KEY = "statusNotificacion";
    public static final String IMPORTANTE_KEY = "importante";
    public static final String TEXTO_NOTIFICACION_KEY = "message";
    public static final String FECHA_KEY = "fechaHora";
    public static final String HORA_KEY = "fechaHora";
    public static final String ID_OBJETO_KEY = "idObjeto";
    public static final String ID_PROGRAMA_KEY = "idPrograma";
    public static final String USADO_KEY = "usado";
    public static final String POSJ1_KEY = "posicionj1";
    public static final String POSJ2_KEY = "posicionj2";
    public static final String TURNO_KEY = "turno";
    public static final String MAPA_KEY = "mapa";
    public static final String NOMBRE_KEY = "nombre";
    public static final String ID_RETADOR_KEY = "idEmpleado";
    public static final String ID_EMPLEADO_RETADOR_KEY = "idEmpleadoRetador";


    private ArrayList<NotificacionesData> todasLasNotificaciones;

    public NotificacionesInnerData(JSONObject data) throws JSONException {
        JSONArray facilitadoresData = data.optJSONArray(MAIN_ARRAY_KEY);
        todasLasNotificaciones = new ArrayList<>();
        for(int i = 0; i < facilitadoresData.length(); i++)
        {
            JSONObject subdata = facilitadoresData.getJSONObject(i);
            todasLasNotificaciones.add(new NotificacionesData(
                    subdata.optInt(ID_NOTIFICACION_KEY),
                    subdata.optInt(ID_EMPLEADO_SOLICITANTE_KEY),
                    subdata.optInt(ID_JEFE_DIRECTO_KEY),
                    subdata.optInt(ID_FACILITADOR_KEY),
                    subdata.optInt(ID_SOLICITUD_KEY),
                    subdata.optString(TIITULO_NOTIFICACION_KEY),
                    subdata.optBoolean(STATUS_NOTIFICACION_KEY),
                    subdata.optBoolean(IMPORTANTE_KEY),
                    subdata.optString(TEXTO_NOTIFICACION_KEY),
                    subdata.optString(FECHA_KEY),
                    subdata.optString(HORA_KEY),
                    subdata.optInt(ID_OBJETO_KEY),
                    subdata.optInt(ID_PROGRAMA_KEY),
                    subdata.optInt(USADO_KEY),
                    subdata.optInt(POSJ1_KEY),
                    subdata.optInt(POSJ2_KEY),
                    subdata.optInt(TURNO_KEY),
                    subdata.optInt(MAPA_KEY),
                    subdata.optString(NOMBRE_KEY),
                    subdata.optInt(ID_RETADOR_KEY),
                    subdata.optInt(ID_EMPLEADO_RETADOR_KEY)
                    ));
        }
    }

    public ArrayList<NotificacionesData> getTodasLasNotificaciones() {
        return todasLasNotificaciones;
    }
}
