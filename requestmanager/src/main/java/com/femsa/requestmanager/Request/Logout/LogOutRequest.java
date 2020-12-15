package com.femsa.requestmanager.Request.Logout;

import android.content.Context;

import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LogOutRequest implements Callback<ResponseBody> {

    private static final String TOKEN_KEY = "tokenUsuario";

    private Context mContext;
    private LogoutResponseContract mListener;

    public interface LogoutResponseContract extends ResponseContract
    {
        void onResponseLogOutCorrecto();
        void onResponseLogOutError();
        void OnNoAuth();
        void OnUnexpectedError(int codigoRespuesta);
    }

    public LogOutRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(String userToken, LogoutResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.GetMethods getMethod = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = getMethod.requestLogOut(userToken);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }


    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
    {
        switch (response.code()) {
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseLogOutCorrecto();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseLogOutError();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            default:
                mListener.OnUnexpectedError(response.code());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
