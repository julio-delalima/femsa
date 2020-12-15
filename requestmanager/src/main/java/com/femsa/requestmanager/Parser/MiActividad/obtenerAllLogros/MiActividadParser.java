package com.femsa.requestmanager.Parser.MiActividad.obtenerAllLogros;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.MiActividad.MiActividadResponseData;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class MiActividadParser extends Parser<MiActividadResponseData> {

    private int mCodigoError;

    public MiActividadParser(int codigoError){
        mCodigoError = codigoError;
    }

    @Override
    protected Response<MiActividadResponseData> doInBackground(String... json) {

        JSONObject nodoPrincipal;

        try{
            nodoPrincipal = new JSONObject(json[0]);
        }catch (JSONException e){
            nodoPrincipal = new JSONObject();
        }

        Response<MiActividadResponseData> data = new Response<>();

        data.setError(new ErrorDTO(mCodigoError));

        boolean contieneDatos = nodoPrincipal.has(Response.ERROR_KEY);

        if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK){
            try {
                data.setData(new MiActividadResponseData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
