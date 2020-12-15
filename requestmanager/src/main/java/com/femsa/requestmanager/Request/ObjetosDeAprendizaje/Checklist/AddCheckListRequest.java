package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.Checklist;

import android.content.Context;
import androidx.annotation.NonNull;

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

public class AddCheckListRequest implements Callback<ResponseBody> {

    private static final String ID_OBKJETO_KEY = "idObjeto";
    private static final String CHECKLIST_KEY = "checklist";
    private static final String ID_CHECK_KEY = "idCheck";

    private Context mContext;

    private AddChecklistResponseContract mListener;

    public interface AddChecklistResponseContract extends ResponseContract
    {
        void OnCheckSuccess();
        void OnNoAuth();
        void OnUnexpectedError(int codigoRespuesta);
    }

        public AddCheckListRequest(Context context)
        {
            mContext = context;
        }

        private RequestBody makeParameters(int idObjeto, ArrayList<String> datos)
        {
            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            ArrayList<HashMap> arregloCheks = new ArrayList<>();
            for(int i = 0; i < datos.size(); i++)
                {
                    HashMap<String, Object> idCheckHash = new HashMap<>();
                    idCheckHash.put(ID_CHECK_KEY, Integer.parseInt(datos.get(i)));
                    arregloCheks.add(idCheckHash);
                }
            params.put(ID_OBKJETO_KEY, idObjeto);
            params.put(CHECKLIST_KEY, arregloCheks);
            return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
        }

        public void makeRequest(String token, int idObjeto, ArrayList<String> datos, AddChecklistResponseContract listener)
        {
            mListener = listener;
            if (Utilities.getConnectivityStatus(mContext))
            {
                RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
                Call<ResponseBody> request = postMethod.requestInsertaCheckList(makeParameters(idObjeto, datos), token);
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
