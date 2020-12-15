package com.femsa.requestmanager.Request.Home;

import android.content.Context;
import android.util.Log;

import com.femsa.requestmanager.Parser.Home.SubcategoriesParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Home.SubcategoriesResponse;
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

public class SubcategoriesRequest implements Callback<ResponseBody>, Parser.ParserListener<SubcategoriesResponse> {

    private Context mContext;

    private SubcategoriesResponseContract mListener;

    private final String ID_CATEGORY_KEY = "idCategorias";

    public interface SubcategoriesResponseContract extends ResponseContract
    {
        void OnSubcategoriesSuccess(SubcategoriesResponse data);

        void OnNoAuth();

        void OnSubcategoriesError();

        void OnNoContent();

        void OnUnexpectedError(int codigoRespuesta);
    }

    public SubcategoriesRequest(Context context)
    {
        mContext = context;
    }

    @Override
    public void jsonTraducido(Response<SubcategoriesResponse> traduccion) {
        switch (traduccion.getError().getCodigoError())
        {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnSubcategoriesSuccess(traduccion.getData());
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
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        switch(response.code())
        {
            case RequestManager.CODIGO_SERVIDOR.OK:
                SubcategoriesParser parser = new SubcategoriesParser(response.code());
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

    public void makeRequest(String token, int idCategory,  SubcategoriesRequest.SubcategoriesResponseContract listener)
    {
        mListener = listener;
        if(Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods requestSubcategories = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = requestSubcategories.requestCategoriasFiltrados(makeParameters(idCategory), token);
            //Log.d(this.getClass().getSimpleName(), request.request().url().url().toString());
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }


    private RequestBody makeParameters(int idCategory)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_CATEGORY_KEY, idCategory);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }
}
