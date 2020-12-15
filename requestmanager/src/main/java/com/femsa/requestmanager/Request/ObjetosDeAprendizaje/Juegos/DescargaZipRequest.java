package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos;

import android.content.Context;

import androidx.annotation.NonNull;

import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescargaZipRequest implements Callback<ResponseBody>//, Parser.ParserListener<DescargaZipResponseData> {{
{

    private static final String PROGRAM_ID_KEY = "idObjeto";

    private Context mContext;

    private DescargaZipResponseContract mListener;

/*    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<DescargaZipResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnDescargado();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }*/

    public interface DescargaZipResponseContract extends ResponseContract
    {
        void OnDescargado(InputStream zip, int buffer);
        void OnNoAuth();
    }

    public DescargaZipRequest(Context context)
    {
        mContext = context;
    }

    private RequestBody makeParameters(int idProgram)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(PROGRAM_ID_KEY, idProgram);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    public void makeRequest(String token, int idProgram, DescargaZipResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestObtenerJuegoZip(makeParameters(idProgram));
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
            assert response.body() != null;
            mListener.OnDescargado(response.body().byteStream(), (int) response.body().contentLength());
        }
        else if(response.code() == RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED)
        {
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
