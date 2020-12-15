package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Checklist;

import android.content.Context;
import androidx.annotation.NonNull;

import com.femsa.requestmanager.Parser.ObjetosAprendizaje.CheckListParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.CheckListResponseData;
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

public class ChecklistRequest implements Callback<ResponseBody>, Parser.ParserListener<CheckListResponseData> {

    private static final String ID_OBjECT_KEY = "idObjeto";

    private Context mContext;

    private CheckListResponseContract mListener;

    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<CheckListResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnChecklistInteractivoSuccess(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
            default:
                mListener.OnUnexpectedError(traduccion.getError().getCodigoError());
                break;
        }
    }

    public interface CheckListResponseContract extends ResponseContract
    {
        void OnChecklistInteractivoSuccess(CheckListResponseData data);
        void OnNoAuth();
        void OnUnexpectedError(int responseCode);
    }

    public ChecklistRequest(Context context)
    {
        mContext = context;
    }


    public void makeRequest(String token, int idObjeto, CheckListResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestCheckList(creaParametros(idObjeto), token);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody creaParametros(int idObjeto)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_OBjECT_KEY, idObjeto);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        if (response.code() == RequestManager.CODIGO_SERVIDOR.OK)
        {
            CheckListParser parser = new CheckListParser(response.code());
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
            CheckListParser parser = new CheckListParser(response.code());
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
