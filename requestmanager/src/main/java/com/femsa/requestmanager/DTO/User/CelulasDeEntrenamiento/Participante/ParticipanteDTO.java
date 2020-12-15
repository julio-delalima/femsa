package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante;

import org.json.JSONObject;

import java.io.Serializable;

public class ParticipanteDTO implements Serializable {

    private static final String ID_PARTICIPANTE_KEY = "idEmpleado";
    private static final String NOMBRE_KEY = "nombre";
    private static final String NUMERO_EMPLEADO_KEY = "numEmpleado";
    private static final String PUESTO_KEY = "descPosicion";
    private static final String AREA_FUNCIONAL_KEY = "descArFun";
    private static final String SUB_AREA_PROCESO_KEY = "descNivContri";  /** T E M P O R A L*/
    private static final String FOTO_PERFIL_KEY = "fotoPerfil";
    private static final String NUMERO_EMPLEDO_JEFE_KEY = "numPersonalSuperior";
    private static final String NOMBRE_EMPLEADO_JEFE_KEY = "nombrePersonalSuperior";
    private static final String POSICION_JEFE_KEY = "descPosicionSuperior";
    private static final String CORREO_JEFE_KEY = "correoJefe";
    private static final String ID_PAIS_KEY = "idPais";

    private int idParticipante;
    private String NombreParticipante;
    private int NumeroParticipante;
    private String PuestoParticipante;
    private String AreaFuncionalParticipante;
    private String SubAreaProcesoParticipante;
    private int NumeroJefeParticipante;
    private String NombreJefeParticipante;
    private String PosicionJefeParticipante;
    private String CorreoJefeParticipante;
    private String mFotoPerfil;
    private int mIDPais;
    private String mNombrePais;

    public ParticipanteDTO(JSONObject objeto){
        idParticipante = objeto.optInt(ID_PARTICIPANTE_KEY);
        NombreParticipante = objeto.optString(NOMBRE_KEY);
        NumeroParticipante = objeto.optInt(NUMERO_EMPLEADO_KEY);
        PuestoParticipante = objeto.optString(PUESTO_KEY);
        AreaFuncionalParticipante = objeto.optString(AREA_FUNCIONAL_KEY);
        SubAreaProcesoParticipante = objeto.optString(SUB_AREA_PROCESO_KEY);
        NumeroJefeParticipante = objeto.optInt(NUMERO_EMPLEDO_JEFE_KEY);
        NombreJefeParticipante = objeto.optString(NOMBRE_EMPLEADO_JEFE_KEY);
        PosicionJefeParticipante = objeto.optString(POSICION_JEFE_KEY);
        CorreoJefeParticipante = objeto.optString(CORREO_JEFE_KEY);
        mFotoPerfil = objeto.optString(FOTO_PERFIL_KEY);
        mIDPais = objeto.optInt(ID_PAIS_KEY);
    }

    public ParticipanteDTO(int idParticipante, String nombreParticipante, String areaFuncionalParticipante, String fotoPerfil, String nombrePais) {
        this.idParticipante = idParticipante;
        NombreParticipante = nombreParticipante;
        AreaFuncionalParticipante = areaFuncionalParticipante;
        mFotoPerfil = fotoPerfil;
        mNombrePais = nombrePais;
    }

    public String getNombreParticipante() {
        return NombreParticipante;
    }

    public String getNombrePais() {
        return mNombrePais;
    }

    public int getNumeroParticipante() {
        return NumeroParticipante;
    }

    public String getPuestoParticipante() {
        return PuestoParticipante;
    }

    public String getAreaFuncionalParticipante() {
        return AreaFuncionalParticipante;
    }

    public int getNumeroJefeParticipante() {
        return NumeroJefeParticipante;
    }

    public String getNombreJefeParticipante() {
        return NombreJefeParticipante;
    }

    public String getPosicionJefeParticipante() {
        return PosicionJefeParticipante;
    }

    public String getCorreoJefeParticipante() {
        return CorreoJefeParticipante;
    }

    public String getmFotoPerfil() {
        return mFotoPerfil;
    }

    public String getSubAreaProcesoParticipante() {
        return SubAreaProcesoParticipante;
    }

    public String getFotoPerfil() {
        return mFotoPerfil;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public ParticipanteDTO(int idParticipante, String nombreParticipante, int numeroParticipante, String puestoParticipante, String areaFuncionalParticipante, String subAreaProcesoParticipante, int numeroJefeParticipante, String nombreJefeParticipante, String posicionJefeParticipante, String correoJefeParticipante, String fotoPerfil) {
        this.idParticipante = idParticipante;
        NombreParticipante = nombreParticipante;
        NumeroParticipante = numeroParticipante;
        PuestoParticipante = puestoParticipante;
        AreaFuncionalParticipante = areaFuncionalParticipante;
        SubAreaProcesoParticipante = subAreaProcesoParticipante;
        NumeroJefeParticipante = numeroJefeParticipante;
        NombreJefeParticipante = nombreJefeParticipante;
        PosicionJefeParticipante = posicionJefeParticipante;
        CorreoJefeParticipante = correoJefeParticipante;
        mFotoPerfil = fotoPerfil;
    }

    public ParticipanteDTO(int idParticipante, String nombreParticipante, int numeroParticipante, String puestoParticipante, String areaFuncionalParticipante, String subAreaProcesoParticipante, int numeroJefeParticipante, String nombreJefeParticipante, String posicionJefeParticipante, String correoJefeParticipante, String fotoPerfil, int IDPais) {
        this.idParticipante = idParticipante;
        NombreParticipante = nombreParticipante;
        NumeroParticipante = numeroParticipante;
        PuestoParticipante = puestoParticipante;
        AreaFuncionalParticipante = areaFuncionalParticipante;
        SubAreaProcesoParticipante = subAreaProcesoParticipante;
        NumeroJefeParticipante = numeroJefeParticipante;
        NombreJefeParticipante = nombreJefeParticipante;
        PosicionJefeParticipante = posicionJefeParticipante;
        CorreoJefeParticipante = correoJefeParticipante;
        mFotoPerfil = fotoPerfil;
        mIDPais = IDPais;
    }

    @Override
    public String toString() {
        return "ParticipanteDTO{" +
                "idParticipante=" + idParticipante +
                ", NombreParticipante='" + NombreParticipante + '\'' +
                ", NumeroParticipante=" + NumeroParticipante +
                ", PuestoParticipante='" + PuestoParticipante + '\'' +
                ", AreaFuncionalParticipante='" + AreaFuncionalParticipante + '\'' +
                ", SubAreaProcesoParticipante='" + SubAreaProcesoParticipante + '\'' +
                ", NumeroJefeParticipante=" + NumeroJefeParticipante +
                ", NombreJefeParticipante='" + NombreJefeParticipante + '\'' +
                ", PosicionJefeParticipante='" + PosicionJefeParticipante + '\'' +
                ", CorreoJefeParticipante='" + CorreoJefeParticipante + '\'' +
                ", mFotoPerfil='" + mFotoPerfil + '\'' +
                '}';
    }

    public int getIDPais() {
        return mIDPais;
    }
}
