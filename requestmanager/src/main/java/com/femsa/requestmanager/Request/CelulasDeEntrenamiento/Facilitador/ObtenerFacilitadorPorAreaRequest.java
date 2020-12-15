package com.femsa.requestmanager.Request.CelulasDeEntrenamiento.Facilitador;

import android.content.Context;

import com.femsa.requestmanager.Parser.CelulasDeEntrenamiento.Facilitador.FacilitadorParser;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.Request.ResponseContract;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.FacilitadorData.FacilitadorData;
import com.femsa.requestmanager.Response.Response;
import com.femsa.requestmanager.Utilities.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ObtenerFacilitadorPorAreaRequest implements Callback<ResponseBody>, Parser.ParserListener<FacilitadorData> {

    private static final String ARRAY_AREAS_KEY = "areasRequest";
    private static final String ID_AREAS_KEY = "idAreasFun";
    private static final String ID_PAIS_CEPTOR = "idPais";
    private static final String PALABRA_CLAVE_KEY = "palabraClave";

    private OnResponseFacilitadorPorArea mListener;
    private Context mContext;

    public interface OnResponseFacilitadorPorArea extends ResponseContract {
        void onResponseFacilitadorPorArea(FacilitadorData data);
        void onResponseTokenInvalido();
        void onResponseErrorInesperado(int codigoRespuesta);
        void onResponseSinFacilitadores();
    }

    public ObtenerFacilitadorPorAreaRequest(Context context){
        mContext = context;
    }

    public void makeRequest(String tokenUsuario, ArrayList<Integer> arrayAreas, String palabraClave, int idPaisReceptor,  OnResponseFacilitadorPorArea listener){
        mListener = listener;
        if(Utilities.getConnectivityStatus(mContext))
            {
            RequestManager.PostMethods postMethods = RequestManager.getCliente().create(RequestManager.PostMethods.class);
            Call<ResponseBody> request;
            request = postMethods.requestObtenerFacilitadorPorArea(crearParametros(arrayAreas, palabraClave, idPaisReceptor), tokenUsuario);
            request.enqueue(this);
        }
        else{
            mListener.onResponseSinConexion();
        }
    }

    private RequestBody crearParametros(ArrayList<Integer> areasIDs, String palabraClave, int idPaisReceptor)
        {
            HashMap<String, Object> params = new HashMap<>();

            ArrayList<HashMap<String, Object>> areasValues = new ArrayList<>();

            for (Integer area : areasIDs) {
                HashMap<String, Object> areaHash = new HashMap<>();
                areaHash.put(ID_AREAS_KEY, area);
                areasValues.add(areaHash);
            }
            params.put(PALABRA_CLAVE_KEY, palabraClave);
            params.put(ID_PAIS_CEPTOR, idPaisReceptor);
            params.put(ARRAY_AREAS_KEY, areasValues);
            return RequestBody.create(MediaType.parse(RequestManager.APP_JSON), new JSONObject(params).toString());
        }


    @Override
    public void jsonTraducido(Response<FacilitadorData> traduccion) {
        switch (traduccion.getError().getCodigoError()){
            case RequestManager.CODIGO_RESPUESTA.RESPONSE_OK:
                mListener.onResponseFacilitadorPorArea(traduccion.getData());
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            case RequestManager.CODIGO_SERVIDOR.NO_CONTENT:
                    mListener.onResponseSinFacilitadores();
            default:
                mListener.onResponseErrorInesperado(traduccion.getError().getCodigoError());
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        int codigoRespuesta = response.code();
        switch(codigoRespuesta) {
            case RequestManager.CODIGO_SERVIDOR.OK:
                FacilitadorParser parser = new FacilitadorParser(codigoRespuesta);
                parser.setParserListener(this);
                try {
                    String responseString = response.body().string();
                    parser.traducirJSON(responseString);
                } catch (IOException e) {
                    mListener.onResponseErrorServidor();
                }
                break;
            case RequestManager.CODIGO_SERVIDOR.INTERNAL_SERVER_ERROR:
                mListener.onResponseErrorServidor();
                break;
            case RequestManager.CODIGO_SERVIDOR.UNAUTHORIZED:
                mListener.onResponseTokenInvalido();
                break;
            default:
                mListener.onResponseErrorInesperado(codigoRespuesta);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mListener.onResponseTiempoAgotado();
    }
}
