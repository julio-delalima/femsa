package com.femsa.requestmanager.Request.MyActivity;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros.ObtenerAllLogrosData;
import com.femsa.requestmanager.Parser.MiActividad.obtenerAllLogros.ObtenerAllLogrosParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ObtenerAllLogrosRequest implements Callback<ResponseBody>, Parser.ParserListener<ObtenerAllLogrosData> {

    public OnResponseObtenerAllLogros mListener;
    private Context mContext;

    public interface OnResponseObtenerAllLogros extends ResponseContract {
        void onResponseObtenerAllLogros(ObtenerAllLogrosData data);
        void onResponseTokenInvalido();
        void onResponseSinLogros();
    }

    public ObtenerAllLogrosRequest(Context context){
        mContext = context;
    }

    public void makeRequest(String token, OnResponseObtenerAllLogros listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.GetMethods getMethod = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = getMethod.requestObtenerAllLogros(token);
            request.enqueue(this);
        }
        else{
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void jsonTraducido(Response<ObtenerAllLogrosData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerAllLogros(traduccion.getData());
                break;

            case RequestManager.CODIGO_RESPUESTA.RESPONSE_ERROR_CREACION:
                mListener.onResponseSinLogros();
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
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        if (response.code() == RequestManager.CODIGO_SERVIDOR.OK){
            ObtenerAllLogrosParser parser = new ObtenerAllLogrosParser(response.code());
            parser.setParserListener(this);
            try {
                parser.traducirJSON(response.body().string());
            }
            catch (IOException e){
                mListener.onResponseErrorServidor();
            }
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
