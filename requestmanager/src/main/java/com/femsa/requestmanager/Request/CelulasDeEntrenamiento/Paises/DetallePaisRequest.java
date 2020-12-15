package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Paises;

import android.content.Context;

import com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.detalle.DetallePaisesParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DetallePaisRequest implements Callback<ResponseBody>, Parser.ParserListener<DetallePaisesResponseData> {

    public OnResponseObtenerListadoPaises mListener;
    public Context mContext;

    public interface OnResponseObtenerListadoPaises extends ResponseContract {
        void onResponseObtenerDetallePais(DetallePaisesResponseData data);
        void onResponseTokenInvalido();
        void onResponseMuestraMensaje(int codigoRespuesta);
        void onResponseSinPaises();
    }

    public DetallePaisRequest(Context context){
        mContext = context;
    }

    public void makeRequest(String token, int idPais, OnResponseObtenerListadoPaises listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.GetMethods get = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = get.requestDetallePais(token, idPais);
            request.enqueue(this);
        }
        else {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void jsonTraducido(Response<DetallePaisesResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerDetallePais(traduccion.getData());
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
            DetallePaisesParser parser = new DetallePaisesParser(response.code());
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
