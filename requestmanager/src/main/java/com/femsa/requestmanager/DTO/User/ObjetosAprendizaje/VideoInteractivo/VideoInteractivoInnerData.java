package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VideoInteractivo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoInteractivoInnerData {

    private static final String MAIN_DETALLE_KEY = "detalleObjetoAprendizaje";
    private static final String ID_OBJETO_KEY = "idObjeto";
    private static final String ID_CONTENIDO_KEY = "idContenido";
    private static final String CONTENIDO_ARRAY_KEY = "contenidoList";
    private static final String VIDEO_KEY = "video";
    private static final String VIDEO_TITULO_KEY = "video";
    private static final String VIDEO_ID_KEY = "idVideo";
    private static final String PREGUNTAS_ARRAY_KEY = "preguntasList";
    private static final String PREGUNTA_TEXTO_KEY = "preguntas";
    private static final String RESPUESTA_ARRAY_KEY = "respuestasList";
    private static final String RESPUESTA_TEXTO_KEY = "respuestas";
    private static final String RESPUESTA_ES_CORRECTA_KEY = "correcta";
    private static final String ID_RESPUESTA_KEY = "respuesta";
    private static final String SEGUNDOS_KEY = "seg";


    private int mIDObjeto;

    private int mIDContenido;

    private int mSegundos;

    private ArrayList<Contenido> mContenidos;

    public VideoInteractivoInnerData(JSONObject array) throws JSONException {
        JSONObject detalleObjeto = array.optJSONObject(MAIN_DETALLE_KEY);


        mIDObjeto = detalleObjeto.optInt(ID_OBJETO_KEY);
        mIDContenido = detalleObjeto.optInt(ID_CONTENIDO_KEY);

        JSONArray contenidoarray = detalleObjeto.optJSONArray(CONTENIDO_ARRAY_KEY);
        ArrayList<Contenido> tempContenido = new ArrayList<>();
        for(int i = 0; i < contenidoarray.length(); i++)
            {
                JSONObject contenidoActual = contenidoarray.optJSONObject(i);
                JSONArray preguntasArray = contenidoActual.optJSONArray(PREGUNTAS_ARRAY_KEY);

                ArrayList<Pregunta> tempPreguntas = new ArrayList<>();
                    for(int j = 0; j < preguntasArray.length(); j++)
                        {
                            ArrayList<Respuesta> tempRespuestas = new ArrayList<>();
                            JSONObject preguntaActual = preguntasArray.optJSONObject(j);
                            JSONArray respuestaArray = preguntaActual.optJSONArray(RESPUESTA_ARRAY_KEY);
                            mSegundos = preguntaActual.optInt(SEGUNDOS_KEY);
                                for(int k = 0; k < respuestaArray.length(); k++)
                                    {
                                        JSONObject respuestaActual = respuestaArray.optJSONObject(k);
                                        tempRespuestas.add(
                                                new Respuesta(
                                                        respuestaActual.optString(RESPUESTA_TEXTO_KEY),
                                                        respuestaActual.optBoolean(RESPUESTA_ES_CORRECTA_KEY),
                                                        respuestaActual.optInt(ID_RESPUESTA_KEY))
                                        );
                                    }
                            tempPreguntas.add(new Pregunta(preguntaActual.optString(PREGUNTA_TEXTO_KEY), tempRespuestas));
                        }
                    tempContenido.add(
                            new Contenido(
                                    contenidoActual.optString(VIDEO_KEY),
                                    contenidoActual.optString(VIDEO_TITULO_KEY),
                                    contenidoActual.optInt(VIDEO_ID_KEY),
                                    tempPreguntas));
            }
        mContenidos = new ArrayList<>(tempContenido);
    }

    public int getIDObjeto() {
        return mIDObjeto;
    }

    public int getIDContenido() {
        return mIDContenido;
    }

    public ArrayList<Contenido> getContenidos() {
        return mContenidos;
    }

    public int getSegundos() {
        return mSegundos;
    }
}
