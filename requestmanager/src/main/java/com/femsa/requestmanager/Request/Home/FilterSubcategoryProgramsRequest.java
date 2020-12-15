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

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class FilterSubcategoryProgramsRequest implements Callback<ResponseBody>, Parser.ParserListener<ProgramsResponse> {

    private Context mContext;

    private FilterSubcategoryResponseContract mListener;

    private static final String ID_SUBCATEGORY_KEY = "idSubCategoria";

    public interface FilterSubcategoryResponseContract extends ResponseContract
    {
        void OnProgramsSuccess(ProgramsResponse data);

        void OnNoAuth();

        void OnProgramsError();

        void OnNoContent();

        void OnUnexpectedError(int codigoRespuesta);
    }
    public FilterSubcategoryProgramsRequest(Context context)
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
                mListener.OnUnexpectedError(response.code());
                break;
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseErrorServidor();
    }

    /**
     * Método para realizar la petición.
     * @Param token     es el token de usuario.
     * @Param listener  Listener que escuchará los eventos de la petición.
     * @Param inText    texto introducido por el usuario mediante el buscador
     * */

    public void makeRequest(int idSubcategory, String token, FilterSubcategoryResponseContract listener)
    {
        mListener = listener;
        if(Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods requestPrograms = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = requestPrograms.requestProgramasFiltrados(crearParametros(idSubcategory),token);
            //Log.d(this.getClass().getSimpleName(), request.request().url().url().toString());
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    /***
     * Método para convertir el texto en Request body para enviarlo dentro de la petición post
     */
    public static RequestBody crearParametros(int idSub)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_SUBCATEGORY_KEY, idSub);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }
}
