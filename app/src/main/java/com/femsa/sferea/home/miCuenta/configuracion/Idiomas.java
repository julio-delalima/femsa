package com.femsa.sferea.home.miCuenta.configuracion;

import android.content.Context;

public class Idiomas {

    private String mNombreIdioma;

    private int mBandera;

    private int mIdIdioma;

    private String mRutaImagen;

    private String mISO;

    public Idiomas(String nombreIdioma, int bandera, int idPais, String rutaImagen, String ISO) {
        mNombreIdioma = nombreIdioma;
        mBandera = bandera;
        mIdIdioma = idPais;
        mRutaImagen = rutaImagen;
        mISO = ISO;
    }

    public Idiomas(String nombreIdioma, int bandera)
        {
            mNombreIdioma = nombreIdioma;
            mIdIdioma = -1;
            mBandera = bandera;
        }

    public Idiomas(String nombreIdioma, int idPais, String rutaImagen, String ISO) {
        mNombreIdioma = nombreIdioma;
        mIdIdioma = idPais;
        mRutaImagen = rutaImagen;
        mISO = ISO;
    }

    public Idiomas(String nombreIdioma, int bandera, int idPais, String rutaImagen) {
        mNombreIdioma = nombreIdioma;
        mBandera = bandera;
        mIdIdioma = idPais;
        mRutaImagen = rutaImagen;
    }

    public Idiomas(String nombreIdioma, int bandera, int idPais)
    {
        mNombreIdioma = nombreIdioma;
        mIdIdioma = idPais;
        mBandera = bandera;
    }

    public String getISO() {
        return mISO;
    }

    public String getRutaImagen() {
        return mRutaImagen;
    }

    public String getNombreIdioma() {
        return mNombreIdioma;
    }

    public int getBandera() {
        return mBandera;
    }

    public int getIdIdioma() {
        return mIdIdioma;
    }
}
