package com.femsa.sferea.home.descargas.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.femsa.sferea.home.descargas.base.entity.ProgramaEntity;

import java.util.List;

import static com.femsa.sferea.home.descargas.base.db.DBNames.TABLE_NAME_PROGRAMA;

@Dao
public interface ProgramaDAO {
    String QUERY_SELECT_ALL = "select * from " + TABLE_NAME_PROGRAMA;
    String QUERY_SELECT_COUNT = "select idPrograma, count(idPrograma) from " + TABLE_NAME_PROGRAMA + " where idPrograma = :id";
    String QUERY_TRUNCATE_TABLE = "delete from " + TABLE_NAME_PROGRAMA;
    String QUERY_DELETE_BY_ID = "delete from " + TABLE_NAME_PROGRAMA + " where idPrograma = :id";

    @Query(QUERY_SELECT_ALL)
    List<ProgramaEntity> getListadoProgramas();

    @Query(QUERY_SELECT_COUNT)
    int getCuentaRegistros(int id);

    @Query(QUERY_TRUNCATE_TABLE)
    void truncateTableProgramas();

    @Insert
    void insertPrograma(ProgramaEntity mPrograma);

    @Delete
    void deletePrograma(ProgramaEntity programa);

    @Query(QUERY_DELETE_BY_ID)
    void deleteProgramaById(int id);
}
