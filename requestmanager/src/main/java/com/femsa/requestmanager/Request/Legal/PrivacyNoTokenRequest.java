package com.femsa.requestmanager.Request.Legal;

import android.content.Context;
import android.util.Log;

import com.femsa.requestmanager.Parser.Legal.PrivacyParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Legal.PrivacyAdviceResponseData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PrivacyNoTokenRequest implements Callback<ResponseBody>, Parser.ParserListener<PrivacyAdviceResponseData> {

    private static final String LANGUAGE_KEY = "idioma";

    private Context mContext;

    private PrivacyNoTokenRequest.ResponseContract mListener;

    public interface ResponseContract extends com.femsa.requestmanager.Request.ResponseContract
    {
        void onPrivacySuccess(PrivacyAdviceResponseData data);
        void onPrivacyFailure();
        void noAuth();
        void OnNoContent();
        void OnUnexpectedError(int codigoRespuesta);
    }

    public PrivacyNoTokenRequest(Context context)
    {
        mContext = context;
    }

    public void makeRequest(String language, PrivacyNoTokenRequest.ResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            if(!language.equals("en") && !language.equals("es") && !language.equals("pt")){
                language = "en";
            }
            RequestManager.PostMethods postMethods = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethods.requestAdviceNoToken(createParameters(language));
            //Log.d(this.getClass().getSimpleName(), request.request().url().url().toString());
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody createParameters(String lang)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(LANGUAGE_KEY, lang);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void jsonTraducido(Response<PrivacyAdviceResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError())
        {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onPrivacySuccess(traduccion.getData());
                break;
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_ERROR_CREACION:
                mListener.onPrivacyFailure();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.noAuth();
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

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        switch(response.code()) {
            case RequestManager.CODIGO_SERVIDOR.OK:
                PrivacyParser parser = new PrivacyParser(response.code());
                parser.setParserListener(this);
                try {
                    parser.traducirJSON(response.body().string());
                } catch (IOException e) {
                    mListener.onResponseErrorServidor();
                }
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.noAuth();
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

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onPrivacyFailure();
    }
}
