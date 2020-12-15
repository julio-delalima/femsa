package com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.listado;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado.ListaSolicitudesData;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ObtenerListaSolicitudesParser extends Parser<ListaSolicitudesData> {

    private int mCodigoError;

    public ObtenerListaSolicitudesParser(int codigoError){
        mCodigoError = codigoError;
    }

    @Override
    protected Response<ListaSolicitudesData> doInBackground(String... json) {

        JSONObject nodoPrincipal;

        try{
            nodoPrincipal = new JSONObject(json[0]);
        } catch (JSONException e){
            nodoPrincipal = new JSONObject();
        }

        Response<ListaSolicitudesData> data = new Response<>();
        data.setError(new ErrorDTO(mCodigoError));

        boolean contieneDatos = nodoPrincipal.has(Response.ERROR_KEY);

        if (contieneDatos && data.getError().getCodigoError()== RequestManager.CODIGO_RESPUESTA.RESPONSE_OK){
            data.setData(new ListaSolicitudesData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }
        return data;
    }
}
