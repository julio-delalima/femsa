package com.femsa.requestmanager.Parser.ObjetosAprendizaje;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.EncuestaResponseData;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;


public class EncuestaParser extends Parser<EncuestaResponseData> {

    private int mErrorCode;

    public EncuestaParser(int errorCode)
    {
        mErrorCode = errorCode;
    }

    @Override
    protected Response<EncuestaResponseData> doInBackground(String... json) {

        JSONObject nodoPrincipal;
        try
        {
            nodoPrincipal = new JSONObject(json[0]);
        }
        catch (JSONException e)
        {
            nodoPrincipal = new JSONObject();
        }

        Response<EncuestaResponseData> data = new Response<>();

        data.setError(new ErrorDTO(mErrorCode));

        boolean contieneDatos = nodoPrincipal.has(Response.DATA_KEY);

        if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK)
        {
            try {
                data.setData(new EncuestaResponseData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
}
