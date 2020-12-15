package com.femsa.requestmanager.Request.Login;

import android.content.Context;

import com.femsa.requestmanager.Parser.Login.UsuarioParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginUnicoRequest implements Callback<ResponseBody>, Parser.ParserListener<UserResponseData> {

    private static final String USER_KEY = "usuario";

    private Context mContext;
    private LoginResponseContract mListener;

    public interface LoginResponseContract extends ResponseContract
    {
        void onResponseLoginCorrecto(UserResponseData usuarioDTO);
        void OnResponseNoAuth();

    }

    public LoginUnicoRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(String user, LoginResponseContract listener) {

        mListener = listener;

        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestLoginUnico(crearParametrosLogin(user));
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametrosLogin(String user)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(USER_KEY, user);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
    {
        if (response.code() == RequestManager.CODIGO_SERVIDOR.OK)
        {
            UsuarioParser parser = new UsuarioParser(response.code());
            parser.setParserListener(this);
            try
            {
                parser.traducirJSON(response.body().string());
            }
            catch (IOException e)
            {
                mListener.onResponseErrorServidor();
            }
        }
        else if(response.code() == RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED)
        {
            UsuarioParser parser = new UsuarioParser(response.code());
            parser.setParserListener(this);
            mListener.OnResponseNoAuth();
        }
        else
        {
            mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }

    @Override
    public void jsonTraducido(Response<UserResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseLoginCorrecto(traduccion.getData());
                break;
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_CREDENCIALES_INCORRECTAS:
                // mListener.onResponseUsuarioNoExiste(mSesionFacebook);
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                if(traduccion.getError().equals("Sesion activa"))
                    mListener.OnResponseNoAuth();
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }
}
