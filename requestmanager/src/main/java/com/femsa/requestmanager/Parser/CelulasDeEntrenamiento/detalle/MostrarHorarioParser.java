package com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.detalle;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.MostrarHorarioData;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class MostrarHorarioParser extends Parser<MostrarHorarioData> {

    private int mCodigoError;

    public MostrarHorarioParser(int codigoError){
        mCodigoError = codigoError;
    }

    @Override
    protected Response<MostrarHorarioData> doInBackground(String... json) {

        JSONObject nodoPrincipal;

        try {
            nodoPrincipal = new JSONObject(json[0]);
        } catch (JSONException e){
            nodoPrincipal = new JSONObject();
        }

        Response<MostrarHorarioData> data = new Response<>();
        data.setError(new ErrorDTO(mCodigoError));

        boolean contieneDatos = nodoPrincipal.has(Response.ERROR_KEY);

        if (contieneDatos && data.getError().getCodigoError()== RequestManager.CODIGO_RESPUESTA.RESPONSE_OK){
            data.setData(new MostrarHorarioData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }
        return data;
    }
}
