package com.femsa.requestmanager.Request.Home;

import android.content.Context;

import com.femsa.requestmanager.Parser.Home.CategoriesParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CategoriesRequest implements Callback<ResponseBody>, Parser.ParserListener<CategoriesResponse> {

    private Context mContext;

    private CategoriesRequest.CategoriesRequestContract mListener;


    public interface CategoriesRequestContract extends ResponseContract
    {
        void OnRequestError();

        void OnCategoriesSuccess(CategoriesResponse data);

        void OnNoAuth();

        void OnUnexpectedError(int codigoRespuesta);

        void OnNoContent();
    }

    @Override
    public void jsonTraducido(Response<CategoriesResponse> traduccion) {
        switch (traduccion.getError().getCodigoError())
            {
                case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                    mListener.OnCategoriesSuccess(traduccion.getData());
                break;
                case RequestManager.CODIGO_RESPUESTA.RESPONSE_ERROR_CREACION:
                    mListener.OnRequestError();
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
                    CategoriesParser parser = new CategoriesParser(response.code());
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
                  mListener.OnUnexpectedError(response.code());
            }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseErrorServidor();
    }


    public CategoriesRequest(Context context)
    {
        mContext = context;
    }

    public void makeRequest(String tokenUser, CategoriesRequest.CategoriesRequestContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.GetMethods requestCategories = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request = requestCategories.requestCategories(tokenUser);
            //Log.d(this.getClass().getSimpleName(), request.request().url().url().toString());
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }


}
