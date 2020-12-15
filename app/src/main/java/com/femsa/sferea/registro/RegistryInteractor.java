package com.femsa.sferea.registro;

import com.femsa.requestmanager.Request.Login.ExistanceRequest;
import com.femsa.requestmanager.Request.Login.LoginUnicoRequest;
import com.femsa.requestmanager.Request.Login.ValidateDateRequest;
import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

public class RegistryInteractor {


    private OnRegistryInteractor mListener;

    public interface OnRegistryInteractor
    {
        void OnInteractorUserNotExists();

        void OnInteractorUserExists(String employeeNumber);

        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorCheckEmployeeExistance(String employee);

        void OnInteractorDoUniqueLogin(String employeeNumber);

        void OnInteractorSuccessfulLogin(UserResponseData data);

        void OnInteractorActiveSession();

        void OnInteractorCheckDate(String date, int id);

        void OnInteractorsuccessfulDate();

        void OnInteractorDateFailure();
    }


    public void checkEmployeeExistance(String employeeNumber, OnRegistryInteractor listener) {
        mListener = listener;
        ExistanceRequest request = new ExistanceRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(employeeNumber, new ExistanceRequest.ExistanceResponseContract() {
            @Override
            public void OnResponseThereIsNoEmployee() {
                mListener.OnInteractorUserNotExists();
            }

            @Override
            public void OnResponseEmployeeExists() {
                mListener.OnInteractorUserExists(employeeNumber);
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }
        });
    }

    public void doUniqueLogin(String employeeNumber, OnRegistryInteractor listener)
    {
        mListener = listener;

        LoginUnicoRequest loginuq = new LoginUnicoRequest(AppTalentoRHApplication.getApplication());
        loginuq.makeRequest(employeeNumber, new LoginUnicoRequest.LoginResponseContract() {
            @Override
            public void onResponseLoginCorrecto(UserResponseData usuarioDTO) {
                mListener.OnInteractorSuccessfulLogin(usuarioDTO);
            }

            @Override
            public void OnResponseNoAuth() {
                mListener.OnInteractorActiveSession();
            }


            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }
        });
    }

    public void validateDate(String date, int id, OnRegistryInteractor listener)
    {
        mListener = listener;
        ValidateDateRequest request = new ValidateDateRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(date, id, new ValidateDateRequest.ValidateDateResponseContract() {
            @Override
            public void OnDateSuccess() {
                mListener.OnInteractorsuccessfulDate();
            }

            @Override
            public void OnResponseNoAuth() {
                mListener.OnInteractorDateFailure();
            }

            @Override
            public void onResponseErrorServidor() {
                mListener.OnInteractorServerError();
            }

            @Override
            public void onResponseSinConexion() {
                mListener.OnInteractorNoInternet();
            }

            @Override
            public void onResponseTiempoAgotado() {
                mListener.OnInteractorTimeout();
            }
        });
    }
}
