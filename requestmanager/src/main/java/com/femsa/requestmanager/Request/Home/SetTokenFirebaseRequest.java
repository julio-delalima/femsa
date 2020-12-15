package com.femsa.requestmanager.Request.Home;

import android.content.Context;
import androidx.annotation.NonNull;

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
import retrofit2.Response;

public class SetTokenFirebaseRequest implements Callback<ResponseBody> {

    private static final String TIPO_DISPOSITIVO_KEY = "tipoDispositivo";
    private static final String TOKEN_FIREBASE_KEY = "tokenFirebase";

    private Context mContext;

    private setTokenFirebaseResponseContract mListener;

    public interface setTokenFirebaseResponseContract extends ResponseContract
    {
        void OnTokenDefinido();
        void OnNoAuth();
        void OnUnexpectedError(int codigoRespuesta);
    }

    public SetTokenFirebaseRequest(Context context)
    {
        mContext = context;
    }

    private RequestBody makeParameters(String tokenFirebase, String tipoDispositivo)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(TOKEN_FIREBASE_KEY, tokenFirebase);
        params.put(TIPO_DISPOSITIVO_KEY, tipoDispositivo);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    public void makeRequest(String tokenFirebase, String tipoDispositivo, String tokenUsuario, setTokenFirebaseResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestSetTokenFirebase(makeParameters(tokenFirebase, tipoDispositivo), tokenUsuario);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        switch(response.code())
        {
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.OnTokenDefinido();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            default:
                mListener.OnUnexpectedError(response.code());

        }
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        mListener.onResponseTiempoAgotado();
    }


}
