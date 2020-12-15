package com.femsa.sferea.home.notificaciones;

import com.femsa.requestmanager.Response.NotificacionesResponse.NotificacionesResponseData;

public interface NotificacionesView {

    /**
     * Mandado a llamar cuando la sesión caduca.
     * */
    void OnViewNoAuth();

    /**
     * Cuando se eliminó una notificación exitosamente se manda a llamar este método.
     * */
    void OnViewNotificacionEliminada();

    /**
     * Cuando una notificación se ha marcado como importante exitosamente.
     * */
    void OnViewNotificacionImportante();

    /**
     * Muestra en pantalla el listado de notificaciones provenientes del web Service.
     * */
    void OnViewMostrarNotificaciones(NotificacionesResponseData data);

    /**
     * Cuando no se tiene ninguna notificación.
     * */
    void OnViewSinNotificaciones();

    /**
     * Utilizado para mostrar el Loader en la pantalla
     */
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
}
