package com.femsa.sferea.Login;

import com.femsa.requestmanager.Response.Login.UserResponseData;

public interface LoginView {

    void OnHideLoader();

    void OnShowLoader();

    void OnSuccessFulLogin(UserResponseData usuarioDTO);

    void OnWrongCredentials();

    void OnActiveSession();

    void OnSuccessMail();

    void OnFailMail();

    void OnBlocked();

    void OnShowMessage(int messageType);
}
