package com.femsa.sferea.home.miCuenta.miPerfil;

import com.femsa.requestmanager.DTO.User.Image.ImageResponseData;
import com.femsa.requestmanager.Request.ImagenPerfil.ProfilePicRequest;
import com.femsa.requestmanager.Request.Login.CambiarCorreoRequest;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

public class ProfileInteractor {

    private ProfileInteractor.OnProfileInteractorListener mListener;

    public interface OnProfileInteractorListener
        {
            void OnInteractorSuccesfulChange(ImageResponseData image);

            void OnInteractorServerError();

            void OnInteractorTimeout();

            void OnInteractorNoInternet();

            void OnInteractorChangeImage(String newImage);

            void OnInteractorNoAuth();

            void OnInteractorActualizaCorreo(String correo);

            void OnInteractorActualizaCorreoExitoso();

            void OnInteractorErrorInesperado(int codigoRespuesta);

            void OnInteractorCorreoYaRegistrado();
        }

    public void changeImage(String image, OnProfileInteractorListener listener)
        {
            mListener = listener;
            ProfilePicRequest request = new ProfilePicRequest(AppTalentoRHApplication.getApplication());
            request.makeRequest(SharedPreferencesUtil.getInstance().getTokenUsuario(), image, new ProfilePicRequest.ProfilePicRequestContract()
            {
                /**
                 * <p>Método que indica que el servidor tiene un error y no fue posible obtener la respuesta.</p>
                 */
                @Override
                public void onResponseErrorServidor() {
                    //Toast.makeText(ProfileActivity.this, "Ocurrió un error en el servidor", Toast.LENGTH_SHORT).show();
                    mListener.OnInteractorServerError();
                }

                /**
                 * <p>Método que indica que el dispositivo no cuenta con conexión a Internet.</p>
                 */
                @Override
                public void onResponseSinConexion() {
                   // Toast.makeText(ProfileActivity.this, "No tienes conexión a internet", Toast.LENGTH_SHORT).show();
                    mListener.OnInteractorNoInternet();
                }

                /**
                 * <p>Método que indica que el tiempo de espera que tiene la configuración del servicio se ha
                 * agotado.</p>
                 */
                @Override
                public void onResponseTiempoAgotado() {
                    //Toast.makeText(ProfileActivity.this, "Se acabó el tiempo de espera", Toast.LENGTH_SHORT).show();
                    mListener.OnInteractorTimeout();
                }

                @Override
                public void onResponseImagenCorrecta(ImageResponseData data) {
                    mListener.OnInteractorSuccesfulChange(data);
                   // Toast.makeText(ProfileActivity.this, "Imagen actualizada correctamente", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNoAuth() {
                    mListener.OnInteractorNoAuth();
                }
            });
        }

    public void callActualizaCorreo(String nuevoCorreo, String token, OnProfileInteractorListener listener)
    {
        mListener = listener;
        CambiarCorreoRequest request = new CambiarCorreoRequest(AppTalentoRHApplication.getApplication());
        request.makeRequest(token, nuevoCorreo, new CambiarCorreoRequest.CambiarCorreoResponseContract() {
            @Override
            public void OnCambioExitoso() {
                mListener.OnInteractorActualizaCorreoExitoso();
            }

            @Override
            public void OnNoAuth() {
                mListener.OnInteractorNoAuth();
            }

            @Override
            public void OnUnexpectedError(int codigoRespuesta) {
                mListener.OnInteractorErrorInesperado(codigoRespuesta);
            }

            @Override
            public void OnCorreoYaRegistrado() {
                mListener.OnInteractorCorreoYaRegistrado();
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
