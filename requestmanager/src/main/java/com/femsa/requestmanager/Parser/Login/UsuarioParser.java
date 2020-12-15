package com.femsa.requestmanager.Parser.Login;


import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioParser extends Parser<UserResponseData> {

    private int errorCode;

    public UsuarioParser(int errorCode)
        {
            this.errorCode = errorCode;
        }

    @Override
    protected Response<UserResponseData> doInBackground(String... json)
        {
            JSONObject nodoPrincipal;
            try
                {
                    nodoPrincipal = new JSONObject(json[0]);
                }
            catch (JSONException e)
                {
                    nodoPrincipal = new JSONObject();
                }

            Response<UserResponseData> data = new Response<>();

            data.setError(new ErrorDTO(errorCode));

            boolean contieneDatos = nodoPrincipal.has(Response.DATA_KEY);

            if (contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK)
                {
                    data.setData(new UserResponseData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
                }
            else if(contieneDatos && data.getError().getCodigoError() == RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED)
                {
                    data.setData(new UserResponseData(nodoPrincipal.optString(Response.ERROR_KEY), 0));
                }

            return data;
        }
}