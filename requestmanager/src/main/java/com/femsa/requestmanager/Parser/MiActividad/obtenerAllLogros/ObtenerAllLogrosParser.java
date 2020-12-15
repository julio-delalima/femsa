package com.femsa.requestmanager.Parser.MiActividad.obtenerAllLogros;

import com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros.ObtenerAllLogrosData;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ObtenerAllLogrosParser extends Parser<ObtenerAllLogrosData> {

    private int mCodigoError;

    public ObtenerAllLogrosParser(int codigoError){
        mCodigoError = codigoError;
    }

    @Override
    protected Response<ObtenerAllLogrosData> doInBackground(String... json) {

        JSONObject nodoPrincipal;

        try{
            nodoPrincipal = new JSONObject(json[0]);
        }catch (JSONException e){
            nodoPrincipal = new JSONObject();
        }

        Response<ObtenerAllLogrosData> data = new Response<>();

        data.setError(new ErrorDTO(mCodigoError));

        boolean contieneDatos = nodoPrincipal.has(Response.ERROR_KEY);

        if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK){
            data.setData(new ObtenerAllLogrosData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }
        return data;
    }
}
