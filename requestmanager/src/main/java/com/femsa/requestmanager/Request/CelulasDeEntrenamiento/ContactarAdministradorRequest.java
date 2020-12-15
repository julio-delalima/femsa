package com.femsa.requestmanager.Request.CelulasDeEntrenamiento;

import android.content.Context;

import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactarAdministradorRequest implements Callback<ResponseBody> {

    private static final String BODY_KEY = "body";
    private static final String TIPO_KEY = "tipoMensaje";
    private static final String ID_SOLICITUD_KEY = "idSolicitud";

    private OnResponseContactarAdministrador mListener;
    private Context mContext;

    /**
     * <p>Constructor de la petición.</p>
     * @param context Contexto para revisar que haya conexión a internet.
     */
    public ContactarAdministradorRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Método que permite crear el cuerpo de la petición para contactar al administrador.</p>
     * @param mensaje Mensaje que se envía al administrador.
     * @param tokenUsuario Token del usuario que hace la petición.
     * @param listener Listener para manejar las respuestas de la petición.
     */
    public void makeRequest(String mensaje, int idSolicitud, String tokenUsuario, int tipoMensaje, OnResponseContactarAdministrador listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.requestContactarAdministrador(crearParametros(mensaje, idSolicitud, tipoMensaje), tokenUsuario);
            request.enqueue(this);
        } else{
            mListener.onResponseSinConexion();
        }
    }

    /**
     * <p>Método que crea el cuerpo de la petición.</p>
     * @param mensaje Mensaje que se va a enviar al administrador.
     * @param idSolicitud el id de la solicitud que se está trabajando
     * @param tipoMensaje no sé qué sea esto pero me dijeron que siempre es 1, así que solo lo puse. :3
     * @return Cuerpo de la petición.
     */
    private RequestBody crearParametros(String mensaje, int idSolicitud, int tipoMensaje){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put(BODY_KEY, mensaje);
        parametros.put(ID_SOLICITUD_KEY, idSolicitud);
        parametros.put(TIPO_KEY, tipoMensaje);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(parametros).toString());
    }

    public interface OnResponseContactarAdministrador extends ResponseContract{
        void onResponseMensajeEnviado();
        void onResponseTokenInvalido();
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        switch (codigoRespuesta){
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseMensajeEnviado();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
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
