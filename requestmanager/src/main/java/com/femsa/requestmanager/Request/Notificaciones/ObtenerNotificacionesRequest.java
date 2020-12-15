package com.femsa.requestmanager.Request.Notificaciones;

import android.content.Context;

import com.femsa.requestmanager.Parser.Notificaciones.NotificacionesParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.NotificacionesResponse.NotificacionesResponseData;
import com.femsa.requestmanager.Utilities.Utilities;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObtenerNotificacionesRequest implements Callback<ResponseBody>, Parser.ParserListener<NotificacionesResponseData> {

    private OnNotificacionesResponseContract mListener;

    private Context mContext;

    public ObtenerNotificacionesRequest(Context context)
    {
        mContext = context;
    }

    public interface OnNotificacionesResponseContract extends ResponseContract
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
         * Cuando se hayan obtenido todas las notificaciones se llamará a éste método que las contendrá
         * */

        void OnTodasLasNotificaciones(NotificacionesResponseData data);

        /**
         * Cuando se recibe un código de respuesta no esperado (diferente a 200, 401 o 500) se llama a éste método.
         * */

        void OnUnexpectedError(int codigoRespuesta);
    }

    public void makeRequest(String token, OnNotificacionesResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.GetMethods getNotificaciones = RequestManager.getCliente().create(RequestManager.GetMethods.class);
            Call<ResponseBody> request;
            request = getNotificaciones.requestObtenerTodasLasNotificaciones(token);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
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
                    NotificacionesParser parser = new NotificacionesParser(response.code());
                    parser.setParserListener(this);
                    try
                        {
                            assert response.body() != null;
                            parser.traducirJSON(response.body().string());
                        }
                    catch (IOException e)
                        {
                            mListener.onResponseErrorServidor();
                        }
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

    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<NotificacionesResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnTodasLasNotificaciones(traduccion.getData());
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
                mListener.OnUnexpectedError(traduccion.getError().getCodigoError());
        }
    }

}
