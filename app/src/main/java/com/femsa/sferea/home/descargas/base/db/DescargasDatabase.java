package com.femsa.sferea.home.descargas.base.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.femsa.sferea.home.descargas.base.dao.ObjetoDAO;
import com.femsa.sferea.home.descargas.base.dao.ProgramaDAO;
import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;
import com.femsa.sferea.home.descargas.base.entity.ProgramaEntity;

import static com.femsa.sferea.home.descargas.base.db.DBNames.DB_NAME;

@Database(
        entities = {ObjetoAprendizajeEntity.class, ProgramaEntity.class},
        exportSchema = false,
        version = 2
)
public abstract class DescargasDatabase extends RoomDatabase {


    private static DescargasDatabase mInstanciaBD;


    public static synchronized DescargasDatabase getInstance(Context context){
        if(mInstanciaBD == null){
            mInstanciaBD = Room.
                    databaseBuilder(context.getApplicationContext(), DescargasDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstanciaBD;
    }


    public abstract ObjetoDAO mObjetoDAO();

    public abstract ProgramaDAO mProgramaDAO();
}
