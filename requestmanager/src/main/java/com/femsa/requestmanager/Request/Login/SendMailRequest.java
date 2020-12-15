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

public class SendMailRequest implements Callback<ResponseBody> {

    private static final String EMAIL_KEY = "mail";

    private Context mContext;
    private SendMailRequest.SendMailResponseContract mListener;

    public interface SendMailResponseContract extends ResponseContract
    {
        void onResponseMailSuccess();
        void OnResponseWrongMail();
    }

    public SendMailRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(String email, SendMailRequest.SendMailResponseContract listener) {

        mListener = listener;

        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requesrSendMail(createParameter(email));
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody createParameter(String email)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(EMAIL_KEY, email);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response)
        {
            if (response.code() == RequestManager.CODIGO_SERVIDOR.OK)
            {
               mListener.onResponseMailSuccess();
            }
            else if(response.code() == RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED)
            {
                mListener.OnResponseWrongMail();
            }
            else
            {
                mListener.onResponseErrorServidor();
            }
        }

        @Override
        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t)
            {
                mListener.onResponseTiempoAgotado();
            }

}

