package com.femsa.requestmanager.Request.MyActivity;

import android.content.Context;
import androidx.annotation.NonNull;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Parser.Ranking.RankingParser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyActivityRequest implements Callback<ResponseBody>, Parser.ParserListener<RankingResponseData> {

    private Context mContext;

    private ActivityResponseContract mListener;

    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<RankingResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnActivitySuccess(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }

    public interface ActivityResponseContract extends ResponseContract
    {
        void OnActivitySuccess(RankingResponseData data);
        void OnNoAuth();
    }

    public MyActivityRequest(Context context)
    {
        mContext = context;
    }


    public void makeRequest(String token, ActivityResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.GetMethods getMethod = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = getMethod.requestRanking(token);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        if (response.code() == RequestManager.CODIGO_SERVIDOR.OK)
        {
            RankingParser parser = new RankingParser(response.code());
            parser.setParserListener(this);
            try
            {
                assert response.body() != null;
                parser.traducirJSON(response.body().string());
            }
            catch (IOException e)
            {
                mListener.onResponseErrorServidor();
            }
        }
        else if(response.code() == RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED)
        {
            RankingParser parser = new RankingParser(response.code());
            parser.setParserListener(this);
            mListener.OnNoAuth();
        }
        else
        {
            mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        mListener.onResponseTiempoAgotado();
    }


}
