package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudData;
import com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.detalle.ObtenerDetalleSolicitudParser;
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

public class ObtenerDetalleSolicitudRequest implements Callback<ResponseBody>, Parser.ParserListener<DetalleSolicitudData> {

    private static final String ID_SOLICITUD_KEY = "idSolicitud";

    private OnResponseObtenerDetalleSolicitud mListener;
    private Context mContext;

    /**
     * <p>Interfaz que define los métodos de las posibles respuestas de la petición.</p>
     */
    public interface OnResponseObtenerDetalleSolicitud extends ResponseContract{
        void onResponseObtenerDetalleSolicitud(DetalleSolicitudData data);
        void onResponseTokenInvalido();
    }

    public ObtenerDetalleSolicitudRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Método que permite realizar la petición para obtener el detalle de una solicitud.</p>
     * @param idSolicitud Id de la solicitud de la cual se quiere obtener el detalle.
     * @param tokenUsuario Token del usuario que realiza la petición.
     * @param listener Listener para manejar las posibles respuestas de la petición.
     */
    public void makeRequest(int idSolicitud, String tokenUsuario, OnResponseObtenerDetalleSolicitud listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.obtenerDetalleSolicitud(crearParametros(idSolicitud), tokenUsuario);
            request.enqueue(this);
        } else{
            mListener.onResponseSinConexion();
        }
    }

    /**
     * <p>Método que crea el cuerpo de la petición para agregar el id de la solicitud.</p>
     * @param idSolicitud Id de la solicitud de la cual se quieren obtener los detalles.
     * @return Cuerpo de la petición.
     */
    private RequestBody crearParametros(int idSolicitud){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put(ID_SOLICITUD_KEY, idSolicitud);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(parametros).toString());
    }

    @Override
    public void jsonTraducido(Response<DetalleSolicitudData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseObtenerDetalleSolicitud(traduccion.getData());
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
        if (codigoRespuesta==RequestManager.CODIGO_SERVIDOR.OK){
            ObtenerDetalleSolicitudParser parser = new ObtenerDetalleSolicitudParser(codigoRespuesta);
            parser.setParserListener(this);
            try{
                parser.traducirJSON(response.body().string());
            } catch (IOException e){
                mListener.onResponseErrorServidor();
            }
        }
        else if (codigoRespuesta==RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED){
            mListener.onResponseTokenInvalido();
        }
        else{
            mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
