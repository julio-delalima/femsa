package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Insomnia:
 * mostrarHorario
 */
public class DatosDTO implements Serializable {

    private static final String ID_SOLICITUD_KEY = "idSolicitud";
    private static final String ID_FACILITADOR_KEY = "idFacilitador";
    private static final String FECHAS_KEY = "fechas";
    private static final String NOMBRE_TEMA_ESPECIFICO_KEY = "nombreTemaEspecifico";
    private static final String TIPO_KEY = "tipo";
    private static final String TIEMPO_KEY = "tiempo";
    private static final String FECHA_INICIO_KEY = "fechaInicio";
    private static final String FECHA_FIN_KEY = "fechaFin";
    private static final String NOMBRE_TEMA_GENERAL_KEY = "nombreTema";
    private static final String NOMBRE_FACILITADOR_KEY = "nombreFacilitador";
    private static final String INFORMACION_BUSY_SCHEDULE_KEY = "informacion";

    private int mIdSolicitud;
    private int mIdFacilitador;
    private ArrayList<FechaDTO> mListFechas;
    private String mTemaEspecifico;
    private String mTipoSolicitud;
    private String mTiempoSolicitud;
    private String mFechaInicio;
    private String mFechaFin;
    private String mTemaGeneral;
    private String mNombreFacilitador;

    public DatosDTO(JSONObject data){
        if (data!=null){
            mIdSolicitud = data.optInt(ID_SOLICITUD_KEY);
            mIdFacilitador = data.optInt(ID_FACILITADOR_KEY);
            obtenerListaFechas(data.optJSONArray(FECHAS_KEY));
            mTemaEspecifico = data.optString(NOMBRE_TEMA_ESPECIFICO_KEY);
            mTipoSolicitud = data.optString(TIPO_KEY);
            mTiempoSolicitud = data.optString(TIEMPO_KEY);
            mFechaInicio = data.optString(FECHA_INICIO_KEY);
            mFechaFin = data.optString(FECHA_FIN_KEY);
            mTemaGeneral = data.optString(NOMBRE_TEMA_GENERAL_KEY);
            mNombreFacilitador = data.optString(NOMBRE_FACILITADOR_KEY);
        }
    }

    /**
     * <p>Método que permite obtener la lista de fechas que ya se hayan programado.</p>
     * @param array
     */
    private void obtenerListaFechas(JSONArray array){
        if (array!=null){
            mListFechas = new ArrayList<>();
            for (int i=0; i<array.length(); i++){
                mListFechas.add(new FechaDTO(array.optJSONObject(i), array.optJSONObject(i).optJSONObject(INFORMACION_BUSY_SCHEDULE_KEY)));
            }
        }
    }

    public int getIdSolicitud() {
        return mIdSolicitud;
    }

    public int getIdFacilitador() {
        return mIdFacilitador;
    }

    public ArrayList<FechaDTO> getListFechas() {
        return mListFechas;
    }

    public String getTemaEspecifico() {
        return mTemaEspecifico;
    }

    public String getNombreFacilitador(){
        return mNombreFacilitador;
    }

    public String getTipoSolicitud() {
        return mTipoSolicitud;
    }

    public String getTiempoSolicitud() {
        return mTiempoSolicitud;
    }

    public String getFechaInicio() {
        return mFechaInicio;
    }

    public String getFechaFin() {
        return mFechaFin;
    }

    public String getTemaGeneral() {
        return mTemaGeneral;
    }
}
