package com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.detalle;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudData;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ObtenerDetalleSolicitudParser extends Parser<DetalleSolicitudData> {

    private int mCodigoError;

    public ObtenerDetalleSolicitudParser(int mCodigoError) {
        this.mCodigoError = mCodigoError;
    }

    @Override
    protected Response<DetalleSolicitudData> doInBackground(String... json) {

        JSONObject nodoPrincipal;

        try{
            nodoPrincipal = new JSONObject(json[0]);
        } catch (JSONException e){
            nodoPrincipal = new JSONObject();
        }

        Response<DetalleSolicitudData> data = new Response<>();
        data.setError(new ErrorDTO(mCodigoError));

        boolean contieneDatos = nodoPrincipal.has(Response.ERROR_KEY);

        if (contieneDatos && data.getError().getCodigoError()== RequestManager.CODIGO_RESPUESTA.RESPONSE_OK){
            data.setData(new DetalleSolicitudData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }
        return data;
    }
}
