package com.femsa.sferea.home.descargas.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;

import java.util.List;

import static com.femsa.sferea.home.descargas.base.db.DBNames.COLUMN_ID_PROGRAMA;
import static com.femsa.sferea.home.descargas.base.db.DBNames.TABLE_NAME_OBJETO_APRENDIZAJE;



@Dao
public interface ObjetoDAO {
    String QUERY_SELECT_ALL = "select * from " + TABLE_NAME_OBJETO_APRENDIZAJE;
    String QUERY_SELECT_BY_ID = "select * from " + TABLE_NAME_OBJETO_APRENDIZAJE + " where id= :id";
    String QUERY_SELECT_ALL_BY_PROGRAMA = "select * from " + TABLE_NAME_OBJETO_APRENDIZAJE +" where " + COLUMN_ID_PROGRAMA +" = :id";
    String QUERY_TRUNCATE_TABLE = "DELETE FROM " + TABLE_NAME_OBJETO_APRENDIZAJE;
    String QUERY_COUNT_OBJETOS = "select idObjeto, count(idObjeto) from " + TABLE_NAME_OBJETO_APRENDIZAJE + " where idObjeto = :id" ;
    String QUERY_COUNT_PROGRAMA = "select count(idPrograma) from " + TABLE_NAME_OBJETO_APRENDIZAJE + " where idPrograma = :id" ;

    @Query(QUERY_SELECT_ALL)
    List<ObjetoAprendizajeEntity> getListadoObjetosDescargados();

    @Query(QUERY_SELECT_ALL_BY_PROGRAMA)
    List<ObjetoAprendizajeEntity> getListadoObjetosPorIDPrograma(int id);

    @Insert
    void insertObjeto(ObjetoAprendizajeEntity objeto);

    @Query(QUERY_TRUNCATE_TABLE)
    void clearTableObjetos();

    @Query(QUERY_COUNT_OBJETOS)
    int getcuentaObjetos(int id);

    @Query(QUERY_SELECT_BY_ID)
    ObjetoAprendizajeEntity getObjetoAprendizajeByID(int id);

    @Delete
    void deleteObjeto(ObjetoAprendizajeEntity objeto);

    @Query(QUERY_COUNT_PROGRAMA)
    int cuentaPrograma(int id);
}
