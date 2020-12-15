package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.Multijugador;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Juegos.RetadorDTO;
import com.femsa.requestmanager.Parser.ObjetosAprendizaje.Juegos.RetadorParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.Juegos.RetadorResponseData;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetListadoRetadoresRequest implements Callback<ResponseBody>, Parser.ParserListener<RetadorResponseData> {

    private static final String ID_PROGRAMA_KEY = "idPrograma";

    private Context mContext;

    private GetListadoRetadoresResponseContract mListener;

    public interface GetListadoRetadoresResponseContract extends ResponseContract {
        void onGetListadoRetadores(RetadorResponseData data);
        void onNoAuth();
        void onUnexpectedError(int errorcode);
        void onSinRetadores();
    }

    public GetListadoRetadoresRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(int idPrograma, String token, GetListadoRetadoresResponseContract listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestGetListadoJugadores(crearParametros(idPrograma), token);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametros(int idPrograma){
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_PROGRAMA_KEY, idPrograma);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        switch (response.code()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                RetadorParser parser = new RetadorParser(response.code());
                parser.setParserListener(this);
                try{
                    parser.traducirJSON(response.body().string());
                } catch (IOException e) {
                    mListener.onResponseErrorServidor();
                }
                break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                mListener.onSinRetadores();
                break;

            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onNoAuth();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            default:
                mListener.onUnexpectedError(response.code());
        }
    }


    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<RetadorResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onGetListadoRetadores(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                mListener.onSinRetadores();
                break;

            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onNoAuth();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            default:
                mListener.onUnexpectedError(traduccion.getError().getCodigoError());
        }
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseErrorServidor();
    }
}
