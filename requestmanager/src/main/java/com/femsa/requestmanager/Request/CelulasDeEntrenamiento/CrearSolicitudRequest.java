package com.femsa.requestmanager.Request.CelulasDeEntrenamiento;

import android.content.Context;
import android.util.Log;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.CelulaDTO;
import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.TemaEspecificoDTO;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CrearSolicitudRequest implements Callback<ResponseBody> {

   private static final String ID_EMPLEADO_KEY = "idEmpleado";
   private static final String TIPO_SOLUCITUD_KEY ="tipoSolicitud";
   private static final String PARTICIPANTES_ARRAY_KEY = "participantes";
   private static final String INFORMACION_ARRAY_KEY = "informacion";
   private static final String NOMBRE_TEMA_GENERAL_KEY = "nombreTema";
   private static final String TEMA_ESPECIFICO_KEY = "temaEspecifico";
   private static final String ID_FACILITADOR_KEY = "idFacilitador";
   private static final String SUBTEMA_ESPECIFICO_ARRAY_KEY = "subTemaEspecifico";
   private static final String NOMBRE_SUBTEMA_ESPECIFICO_KEY = "nombreSubTemaEspecifico";
   private static final String TIEMPO_KEY = "tiempo";
   private static final String SUBAREA_KEY = "subArea";
   private static final String FECHA_INICIO_KEY = "fechaInicio";
   private static final String FECHA_FIN_KEY = "fechaFin";
   private static final String PAIS_REPECPTOR_KEY = "paisReceptor";
   private static final String PAIS_SOLICITANTE_KEY = "paisSolicitante";

    private OnResponseNuevaSolicitud mListener;
    private Context mContext;

    public interface OnResponseNuevaSolicitud extends ResponseContract {
        void onResponseSolicitudCreada();
        void onResponseTokenInvalido();
        void onResponseErrorInesperado(int codigoRespuesta);
    }

    public CrearSolicitudRequest(Context context){
        mContext = context;
    }

    public void makeRequest(String tipo, String tokenUsuario, CelulaDTO celula, int idSolicitante, OnResponseNuevaSolicitud listener){
        mListener = listener;
        if(Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethods = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethods.requestNuevasolicitud(crearParametros(celula, idSolicitante, tipo), tokenUsuario);
            request.enqueue(this);
        }
        else{
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametros(CelulaDTO celula, int idSolicitante, String tipo)
    {
        HashMap<String, Object> params = new HashMap<>();

        ArrayList<HashMap<String, Object>> participantesArray = new ArrayList<>();
        ArrayList<HashMap<String, Object>> informacionArray = new ArrayList<>();
        ArrayList<HashMap<String, Object>> temaEspecificoArray = new ArrayList<>();
        //


        //Participantes
        for (ParticipanteDTO participanteElement : celula.getListadoParticipantes()) {
            HashMap<String, Object> participanteHash = new HashMap<>();
            participanteHash.put(ID_EMPLEADO_KEY, participanteElement.getIdParticipante());
            participantesArray.add(participanteHash);
        }

        //informacion
            HashMap<String, Object> informacionHash = new HashMap<>();
                int j = 0;
                for(TemaEspecificoDTO temaEspecifico : celula.getListadoTemasEspecificos())
                    {
                        HashMap<String, Object> temaEspecificoHash = new HashMap<>();
                        ArrayList<HashMap<String, Object>> subtemaArray = new ArrayList<>();
                                HashMap<String, Object> subtemahash = new HashMap<>();
                                subtemahash.put(NOMBRE_SUBTEMA_ESPECIFICO_KEY, celula.getListadoTemasEspecificos().get(j).getSubtemaEspecifico());
                                subtemahash.put(TIEMPO_KEY, tiempoChidoAPorqueriasDeBack(celula.getListadoTemasEspecificos().get(j).getTiempoSugerido()));
                                subtemahash.put(SUBAREA_KEY, celula.getListadoTemasEspecificos().get(j).getAreaProceso());
                                subtemaArray.add(subtemahash);
                            j+=1;
                        temaEspecificoHash.put(ID_FACILITADOR_KEY, temaEspecifico.getFacilitador().getIdFacilitador());
                        temaEspecificoHash.put(NOMBRE_TEMA_GENERAL_KEY, temaEspecifico.getTituloTemaEspecifico());
                        temaEspecificoHash.put(SUBTEMA_ESPECIFICO_ARRAY_KEY, subtemaArray);
                        temaEspecificoArray.add(temaEspecificoHash);
                    }
            informacionHash.put(NOMBRE_TEMA_GENERAL_KEY, celula.getTemaGeneral());
            informacionHash.put(TEMA_ESPECIFICO_KEY, temaEspecificoArray);
        informacionArray.add(informacionHash);


        params.put(ID_EMPLEADO_KEY, idSolicitante);
        params.put(PARTICIPANTES_ARRAY_KEY, participantesArray);
        params.put(INFORMACION_ARRAY_KEY, informacionArray);


        params.put(TIPO_SOLUCITUD_KEY, Integer.parseInt(tipo));

        params.put(FECHA_INICIO_KEY, FechaALong(celula.getFechaInicio()));
        params.put(FECHA_FIN_KEY, FechaALong(celula.getFechaFin()));
        //params.put(FECHA_INICIO_KEY, celula.getFechaInicio());
        //params.put(FECHA_FIN_KEY, celula.getFechaFin());
        params.put(PAIS_REPECPTOR_KEY, celula.getPaisReceptorId());
        params.put(PAIS_SOLICITANTE_KEY, celula.getPaisSolicitanteId());
        //Log.d("CELULANUEVA", new JSONObject(params).toString());
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    private Long FechaALong(String fecha)
        {
            final String OLD_FORMAT = "dd/MMM/yyyy";
            final String NEW_FORMAT = "yyyy/MMM/dd";

            String newDateString;

            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.getDefault());
            Date d = null;
            try
                {
                    d = sdf.parse(fecha);
                }
            catch (ParseException e)
                {
                    e.printStackTrace();
                }
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);

            try
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(NEW_FORMAT, Locale.getDefault());
                    Date parsedDate = dateFormat.parse(newDateString);
                    return new java.sql.Timestamp(parsedDate.getTime()).getTime();
                }
            catch(Exception e)
                { //this generic but you can control another types of exception
                    // look the origin of excption
                    return null;
                }

        }

    private String tiempoChidoAPorqueriasDeBack(String tiempo)
        {
            tiempo = tiempo.trim();
            String tiempoMalHechoDeBack = "";
            String horasChidas =  "";
            String medias = "";
            String[] tiempoChar = tiempo.split("\\.");
            horasChidas = tiempoChar[0];
            medias = tiempoChar[1];

            tiempoMalHechoDeBack += horasChidas + ":";
            tiempoMalHechoDeBack += (medias.equals("5")) ? "30:00" : "00:00";
            return  tiempoMalHechoDeBack.trim();
        }


    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        switch(codigoRespuesta) {
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseSolicitudCreada();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            default:
                mListener.onResponseErrorInesperado(codigoRespuesta);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
