package com.femsa.sferea.registro;

import com.femsa.requestmanager.Response.Login.UserResponseData;

public interface RegistryView {

    void OnEmployeeExists(String employee);

    void OnEmployeeNotExists();

    void OnHideLoader();

    void OnShowLoader();

    void OnUniqueLoginSuccess(UserResponseData data);

    void OnNoAuth();

    void OnDateSuccess();

    void OnDateFailure();

    void OnMostrarMensaje(int tipoMensaje);
}
