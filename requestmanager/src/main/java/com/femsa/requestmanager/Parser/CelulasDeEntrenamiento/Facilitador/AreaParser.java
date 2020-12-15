package com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.Facilitador;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.Area.AreaData;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class AreaParser extends Parser<AreaData> {

    private int mCodigoError;

    public AreaParser(int mCodigoError){
        this.mCodigoError = mCodigoError;
    }

    @Override
    protected Response<AreaData> doInBackground(String... json) {
        JSONObject nodoPrincipal;

        try
            {
                nodoPrincipal = new JSONObject(json[0]);
            }
        catch (JSONException e)
            {
                nodoPrincipal = new JSONObject();
            }
        Response<AreaData> data = new Response<>();
        data.setError( new ErrorDTO(mCodigoError));

        boolean continedatos = nodoPrincipal.has(Response.ERROR_KEY);

        if (continedatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK){
            data.setData(new AreaData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }

        return data;
    }
}
