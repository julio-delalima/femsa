package com.femsa.requestmanager.Request.Legal;

import android.content.Context;
import android.util.Log;

import com.femsa.requestmanager.Parser.Legal.TermsParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Legal.TermsAndConditionsResponseData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TermsAndConditionsRequest implements Callback<ResponseBody>, Parser.ParserListener<TermsAndConditionsResponseData> {

    private TermsResponseContract mListener;
    private Context mContext;

    public interface TermsResponseContract extends ResponseContract
        {
            void onTermsSuccesful(TermsAndConditionsResponseData termsResponse);
            void onTermsFailed();
            void onNoAuth();
            void OnNoContent();
            void OnUnexpectedError(int codigoRespuesta);
        }

    public TermsAndConditionsRequest(Context context)
        {
            mContext = context;
        }

    public void makeRequest(String tokenUsuario, TermsResponseContract listener)
        {
            mListener = listener;
            if (Utilities.getConnectivityStatus(mContext))
                {
                    RequestManager.GetMethods getMethods = RequestManager.getCliente().create(RequestManager.GetMethods.class);
                    Call<ResponseBody> request = getMethods.requestTermsAndConditions(tokenUsuario);
                    //Log.d(this.getClass().getSimpleName(), request.request().url().url().toString());
                    request.enqueue(this);
                }
            else
                {
                    mListener.onResponseSinConexion();
                }
        }

    @Override
    public void jsonTraducido(Response<TermsAndConditionsResponseData> traduccion)
        {
            switch (traduccion.getError().getCodigoError())
                {
                    case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                        mListener.onTermsSuccesful(traduccion.getData());
                        break;
                    case RequestManager.CODIGO_RESPUESTA.RESPONSE_ERROR_CREACION:
                        mListener.onTermsFailed();
                        break;
                    case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                        mListener.onNoAuth();
                    case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                        mListener.onResponseErrorServidor();
                        break;
                    case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                        mListener.OnNoContent();
                        break;
                    default:
                        mListener.OnUnexpectedError(traduccion.getError().getCodigoError());
                        break;
                }
        }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        switch(response.code()) {
            case RequestManager.CODIGO_SERVIDOR.OK: {
                TermsParser parser = new TermsParser(response.code());
                parser.setParserListener(this);
                try {
                    parser.traducirJSON(response.body().string());
                } catch (IOException e) {
                    mListener.onResponseErrorServidor();
                }
            }
            break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onNoAuth();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                mListener.OnNoContent();
                break;
            default:
                mListener.OnUnexpectedError(response.code());
                break;
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseErrorServidor();
    }
}
