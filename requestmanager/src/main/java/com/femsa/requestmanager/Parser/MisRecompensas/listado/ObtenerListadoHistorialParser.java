package com.femsa.requestmanager.Parser.MisRecompensas.listado;

import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.ObtenerListadoHistorialData;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ObtenerListadoHistorialParser extends Parser<ObtenerListadoHistorialData> {

    private int mCodigoError;

    public ObtenerListadoHistorialParser(int codigoError){
        mCodigoError = codigoError;
    }

    @Override
    protected Response<ObtenerListadoHistorialData> doInBackground(String... json) {
        JSONObject nodoPrincipal;

        try{
            nodoPrincipal = new JSONObject(json[0]);
        } catch(JSONException e){
            nodoPrincipal = new JSONObject();
        }

        Response<ObtenerListadoHistorialData> data = new Response<>();
        data.setError(new ErrorDTO(mCodigoError));

        boolean contieneDatos = nodoPrincipal.has(Response.ERROR_KEY);

        if (contieneDatos && data.getError().getCodigoError()== RequestManager.CODIGO_RESPUESTA.RESPONSE_OK){
            data.setData(new ObtenerListadoHistorialData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }
        return data;
    }
}
