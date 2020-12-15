package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle.facilitador;

import android.content.Context;

import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Insomnia: actualizarFacilitador
 */
public class ActualizarFacilitadorRequest implements Callback<ResponseBody> {

    private static final String CORREO_KEY = "correo";
    private static final String ID_SOLICITUD_KEY = "idSolicitud";
    private static final String ID_FACILITADOR_KEY = "idFacilitado";
    private static final String RESPONSABLE_KEY = "responsable";
    private static final String DISPONIBLE_KEY = "disponible";
    private static final String NOMBRE_KEY = "nombre";

    private OnResponseActualizarFacilitador mListener;
    private Context mContext;

    /**
     * <p>Constructor de la petición.</p>
     * @param context Contexto para verificar que exista conexión a internet.
     */
    public ActualizarFacilitadorRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Interface que contiene los métodos para manejar las respuestas de la petición.</p>
     */
    public interface OnResponseActualizarFacilitador extends ResponseContract {
        void onResponseFacilitadorActualizado();
        void onResponseTokenInvalido();
        void onResponseNoHayFacilitador();
    }

    /**
     * <p>Método que crea el cuerpo de la petición.</p>
     * @param correo Correo de la persona responsable.
     * @param idSolicitud Id de la solicitud.
     * @param idFacilitador Id del facilitador que hace la petición.
     * @param nombre Nombre de la próxima personal responsable.
     * @return Cuerpo de la petición.
     */
    private RequestBody crearParametros(String correo, String idSolicitud, String idFacilitador,
                                        boolean responsable, boolean disponible, String nombre){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put(CORREO_KEY, correo);
        parametros.put(ID_SOLICITUD_KEY, idSolicitud);
        parametros.put(ID_FACILITADOR_KEY, idFacilitador);
        parametros.put(RESPONSABLE_KEY, responsable);
        parametros.put(DISPONIBLE_KEY, disponible);
        parametros.put(NOMBRE_KEY, nombre);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(parametros).toString());
    }

    /**
     * <p>Método que realiza la petición para actualizar a un facilitador.</p>
     * @param correo Correo del nuevo facilitador.
     * @param idSolicitud Id de la solicitud que se va a actualizar.
     * @param idFacilitador Id del facilitador que realiza la petición.
     * @param responsable Bandera para indicar si el facilitador no se el responsable.
     * @param disponible Bandera para indicar si el facilitador no se encuentra disponible.
     * @param nombre Nombre del nuevo facilitador.
     * @param tokenUsuario Token del usuario que realiza la petición.
     * @param listener Listener para manejar las respuestas de la petición.
     */
    public void makeRequest(String correo, String idSolicitud, String idFacilitador, boolean responsable,
                            boolean disponible, String nombre, String tokenUsuario, OnResponseActualizarFacilitador listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.requestActualizarSolicitud(crearParametros(correo, idSolicitud, idFacilitador, responsable, disponible, nombre), tokenUsuario);
            request.enqueue(this);
        } else {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        switch (codigoRespuesta){
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseFacilitadorActualizado();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            case RequestManager.CODIGO_SERVIDOR.NOT_IMPLEMENTED:
                mListener.onResponseNoHayFacilitador();
            default:
                mListener.onResponseErrorServidor();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
