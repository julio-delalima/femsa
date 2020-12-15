package com.femsa.requestmanager.Request.Notificaciones;

import android.content.Context;

import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionesVistasRequest implements Callback<ResponseBody> {

    private NotificacionesVistasResponseContract mListener;

    private Context mContext;

    public static final String ID_NOTIFICACION_KEY = "idNotificacion";

    public NotificacionesVistasRequest(Context context)
    {
        mContext = context;
    }

    public interface NotificacionesVistasResponseContract extends ResponseContract
    {
        /**
         * Se manda a llamar cuando no se tiene ninguna notificación.
         * */
        void OnNoHayNotificaciones();

        /**
         * Si la seisón ha expirado, el código de error 401 recibido nos enviará aquí.
         * */
        void OnNoAuth();

        /**
         * Llamado cuando se marca una notificación como vista
         * */

        void OnNotificacionVista();

        /**
         * Cuando se recibe un código de respuesta no esperado (diferente a 200, 401 o 500) se llama a éste método.
         * */

        void OnUnexpectedError(int codigoRespuesta);
    }

    public void makeRequest(String token, int idNotificacion, NotificacionesVistasResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods delNotificaciones = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = delNotificaciones.requestMarcarNotificacionVista(crearParametros(idNotificacion), token);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametros(int idNotificacion)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_NOTIFICACION_KEY, idNotificacion);
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

        switch (response.code())
        {
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.OnNotificacionVista();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                mListener.OnNoHayNotificaciones();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            default:
                mListener.OnUnexpectedError(response.code());
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

