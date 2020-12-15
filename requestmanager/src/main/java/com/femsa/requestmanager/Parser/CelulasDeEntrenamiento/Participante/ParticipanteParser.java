package com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.Participante;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteData;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ParticipanteParser extends Parser<ParticipanteData> {
    private int mCodigoError;

    public ParticipanteParser(int mCodigoError){
        this.mCodigoError = mCodigoError;
    }

    @Override
    protected Response<ParticipanteData> doInBackground(String... json) {
        JSONObject nodoPrincipal;

        try {
            nodoPrincipal = new JSONObject(json[0]);
        } catch (JSONException e) {
            nodoPrincipal = new JSONObject();
        }
        Response<ParticipanteData> data = new Response<>();
        data.setError(new ErrorDTO(mCodigoError));
        boolean contienedatos = nodoPrincipal.has(Response.ERROR_KEY);

        if (contienedatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK){
            data.setData(new ParticipanteData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
        }
        return data;
    }
}
