package com.femsa.requestmanager.Parser.Login;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Login.ExistanceResponseData;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ExistanceParser extends Parser<ExistanceResponseData> {

    private int errorCode;

    public ExistanceParser(int errorCode)
    {
        this.errorCode = errorCode;
    }

    @Override
    protected Response<ExistanceResponseData> doInBackground(String... json) {
        JSONObject nodoPrincipal;
        try
        {
            nodoPrincipal = new JSONObject(json[0]);
        }
        catch (JSONException e)
        {
            nodoPrincipal = new JSONObject();
        }

        Response<ExistanceResponseData> data = new Response<>();

        data.setError(new ErrorDTO(errorCode));

        boolean contieneDatos = nodoPrincipal.has(Response.DATA_KEY);

        if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK)
        {
            data.setData(new ExistanceResponseData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }

        return data;
    }
}
