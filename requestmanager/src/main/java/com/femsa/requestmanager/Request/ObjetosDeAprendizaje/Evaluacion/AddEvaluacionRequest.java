package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Evaluacion;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEvaluacionRequest implements Callback<ResponseBody> {

    private static final String ID_OBKJETO_KEY = "idObjeto";
    private static final String CHECKLIST_KEY = "checklist";
    private static final String ID_EVALUACION_KEY = "idEvaluacion";
    private static final String ID_RESPUESTA_KEY = "idRespuesta";
    private static final String APROBADO_KEY = "aprobado";
    private static final String CALIF_KEY = "calificacion";

    private Context mContext;

    private EvaluacionResponseContract mListener;

    public interface EvaluacionResponseContract extends ResponseContract
    {
        void OnCheckSuccess();
        void OnNoAuth();
        void OnUnexpectedError(int codigoRespuesta);
    }

    public AddEvaluacionRequest(Context context)
    {
        mContext = context;
    }

    private RequestBody makeParameters(int idObjeto, ArrayList<String> idRespuestas, ArrayList<String> idEvaluacion, int aprobado, int calif)
    {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        ArrayList<HashMap> arregloCheks = new ArrayList<>();
        for(int i = 0; i < idRespuestas.size(); i++)
        {
            HashMap<String, Object> idCheckHash = new HashMap<>();
            idCheckHash.put(ID_EVALUACION_KEY, idEvaluacion.get(i));
            idCheckHash.put(ID_RESPUESTA_KEY, Integer.parseInt(idRespuestas.get(i)));
            arregloCheks.add(idCheckHash);
        }
        params.put(ID_OBKJETO_KEY, idObjeto);
        params.put(APROBADO_KEY, aprobado);
        params.put(CHECKLIST_KEY, arregloCheks);
        params.put(CALIF_KEY, calif);
        //Log.d("evaluar", new JSONObject(params).toString());
        return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
    }

    public void makeRequest(String token, int idObjeto, ArrayList<String> datos, ArrayList<String> idEvaluacion, int aprobado, int calif, EvaluacionResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
        {
            RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request = postMethod.requestInsertaEvaluacion(makeParameters(idObjeto, datos, idEvaluacion, aprobado, calif), token);
            request.enqueue(this);
        }
        else
        {
            mListener.onResponseSinConexion();
        }
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        switch(response.code())
        {
            case RequestManager.CODIGO_SERVIDOR.OK:
                mListener.OnCheckSuccess();
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
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        mListener.onResponseTiempoAgotado();
    }


}
