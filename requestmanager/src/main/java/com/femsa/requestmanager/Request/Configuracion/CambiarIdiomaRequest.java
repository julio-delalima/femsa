package com.femsa.requestmanager.Request.Configuracion;

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

public class CambiarIdiomaRequest implements Callback<ResponseBody> {

    private static final String ID_IDIOMA_KEY = "idIdioma";

    private Context mContext;

    private CambiarIdiomaResponseContract mListener;

    public interface CambiarIdiomaResponseContract extends ResponseContract
    {
        void OnIdiomaCambiado();
        void OnNoAuth();
        void OnUnexpectedError(int codigoRespuesta);
    }

    public CambiarIdiomaRequest(Context context)
    {
        mContext = context;
    }

    private RequestBody makeParameters(int idIdioma)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_IDIOMA_KEY, idIdioma);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    public void makeRequest(int idIdioma, String token, CambiarIdiomaResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestCambiarIdioma(makeParameters(idIdioma), token);
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
                mListener.OnIdiomaCambiado();
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
