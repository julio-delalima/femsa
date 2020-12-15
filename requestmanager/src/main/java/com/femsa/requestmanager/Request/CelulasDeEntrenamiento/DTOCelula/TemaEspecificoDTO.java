package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.FacilitadorDTO;

import java.io.Serializable;

public class TemaEspecificoDTO implements Serializable {
    private String mTituloTemaEspecifico;
    private String mAreaProceso;
    private String mSubtemaEspecifico;
    private FacilitadorDTO mFacilitador;
    private String mTiempoSugerido;

    public TemaEspecificoDTO(String tituloTemaEspecifico, String areaProceso, String subtemaEspecifico, FacilitadorDTO facilitador, String tiempoSugerido) {
        mTituloTemaEspecifico = tituloTemaEspecifico;
        mAreaProceso = areaProceso;
        mSubtemaEspecifico = subtemaEspecifico;
        mFacilitador = facilitador;
        mTiempoSugerido = tiempoSugerido;
    }

    public String getTituloTemaEspecifico() {
        return mTituloTemaEspecifico;
    }

    public String getAreaProceso() {
        return mAreaProceso;
    }

    public String getSubtemaEspecifico() {
        return mSubtemaEspecifico;
    }

    public FacilitadorDTO getFacilitador() {
        return mFacilitador;
    }

    public String getTiempoSugerido() {
        return mTiempoSugerido;
    }

    @Override
    public String toString() {
        return "TemaEspecificoDTO{" +
                "mTituloTemaEspecifico='" + mTituloTemaEspecifico + '\'' +
                ", mAreaProceso='" + mAreaProceso + '\'' +
                ", mSubtemaEspecifico='" + mSubtemaEspecifico + '\'' +
                ", mFacilitador=" + mFacilitador +
                ", mTiempoSugerido='" + mTiempoSugerido + '\'' +
                '}';
    }
}
