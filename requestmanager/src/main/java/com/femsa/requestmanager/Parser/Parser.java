package com.femsa.requestmanager.Parser;


import android.os.AsyncTask;

import com.femsa.requestmanager.Response.Response;

public abstract class Parser<Data> extends AsyncTask<String, Void, Response<Data>> {

    private ParserListener<Data> mListener;

    public void setParserListener(ParserListener<Data> listener) {
        mListener = listener;
    }

    public void traducirJSON(String json) {
        execute(json);
    }

    @Override
    protected void onPostExecute(Response<Data> dataResponse) {
        if (mListener != null) {
            mListener.jsonTraducido(dataResponse);
        }
    }

    public interface ParserListener<Data> {
        void jsonTraducido(Response<Data> traduccion);
    }
}
