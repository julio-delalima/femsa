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

public class UpdatePasswordRequest implements Callback<ResponseBody>{

    private static final String PASSWORD_KEY = "credencial";
    private static final String MAIL_KEY = "correo";

    private Context mContext;

    private UpdatePasswordResponseContract mListener;

    public interface UpdatePasswordResponseContract extends ResponseContract
    {
        void OnPasswordsuccess();
        void OnNoAuth();
        void OnUnexpectedError(int codigoRespuesta);
    }

    public UpdatePasswordRequest(Context context)
    {
        mContext = context;
    }

    private RequestBody makeParameters(String password, String mail)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(PASSWORD_KEY, password);
        params.put(MAIL_KEY, mail);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    public void makeRequest(String token, String mail, String password, UpdatePasswordResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestUpdateCredential(makeParameters(password, mail), token);
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
                mListener.OnPasswordsuccess();
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
