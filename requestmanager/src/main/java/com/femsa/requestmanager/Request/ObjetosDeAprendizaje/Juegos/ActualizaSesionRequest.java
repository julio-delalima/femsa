package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos;

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

public class ActualizaSesionRequest implements Callback<ResponseBody> {

    private static final String TOKEN_KEY = "tokenUsuario";

    private Context mContext;
    private SesionResponseContract mListener;

    public interface SesionResponseContract extends ResponseContract
    {
        void onResponseSesionActivada();
        void onNoAuth();
        void onUnexpectedError(int code);
    }

    public ActualizaSesionRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(String token,SesionResponseContract listener) {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestActualizaSesion(crearParametros(token));
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametros(String token)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(TOKEN_KEY, token);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
    {
        switch (response.code()){
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseSesionActivada();
            break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onNoAuth();
            break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
            break;
            default:
                mListener.onUnexpectedError(response.code());
        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
