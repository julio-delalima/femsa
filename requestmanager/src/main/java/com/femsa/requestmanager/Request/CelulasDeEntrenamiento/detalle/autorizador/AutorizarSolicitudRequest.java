package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.autorizador;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.autorizador.AutorizarSolicitudDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.autorizador.ParticipanteDTO;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Insomnia: abtenerAutorizacionParticipante
 */
public class AutorizarSolicitudRequest implements Callback<ResponseBody> {

    private static final String ID_SOLICITUD_KEY = "idSolicitud";
    private static final String ID_EMPLEADO_KEY = "idEmpleado";
    private static final String PARTICIPANTES_KEY = "participantes";
    private static final String ID_EMPLEADO_PARTICIPANTE = "idEmpleadoParticipante";

    private OnResponseAutorizarSolicitud mListener;
    private Context mContext;

    /**
     * <p>Constructor de la petición.</p>
     * @param context Contexto para verificar si existe conexión a internet.
     */
    public AutorizarSolicitudRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Método que permite hacer la petición para obtener la autorización de la solicitud.</p>
     * @param autorizarSolicitudDTO Objeto que contiene los parámetros que se envían en el cuerpo de la petición.
     * @param tokenUsuario Token del usuario.
     * @param listener Listener para manejar las respuestas de la petición.
     */
    public void makeRequest(AutorizarSolicitudDTO autorizarSolicitudDTO, String tokenUsuario, OnResponseAutorizarSolicitud listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.requestAutorizarSolicitud(crearParametros(autorizarSolicitudDTO), tokenUsuario);
            request.enqueue(this);
        }
        else {
            mListener.onResponseSinConexion();
        }
    }

    /**
     * <p>Método que crea el cuerpo de la petición para agregar el id de la solicitud que se va a autorizar.</p>
     * @param autorizarSolicitudDTO Objeto que contiene los elementos que se enviarán en el cuerpo de la petición..
     * @return Cuerpo de la petición.
     */
    private RequestBody crearParametros(AutorizarSolicitudDTO autorizarSolicitudDTO){
        HashMap<String, Object> parametros = new HashMap<>();

        ArrayList<HashMap<String, Object>> participantesArray = new ArrayList<>();
        ArrayList<ParticipanteDTO> participantesList = autorizarSolicitudDTO.getListParticipantes();
        for (int i=0; i<participantesList.size(); i++){
            HashMap<String, Object> participantesHash = new HashMap<>();
            participantesHash.put(ID_EMPLEADO_PARTICIPANTE, participantesList.get(i).getIdParticipante());
            participantesArray.add(participantesHash);
        }

        parametros.put(ID_SOLICITUD_KEY, autorizarSolicitudDTO.getIdSolicitud());
        parametros.put(ID_EMPLEADO_KEY, autorizarSolicitudDTO.getIdEmpleado());
        parametros.put(PARTICIPANTES_KEY, participantesArray);

        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(parametros).toString());
    }

    /**
     * <p>Interface que contiene los métodos para manejar las respuestas de la petición.</p>
     */
    public interface OnResponseAutorizarSolicitud extends ResponseContract {
        void onResponseAutorizacionConcedida();
        void onResponseTokenInvalido();
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        switch (codigoRespuesta){
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseAutorizacionConcedida();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            default:
                mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
