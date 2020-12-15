package com.femsa.requestmanager.Parser.Image;

import android.util.Log;

import com.femsa.requestmanager.DTO.User.Image.ImageResponseData;
import com.femsa.requestmanager.Parser.Parser;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ErrorDTO;
import com.femsa.requestmanager.Response.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageParser extends Parser<ImageResponseData> {

        private int errorCode;

        public ImageParser (int errorCode)
        {
            this.errorCode = errorCode;
        }

        @Override
        protected Response<ImageResponseData> doInBackground(String... json)
        {
            JSONObject nodoPrincipal;
            try
            {
                nodoPrincipal = new JSONObject(json[0]);
            }
            catch (JSONException e)
            {
                nodoPrincipal = new JSONObject();
            }

            Response<ImageResponseData> data = new Response<>();

            data.setError(new ErrorDTO(errorCode));

            boolean isImage64 = nodoPrincipal.has(Response.DATA_KEY);

            if (isImage64 && data.getError().getCodigoError() == RequestManager.CODIGO_RESPUESTA.RESPONSE_OK)
            {
               // Log.d("Image", "Image parser - tiene: " + isImage64 + " y hay " +nodoPrincipal.optJSONObject(Response.DATA_KEY));
                data.setData(new ImageResponseData(nodoPrincipal.optJSONObject(Response.DATA_KEY)));
            }

            return data;
        }

}
