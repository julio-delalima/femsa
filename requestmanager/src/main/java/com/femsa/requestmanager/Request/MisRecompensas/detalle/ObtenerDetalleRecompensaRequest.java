package com.femsa.requestmanager.Request.MisRecompensas.detalle;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.MisRecompensas.detalle.DetalleRecompensaData;
import com.femsa.requestmanager.Parser.MisRecompensas.detalle.ObtenerDetalleRecompensaParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ObtenerDetalleRecompensaRequest implements Callback<ResponseBody>, Parser.ParserListener<DetalleRecompensaData> {

    private static final String ID_RECOMPENSA = "idRecompensas";

    private OnResponseObtenerDetalleRecompensa mListener;
    private Context mContext;

    public ObtenerDetalleRecompensaRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Interfaz que define los métodos de las posibles respuestas del servidor.</p>
     */
    public interface OnResponseObtenerDetalleRecompensa extends ResponseContract {
        void onResponseObtenerDetalleRecompensa(DetalleRecompensaData data);
        void onResponseTokenInvalido();
    }

    public void makeRequest(int idRecompensa, String tokenUsuario, OnResponseObtenerDetalleRecompensa listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.requestObtenerDetalleRecompensa(crearParametros(idRecompensa), tokenUsuario);
            request.enqueue(this);
        } else {
            mListener.onResponseSinConexion();
        }
    }

    /**
     * <p>Método que permite crear el cuerpo de la petición para mandar el detalle de la recompensa.</p>
     * @param idRecompensa Detalle de la recompensa que se quiere visualizar.
     * @return Cuerpo de la petición.
     */
    private RequestBody crearParametros(int idRecompensa){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put(ID_RECOMPENSA, idRecompensa);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(parametros).toString());
    }

    @Override
    public void jsonTraducido(Response<DetalleRecompensaData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerDetalleRecompensa(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            case RequestManager.CODIGO_SERVIDOR.REQUEST_TIMEOUT:
                mListener.onResponseTiempoAgotado();
                break;
            default:
                mListener.onResponseErrorServidor();
                break;
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        if (codigoRespuesta== RequestManager.CODIGO_SERVIDOR.OK){
            ObtenerDetalleRecompensaParser parser = new ObtenerDetalleRecompensaParser(codigoRespuesta);
            parser.setParserListener(this);
            try{
                parser.traducirJSON(response.body().string());
            } catch (IOException e){
                mListener.onResponseErrorServidor();
            }
        } else if (codigoRespuesta==RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED){
            mListener.onResponseTokenInvalido();
        }
        else {
            mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
