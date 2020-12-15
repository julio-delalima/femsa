package com.femsa.sferea.Login;

import com.femsa.requestmanager.Request.Login.LoginRequest;
import com.femsa.requestmanager.Request.Login.SendMailRequest;
import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;

public class LoginInteractor {

    private OnLoginInteractorListener mListener;

    public interface OnLoginInteractorListener
    {
        void OnInteractorActiveSession();

        void OnInteractorWrongCredetials();

        void OnInteractorSuccesfulLogin(UserResponseData usuarioDTO);

        void OnInteractorServerError();

        void OnInteractorTimeout();

        void OnInteractorNoInternet();

        void OnInteractorDoLogin(String employee, String password);

        void OnInteractorSuccessMail();

        void OnInteractorWrongMail();

        void OnInteractorDoMail(String mail);

        void OnInteractorBlocked();
    }



    public void doLogin(String employeeNumber, String password, LoginInteractor.OnLoginInteractorListener listener)
        {
            mListener = listener;
            LoginRequest request = new LoginRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(employeeNumber, password, "token", new LoginRequest.LoginResponseContract() {
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

                @Override
                public void onResponseLoginCorrecto(UserResponseData usuarioDTO) {
                    mListener.OnInteractorSuccesfulLogin(usuarioDTO);
                }

                @Override
                public void onResponseUsuarioNoExiste() {

                }

                @Override
                public void onResponseActiveSession() {
                    mListener.OnInteractorActiveSession();
                }


                @Override
                public void onResponseCredencialesIncorrectas() {
                    mListener.OnInteractorWrongCredetials();
                }

                @Override
                public void onResponseBlockedUser() {
                    mListener.OnInteractorBlocked();
                }
            });
        }

        public void sendEmail(String mail, LoginInteractor.OnLoginInteractorListener listener)
        {
            mListener = listener;
            SendMailRequest mailRequest = new SendMailRequest(AppTalentoRHApplication.getApplication());
                mailRequest.makeRequest(mail, new SendMailRequest.SendMailResponseContract() {
                    @Override
                    public void onResponseMailSuccess() {
                        mListener.OnInteractorSuccessMail();
                    }

                    @Override
                    public void OnResponseWrongMail() {
                        mListener.OnInteractorWrongMail();
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
