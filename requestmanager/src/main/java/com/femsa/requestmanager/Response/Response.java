package com.femsa.requestmanager.Response;


public class Response<Data> {
    public static final String ERROR_KEY = "error";
    public static final String DATA_KEY = "data";

    private ErrorDTO mErrorDTO;
    private Data mData;

    public void setError(ErrorDTO errorDTO) {
        mErrorDTO = errorDTO;
    }

    public void setData(Data data) {
        mData = data;
    }

    public ErrorDTO getError() {
        return mErrorDTO;
    }

    public Data getData() {
        return mData;
    }
}
