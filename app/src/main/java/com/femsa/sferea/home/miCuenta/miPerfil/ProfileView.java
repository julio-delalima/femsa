package com.femsa.sferea.home.miCuenta.miPerfil;

public interface ProfileView {

    void OnHideLoader();

    void OnShowLoader();

    void OnSuccessfulChange(String newImage);

    void OnNoAuth();

    void OnMostrarMensaje(int tipoMensaje);

    void OnMostrarMensaje(int tipoMensaje, int codigoRespuesta);

    void OnMostrarToast(int mensaje);

    void OnCorreoActualizado();

    void OnCorreoYaRegistrado();
}
