package com.femsa.requestmanager.Request.Home;

import android.content.Context;
import android.util.Log;

import com.femsa.requestmanager.Parser.Home.ProgramsParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Home.ProgramsResponse;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ProgramsRequest implements Callback<ResponseBody>, Parser.ParserListener<ProgramsResponse> {

    private Context mContext;

    private ProgramsRequest.ProgramsRequestResponseContract mListener;

    public interface ProgramsRequestResponseContract extends ResponseContract
    {
        void OnProgramsSuccess(ProgramsResponse data);

        void OnNoAuth();

        void OnProgramsError();

        void OnNoContent();

        void OnUnexpectedError(int cogidoRespuesta);
    }

    public ProgramsRequest(Context context)
    {
        mContext = context;
    }

    @Override
    public void jsonTraducido(Response<ProgramsResponse> traduccion) {
        switch (traduccion.getError().getCodigoError())
        {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnProgramsSuccess(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.OnProgramsError();
                break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                mListener.OnNoContent();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            default:
                mListener.OnUnexpectedError(traduccion.getError().getCodigoError());
                break;
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        switch(response.code())
        {
            case RequestManager.CODIGO_SERVIDOR.OK:
                ProgramsParser parser = new ProgramsParser(response.code());
                parser.setParserListener(this);
                try
                {
                    parser.traducirJSON(response.body().string());
                }
                catch (IOException e)
                {
                    mListener.onResponseErrorServidor();
                }
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                {
                    mListener.OnNoAuth();
                }
            break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                {
                    mListener.OnNoContent();
                }
            break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
            {
                mListener.onResponseErrorServidor();
            }
            break;
            default:
                mListener.OnUnexpectedError(response.code());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseErrorServidor();
    }

    public void makeRequest(String token, ProgramsRequest.ProgramsRequestResponseContract listener)
    {
        mListener = listener;
        if(Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.GetMethods requestPrograms = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = requestPrograms.requestPrograms(token);
            //Log.d(this.getClass().getSimpleName(), request.request().url().url().toString());
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }


}
