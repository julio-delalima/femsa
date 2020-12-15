package com.femsa.requestmanager.Request.Login;

import android.content.Context;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Login.ValidateDateResponseData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ValidateDateRequest implements Callback<ResponseBody>, Parser.ParserListener<ValidateDateResponseData> {

    private static final String DATE_KEY = "fechaNacimiento";
    private static final String ID_EMP_KEY = "idEmpleado";

    private Context mContext;
    private ValidateDateResponseContract mListener;

    public interface ValidateDateResponseContract extends ResponseContract
    {
        void OnDateSuccess();
        void OnResponseNoAuth();
    }

    public ValidateDateRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(String date, int employeeID, ValidateDateResponseContract listener) {

        mListener = listener;

        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestCheckDate(crearParametrosLogin(date, employeeID + ""));
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametrosLogin(String date, String idemployee)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_EMP_KEY, idemployee);
        params.put(DATE_KEY, date);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
    {
        if (response.code() == RequestManager.CODIGO_SERVIDOR.OK)
        {
            mListener.OnDateSuccess();
        }
        else if(response.code() == RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED)
        {
            mListener.OnResponseNoAuth();
        }
        else
        {
            mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }

    @Override
    public void jsonTraducido(Response<ValidateDateResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                    mListener.OnDateSuccess();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                    mListener.OnResponseNoAuth();
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }
}
