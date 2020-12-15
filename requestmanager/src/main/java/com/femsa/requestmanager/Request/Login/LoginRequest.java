package com.femsa.requestmanager.Request.Login;

import android.content.Context;

import com.femsa.requestmanager.Parser.Login.UsuarioParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Cifrado;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginRequest implements Callback<ResponseBody>, Parser.ParserListener<UserResponseData> {

    private static final String USER_KEY = "usuario";
    private static final String HASH_ADDITION = "femsa";
    private static final String PASSWORD_KEY = "credencial";
    private static final String TOKEN_FIREBASE_KEY = "tokenFirebase";
    private static final String ID_SO_KEY = "idSistemaOperativo";

    private Context mContext;
    private LoginResponseContract mListener;

    public interface LoginResponseContract extends ResponseContract
    {
        void onResponseLoginCorrecto(UserResponseData usuarioDTO);
        void onResponseUsuarioNoExiste();
        void onResponseActiveSession();
        void onResponseCredencialesIncorrectas();
        void onResponseBlockedUser();
    }

    public LoginRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(String user, String pass, String tokenFirebase,
                            LoginResponseContract listener) {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestLogin(crearParametrosLogin(user, pass, tokenFirebase));
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametrosLogin(String user, String pass, String tokenFirebase)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(USER_KEY, user);
        //params.put(PASSWORD_KEY, Encripter.getMD5Hash(pass + HASH_ADDITION));
        params.put(PASSWORD_KEY, Cifrado.SHA256(pass + HASH_ADDITION));
        params.put(TOKEN_FIREBASE_KEY, tokenFirebase);
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
            try
            {
                parser.traducirJSON(response.errorBody().string() );
            }
            catch (IOException e)
            {
                mListener.onResponseErrorServidor();
            }
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
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_ERROR_CREACION:
                mListener.onResponseCredencialesIncorrectas();
                break;
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_CREDENCIALES_INCORRECTAS:
                // mListener.onResponseUsuarioNoExiste(mSesionFacebook);
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                String tempError = traduccion.getData().getmError();
                if(tempError.contains("Sesión activa"))
                    {
                        mListener.onResponseActiveSession();
                    }
                else if(tempError.contains("El usuario o contraseña son inválidos"))
                    {
                        mListener.onResponseCredencialesIncorrectas();
                    }
                else if(tempError.contains("Usuario bloqueado contacte a su administrador"))
                    {
                        mListener.onResponseBlockedUser();
                    }
                else
                    {
                        mListener.onResponseErrorServidor();
                    }
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }
}
