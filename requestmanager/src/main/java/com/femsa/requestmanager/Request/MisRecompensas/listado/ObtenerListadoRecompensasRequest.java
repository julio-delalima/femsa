package com.femsa.requestmanager.Request.MisRecompensas.listado;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoRecompensasData;
import com.femsa.requestmanager.Parser.MisRecompensas.listado.ObtenerListadoRecompensasParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ObtenerListadoRecompensasRequest implements Callback<ResponseBody>, Parser.ParserListener<ObtenerListadoRecompensasData> {

    private OnResponseObtenerListadoRecompensas mListener;
    private Context mContext;

    @Override
    public void jsonTraducido(Response<ObtenerListadoRecompensasData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerListadoRecompensas(traduccion.getData());
                break;

            case RequestManager.CODIGO_RESPUESTA.RESPONSE_ERROR_CREACION:
                mListener.onResponseSinRecompensas();
                break;

            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();

            case RequestManager.CODIGO_SERVIDOR.REQUEST_TIMEOUT:
                mListener.onResponseTiempoAgotado();

            default:
                mListener.onResponseErrorServidor();
        }
    }

    public interface OnResponseObtenerListadoRecompensas extends ResponseContract {
        void onResponseObtenerListadoRecompensas(ObtenerListadoRecompensasData data);
        void onResponseTokenInvalido();
        void onResponseSinRecompensas();
    }

    public ObtenerListadoRecompensasRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Método que hace la petición para obtener el listado de recompensas.</p>
     * @param token Token del usuario.
     * @param listener Listener que contiene los métodos para manejar las distintas respuestas de la petición.
     */
    public void makeRequestListado(String token, OnResponseObtenerListadoRecompensas listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.GetMethods getMethod = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = getMethod.requestObtenerListadoRecompensas(token);
            request.enqueue(this);
        }
        else {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        if (codigoRespuesta==RequestManager.CODIGO_SERVIDOR.OK){
            ObtenerListadoRecompensasParser parser = new ObtenerListadoRecompensasParser(codigoRespuesta);
            parser.setParserListener(this);
            try{
                parser.traducirJSON(response.body().string());
            }
            catch (IOException e){
                mListener.onResponseErrorServidor();
            }
        }
        else if (codigoRespuesta==RequestManager.CODIGO_SERVIDOR.NO_CONTENT){
            mListener.onResponseSinRecompensas();
        }
        else{
            mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
