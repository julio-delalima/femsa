package com.femsa.sferea.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <p>Clase que contiene operaciones útiles para las peticiones.</p>
 */
public final class WebUtil
{
    /**
     * <p>Constructor por defecto.</p>
     */
    private WebUtil() {}

    /**
     * <p>OBtiene el estado de la red.</p>
     *
     * @param context contexto de la aplicación.
     * @return true si hay conexión a internet, false en caso contrario.
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
