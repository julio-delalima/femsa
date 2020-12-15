package com.femsa.requestmanager.Request.Login;

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

public class CambiarCorreoRequest implements Callback<ResponseBody>{

    private static final String MAIL_KEY = "correo";

    private Context mContext;

    private CambiarCorreoResponseContract mListener;

    public interface CambiarCorreoResponseContract extends ResponseContract
    {
        void OnCambioExitoso();
        void OnNoAuth();
        void OnUnexpectedError(int codigoRespuesta);
        void OnCorreoYaRegistrado();
    }

    public CambiarCorreoRequest(Context context)
    {
        mContext = context;
    }

    private RequestBody makeParameters(String mail)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(MAIL_KEY, mail);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    public void makeRequest(String token, String mail,CambiarCorreoResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestEditarCorreo(makeParameters(mail), token);
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
                mListener.OnCambioExitoso();
                break;
            case RequestManager.CODIGO_SERVIDOR.NOT_IMPLEMENTED:
                mListener.OnCorreoYaRegistrado();
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
