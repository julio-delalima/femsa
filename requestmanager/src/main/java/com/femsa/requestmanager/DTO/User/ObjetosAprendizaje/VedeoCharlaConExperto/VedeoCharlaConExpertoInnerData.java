package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VedeoCharlaConExperto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VedeoCharlaConExpertoInnerData {

    private static final String VEDEO_ARRAY_KEY = "video";
    private static final String VEDEO_ID_OBJETO_KEY = "idObjeto";
    private static final String VEDEO_ID_HORA_KEY = "idVideoConferencia";
    private static final String VEDEO_HORAS_ARRAY_KEY = "horas";
    private static final String VEDEO_FECHA_KEY = "fecha";
    private static final String VEDEO_HORAIO_DISPONIBLE_KEYKEY = "horasM";
    private static final String VEDEO_HORA_INICIO_KEY = "horaInicio";
    private static final String VEDEO_HORA_FIN_KEY = "horaFin";
    private static final String VEDEO_HORA_FINAL_KEY = "horaFinal";
    private static final String VEDEO_MENSAJE_KEY = "mensaje";
    private static final String VEDEO_URL_SESION_KEY = "urlSesion";
    private static final String VEDEO_CONTESTADA_KEY = "contestada";
    private static final String VEDEO_APARTADO_KEY = "fechaApartada";
    private static final String VEDEO_APARTADO_STATUS = "activo";

    private int mIdObjeto;

    private boolean mContestada;

    private String mensaje;

    private IntervaloHorasSensuales mApartado;

    private ArrayList<Dias> mListadoDiasDisponiblesParaUnaVedeoCharlaConExperto;

        public VedeoCharlaConExpertoInnerData(JSONObject data) {
            mListadoDiasDisponiblesParaUnaVedeoCharlaConExperto = new ArrayList<>();
            JSONArray vedeo = data.optJSONArray(VEDEO_ARRAY_KEY);
            if(vedeo.length() > 0){
                JSONObject vedeoObj = vedeo.optJSONObject(0);
                mIdObjeto = vedeoObj.optInt(VEDEO_ID_OBJETO_KEY);
                mContestada = vedeoObj.optBoolean(VEDEO_CONTESTADA_KEY);
                JSONArray arrayFechas = vedeoObj.optJSONArray(VEDEO_HORAS_ARRAY_KEY);
                for(int i = 0; i < arrayFechas.length(); i++)
                {
                    JSONObject fechaActual = arrayFechas.optJSONObject(i);
                    JSONArray horasDeEsteDia = fechaActual.optJSONArray(VEDEO_HORAIO_DISPONIBLE_KEYKEY);
                    ArrayList<IntervaloHorasSensuales> mHorasDeHoy = new ArrayList<>();
                    for(int j = 0; j < horasDeEsteDia.length(); j++)
                    {
                        JSONObject horaActual = horasDeEsteDia.optJSONObject(j);
                        mHorasDeHoy.add(
                                new IntervaloHorasSensuales(
                                        horaActual.optString(VEDEO_HORA_INICIO_KEY),
                                        horaActual.optString(VEDEO_HORA_FIN_KEY),
                                        horaActual.optString(VEDEO_URL_SESION_KEY),
                                        horaActual.optInt(VEDEO_ID_HORA_KEY),
                                        horaActual.optInt(VEDEO_APARTADO_STATUS)
                                ));
                    }

                    mListadoDiasDisponiblesParaUnaVedeoCharlaConExperto.add(
                            new Dias(fechaActual.optString(VEDEO_FECHA_KEY),mHorasDeHoy)
                    );
                }
                JSONArray apartadoArray = data.optJSONArray(VEDEO_APARTADO_KEY);
                JSONObject apartado = apartadoArray.optJSONObject(0);
                if(apartado != null)
                {
                    mApartado = new IntervaloHorasSensuales(
                            apartado.optString(VEDEO_HORA_INICIO_KEY),
                            apartado.optString(VEDEO_HORA_FINAL_KEY),
                            apartado.optString(VEDEO_URL_SESION_KEY),
                            apartado.optInt(VEDEO_ID_HORA_KEY),
                            apartado.optString(VEDEO_FECHA_KEY),
                            apartado.optInt(VEDEO_APARTADO_STATUS)
                    );
                }
            }
            else{
                mensaje = data.optString(VEDEO_MENSAJE_KEY);
            }
        }

    public int getIdObjeto() {
        return mIdObjeto;
    }

    public ArrayList<Dias> getListadoDiasDisponiblesParaUnaVedeoCharlaConExperto() {
        return mListadoDiasDisponiblesParaUnaVedeoCharlaConExperto;
    }

    public boolean isContestada() {
        return mContestada;
    }

    public String getMensaje() {
        return mensaje;
    }

    public IntervaloHorasSensuales getApartado() {
        return mApartado;
    }
}
