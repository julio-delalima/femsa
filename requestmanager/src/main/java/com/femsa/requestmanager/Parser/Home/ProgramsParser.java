package com.femsa.requestmanager.Parser.Home;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Home.ProgramsResponse;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ProgramsParser extends Parser<ProgramsResponse> {

    private int mErrorCode;

    public ProgramsParser(int errorCode)
    {
        mErrorCode = errorCode;
    }

    @Override
    protected Response<ProgramsResponse> doInBackground(String... json) {

        JSONObject nodoPrincipal;

        try
        {
            nodoPrincipal = new JSONObject(json[0]);
        }
        catch (JSONException e)
        {
            nodoPrincipal = new JSONObject();
        }

        Response<ProgramsResponse> data = new Response<>();

        data.setError(new ErrorDTO(mErrorCode));

        boolean contieneDatos = nodoPrincipal.has(Response.DATA_KEY);

        if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK)
        {
            data.setData(new ProgramsResponse(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }

        return data;
    }
}
