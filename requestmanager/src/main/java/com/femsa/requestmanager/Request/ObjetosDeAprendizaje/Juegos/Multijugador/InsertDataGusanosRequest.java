package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.Multijugador;


import android.content.Context;
import android.util.Log;

import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertDataGusanosRequest implements Callback<ResponseBody> {

    private static final String ID_PARTIDA_KEY = "idPartida";
    private static final String USADO_KEY = "usado";
    private static final String POS_J1_KEY = "posJ1";
    private static final String POS_J2_KEY = "posJ2";
    private static final String TURNO_KEY = "turno";
    private static final String MAPA_KEY = "mapa";

    private Context mContext;

    private GusanosResponseContract mListener;

    public interface GusanosResponseContract extends ResponseContract{
        void OnAddDataSuccess();
        void OnNoAuth();
        void OnUnexpectedError(int errorcode);
    }

    public InsertDataGusanosRequest(Context context) {
        mContext = context;
    }

    private RequestBody creaParametros(int idPartida, int usado, int posj1, int posj2, int mapa, int turno){
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_PARTIDA_KEY, idPartida);
        params.put(USADO_KEY, usado);
        params.put(POS_J1_KEY, posj1);
        params.put(POS_J2_KEY, posj2);
        params.put(TURNO_KEY, turno);
        params.put(MAPA_KEY, mapa);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    public void makeRequest(String token, int idempleado, int usado, int posj1, int posj2, int mapa, int turno, GusanosResponseContract listener){
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestEnviarDatosGusanosEscaleras(creaParametros(idempleado, usado, posj1, posj2, mapa, turno), token);
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
        switch(response.code())
        {
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.OnAddDataSuccess();
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

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
