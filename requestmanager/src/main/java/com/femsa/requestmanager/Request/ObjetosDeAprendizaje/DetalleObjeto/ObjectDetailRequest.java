package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.DetalleObjeto;

import android.content.Context;
import androidx.annotation.NonNull;

import com.femsa.requestmanager.Parser.ObjetosAprendizaje.ObjectDetailParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectDetailResponseData;
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

public class ObjectDetailRequest implements Callback<ResponseBody>, Parser.ParserListener<ObjectDetailResponseData> {

    private static final String PROGRAM_ID_KEY = "idObjeto";

    private Context mContext;

    private ObjectDetailResponseContract mListener;

    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<ObjectDetailResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnDetailSuccess(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                    mListener.OnNoAuth();
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }

    public interface ObjectDetailResponseContract extends ResponseContract
        {
            void OnDetailSuccess(ObjectDetailResponseData data);
            void OnNoAuth();
        }

    public ObjectDetailRequest(Context context)
        {
            mContext = context;
        }

    private RequestBody makeParameters(int idProgram)
        {
            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put(PROGRAM_ID_KEY, idProgram);
            return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
        }

    public void makeRequest(String token, int idProgram, ObjectDetailResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
            {
                RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
                Call<ResponseBody> request = postMethod.requestDetailObject(makeParameters(idProgram), token);
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
            ObjectDetailParser parser = new ObjectDetailParser(response.code());
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
            ObjectDetailParser parser = new ObjectDetailParser(response.code());
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
