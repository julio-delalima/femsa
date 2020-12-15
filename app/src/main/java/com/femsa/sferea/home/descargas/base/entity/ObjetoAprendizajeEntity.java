package com.femsa.sferea.home.descargas.base.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static com.femsa.sferea.home.descargas.base.db.DBNames.COLUMN_ID_PROGRAMA;
import static com.femsa.sferea.home.descargas.base.db.DBNames.TABLE_NAME_OBJETO_APRENDIZAJE;

@Entity(tableName = TABLE_NAME_OBJETO_APRENDIZAJE)
public class ObjetoAprendizajeEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "idObjeto")
    private int idObjeto;
    @ColumnInfo(name = "nombreObjeto")
    private String nombreObjeto;
    @ColumnInfo(name = "descripcion")
    private String descripcion;
    @ColumnInfo(name = "rutaImagen")
    private String rutaImagen;
    @ColumnInfo(name = "rutaArchivo")
    private String rutaArchivo;
    @ColumnInfo(name = "tiempoEstimado")
    private String tiempoEstimado;
    @ColumnInfo(name = "corcholatas")
    private int corcholatas;
    @ColumnInfo(name = "like")
    private boolean like;
    @ColumnInfo(name = "tipo")
    private int tipo;
    @ColumnInfo(name = "completado")
    private boolean completado;
    @ColumnInfo(name = COLUMN_ID_PROGRAMA)
    private int idPrograma;

    @Ignore
    public ObjetoAprendizajeEntity(int id, int idObjeto, String nombreObjeto, String descripcion, String rutaImagen, String rutaArchivo, String tiempoEstimado, int corcholatas, boolean like, int tipo, boolean completado, int idPrograma) {
        this.id = id;
        this.idObjeto = idObjeto;
        this.nombreObjeto = nombreObjeto;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.rutaArchivo = rutaArchivo;
        this.tiempoEstimado = tiempoEstimado;
        this.corcholatas = corcholatas;
        this.like = like;
        this.tipo = tipo;
        this.completado = completado;
        this.idPrograma = idPrograma;
    }

    public ObjetoAprendizajeEntity(int idObjeto, String nombreObjeto, String descripcion, String rutaImagen, String rutaArchivo, String tiempoEstimado, int corcholatas, boolean like, int tipo, boolean completado, int idPrograma) {
        this.idObjeto = idObjeto;
        this.nombreObjeto = nombreObjeto;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.rutaArchivo = rutaArchivo;
        this.tiempoEstimado = tiempoEstimado;
        this.corcholatas = corcholatas;
        this.like = like;
        this.tipo = tipo;
        this.completado = completado;
        this.idPrograma = idPrograma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(int idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(String tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public int getCorcholatas() {
        return corcholatas;
    }

    public void setCorcholatas(int corcholatas) {
        this.corcholatas = corcholatas;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }
}
