package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Participante;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteData;
import com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.Participante.ParticipanteParser;
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

public class ObtenerParticpante implements Callback<ResponseBody>, Parser.ParserListener<ParticipanteData> {

    private static final String NUMERO_USUARIO_KEY = "numRedParticipante" ;
    private ObtenerParticpante.OnResponseObtenerParticipante mListener;
    private Context mContext;

    public interface OnResponseObtenerParticipante extends ResponseContract{
     void onResponseObtenerParticipante(ParticipanteData data);
     void onResponseTokenInvalido();
     void onResponseMensajeInesperado(int codigoRespuesta);
    }

    public ObtenerParticpante(Context context){
        mContext = context;
    }

    public void makeRequest(String tokenUsuario, int numUsuario, OnResponseObtenerParticipante Listener){
        mListener = Listener;
        if(Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethods = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethods.requestObtenerParticipantes(crearParametrosObtenerParticipantes(numUsuario),tokenUsuario);
            request.enqueue(this);
        }else {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametrosObtenerParticipantes(int numUsurio){
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(NUMERO_USUARIO_KEY, numUsurio);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON),new JSONObject(params).toString());
    }

    @Override
    public void jsonTraducido(Response<ParticipanteData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerParticipante(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            case RequestManager.CODIGO_SERVIDOR.NOT_IMPLEMENTED:
                mListener.onResponseErrorServidor();
                default:
                    mListener.onResponseErrorServidor();
                    break;
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
        {
            int codigoRespuesta = response.code();
            switch (codigoRespuesta){
                case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                    ParticipanteParser parser = new ParticipanteParser(codigoRespuesta);
                    parser.setParserListener(this);
                    try
                    {
                        parser.traducirJSON(response.body().string());
                    }
                    catch (IOException e)
                    {
                        mListener.onResponseErrorServidor();
                    }
                break;
                case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                    mListener.onResponseTokenInvalido();
                break;
                default:
                    mListener.onResponseErrorServidor();
            }
        }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }


}
