package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO;

import java.io.Serializable;
import java.util.ArrayList;

public class CelulaDTO implements Serializable {

    //Celula Parte Dos
    private String mFechadeSolicitud;
    private ArrayList<ParticipanteDTO> mListadoParticipantes;

    //Célula Parte Tres
    private String mTemaGeneral;
    private String mPaisSolicitante;
    private String mPaisReceptor;
    private int mPaisSolicitanteId;
    private int mPaisReceptorId;
    private ArrayList<TemaEspecificoDTO> mListadoTemasEspecificos;

    //Fechas
    private String mFechaInicio;
    private String mFechaFin;

    //Banderas
    private String mUsuarioBandera;
    private String mPaisSolicitanteBandera;
    private String mPaisReceptorBandera;

    public CelulaDTO(String fechadeSolicitud, ArrayList<ParticipanteDTO> listadoParticipantes, String temaGeneral, String paisSolicitante, String paisReceptor, int paisSolicitanteId, int paisReceptorId, ArrayList<TemaEspecificoDTO> listadoTemasEspecificos, String fechaInicio, String fechaFin) {
        mFechadeSolicitud = fechadeSolicitud;
        mListadoParticipantes = listadoParticipantes;
        mTemaGeneral = temaGeneral;
        mPaisSolicitante = paisSolicitante;
        mPaisReceptor = paisReceptor;
        mPaisSolicitanteId = paisSolicitanteId;
        mPaisReceptorId = paisReceptorId;
        mListadoTemasEspecificos = listadoTemasEspecificos;
        mFechaInicio = fechaInicio;
        mFechaFin = fechaFin;
    }

    public CelulaDTO(CelulaDTO c) {
        mFechadeSolicitud = c.getFechadeSolicitud();
        mListadoParticipantes = c.getListadoParticipantes();
        mTemaGeneral = c.mTemaGeneral;
        mPaisSolicitante = c.getPaisSolicitante();
        mPaisReceptor = c.mPaisReceptor;
        mPaisSolicitanteId = c.getPaisSolicitanteId();
        mPaisReceptorId = c.getPaisReceptorId();
        mListadoTemasEspecificos = c.mListadoTemasEspecificos;
        mFechaInicio = c.getFechaInicio();
        mFechaFin = c.mFechaFin;
        mPaisReceptorBandera = c.getPaisReceptorBandera();
        mPaisSolicitanteBandera = c.getPaisSolicitanteBandera();
    }

    public CelulaDTO() {
    }

    public String getFechadeSolicitud() {
        return mFechadeSolicitud;
    }

    public ArrayList<ParticipanteDTO> getListadoParticipantes() {
        return mListadoParticipantes;
    }

    public String getTemaGeneral() {
        return mTemaGeneral;
    }

    public String getPaisSolicitante() {
        return mPaisSolicitante;
    }

    public ArrayList<TemaEspecificoDTO> getListadoTemasEspecificos() {
        return mListadoTemasEspecificos;
    }

    public void setTemaGeneral(String temaGeneral) {
        mTemaGeneral = temaGeneral;
    }

    public void setPaisSolicitante(String paisSolicitante) {
        mPaisSolicitante = paisSolicitante;
    }

    public void setListadoTemasEspecificos(ArrayList<TemaEspecificoDTO> listadoTemasEspecificos) {
        mListadoTemasEspecificos = listadoTemasEspecificos;
    }

    public void setPaisReceptor(String paisReceptor) {
        mPaisReceptor = paisReceptor;
    }

    public void setFechadeSolicitud(String fechadeSolicitud) {
        mFechadeSolicitud = fechadeSolicitud;
    }

    public void setListadoParticipantes(ArrayList<ParticipanteDTO> listadoParticipantes) {
        mListadoParticipantes = listadoParticipantes;
    }

    public String getPaisReceptor() {
        return mPaisReceptor;
    }

    public String getFechaInicio() {
        return mFechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        mFechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return mFechaFin;
    }

    public void setFechaFin(String fechaFin) {
        mFechaFin = fechaFin;
    }

    @Override
    public String toString() {
        return "CelulaDTO{" +
                "mFechadeSolicitud='" + mFechadeSolicitud + '\'' +
                ", mListadoParticipantes=" + mListadoParticipantes +
                ", mTemaGeneral='" + mTemaGeneral + '\'' +
                ", mPaisSolicitante='" + mPaisSolicitante + '\'' +
                ", mPaisReceptor='" + mPaisReceptor + '\'' +
                ", mListadoTemasEspecificos=" + mListadoTemasEspecificos +
                '}';
    }

    public int getPaisSolicitanteId() {
        return mPaisSolicitanteId;
    }

    public int getPaisReceptorId() {
        return mPaisReceptorId;
    }

    public void setPaisSolicitanteId(int paisSolicitanteId) {
        mPaisSolicitanteId = paisSolicitanteId;
    }

    public void setPaisReceptorId(int paisReceptorId) {
        mPaisReceptorId = paisReceptorId;
    }

    public String getUsuarioBandera() {
        return mUsuarioBandera;
    }

    public void setUsuarioBandera(String usuarioBandera) {
        mUsuarioBandera = usuarioBandera;
    }

    public String getPaisSolicitanteBandera() {
        return mPaisSolicitanteBandera;
    }

    public void setPaisSolicitanteBandera(String paisSolicitanteBandera) {
        mPaisSolicitanteBandera = paisSolicitanteBandera;
    }

    public String getPaisReceptorBandera() {
        return mPaisReceptorBandera;
    }

    public void setPaisReceptorBandera(String paisReceptorBandera) {
        mPaisReceptorBandera = paisReceptorBandera;
    }
}
