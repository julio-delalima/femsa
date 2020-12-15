package com.femsa.requestmanager.Request.Configuracion;

import android.content.Context;

import com.femsa.requestmanager.Parser.Configuracion.ListadoIdiomasParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Configuracion.IdiomaResponseData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ListadoIdiomasRequest implements Callback<ResponseBody>, Parser.ParserListener<IdiomaResponseData> {

    public OnResponseObtenerListadoIdiomas mListener;
    public Context mContext;

    public interface OnResponseObtenerListadoIdiomas extends ResponseContract {
        void onResponseObtenerListadoIdiomas(IdiomaResponseData data);
        void onResponseTokenInvalido();
        void onResponseMuestraMensaje(int codigoRespuesta);
        void onResponseSinIdiomas();
    }

    public ListadoIdiomasRequest(Context context){
        mContext = context;
    }

    public void makeRequest(int ididioma, String token, OnResponseObtenerListadoIdiomas listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.GetMethods get = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = get.requestTraeIdiomas(ididioma, token);
            request.enqueue(this);
        }
        else {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void jsonTraducido(Response<IdiomaResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerListadoIdiomas(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                mListener.onResponseSinIdiomas();
                break;

            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            default:
                mListener.onResponseMuestraMensaje(traduccion.getError().getCodigoError());
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        if (codigoRespuesta == RequestManager.CODIGO_SERVIDOR.OK){
            ListadoIdiomasParser parser = new ListadoIdiomasParser(response.code());
            parser.setParserListener(this);
            try{
                parser.traducirJSON(response.body().string());
            } catch (IOException e) {
                mListener.onResponseErrorServidor();
            }
        }
        else if(codigoRespuesta==RequestManager.CODIGO_SERVIDOR.NO_CONTENT){
            mListener.onResponseSinIdiomas();
        }
        else if (codigoRespuesta==RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED){
            mListener.onResponseTokenInvalido();
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
