package com.femsa.sferea.home.legal;

import com.femsa.requestmanager.Response.Legal.PrivacyAdviceResponseData;
import com.femsa.requestmanager.Response.Legal.TermsAndConditionsResponseData;

public interface LegalView {

    void onShowTermsAndConditions(TermsAndConditionsResponseData data);

    void onShowPrivacyAdvice(PrivacyAdviceResponseData data);

    void onShowLoader();

    void onHideLoader();

    void onBackToLogin();

    /**
     * Se llama cuando se necesita mostrar un mensaje como error 500
     * */
    void onMostrarMensage(int tipoMensaje);

    /**
     * Se llama cuando se necesita mostrar un mensaje como error 500
     * */
    void onMostrarMensageInesperado(int tipoMensaje, int codigoRespuesta);
}
