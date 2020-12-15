package com.femsa.requestmanager.Request.Configuracion;

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

public class HabilitarNotificacionesRequest implements Callback<ResponseBody> {

    private Context mContext;
    private HabilitarNotificacionesContract mListener;
    private static final String NOTIF_KEY = "notificacionActiva";

    public interface HabilitarNotificacionesContract extends ResponseContract {

        void OnNotificacionCambiada();

        void OnNoAuth();

        void OnUnexpectedError(int codigoRespuesta);
    }

    public HabilitarNotificacionesRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(String userToken, boolean notificaciones, HabilitarNotificacionesContract listener) {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext)) {
            RequestManager.PostMethods getMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = getMethod.requestHabilitarNotificaciones(userToken, creaParametrosNotif(notificaciones));
            request.enqueue(this);
        } else {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody creaParametrosNotif(boolean iswifi){
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(NOTIF_KEY, iswifi);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        switch (response.code()) {
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.OnNotificacionCambiada();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            default:
                mListener.OnUnexpectedError(response.code());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}