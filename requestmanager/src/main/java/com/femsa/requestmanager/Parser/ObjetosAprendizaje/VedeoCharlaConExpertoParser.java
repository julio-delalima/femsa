package com.femsa.requestmanager.Parser.ObjetosAprendizaje;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.VedeoCharlaConExpertoResponseData;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;


public class VedeoCharlaConExpertoParser extends Parser<VedeoCharlaConExpertoResponseData> {

    private int mErrorCode;

    public VedeoCharlaConExpertoParser(int errorCode)
    {
        mErrorCode = errorCode;
    }

    @Override
    protected Response<VedeoCharlaConExpertoResponseData> doInBackground(String... json) {

        JSONObject nodoPrincipal;
        try
        {
            nodoPrincipal = new JSONObject(json[0]);
        }
        catch (JSONException e)
        {
            nodoPrincipal = new JSONObject();
        }

        Response<VedeoCharlaConExpertoResponseData> data = new Response<>();

        data.setError(new ErrorDTO(mErrorCode));

        boolean contieneDatos = nodoPrincipal.has(Response.DATA_KEY);

        if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK)
        {
            try {
                data.setData(new VedeoCharlaConExpertoResponseData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
}
