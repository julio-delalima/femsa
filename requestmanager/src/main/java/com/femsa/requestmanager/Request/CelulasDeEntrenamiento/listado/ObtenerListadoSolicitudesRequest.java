package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.listado;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado.ListaSolicitudesData;
import com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.listado.ObtenerListaSolicitudesParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ObtenerListadoSolicitudesRequest implements Callback<ResponseBody>, Parser.ParserListener<ListaSolicitudesData> {

    public OnResponseObtenerListadoSolicitudes mListener;
    public Context mContext;

    public interface OnResponseObtenerListadoSolicitudes extends ResponseContract{
        void onResponseObtenerListadoSolicitudes(ListaSolicitudesData data);
        void onResponseTokenInvalido();
        void onResponseSinSolicitudes();
    }

    public ObtenerListadoSolicitudesRequest(Context context){
        mContext = context;
    }

    public void makeRequest(String token, OnResponseObtenerListadoSolicitudes listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.GetMethods getMethod = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = getMethod.requestObtenerListadoSolicitudes(token);
            request.enqueue(this);
        }
        else {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void jsonTraducido(Response<ListaSolicitudesData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerListadoSolicitudes(traduccion.getData());
                break;

            case RequestManager.CODIGO_RESPUESTA.RESPONSE_ERROR_CREACION:
                mListener.onResponseSinSolicitudes();
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
        int codigoRespuesta = response.code();
        if (codigoRespuesta == RequestManager.CODIGO_SERVIDOR.OK){
            ObtenerListaSolicitudesParser parser = new ObtenerListaSolicitudesParser(response.code());
            parser.setParserListener(this);
            try{
                parser.traducirJSON(response.body().string());
            } catch (IOException e) {
                mListener.onResponseErrorServidor();
            }
        }
        else if(codigoRespuesta==RequestManager.CODIGO_SERVIDOR.NO_CONTENT){
            mListener.onResponseSinSolicitudes();
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
