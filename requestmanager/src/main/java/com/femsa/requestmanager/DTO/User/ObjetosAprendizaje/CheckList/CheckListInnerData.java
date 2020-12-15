package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.CheckList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckListInnerData {

    public static final String ID_OBJETO_KEY = "idObjeto";
    public static final String CHECKLIST_ARRAY_KEY = "checklist";
    public static final String BLOQUE_ID_OBJETO_KEY = "idObjeto";
    public static final String ID_CHECK_KEY = "idCheck";
    public static final String BLOQUE_KEY = "bloque";
    public static final String TEXTO_PREGUNTA_KEY = "nombreEncabezado";
    public static final String ARRAY_PREGUNTAS_KEY = "listCheck";
    public static final String ID_RESPUESTA_CHECK_KEY = "idCheck";
    public static final String RESPUESTA_CHECK_KEY = "pregunta";
    private static final String STATUS_KEY ="status";
    private static final String CORRECTA_KEY ="correcta";
    private CheckListCompleto mCheckList;


    public CheckListInnerData(JSONObject data) throws JSONException {

        ArrayList<CheckListPregunta> preguntas = new ArrayList<>();
        JSONArray arregloPreguntas = data.getJSONArray(CHECKLIST_ARRAY_KEY);
            for(int i = 0; i < arregloPreguntas.length(); i++)
                {
                    JSONObject preguntaActual = arregloPreguntas.getJSONObject(i);

                        JSONArray arregloRespuestas = preguntaActual.getJSONArray(ARRAY_PREGUNTAS_KEY);
                        ArrayList<CheckListRespuesta> respuestas = new ArrayList<>();
                        for(int j = 0; j < arregloRespuestas.length(); j++)
                            {
                                JSONObject respuestaActual = arregloRespuestas.optJSONObject(j);
                                respuestas.add(new CheckListRespuesta(
                                        respuestaActual.optInt(ID_RESPUESTA_CHECK_KEY),
                                        respuestaActual.optString(RESPUESTA_CHECK_KEY),
                                        respuestaActual.optBoolean(STATUS_KEY),
                                        respuestaActual.optBoolean(CORRECTA_KEY)
                                ));
                            }

                    preguntas.add(new CheckListPregunta(
                            preguntaActual.optInt(BLOQUE_ID_OBJETO_KEY),
                            preguntaActual.optInt(ID_CHECK_KEY),
                            preguntaActual.optInt(BLOQUE_KEY),
                            preguntaActual.optString(TEXTO_PREGUNTA_KEY),
                            respuestas
                    ));
                }

        mCheckList = new CheckListCompleto(data.optInt(ID_OBJETO_KEY), preguntas);
    }

    public CheckListCompleto getCheckList() {
        return mCheckList;
    }
}
