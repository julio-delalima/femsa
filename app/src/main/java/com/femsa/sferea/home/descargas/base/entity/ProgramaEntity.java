package com.femsa.sferea.home.descargas.base.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.femsa.sferea.home.descargas.base.db.DBNames.COLUMN_ID_PROGRAMA;
import static com.femsa.sferea.home.descargas.base.db.DBNames.TABLE_NAME_PROGRAMA;

@Entity(tableName = TABLE_NAME_PROGRAMA)
public class ProgramaEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = COLUMN_ID_PROGRAMA)
    private int idPrograma;
    @ColumnInfo(name = "tituloPrograma")
    private String tituloPrograma;

    @Ignore
    public ProgramaEntity(int id, int idPrograma, String tituloPrograma) {
        this.id = id;
        this.idPrograma = idPrograma;
        this.tituloPrograma = tituloPrograma;
    }

    public ProgramaEntity(int idPrograma, String tituloPrograma) {
        this.idPrograma = idPrograma;
        this.tituloPrograma = tituloPrograma;
    }

    public int getId() {
        return id;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public String getTituloPrograma() {
        return tituloPrograma;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    public void setTituloPrograma(String tituloPrograma) {
        this.tituloPrograma = tituloPrograma;
    }
}
