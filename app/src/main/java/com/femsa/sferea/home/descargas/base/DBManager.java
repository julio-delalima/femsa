package com.femsa.sferea.home.descargas.base;

import android.content.Context;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
import com.femsa.sferea.home.descargas.base.db.DescargasDatabase;
import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;
import com.femsa.sferea.home.descargas.base.entity.ProgramaEntity;

import java.util.List;

public class DBManager {

    DescargasDatabase mDatabase;

    public DBManager(Context mContext) {
        mDatabase = DescargasDatabase.getInstance(mContext);
    }


    public static class ObjetoAprendizaje extends DBManager{
        public ObjetoAprendizaje(Context mContext) {
            super(mContext);
        }

        //Variable a usar para obtener información de los objetos de aprendizaje a descargar
        private ObjectDetailResponseData mData;


        public void setmData(ObjectDetailResponseData mData) {
            this.mData = mData;
        }

        /**
         * <p>Retorna el listado de objetos que han sido guardados en la BD</p>
         * @return todos los ObjetoAprendizajeEntity que están dentro de la BD.
         * */
        public List<ObjetoAprendizajeEntity> getListadoObjetosDescargados(){
            return mDatabase.mObjetoDAO().getListadoObjetosDescargados();
        }

        /**
         * <p>Regresa el ObjetoAprendizajeEntity de la BD que tiene el idObjeto = id</p>
         * @param id el idObjeto que usaremos para buscar el ObjetoAprendizajeEntity asociado.
         * @return ObjetoAprendizajeEntity que cumple la condición idObjeto = id;
         * */
        public ObjetoAprendizajeEntity getObjetoByID(int id){
            return mDatabase.mObjetoDAO().getObjetoAprendizajeByID(id);
        }

        /**
         * <p>Inserta dentro de la BD un ObjetoAprendizajeEntity</p>
         * @param entity el ObjetoAprendizajeEntity que se va a guardar.
         * */
        void insertObjeto(ObjetoAprendizajeEntity entity){
            mDatabase.mObjetoDAO().insertObjeto(entity);
        }

        /**
         * <p>Trae el listado de Programas guardados en la BD</p>
         * @return lista de ProgramaEntity guardados en la BD;
         * */
        public List<ProgramaEntity> getListadoProgramas(){
            return mDatabase.mProgramaDAO().getListadoProgramas();
        }

        /**
         * <p>Inserta dentro de la BD un programa</p>
         * @param programa la entidad del Programa que se va a insertar.
         * */
        void insertPrograma(ProgramaEntity programa){
            mDatabase.mProgramaDAO().insertPrograma(programa);
        }

        /**
         * <p>Regresa la cantidad de programas en la BD que tienen ese mismo ID</p>
         * @param idPrograma id del programa que queremoz conocer su cantidad de registros.
         * @return cantidad de programas con el mismo idPrograma.
         * */
        private int getCuentaRegistros(int idPrograma){
            return mDatabase.mProgramaDAO().getCuentaRegistros(idPrograma);
        }

        /**
         * <p>Regresa la cantidad de objetos de aprendizaje en la BD que tienen ese mismo ID</p>
         * @param idObjeto id del objeto que queremoz conocer su cantidad de registros.
         * @return cantidad de objetos con el mismo idObjeto.
         * */
        private int getCuentaObjetosAp(int idObjeto){
            return mDatabase.mObjetoDAO().getcuentaObjetos(idObjeto);
        }

        /**
         * <p>Método para vaciar el contenido de todas las tablas construidas.</p>
         * */
        public void dropTables(){
            //mDatabase.mObjetoDAO().clearTableObjetos();
            //mDatabase.mProgramaDAO().truncateTableProgramas();
            mDatabase.clearAllTables();
        }

        public int cuentaProgramasPorId(int id){
            return mDatabase.mObjetoDAO().cuentaPrograma(id);
        }


        public void deleteObjeto(ObjetoAprendizajeEntity obj){
            mDatabase.mObjetoDAO().deleteObjeto(obj);
        }

        public void deletePrograma(ProgramaEntity prog){
            mDatabase.mProgramaDAO().deletePrograma(prog);
        }

        public void deleteProgramaById(int id){
            mDatabase.mProgramaDAO().deleteProgramaById(id);
        }

        /**
         * <p> Método ara insertas un objeto y un programa en la BD, reciciendo la ubicación de las descargas,
         * además de que un objeto solo s epued eguardar una vez, pidoendo haber n objetos
         * y un programa igual solo se puede registrar una vez. </p>
         * @param rutaArchivo ubicación dentro de la memoria donde se guardó el archivo.
         * @param rutaImagen ubicación dentro de la memoria donde se guard+o la imagen del preview del objeto.
         * @param mNombrePrograma nombre del programa al que está asociado dicho objeto.
         * */
        public void insertarObjetoEnBD(String rutaImagen, String rutaArchivo, String mNombrePrograma){
            if(mData != null){
                if(getCuentaObjetosAp(mData.getCategories().getmIdObject()) < 1){
                    insertObjeto(
                            new ObjetoAprendizajeEntity(
                                    mData.getCategories().getmIdObject(),
                                    mData.getCategories().getmObjectName(),
                                    mData.getCategories().getmDetailContent(),
                                    rutaImagen,
                                    rutaArchivo,
                                    mData.getCategories().getmEstimatedTime(),
                                    mData.getCategories().getmBonus(),
                                    mData.getCategories().getmLike() == 1,
                                    Integer.parseInt(mData.getCategories().getmType()),
                                    mData.getCategories().ismStatusBonus(),
                                    mData.getCategories().getmIdProgram()
                            )
                    );
                }
                if(getCuentaRegistros(mData.getCategories().getmIdProgram()) < 1){
                    insertPrograma(
                            new ProgramaEntity(
                                    mData.getCategories().getmIdProgram(),
                                    mNombrePrograma
                            )
                    );
                }
            }


        }
    }


}
