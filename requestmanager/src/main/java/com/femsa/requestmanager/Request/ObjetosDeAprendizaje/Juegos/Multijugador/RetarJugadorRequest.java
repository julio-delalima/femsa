package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Juegos.Multijugador;

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

import static com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.CheckList.CheckListInnerData.ID_OBJETO_KEY;

public class RetarJugadorRequest implements Callback<ResponseBody> {

    private static final String ID_EMPLEADO_KEY = "idEmpleado";
    private static final String ID_PROGRAMA_KEY = "idPrograma";

    private Context mContext;
    private RetarResponseContract mListener;

    public interface RetarResponseContract extends ResponseContract
    {
        void onResponseRetoEnviado();
        void onNoAuth();
        void onUnexpectedError(int code);
    }

    public RetarJugadorRequest(Context context) {
        mContext = context;
    }

    public void makeRequest(int idEmpleado, int idObjeto, int idPrograma, String token, RetarResponseContract listener) {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestRetarJugador(crearParametros(idEmpleado, idObjeto, idPrograma), token);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametros(int idEmpleado, int idObjeto, int idPrograma)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ID_EMPLEADO_KEY, idEmpleado);
        params.put(ID_OBJETO_KEY, idObjeto);
        params.put(ID_PROGRAMA_KEY, idPrograma);
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
    {
        switch (response.code()){
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.onResponseRetoEnviado();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onNoAuth();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            default:
                mListener.onUnexpectedError(response.code());
        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
