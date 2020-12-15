package com.femsa.requestmanager.Request.Program;

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

public class InscripcionProgramaRequest implements Callback<ResponseBody> {

        private OnInscripcionProgramaContract mListener;

        private Context mContext;

        private static final String ID_KEY = "idPrograma";

        public InscripcionProgramaRequest(Context context)
        {
            mContext = context;
        }

        public interface OnInscripcionProgramaContract extends ResponseContract
        {
            /**
             * Método que saca al usuario en caso de que su sesión caduque.
             * */
            void OnNoAuth();
            /**
             * Método a ejecutar cuando la acción de dar like fue exitosa
             * */
            void OnInscripcionSuccess();

            /**
             * Método para detectar y mostrar en pantalla mensajes de error inesperado.
             * */
            void OnErrorInesperado(int codigoRespuesta);
        }

        /**
         * Método para realizar la petición de los likes
         * @param id ID del objeto o del programa
         * @param token token del usuario
         * @param listener listener de respuestas
         * */
        public void makeRequest(int id, String token, OnInscripcionProgramaContract listener)
        {
            mListener = listener;
            if (Utilities.getConnectivityStatus(mContext))
            {
                RequestManager.PostMethods postLike = RequestManager.getCliente().create(RequestManager.PostMethods.class);
                Call<ResponseBody> request;
                request = postLike.requestInscripcionPrograma(createParameter(id, ID_KEY), token);
                request.enqueue(this);
            }
            else
            {
                mListener.onResponseSinConexion();
            }
        }

        /**
         * Crea los parámetros en formato de RequestBody para la petición tipo Post
         * @param idProgram el id del objeto o programa
         * @param ID_KEY el nombre de la variable del JSON
         * */
        private RequestBody createParameter(int idProgram, String ID_KEY)
        {
            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put(ID_KEY, idProgram);
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
                        mListener.OnInscripcionSuccess();
                    break;
                    case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                        mListener.OnNoAuth();
                    break;
                    case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                        mListener.onResponseErrorServidor();
                    break;
                    default:
                        mListener.OnErrorInesperado(response.code());
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

