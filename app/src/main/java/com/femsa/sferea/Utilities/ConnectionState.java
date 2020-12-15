package com.femsa.sferea.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;


/**
 * <p>Clase la cual contiene métodos estáticos para verificar la conexión del dispositivo a diferentes redes de interner.</p>
 * */
public class ConnectionState {

    public static final int WIFI = 0;
    public static final int MOBILE = 1;
    public static final int NO_NETWORK = 2;

    /**
     * <p>Función estática que retorna el tipo de red a la que se encuentra conectado el usuario en ese momento.</p>
     * @param context contexto de la aplicación.
     * @return tipo de red.
     * */
    public static int getTipoConexion(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()) {
            return WIFI;
        }
        else if (mobile.isConnectedOrConnecting ()) {
            return MOBILE;
        }
        else {
            return NO_NETWORK;
        }
    }


}
