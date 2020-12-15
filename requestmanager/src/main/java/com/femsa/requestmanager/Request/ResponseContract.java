package com.femsa.requestmanager.Request;


/**
 * <p>Interface que permite implementar las funciones básicas que va a tener cualquier petición sin
 * excepción. Implementa a partir de esta clase para cualquier tipo de peticioones que se vayan a
 * crear.</p>
 */
public interface ResponseContract {
    /**
     * <p>Método que indica que el servidor tiene un error y no fue posible obtener la respuesta.</p>
     */
    void onResponseErrorServidor();

    /**
     * <p>Método que indica que el dispositivo no cuenta con conexión a Internet.</p>
     */
    void onResponseSinConexion();

    /**
     * <p>Método que indica que el tiempo de espera que tiene la configuración del servicio se ha
     * agotado.</p>
     */
    void onResponseTiempoAgotado();
}
