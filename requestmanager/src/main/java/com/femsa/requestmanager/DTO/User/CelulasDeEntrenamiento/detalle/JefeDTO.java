package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class JefeDTO implements Serializable {

    private static final String PAIS_KEY = "pais";
    private static final String NOMBRE_JEFE_KEY = "nombreJefe";
    private static final String FOTO_PERFIL_KEY = "fotoPerfil";
    private static final String POSICION_KEY = "posicion";
    private static final String AUTORIZACION_KEY = "autorizacionJefe";
    private static final String COLABORADORES_KEY = "colaboradores";
    private static final String NUMERO_EMPLEADO_SUPERIOR_KEY = "numeroEmpleadoSuperior";
    private static final String CORREO_JEFE_KEY = "correoJefe";

    private String mPais;
    private String mNombreJefe;
    private String mFotoPerfil;
    private String mPosicion;
    private boolean mAutorizacion;
    private ArrayList<ColaboradorDTO> mListColaboradores;
    private String mNumeroEmpleadoSuperior;
    private String mCorreoJefe;
    private int mIDPais;
    private int tipoAdmin = -1;
    public static final int PAIS_RECEPTOR = 0;
    public static final int PAIS_SOLICITANTE = 1;

    /**
     * Constructor utilizado para agregar un administrador de pais en la lista de jefes.
     * */
    public JefeDTO(String nombreJefe, String fotoPerfil, String posicion, boolean autorizacion, int IDPais, int admin) {
        mNombreJefe = nombreJefe;
        mFotoPerfil = fotoPerfil;
        mPosicion = posicion;
        mAutorizacion = autorizacion;
        mIDPais = IDPais;
        tipoAdmin = admin;
    }

    public JefeDTO(JSONObject data){
        if (data!=null){
            mPais = data.optString(PAIS_KEY);
            mNombreJefe = data.optString(NOMBRE_JEFE_KEY);
            mFotoPerfil = data.optString(FOTO_PERFIL_KEY);
            mPosicion = data.optString(POSICION_KEY);
            mAutorizacion = data.optBoolean(AUTORIZACION_KEY);
            obtenerListaColaboradores(data.optJSONArray(COLABORADORES_KEY));
            mNumeroEmpleadoSuperior = data.optString(NUMERO_EMPLEADO_SUPERIOR_KEY);
            mCorreoJefe = data.optString(CORREO_JEFE_KEY);
            tipoAdmin = -1;
        }
    }

    /**
     * <p>Método que permite crear la lista de colaboradores a partir de un arreglo.</p>
     * @param array Arreglo que contiene la lista de colaboradores.
     */
    private void obtenerListaColaboradores(JSONArray array){
        if (array!=null){
            mListColaboradores = new ArrayList<>();
            for (int i=0; i<array.length(); i++){
                mListColaboradores.add(new ColaboradorDTO(array.optJSONObject(i)));
            }
        }
    }

    public String getPais() {
        return mPais;
    }

    public String getNombreJefe() {
        return mNombreJefe;
    }

    public String getFotoPerfil() {
        return mFotoPerfil;
    }

    public String getPosicion() {
        return mPosicion;
    }

    public boolean getAutorizacion(){
        return mAutorizacion;
    }

    public ArrayList<ColaboradorDTO> getListColaboradores() {
        return mListColaboradores;
    }

    public String getNumeroEmpleadoSuperior(){
        return mNumeroEmpleadoSuperior;
    }

    public String getCorreoJefe(){
        return mCorreoJefe;
    }

    public int getIDPais() {
        return mIDPais;
    }

    public int getTipoAdmin() {
        return tipoAdmin;
    }
}
