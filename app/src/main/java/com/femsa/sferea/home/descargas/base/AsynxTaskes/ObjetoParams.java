package com.femsa.sferea.home.descargas.base.AsynxTaskes;

public class ObjetoParams {
    private int mIDObjeto;
    private String mNombreObjeto;
    private String mdescripcion;
    private String mURIArchivo;
    private String mURIImagen;
    private int mIdPrograma;

    public ObjetoParams(int IDObjeto, String nombreObjeto, String mdescripcion, String URIArchivo, String URIImagen, int idPrograma) {
        mIDObjeto = IDObjeto;
        mNombreObjeto = nombreObjeto;
        this.mdescripcion = mdescripcion;
        mURIArchivo = URIArchivo;
        mURIImagen = URIImagen;
        mIdPrograma = idPrograma;
    }

    public int getIDObjeto() {
        return mIDObjeto;
    }

    public String getNombreObjeto() {
        return mNombreObjeto;
    }

    public String getMdescripcion() {
        return mdescripcion;
    }

    public String getURIArchivo() {
        return mURIArchivo;
    }

    public String getURIImagen() {
        return mURIImagen;
    }

    public int getIdPrograma() {
        return mIdPrograma;
    }
}
