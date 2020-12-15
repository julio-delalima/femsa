package com.femsa.requestmanager.Request.Ranking;

import android.content.Context;
import androidx.annotation.NonNull;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Parser.Ranking.RankingTierlistParser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Ranking.RankingTierlistResponseData;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingTierRequest implements Callback<ResponseBody>, Parser.ParserListener<RankingTierlistResponseData> {

    private Context mContext;

    private RankingTierlistResponseContract mListener;

    private static final String ID_PROGRAM_KEY = "idPrograma";

    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<RankingTierlistResponseData> traduccion) {
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

    public interface RankingTierlistResponseContract extends ResponseContract
    {
        void OnRankingSuccess(RankingTierlistResponseData data);
        void OnNoAuth();
    }

    public RankingTierRequest(Context context)
    {
        mContext = context;
    }


    public void makeRequest(int idProgram, String token, RankingTierlistResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethods = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethods.requestRankingTierlist(makeParemeter(idProgram),token);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody makeParemeter(int idProgram)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_PROGRAM_KEY, idProgram);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        if (response.code() == RequestManager.CODIGO_SERVIDOR.OK)
        {
            RankingTierlistParser parser = new RankingTierlistParser(response.code());
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
            RankingTierlistParser parser = new RankingTierlistParser(response.code());
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
