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

public class EliminarSolicitudRequest implements Callback<ResponseBody> {

    private static final String ID_SOLICITUD_KEY = "idSolicitud";

    private OnResponseEliminarSolicitud mListener;
    private Context mContext;

    public interface OnResponseEliminarSolicitud extends ResponseContract {
        void onResponseSolicitudEliminada();
        void onResponseTokenInvalido();
        void onResponseErrorInesperado(int codigoRespuesta);
    }

    public EliminarSolicitudRequest(Context context){
        mContext = context;
    }

    public void makeRequest(String tokenUsuario, int idSolicitud, OnResponseEliminarSolicitud listener){
        mListener = listener;
        if(Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethods = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethods.requestEliminarSolicitud(crearParametros(idSolicitud), tokenUsuario);
            request.enqueue(this);
        }
        else{
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametros(int idSolicitud)
    {
        HashMap<String, Object> params = new HashMap<>();
        params.put(ID_SOLICITUD_KEY, idSolicitud);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        switch(codigoRespuesta) {
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseSolicitudEliminada();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            default:
                mListener.onResponseErrorInesperado(codigoRespuesta);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
