package com.femsa.sferea.home.descargas.base.AsynxTaskes;

public class DescargasParams {

    int tipo;
    String nombreArchivo;
    String URLCompleta;

    public DescargasParams(int tipo, String nombreArchivo, String URLCompleta) {
        this.tipo = tipo;
        this.nombreArchivo = nombreArchivo;
        this.URLCompleta = URLCompleta;
    }

    public int getTipo() {
        return tipo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getURLCompleta() {
        return URLCompleta;
    }
}
