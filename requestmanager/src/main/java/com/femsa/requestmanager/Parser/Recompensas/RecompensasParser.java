package com.femsa.requestmanager.Parser.Recompensas;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Recompensa.RecompensaResponseData;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class RecompensasParser extends Parser<RecompensaResponseData> {

    private int mErrorCode;

    public RecompensasParser(int errorCode)
    {
        mErrorCode = errorCode;
    }

    @Override
    protected Response<RecompensaResponseData> doInBackground(String... json) {

        JSONObject nodoPrincipal;
        try
        {
            nodoPrincipal = new JSONObject(json[0]);
        }
        catch (JSONException e)
        {
            nodoPrincipal = new JSONObject();
        }

        Response<RecompensaResponseData> data = new Response<>();

        data.setError(new ErrorDTO(mErrorCode));

        boolean contieneDatos = nodoPrincipal.has(Response.DATA_KEY);

        if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK)
        {
            data.setData(new RecompensaResponseData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }

        return data;
    }
}

