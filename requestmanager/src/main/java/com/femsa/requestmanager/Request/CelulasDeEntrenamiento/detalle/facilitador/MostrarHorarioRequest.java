package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.facilitador;

import android.content.Context;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.MostrarHorarioData;
import com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.detalle.MostrarHorarioParser;
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
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;

public class MostrarHorarioRequest implements Callback<ResponseBody>, Parser.ParserListener<MostrarHorarioData> {

    private static final String ID_SOLICITUD_KEY = "idSolicitud";
    private static final String ID_FACILITADOR_KEY = "idFacilitador";

    private OnResponseMostrarHorario mListener;
    private Context mContext;

    /**
     * <p>Interface que define los métodos de la posible respuesta de la petición.</p>
     */
    public interface OnResponseMostrarHorario extends ResponseContract{
        void onResponseMostrarHorario(MostrarHorarioData data);
        void onResponseTokenInvalido();
    }

    public MostrarHorarioRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Método que hace la petición para obtener los detalles de una solicitud que se quiere programar.</p>
     * @param idSolicitud Id de la solicitud que se va a programar.
     * @param idFacilitador Id del facilitador que va a programar la solicitud.
     * @param tokenUsuario Token del usuario.
     * @param listener Interface para mandar una respuesta cuando no haya conexión a internet.
     */
    public void makeRequest(int idSolicitud, int idFacilitador, String tokenUsuario, OnResponseMostrarHorario listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.requestMostrarHorario(crearParametros(idSolicitud, idFacilitador), tokenUsuario);
            request.enqueue(this);
        } else {
            mListener.onResponseSinConexion();
        }

    }

    /**
     * <p>Método que permite crear el cuerpo de la petición donde se incluyen los datos de la solicitud a programar.</p>
     * @param idSolicitud Id de la solicitud que se va a programar.
     * @param idFacilitador Id del facilitador que va a programar la solicitud.
     * @retur Cuerpo de la petición.
     */
    private RequestBody crearParametros(int idSolicitud, int idFacilitador){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put(ID_SOLICITUD_KEY, idSolicitud);
        parametros.put(ID_FACILITADOR_KEY, idFacilitador);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(parametros).toString());
    }

    @Override
    public void jsonTraducido(Response<MostrarHorarioData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseMostrarHorario(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
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
            MostrarHorarioParser parser = new MostrarHorarioParser(codigoRespuesta);
            parser.setParserListener(this);
            try{
                parser.traducirJSON(response.body().string());
            } catch (IOException e){
                mListener.onResponseErrorServidor();
            }
        }
        else if (codigoRespuesta==RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED){
            mListener.onResponseTokenInvalido();
        } else {
            mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
