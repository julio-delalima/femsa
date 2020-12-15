package com.femsa.requestmanager.Request.MisRecompensas.listado;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoHistorialData;
import com.femsa.requestmanager.Parser.MisRecompensas.listado.ObtenerListadoHistorialParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ObtenerListadoHistorialRequest implements Callback<ResponseBody>, Parser.ParserListener<ObtenerListadoHistorialData> {

    private OnResponseObtenerHistorialRecompensas mListener;
    private Context mContext;

    public ObtenerListadoHistorialRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Método que hace la petición para obtener el historial de recomensas.</p>
     * @param token Token del usuario.
     * @param listener Listener que permite manejar las diferentes respuestas de la petición.
     */
    public void makeRequestHistorialRecompensas(String token, OnResponseObtenerHistorialRecompensas listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.GetMethods getMethod = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = getMethod.requestObtenerHistorialRecompensas(token);
            request.enqueue(this);
        }
        else {
            mListener.onResponseSinConexion();
        }
    }

    /**
     * <p>Interface que define los métodos para manejar las posibles respuestas de la petición.</p>
     */
    public interface OnResponseObtenerHistorialRecompensas extends ResponseContract{
        void onResponseObtenerListadoHistorial(ObtenerListadoHistorialData data);
        void onResponseTokenInvalido();
        void onResponseSinRecompensas();
    }

    @Override
    public void jsonTraducido(Response<ObtenerListadoHistorialData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerListadoHistorial(traduccion.getData());
                break;
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_ERROR_CREACION:
                mListener.onResponseSinRecompensas();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            case RequestManager.CODIGO_SERVIDOR.REQUEST_TIMEOUT:
                mListener.onResponseTiempoAgotado();
                break;
            default:
                mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        if (codigoRespuesta== RequestManager.CODIGO_SERVIDOR.OK){
            ObtenerListadoHistorialParser parser = new ObtenerListadoHistorialParser(codigoRespuesta);
            parser.setParserListener(this);
            try{
                parser.traducirJSON(response.body().string());
            } catch (IOException e){
                mListener.onResponseErrorServidor();
            }
        }
        else if (codigoRespuesta== RequestManager.CODIGO_SERVIDOR.NO_CONTENT){
            mListener.onResponseSinRecompensas();
        }
        else {
            mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
