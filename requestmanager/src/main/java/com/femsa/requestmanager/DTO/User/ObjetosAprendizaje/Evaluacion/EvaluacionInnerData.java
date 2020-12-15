package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Evaluacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EvaluacionInnerData {

    private static final String ID_OBJETO_KEY = "idObjeto";
    private static final String EVALUACION_PREGUNTAS_ARRAY_KEY = "checklist";
    private static final String ID_EVALUACION_KEY = "idEvaluacion";
    private static final String TEXTO_PREGUNTA_KEY = "nombreEncabezado";
    private static final String TIPO_PREGUNTA_KEY = "tipoPregunta";
    private static final String RESPUESAS_ARRAY_KEY = "listCheck";
    private static final String ID_RESPUESTA_KEY = "idRespuesta";
    private static final String RESPUESTA_TEXTO_KEY = "pregunta";
    private static final String STATUS_KEY = "status";
    private static final String CORRECTA_KEY = "correcta";
    private static final String INTENTOS_KEY = "intentos";
    private static final String CONTESTADA_KEY = "contestada";

    private int mIdObjeto, mIntentos;
    private boolean mContes_Pinches_tada;
    private ArrayList<PreguntasEvaluacion> mEvaluacion = new ArrayList<>();

    public EvaluacionInnerData(JSONObject data) throws JSONException {

        mIdObjeto = data.optInt(ID_OBJETO_KEY);
        mIntentos = data.optInt(INTENTOS_KEY);
        mContes_Pinches_tada = data.optBoolean(CONTESTADA_KEY);
        JSONArray arregloPreguntas = data.optJSONArray(EVALUACION_PREGUNTAS_ARRAY_KEY);
        for (int i = 0; i < arregloPreguntas.length(); i++) {
            JSONObject preguntaActual = arregloPreguntas.optJSONObject(i);
            JSONArray arregloRespuestas = preguntaActual.optJSONArray(RESPUESAS_ARRAY_KEY);

            ArrayList<RespuestasEvaluacion> listadorespuestas = new ArrayList<>();

            for (int j = 0; j < arregloRespuestas.length(); j++) {
                JSONObject respuestaActual = arregloRespuestas.optJSONObject(j);
                listadorespuestas.add(
                        new RespuestasEvaluacion(
                                respuestaActual.optInt(ID_RESPUESTA_KEY),
                                respuestaActual.optString(RESPUESTA_TEXTO_KEY),
                                respuestaActual.optBoolean(STATUS_KEY),
                                respuestaActual.optBoolean(CORRECTA_KEY),
                                preguntaActual.optInt(ID_EVALUACION_KEY)
                        ));
            }

            mEvaluacion.add(new PreguntasEvaluacion(
                    preguntaActual.optInt(ID_EVALUACION_KEY),
                    preguntaActual.optString(TEXTO_PREGUNTA_KEY),
                    preguntaActual.optString(TIPO_PREGUNTA_KEY),
                    listadorespuestas
            ));
        }
    }

    public int getIdObjeto() {
        return mIdObjeto;
    }

    public boolean isContes_Pinches_tada() {
        return mContes_Pinches_tada;
    }

    public ArrayList<PreguntasEvaluacion> getEvaluacion() {
        return mEvaluacion;
    }

    public int getIntentos() {
        return mIntentos;
    }
}