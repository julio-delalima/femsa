package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Facilitador;


import android.content.Context;

import com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.Facilitador.AreaParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.Area.AreaData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ObtenerAreaRequest implements Callback<ResponseBody>, Parser.ParserListener<AreaData> {


    private OnResponseObtenerArea mListener;
    private Context mContext;

    public interface OnResponseObtenerArea extends ResponseContract{
        void onResponseObtenerArea(AreaData data);
        void onResponseTokenInvalido();
        void onResponseErrorInesperado(int codigoRespuesta);
    }

    public ObtenerAreaRequest(Context context){
        mContext = context;
    }

    public void makeRequest(String tokenUsuario, OnResponseObtenerArea listener){
        mListener = listener;
        if(Utilities.getConnectivityStatus(mContext)){
            RequestManager.GetMethods getMethods = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request;
            request = getMethods.requestObtenerArea(tokenUsuario);
            request.enqueue(this);
        }else{
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void jsonTraducido(Response<AreaData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerArea(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            default:
                mListener.onResponseErrorInesperado(traduccion.getError().getCodigoError());
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        if (codigoRespuesta==RequestManager.CODIGO_SERVIDOR.OK){
            AreaParser parser = new AreaParser(codigoRespuesta);
            parser.setParserListener(this);
            try{
                parser.traducirJSON(response.body().string());
            } catch (IOException e){
                mListener.onResponseErrorServidor();
            }
        } else{
            mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
