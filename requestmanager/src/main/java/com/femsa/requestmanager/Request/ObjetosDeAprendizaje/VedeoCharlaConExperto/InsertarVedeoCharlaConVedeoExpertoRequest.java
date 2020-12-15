package com.femsa.requestmanager.Request.ObjetosDeAprendizaje.VedeoCharlaConExperto;

import android.content.Context;
import androidx.annotation.NonNull;

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

public class InsertarVedeoCharlaConVedeoExpertoRequest implements Callback<ResponseBody> {

        private static final String ID_OBKJETO_KEY = "idObjeto";
        private static final String ID_HORAS_KEY = "idVideoConferencia";


        private Context mContext;

        private VedeoCharlaConExpertoResponseContract mListener;

        public interface VedeoCharlaConExpertoResponseContract extends ResponseContract
        {
            void OnInsertarSuccess();
            void OnNoAuth();
            void OnUnexpectedError(int codigoRespuesta);
            void OnFechaInapartableNunca();
        }

        public InsertarVedeoCharlaConVedeoExpertoRequest(Context context)
        {
            mContext = context;
        }

        private RequestBody makeParameters(int idObjeto, int idHoras)
        {
            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put(ID_OBKJETO_KEY, idObjeto);
            params.put(ID_HORAS_KEY, idHoras);
            return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());

        }

        public void makeRequest(String token, int idObjeto, int idHoras, VedeoCharlaConExpertoResponseContract listener)
        {
            mListener = listener;
            if (Utilities.getConnectivityStatus(mContext))
            {
                RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
                Call<ResponseBody> request = postMethod.requestInsertarVedeoCharlaExperto(makeParameters(idObjeto, idHoras), token);
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
                    mListener.OnInsertarSuccess();
                    break;
                case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                    mListener.onResponseErrorServidor();
                    break;
                case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                    mListener.OnNoAuth();
                    break;
                case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                    mListener.OnFechaInapartableNunca();
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
