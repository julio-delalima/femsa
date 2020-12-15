package com.femsa.requestmanager.Request.Ranking;

import android.content.Context;
import androidx.annotation.NonNull;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Parser.Ranking.ProgramRankingParser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramRankingRequest implements Callback<ResponseBody>, Parser.ParserListener<ProgramRankingResponseData> {

    private Context mContext;

    private ProgramRankingResponseContract mListener;

    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<ProgramRankingResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnRankingSuccess(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }

    public interface ProgramRankingResponseContract extends ResponseContract
    {
        void OnRankingSuccess(ProgramRankingResponseData data);
        void OnNoAuth();
    }

    public ProgramRankingRequest(Context context)
    {
        mContext = context;
    }


    public void makeRequest(String token, ProgramRankingResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.GetMethods getMethod = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = getMethod.requestProgramsRanking(token);
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
            ProgramRankingParser parser = new ProgramRankingParser(response.code());
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
            ProgramRankingParser parser = new ProgramRankingParser(response.code());
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
