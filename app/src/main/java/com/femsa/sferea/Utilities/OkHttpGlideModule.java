package com.femsa.sferea.Utilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.module.AppGlideModule;
import com.femsa.requestmanager.Utilities.UnsafeOkHttpClient;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
* <p>Clase que permite validar los certificados del servidor para no tener problemas en las
* peticiones que realiza Glide para descargar las imagenes.</p>
*/
@GlideModule
public class OkHttpGlideModule extends AppGlideModule {

    /**
     * <p>Método que permite registrar los componentes para validar los certificados del servidor.</p>
     * @param context Contexto de la aplicación.
     * @param glide Objeto Glide para manejar las peticiones de descargas de imagenes.
     * @param registry Se encarga de la lógica de descarga de las imagenes.
     */
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        registry.replace(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(client));
    }

    /**
     * <p>Sobreescrita al método de opciones de configuración de Glide en el cual se aplica
     * una nueva configuración a nivel de Log donde solo se permiten los Log de tipo ERROR</p>
     * @param context Contexto de la aplicación.
     * @param builder Constructor de glide sobre el que se aplican las opciones.
     * */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setLogLevel(Log.ERROR);
    }



}