package com.femsa.requestmanager.Request.Home.especiales;

import android.content.Context;

import com.femsa.requestmanager.Parser.Home.CuzYouSawParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Home.CuzYouSawResponse;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PorqueVisteRequest implements Callback<ResponseBody>, Parser.ParserListener<CuzYouSawResponse> {

    private Context mContext;

    private PorqueVisteRequest.CuzYouSawRequestContract mListener;

    public interface CuzYouSawRequestContract extends ResponseContract
    {
        void OnCuzYouSawSuccess(CuzYouSawResponse data);

        void OnNoAuth();

        void OnNoContent();

        void OnUnexpectedError(int codigoRespuesta);
    }

    @Override
    public void jsonTraducido(Response<CuzYouSawResponse> traduccion) {
        switch (traduccion.getError().getCodigoError())
        {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnCuzYouSawSuccess(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
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
                CuzYouSawParser parser = new CuzYouSawParser(response.code(), false);
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
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                mListener.OnNoContent();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
            {
                mListener.OnNoAuth();
            }
            break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            default:
                mListener.OnUnexpectedError(response.code());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseErrorServidor();
    }

    public PorqueVisteRequest(Context context)
    {
        mContext = context;
    }

    public void makeRequest(String tokenUser, PorqueVisteRequest.CuzYouSawRequestContract listener)
    {
        mListener = listener;

        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.GetMethods requestCuzYourSaw = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = requestCuzYourSaw.requestSeen(tokenUser);
            //Log.d(this.getClass().getSimpleName(), request.request().url().url().toString());
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }


}
