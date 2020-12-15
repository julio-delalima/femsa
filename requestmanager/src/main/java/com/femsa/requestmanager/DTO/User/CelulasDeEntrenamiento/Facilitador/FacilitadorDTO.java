package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador;

import java.io.Serializable;

public class FacilitadorDTO implements Serializable {

    private int idFacilitador;
    private String nombreFacilitador;
    private String areaFuncional;
    private String descPosicion;
    private String correo;
    private int idPais;

    private String nombreTema;
    private boolean autorizacion;
    private String fotoPerfil;

    public FacilitadorDTO(int idFacilitador, String nombreFacilitador, String areaFuncional, String descPosicion) {
        this.idFacilitador = idFacilitador;
        this.nombreFacilitador = nombreFacilitador;
        this.areaFuncional = areaFuncional;
        this.descPosicion = descPosicion;
    }

    public FacilitadorDTO(int idFacilitador, String nombreFacilitador, String areaFuncional, String descPosicion, String correo) {
        this.idFacilitador = idFacilitador;
        this.nombreFacilitador = nombreFacilitador;
        this.areaFuncional = areaFuncional;
        this.descPosicion = descPosicion;
        this.correo = correo;
    }

    /**
     * <p>Constructor utilizado en la lista de autorizaciones del detalle de una solicitud.</p>
     * @param nombreFacilitador Nombre del facilitador que impartirá el tema específico.
     * @param descPosicion Posición del facilitador.
     * @param nombreTema Nombre del tema que impartirá el facilitador.
     * @param autorizacion true si el facilitador ya programó el horario.
     * @param fotoPerfil Identificador de la foto de perfil del facilitador.
     */
    public FacilitadorDTO(String nombreFacilitador, String descPosicion, String nombreTema, boolean autorizacion, String fotoPerfil, int idPais){
        this.nombreFacilitador = nombreFacilitador;
        this.descPosicion = descPosicion;
        this.nombreTema = nombreTema;
        this.autorizacion = autorizacion;
        this.fotoPerfil = fotoPerfil;
        this.idPais = idPais;
    }

    public int getIdFacilitador() {
        return idFacilitador;
    }

    public String getNombreFacilitador() {
        return nombreFacilitador;
    }

    public String getAreaFuncional() {
        return areaFuncional;
    }

    public String getDescPosicion() {
        return descPosicion;
    }

    public String getCorreo(){
        return correo;
    }

    public String getNombreTema(){
        return nombreTema;
    }

    public boolean isAutorizacion(){
        return autorizacion;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public int getIdPais(){
        return idPais;
    }

    @Override
    public String toString() {
        return "FacilitadorDTO{" +
                "idFacilitador=" + idFacilitador +
                ", nombreFacilitador='" + nombreFacilitador + '\'' +
                ", areaFuncional='" + areaFuncional + '\'' +
                ", descPosicion='" + descPosicion + '\'' +
                '}';
    }
}
