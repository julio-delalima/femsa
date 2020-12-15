package com.femsa.requestmanager.Request.ImagenPerfil;
import android.content.Context;
import android.util.Log;

import com.femsa.requestmanager.DTO.User.Image.ImageResponseData;
import com.femsa.requestmanager.Parser.Image.ImageParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
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

public class ProfilePicRequest implements Callback<ResponseBody>, Parser.ParserListener<ImageResponseData> {

    private static final String IMAGE_KEY = "fotoPerfil";
    private static final String TOKEN_USUARIO_KEY = "tokenUsuario";
    private static final String EXTENSION_KEY = "extension";


    private Context mContext;
    private ProfilePicRequest.ProfilePicRequestContract mListener;

    public interface ProfilePicRequestContract extends ResponseContract
    {
        void onResponseImagenCorrecta(ImageResponseData data);
        void onNoAuth();
    }

    public ProfilePicRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(String tokenUser, String image64, ProfilePicRequest.ProfilePicRequestContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods requestImage = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = requestImage.updateProfilePic(crearParametrosNuevaImagen(tokenUser, image64));
            //Log.d(this.getClass().getSimpleName(), request.request().url().url().toString());
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }


    private RequestBody crearParametrosNuevaImagen(String token, String base64)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(TOKEN_USUARIO_KEY, token);
        params.put(IMAGE_KEY, base64);
        params.put(EXTENSION_KEY, "jpg");
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }


    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
    {
        if (response.code() == RequestManager.CODIGO_SERVIDOR.OK)
        {
            ImageParser parser = new ImageParser(response.code());
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
                mListener.onNoAuth();
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
    public void jsonTraducido(Response<ImageResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseImagenCorrecta(traduccion.getData());
                break;
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_CREDENCIALES_INCORRECTAS:
                // mListener.onResponseUsuarioNoExiste(mSesionFacebook);
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }

}
