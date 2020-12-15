package com.femsa.requestmanager.DTO.User.Notificaciones;

import java.io.Serializable;

public class NotificacionesData implements Serializable {

    private int idNotificacion;
    private int idEmpleadoSolicitante;
    private int idJefeDirecto;
    private int idFacilitador;
    private int iSolicitud;
    private String tituloNotificacion;
    private boolean status;
    private boolean importante;
    private String texto;
    private String fecha;
    private String hora;
    private int idObjeto;
    private int idPrograma;
    private int usado;
    private int posicionj1;
    private int posicionj2;
    private int turno;
    private int mapa;
    private String nombre;
    private int idRetador;
    private int idEmpleadoRetador;

    public NotificacionesData(int idNotificacion, int idEmpleadoSolicitante, int idJefeDirecto, int idFacilitador, int iSolicitud, String tituloNotificacion, boolean status, boolean importante, String texto, String fecha, String hora, int idObjeto, int idPrograma, int usado, int posicionj1, int posicionj2, int turno, int mapa, String nombre, int idRetador, int idEmpleadoRetador) {
        this.idNotificacion = idNotificacion;
        this.idEmpleadoSolicitante = idEmpleadoSolicitante;
        this.idJefeDirecto = idJefeDirecto;
        this.idFacilitador = idFacilitador;
        this.iSolicitud = iSolicitud;
        this.tituloNotificacion = tituloNotificacion;
        this.status = status;
        this.importante = importante;
        this.texto = texto;
        this.fecha = fecha;
        this.hora = hora;
        this.idObjeto = idObjeto;
        this.idPrograma = idPrograma;
        this.usado = usado;
        this.posicionj1 = posicionj1;
        this.posicionj2 = posicionj2;
        this.turno = turno;
        this.mapa = mapa;
        this.nombre = nombre;
        this.idRetador = idRetador;
        this.idEmpleadoRetador = idEmpleadoRetador;
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public int getIdEmpleadoSolicitante() {
        return idEmpleadoSolicitante;
    }

    public int getIdJefeDirecto() {
        return idJefeDirecto;
    }

    public int getIdFacilitador() {
        return idFacilitador;
    }

    public int getiSolicitud() {
        return iSolicitud;
    }

    public String getTituloNotificacion() {
        return tituloNotificacion;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isImportante() {
        return importante;
    }

    public String getTexto() {
        return texto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public void setIdEmpleadoSolicitante(int idEmpleadoSolicitante) {
        this.idEmpleadoSolicitante = idEmpleadoSolicitante;
    }

    public void setIdJefeDirecto(int idJefeDirecto) {
        this.idJefeDirecto = idJefeDirecto;
    }

    public void setIdFacilitador(int idFacilitador) {
        this.idFacilitador = idFacilitador;
    }

    public void setiSolicitud(int iSolicitud) {
        this.iSolicitud = iSolicitud;
    }

    public void setTituloNotificacion(String tituloNotificacion) {
        this.tituloNotificacion = tituloNotificacion;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setImportante(boolean importante) {
        this.importante = importante;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public int getUsado() {
        return usado;
    }

    public int getPosicionj1() {
        return posicionj1;
    }

    public int getPosicionj2() {
        return posicionj2;
    }

    public int getTurno() {
        return turno;
    }

    public int getMapa() {
        return mapa;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIDRetador() {
        return idRetador;
    }

    public int getIdEmpleadoRetador() {
        return idEmpleadoRetador;
    }
}