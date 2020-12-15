package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Insomnia: obtenerListaSolicitudes
 */
public class SolicitudDTO implements Serializable {

    private static final String ID_SOLICITUD_KEY = "idSolicitud";
    private static final String TIPO_SOLICITUD_KEY = "tipoSolicitud";
    private static final String STATUS_SOLICITUD_KEY = "statusSolicitud";
    private static final String STATUS_PROGRAMACION_KEY = "statusProgramacion";
    private static final String STATUS_AUTORIZACION_KEY = "statusAutorizacion";
    private static final String TEMA_GENERAL_KEY = "temaGeneral";
    private static final String ID_EMPLEADO_SOLICITANTE = "idEmpleadoSolicitante";
    private static final String ID_IDIOMA_KEY = "idIdioma";
    private static final String FOTO_PERFIL_KEY = "fotoPerfil";
    private static final String NOMBRE_SOLICITANTE_KEY = "nombreSolicitante";
    private static final String ROL_SOLICITUD_KEY = "rolSolicitud";

    private int mIdSolicitud;
    private String mTipoSolicitud;
    private String mStatusSolicitud;
    private boolean mStatusProgramacion;
    private boolean mStatusAutorizacion;
    private String mTemaGeneral;
    private int mIdEmpleadoSolicitante;
    private int mIdIdioma;
    private String mFotoPerfil;
    private String mNombreSolicitante;
    private String mRolSolicitud;

    public SolicitudDTO(JSONObject data){
        mIdSolicitud = data.optInt(ID_SOLICITUD_KEY);
        mTipoSolicitud = data.optString(TIPO_SOLICITUD_KEY);
        mStatusSolicitud = data.optString(STATUS_SOLICITUD_KEY);
        mStatusProgramacion = data.optBoolean(STATUS_PROGRAMACION_KEY);
        mStatusAutorizacion = data.optBoolean(STATUS_AUTORIZACION_KEY);
        mTemaGeneral = data.optString(TEMA_GENERAL_KEY);
        mIdEmpleadoSolicitante = data.optInt(ID_EMPLEADO_SOLICITANTE);
        mIdIdioma = data.optInt(ID_IDIOMA_KEY);
        mFotoPerfil = data.optString(FOTO_PERFIL_KEY);
        mNombreSolicitante = data.optString(NOMBRE_SOLICITANTE_KEY);
        mRolSolicitud = data.optString(ROL_SOLICITUD_KEY);
    }

    public int getIdSolicitud() {
        return mIdSolicitud;
    }

    public String getTipoSolicitud() {
        return mTipoSolicitud;
    }

    public String getStatusSolicitud() {
        return mStatusSolicitud;
    }

    public boolean isStatusProgramacion() {
        return mStatusProgramacion;
    }

    public boolean isStatusAutorizacion() {
        return mStatusAutorizacion;
    }

    public String getTemaGeneral() {
        return mTemaGeneral;
    }

    public int getIdioma(){
        return mIdIdioma;
    }

    public String getFotoPerfil() {
        return mFotoPerfil;
    }

    public String getNombreSolicitante() {
        return mNombreSolicitante;
    }

    public String getRolSolicitud() {
        return mRolSolicitud;
    }

    public int getIdEmpleadoSolicitante() {
        return mIdEmpleadoSolicitante;
    }
}
