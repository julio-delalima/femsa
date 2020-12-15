package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.facilitador;

import android.content.Context;
import android.util.Log;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.FechaDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.ProgramarSolicitudDTO;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.LogManager;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Insomnia: programarSolicitud
 */
public class ProgramarSolicitudRequest implements Callback<ResponseBody> {

    private static final String ID_FACILITADOR_KEY = "idFacilitador";
    private static final String ID_SOLICITUD_KEY = "idSolicitud";
    private static final String FECHA_KEY = "fecha";
    private static final String FECHA_FIN_KEY = "fechaFin";
    private static final String FECHA_INICIO_KEY = "fechaInicio";

    private OnResponseProgramarSolicitud mListener;
    private Context mContext;

    /**
     * <p>Constructor de la petición.</p>
     * @param context Contexto para verificar que existe conexión a internet.
     */
    public ProgramarSolicitudRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Interface que contiene los métodos para manejar las respuestas de la petición.</p>
     */
    public interface OnResponseProgramarSolicitud extends ResponseContract {
        void onResponseSolicitudProgramada();
        void onResponseTokenInvalido();
    }

    /**
     * <p>Método que crea el cuerpo de la petición.</p>
     * @param programarSolicitudDTO Objeto que contiene todos los elementos que se enviarán en la petición.
     * @return Cuerpo de la petición.
     */
    private RequestBody crearParametros(ProgramarSolicitudDTO programarSolicitudDTO){
        HashMap<String, Object> parametros = new HashMap<>();

        ArrayList<HashMap<String, Object>> fechasArray = new ArrayList<>();
        ArrayList<FechaDTO> fechasList = programarSolicitudDTO.getListFechas();
        for (int i=0; i<fechasList.size(); i++){
            HashMap<String, Object> fechasHash = new HashMap<>();
            fechasHash.put(FECHA_FIN_KEY, fechasList.get(i).getFechaFin());
            fechasHash.put(FECHA_INICIO_KEY, fechasList.get(i).getFechaInicio());
            fechasArray.add(fechasHash);
        }

        parametros.put(ID_FACILITADOR_KEY, programarSolicitudDTO.getIdFacilitador());
        parametros.put(ID_SOLICITUD_KEY, programarSolicitudDTO.getIdSolicitud());
        parametros.put(FECHA_KEY, fechasArray);

        //Log.d("horario", new JSONObject(parametros).toString());
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(parametros).toString());
    }

    /**
     * <p>Método que permite hacer la petición para programar una solicitud.</p>
     * @param programarSolicitudDTO Objeto que contiene todos la información que se enviará.
     * @param tokenUsuario Token del usuario que realiza la petición.
     * @param listener Listener para manejar la respuesta cuando no hay conexión a internet.
     */
    public void makeRequest(ProgramarSolicitudDTO programarSolicitudDTO, String tokenUsuario, OnResponseProgramarSolicitud listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.requestProgramarSolicitud(crearParametros(programarSolicitudDTO), tokenUsuario);
            request.enqueue(this);
        }
        else {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        switch (codigoRespuesta){
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseSolicitudProgramada();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
