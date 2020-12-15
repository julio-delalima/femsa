package com.femsa.requestmanager.Request.MisRecompensas.detalle;

import android.content.Context;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Parser.Recompensas.RecompensasParser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Recompensa.RecompensaResponseData;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObtenerRecompensaRequest implements Callback<ResponseBody>, Parser.ParserListener<RecompensaResponseData> {

    private static final String ID_RECOMPENSA_KEY = "idRecompensas";

    private OnResponseObtenerRecompensa mListener;
    private Context mContext;

    /**
     * <p>Constructor de la aplicación.</p>
     * @param context Contexto de la aplicación para verificar que haya conexión a internet.
     */
    public ObtenerRecompensaRequest(Context context){
        mContext = context;
    }

    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<RecompensaResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                switch(traduccion.getData().getRecompensa().getCodigoMensaje()){
                    case 1:
                        mListener.onResponseYaCanjeada();
                        break;
                    case 2:
                        mListener.onResponseRecompensaAgotada();
                        break;
                    case 3:
                        mListener.onRespnseInsuficientes();
                        break;
                    case 4:
                        mListener.onResponseRecompensaObtenida();
                        break;
                }
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
            default:
                mListener.onResponseUnexpectedError(traduccion.getError().getCodigoError());
                break;
        }
    }

    /**
     * <p>Interface que contiene los métodos para manejar las respuestas de la petición.</p>
     */
    public interface OnResponseObtenerRecompensa extends ResponseContract{
        void onResponseRecompensaObtenida();
        void onResponseYaCanjeada();
        void onRespnseInsuficientes();
        void onResponseTokenInvalido();
        void onResponseUnexpectedError(int error);
        void onResponseRecompensaAgotada();
    }

    /**
     * <p>Método que hace la petición para obtener una recompensa.</p>
     * @param idRecompensa Id de la recompensa que se desea obtener.
     * @param tokenUsuario Token del usuario que realiza la petición.
     * @param listener Listener para manejar una respuesta en caso de que no haya conexión a internet.
     */
    public void makeRequest(int idRecompensa, String tokenUsuario, OnResponseObtenerRecompensa listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.requestObtenerRecompensa(crearParametros(idRecompensa), tokenUsuario);
            request.enqueue(this);
        } else {
            mListener.onResponseSinConexion();
        }

    }

    /**
     * <p>Método que permite crear el cuerpo de la petición para poder obtener una recompensa.</p>
     * @param idRecompensa Id de la recompensa que se desea obtener.
     * @return Cuerpo de la petición.
     */
    private RequestBody crearParametros(int idRecompensa){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put(ID_RECOMPENSA_KEY, idRecompensa);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(parametros).toString());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        switch (codigoRespuesta){
            case RequestManager.CODIGO_SERVIDOR.OK:
                RecompensasParser parser = new RecompensasParser(response.code());
                    parser.setParserListener(this);
                    try
                    {
                        parser.traducirJSON(response.body().string());
                    }
                    catch (IOException e)
                    {
                        mListener.onResponseErrorServidor();
                    }
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
