package com.femsa.requestmanager.DTO.User.Configuracion;

public class IdiomaDTO {

    private int mIDIdioma;
    private String mISO;
    private String mNombreIdioma;
    private int mIDIdiomaActual;

    public IdiomaDTO(int IDIdioma, String ISO, String nombreIdioma, int IDIdiomaActual) {
        mIDIdioma = IDIdioma;
        mISO = ISO;
        mNombreIdioma = nombreIdioma;
        mIDIdiomaActual = IDIdiomaActual;
    }

    public int getIDIdioma() {
        return mIDIdioma;
    }

    public String getISO() {
        return mISO;
    }

    public String getNombreIdioma() {
        return mNombreIdioma;
    }

    public int getIDIdiomaActual() {
        return mIDIdiomaActual;
    }
}
