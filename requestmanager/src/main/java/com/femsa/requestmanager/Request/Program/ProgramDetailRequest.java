package com.femsa.requestmanager.Request.Program;

import android.content.Context;
import androidx.annotation.NonNull;

import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Parser.Program.ProgramParser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.Program.ProgramDetailResponseData;
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

public class ProgramDetailRequest  implements Callback<ResponseBody>, Parser.ParserListener<ProgramDetailResponseData> {

    private static final String PROGRAM_ID_KEY = "idPrograma";

    private Context mContext;

    private ProgramDetailResponseContract mListener;

    @Override
    public void jsonTraducido(com.femsa.requestmanager.Response.Response<ProgramDetailResponseData> traduccion) {
        switch (traduccion.getError().getCodigoError()) {
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.OnDetailSuccess(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.OnNoAuth();
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                mListener.OnNoContent();
                break;
            default:
                mListener.OnUnexpectedError(traduccion.getError().getCodigoError());
        }
    }

    public interface ProgramDetailResponseContract extends ResponseContract
        {
            void OnDetailSuccess(ProgramDetailResponseData data);
            void OnNoAuth();
            void OnNoContent();
            void OnUnexpectedError(int errorCode);
        }

    public ProgramDetailRequest(Context context)
        {
            mContext = context;
        }

    private RequestBody makeParameters(int idProgram)
        {
            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put(PROGRAM_ID_KEY, idProgram);
            return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
        }

    public void makeRequest(String token, int idProgram, ProgramDetailResponseContract listener)
    {
        mListener = listener;
        if (Utilities.getConnectivityStatus(mContext))
            {
                RequestManager.PostMethods postMethod = RequestManager.getCliente().create(RequestManager.PostMethods.class);
                Call<ResponseBody> request = postMethod.requestProgramDetail(makeParameters(idProgram), token);
                request.enqueue(this);
            }
        else
            {
                mListener.onResponseSinConexion();
            }
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        ProgramParser parser;
        switch(response.code())
            {
                case RequestManager.CODIGO_SERVIDOR.OK:
                        parser = new ProgramParser(response.code());
                        parser.setParserListener(this);
                    try {
                            assert response.body() != null;
                            parser.traducirJSON(response.body().string());
                        }
                    catch (IOException e)
                        {
                            mListener.onResponseErrorServidor();
                        }
                    break;
                case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                    parser = new ProgramParser(response.code());
                    parser.setParserListener(this);
                    mListener.OnNoAuth();
                    break;
                case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                    mListener.onResponseErrorServidor();
                    break;
                case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                    mListener.OnNoContent();
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
