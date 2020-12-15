package com.femsa.requestmanager.Parser.Home;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class CategoriesParser extends Parser<CategoriesResponse> {

    private int mErrorCode;

    public CategoriesParser(int errorCode)
    {
        mErrorCode = errorCode;
    }

    @Override
    protected Response<CategoriesResponse> doInBackground(String... json) {

        JSONObject nodoPrincipal;

        try
        {
            nodoPrincipal = new JSONObject(json[0]);
        }
        catch (JSONException e)
        {
            nodoPrincipal = new JSONObject();
        }

        Response<CategoriesResponse> data = new Response<>();

        data.setError(new ErrorDTO(mErrorCode));

        boolean contieneDatos = nodoPrincipal.has(Response.DATA_KEY);

        if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK)
        {
            data.setData(new CategoriesResponse(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }

        return data;
    }
}
