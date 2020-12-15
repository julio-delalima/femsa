package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Insomnia:
 * mostrarHorario
 */
public class FechaDTO implements Serializable {

    private static final String FECHA_FIN_KEY = "fechaFin";
    private static final String FECHA_INICIO_KEY = "fechaInicio";
    private static final String FACILITADOR_KEY = "nombreFacilitador";
    private static final String TEMA_KEY = "nombreTemaEspecifico";

    private String mFechaInicio;
    private String mFechaFin;
    private String mTema;
    private String mFacilitador;

    public FechaDTO(String fechaInicio, String fechaFin){
        mFechaInicio = fechaInicio;
        mFechaFin = fechaFin;
    }

    public FechaDTO(JSONObject data){
        if (data!=null){
            mFechaFin = data.optString(FECHA_FIN_KEY);
            mFechaInicio = data.optString(FECHA_INICIO_KEY);
        }
    }

    public FechaDTO(JSONObject data, JSONObject informacion){
        if (data!=null){
            mFechaFin = data.optString(FECHA_FIN_KEY);
            mFechaInicio = data.optString(FECHA_INICIO_KEY);
            mTema = informacion.optString(TEMA_KEY);
            mFacilitador = informacion.optString(FACILITADOR_KEY);
        }
    }

    public String getmTema() {
        return mTema;
    }

    public String getmFacilitador() {
        return mFacilitador;
    }

    @Override
    public String toString() {
        return "FechaDTO{" +
                "mFechaInicio='" + mFechaInicio + '\'' +
                ", mFechaFin='" + mFechaFin + '\'' +
                '}';
    }

    public String getFechaInicio() {
        return mFechaInicio;
    }

    public Date getFechaFinDate()
    {
        Date fecha = null, fechaInicio;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        SimpleDateFormat nuevoformato = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        try {
            fechaInicio = sdf.parse(mFechaInicio);
            fecha = new Date(nuevoformato.format(fechaInicio));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public Date getFechaInicioDate()
    {
        Date fecha = null, fechaInicio;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        SimpleDateFormat nuevoformato = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        try {
            fechaInicio = sdf.parse(mFechaFin);
            fecha = new Date(nuevoformato.format(fechaInicio));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public String getFechaFin() {
        return mFechaFin;
    }

    public void setFechaInicio(String fechaInicio) {
        mFechaInicio = fechaInicio;
    }

    public void setFechaFin(String fechaFin) {
        mFechaFin = fechaFin;
    }

    public void setTema(String tema) {
        mTema = tema;
    }

    public void setFacilitador(String facilitador) {
        mFacilitador = facilitador;
    }
}
