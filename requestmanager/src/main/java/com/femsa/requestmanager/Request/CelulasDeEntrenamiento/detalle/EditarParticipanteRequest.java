package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.detalle;

import android.content.Context;

import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Insomnia: editarSolicitudParticipante
 */
public class EditarParticipanteRequest implements Callback<ResponseBody> {

    private static final String ID_SOLICITUD_KEY = "idSolicitud";
    private static final String ID_EMPLEADOS_KEY = "idEmpleados";
    private static final String ACCION_KEY = "accion";

    private OnResponseEditarParticipante mListener;
    private Context mContext;

    /**
     * <p>Constructor del objeto que realiza la petición.</p>
     * @param context Contexto de la aplicación para saber si hay conexión a internet.
     */
    public EditarParticipanteRequest(Context context){
        mContext = context;
    }

    /**
     * <p>Interface que contiene los métodos para manejar las respuestas de la petición.</p>
     */
    public interface OnResponseEditarParticipante extends ResponseContract {
        void onResponseParticipantesEditados();
        void onResponseTokenInvalido();
    }

    /**
     * <p>Método que permite hacer la petición para agregar a un participante.</p>
     * @param idSolicitud Id de la solicitud que se va a editar.
     * @param idEmpleados Ids de los empleados que se van a editar.
     * @param accion True si se van a agregar a los participantes.
     * @param tokenUsuario Token del usuario que realiza la petición.
     * @param listener Listener para manejar las respuestas de la petición.
     */
    public void makeRequestAgregarParticipante(String idSolicitud, ArrayList<Integer> idEmpleados, boolean accion, String tokenUsuario,
                            OnResponseEditarParticipante listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.requestEditarParticipante(crearParametros(idSolicitud, idEmpleados, true), tokenUsuario);
            request.enqueue(this);
        } else {
            mListener.onResponseSinConexion();
        }
    }

    /**
     * <p>Método que permite hacer la petición para eliminar a un participante.</p>
     * @param idSolicitud Id de la solicitud que se va a editar.
     * @param idEmpleados Ids de los empleados que se van a editar.
     * @param accion False si se van a eliminar a los participantes.
     * @param tokenUsuario Token del usuario que realiza la petición.
     * @param listener Listener para manejar las respuestas de la petición.
     */
    public void makeRequestEliminarParticipante(String idSolicitud, ArrayList<Integer> idEmpleados, boolean accion, String tokenUsuario,
                            OnResponseEditarParticipante listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)){
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethod.requestEditarParticipante(crearParametros(idSolicitud, idEmpleados, false), tokenUsuario);
            request.enqueue(this);
        } else {
            mListener.onResponseSinConexion();
        }
    }

    /**
     * <p>Método que crea el cuerpo que contiene los datos para realizar la petición.</p>
     * @param idSolicitud Id de la solicitud.
     * @param idEmpleados Id de los empleados que se van a editar.
     * @param accion True si se va a agregar al participante a la solicitud.
     * @return Cuerpo de la petición.
     */
    private RequestBody crearParametros(String idSolicitud, ArrayList<Integer> idEmpleados, boolean accion){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put(ID_SOLICITUD_KEY, idSolicitud);
        parametros.put(ID_EMPLEADOS_KEY, idEmpleados);
        parametros.put(ACCION_KEY, accion);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(parametros).toString());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        switch (codigoRespuesta){
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseParticipantesEditados();
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
