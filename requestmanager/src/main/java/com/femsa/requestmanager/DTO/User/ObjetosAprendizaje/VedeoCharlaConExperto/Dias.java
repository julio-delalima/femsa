package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VedeoCharlaConExperto;

import java.util.ArrayList;

public class Dias {

    private String mFechita;

    private ArrayList<IntervaloHorasSensuales> mListadoHorasDisponiblesParaEseDiaDeLaVedeoCharlaConExperto;

    public Dias(String fechita, ArrayList<IntervaloHorasSensuales> listadoHorasDisponiblesParaEseDiaDeLaVedeoCharlaConExperto) {
        mFechita = fechita;
        mListadoHorasDisponiblesParaEseDiaDeLaVedeoCharlaConExperto = listadoHorasDisponiblesParaEseDiaDeLaVedeoCharlaConExperto;
    }

    public String getFechita() {
        return mFechita;
    }

    public ArrayList<IntervaloHorasSensuales> getListadoHorasDisponiblesParaEseDiaDeLaVedeoCharlaConExperto() {
        return mListadoHorasDisponiblesParaEseDiaDeLaVedeoCharlaConExperto;
    }
}
