package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.DetalleObjeto;

import android.content.Context;

import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ObjetoMarkAsReadRequest implements Callback<ResponseBody> {


    private static final String OBJECT_KEY = "idObjeto";


    private Context mContext;
    private ObjetoMarkAsReadResponseContract mListener;

    public interface ObjetoMarkAsReadResponseContract extends ResponseContract
    {
        void onResponseBonusCorrecto();
        void onResponseNoAuth();
        void onResponseErrorInesperado(int codigoRespuesta);
    }

    public ObjetoMarkAsReadRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(int idObjeto, String token, ObjetoMarkAsReadResponseContract listener) {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestMarkAsRead(crearParametros(idObjeto), token);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametros(int idObjeto)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(OBJECT_KEY, idObjeto);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
    {
        if (response.code() == RequestManager.CODIGO_SERVIDOR.OK)
        {
            mListener.onResponseBonusCorrecto();
        }
        else if(response.code() == RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED)
        {
            mListener.onResponseNoAuth();
        }
        else if(response.code() == RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR)
        {
            mListener.onResponseErrorServidor();
        }
        else
        {
            mListener.onResponseErrorInesperado(response.code());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }

}
