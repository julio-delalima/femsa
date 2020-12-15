package com.femsa.requestmanager.Parser.Home;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Home.MostSeenResponse;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class MostSeenParser extends Parser<MostSeenResponse> {
    private int mErrorCode;
    private boolean esSubcategoria;

    public MostSeenParser(int errorCode, boolean esSub)
    {
        mErrorCode = errorCode;
        esSubcategoria = esSub;
    }

    @Override
    protected Response<MostSeenResponse> doInBackground(String... json) {

        JSONObject nodoPrincipal;

        try
        {
            nodoPrincipal = new JSONObject(json[0]);
        }
        catch (JSONException e)
        {
            nodoPrincipal = new JSONObject();
        }

        Response<MostSeenResponse> data = new Response<>();

        data.setError(new ErrorDTO(mErrorCode));

        boolean contieneDatos = nodoPrincipal.has(Response.DATA_KEY);

        if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK)
        {
            data.setData(new MostSeenResponse(nodoPrincipal.optJSONObject(Response.DATA_KEY), esSubcategoria));
        }

        return data;
    }
}