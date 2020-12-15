package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Paises;

import android.content.Context;

import com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.detalle.ListadoPaisesParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PaisesRequest implements Callback<ResponseBody>, Parser.ParserListener<PaisesResponseData> {

    public OnResponseObtenerListadoPaises mListener;
    public Context mContext;

    public interface OnResponseObtenerListadoPaises extends ResponseContract {
        void onResponseObtenerListadoPaises(PaisesResponseData data);
        void onResponseTokenInvalido();
        void onResponseMuestraMensaje(int codigoRespuesta);
        void onResponseSinPaises();
    }

    public PaisesRequest(Context context){
        mContext = context;
    }

    public void makeRequest(String token, OnResponseObtenerListadoPaises listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.GetMethods get = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = get.requestTraePaises(token);
            request.enqueue(this);
        }
        else {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void jsonTraducido(Response<PaisesResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerListadoPaises(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                mListener.onResponseSinPaises();
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
            ListadoPaisesParser parser = new ListadoPaisesParser(response.code());
            parser.setParserListener(this);
            try{
                parser.traducirJSON(response.body().string());
            } catch (IOException e) {
                mListener.onResponseErrorServidor();
            }
        }
        else if(codigoRespuesta==RequestManager.CODIGO_SERVIDOR.NO_CONTENT){
            mListener.onResponseSinPaises();
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
