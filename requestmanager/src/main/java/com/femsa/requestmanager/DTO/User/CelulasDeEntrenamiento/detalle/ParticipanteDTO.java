package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Insomnia: participantesList[]
 */
public class ParticipanteDTO implements Serializable {

    private static final String ID_EMPLEADO_KEY = "idEmpleado";
    private static final String NOMBRE_PARTICIPANTE_KEY = "nombreParticipante";
    private static final String DESCRIPCION_POSICION_KEY = "descPosicion";
    private static final String SUB_AREA_KEY = "subArea";
    private static final String FOTO_PERFIL_KEY = "fotoPerfil";
    private static final String ID_PAIS_KEY = "idpais";
    private static final String NUMERO_RED_KEY = "numeroRed";
    private static final String ID_EMPLEADO_SUPERIOR = "idEmpleadoSuperior";
    private static final String AREA_KEY = "area";
    private static final String AUTORIZACION_KEY = "autorizacion";

    private int mIdEmpleado;
    private String mNombreParticipante;
    private String mDescripcionPosicion;
    private String mSubarea;
    private String mFotoPerfil;
    private int mIdPais;
    private int mNumeroRed;
    private int mIdEmpleadoSuperior;
    private String mArea;
    private boolean mAutorizacion;

    private String numeroJefeDirecto, nombreJefeDirecto, PosicionJefeDirecto,CorreoJefe;

    public String getNumeroJefeDirecto() {
        return numeroJefeDirecto;
    }

    public void setNumeroJefeDirecto(String numeroJefeDirecto) {
        this.numeroJefeDirecto = numeroJefeDirecto;
    }

    public String getNombreJefeDirecto() {
        return nombreJefeDirecto;
    }

    public void setNombreJefeDirecto(String nombreJefeDirecto) {
        this.nombreJefeDirecto = nombreJefeDirecto;
    }

    public String getPosicionJefeDirecto() {
        return PosicionJefeDirecto;
    }

    public void setPosicionJefeDirecto(String posicionJefeDirecto) {
        PosicionJefeDirecto = posicionJefeDirecto;
    }

    public String getCorreoJefe() {
        return CorreoJefe;
    }

    public void setCorreoJefe(String correoJefe) {
        CorreoJefe = correoJefe;
    }

    public ParticipanteDTO(JSONObject data){
        if (data!=null){
            mIdEmpleado = data.optInt(ID_EMPLEADO_KEY);
            mNombreParticipante = data.optString(NOMBRE_PARTICIPANTE_KEY);
            mDescripcionPosicion = data.optString(DESCRIPCION_POSICION_KEY);
            mSubarea = data.optString(SUB_AREA_KEY);
            mFotoPerfil = data.optString(FOTO_PERFIL_KEY);
            mIdPais = data.optInt(ID_PAIS_KEY);
            mNumeroRed = data.optInt(NUMERO_RED_KEY);
            mIdEmpleadoSuperior = data.optInt(ID_EMPLEADO_SUPERIOR);
            mArea = data.optString(AREA_KEY);
            mAutorizacion = data.optBoolean(AUTORIZACION_KEY);
        }
    }

    public ParticipanteDTO(int idEmpleado, String nombreParticipante, String descripcionPosicion, String subarea, String fotoPerfil, int idPais, int numeroRed, int idEmpleadoSuperior, String area) {
        mIdEmpleado = idEmpleado;
        mNombreParticipante = nombreParticipante;
        mDescripcionPosicion = descripcionPosicion;
        mSubarea = subarea;
        mFotoPerfil = fotoPerfil;
        mIdPais = idPais;
        mNumeroRed = numeroRed;
        mIdEmpleadoSuperior = idEmpleadoSuperior;
        mArea = area;
    }

    public int getIdEmpleado(){
        return mIdEmpleado;
    }

    public String getNombreParticipante() {
        return mNombreParticipante;
    }

    public String getDescripcionPosicion() {
        return mDescripcionPosicion;
    }

    public String getSubarea() {
        return mSubarea;
    }

    public String getFotoPerfil() {
        return mFotoPerfil;
    }

    public int getIdPais() {
        return mIdPais;
    }

    public int getNumeroRed(){
        return mNumeroRed;
    }

    public int getIdEmpleadoSuperior(){
        return mIdEmpleadoSuperior;
    }

    public String getArea() {
        return mArea;
    }

    public boolean isAutorizacion(){
        return mAutorizacion;
    }
}
