package com.femsa.requestmanager.Request.Login;

import android.content.Context;

import com.femsa.requestmanager.Parser.Login.ExistanceParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Login.ExistanceResponseData;
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

public class ExistanceRequest implements Callback<ResponseBody>, Parser.ParserListener<ExistanceResponseData> {

    private static final String EMPLOYEE_KEY = "numEmpleado";

    private Context mContext;

    private ExistanceResponseContract mListener;

    public interface ExistanceResponseContract extends ResponseContract {
        void OnResponseThereIsNoEmployee();
        void OnResponseEmployeeExists();
    }
    public ExistanceRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(String employeeNumber, ExistanceResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.checkEmployeeExistance(createParameters(employeeNumber));
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }


    private RequestBody createParameters(String employeeNumber)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(EMPLOYEE_KEY, employeeNumber);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void jsonTraducido(Response<ExistanceResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnResponseEmployeeExists();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnResponseThereIsNoEmployee();
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        if (response.code() == RequestManager.CODIGO_SERVIDOR.OK)
            {
                ExistanceParser parser = new ExistanceParser(response.code());
                parser.setParserListener(this);
                try
                {
                    parser.traducirJSON(response.body().string());
                }
                catch (IOException e)
                {
                    mListener.onResponseErrorServidor();
                }
            }
        else if(response.code() == RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED)
            {
                mListener.OnResponseThereIsNoEmployee();
            }
        else
            {
                mListener.onResponseErrorServidor();
            }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseErrorServidor();
    }
}
