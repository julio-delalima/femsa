package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaDos;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteData;

public interface ParticipanteView {

    void OnViewMostrarParticipante(ParticipanteData data);
    void OnViewShowLoader();

    /**
     * Utilizado para ocultar el loader
     * */
    void OnViewHideLoader();

    /**
     * Método para mostrar mensaje al usuario.
     * @param tipoMensaje el numero que identifica a cada tipo de mensaje.
     * */
    void OnViewMostrarMensaje(int tipoMensaje);

    /**
     * Método para mostrar el mensaje de error inesperado y el código de respuesta.
     * @param tipoMensaje el numero que identifica a cada tipo de mensaje.
     * @param codigoError El numero del codigo de respuesta recibido.
     */
    void OnViewMostrarMensajeInesperado(int tipoMensaje, int codigoError);

    void OnViewSesionInvalida();

    void OnViewParticipanteInexistente();
}
