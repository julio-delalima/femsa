package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Enlace;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;

/**
 * <p>Clase que contiene el WebView donde se mostrará la página web.</p>
 */
public class EnlaceWebViewActivity extends AppCompatActivity {

    private WebView mWbEnlace;

    //URL que se cargará en el WebView.
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obteniendo la URL del Intent que inició la Activity.
        url = getIntent().getStringExtra("url");
        setContentView(R.layout.activity_enlace_web_view);
        bindViews();
        initializeWebView();
    }

    private void bindViews(){
        mWbEnlace = findViewById(R.id.wb_activity_enlace);
    }

    /**
     * <p>Método que permite configurar el WebView.</p>
     */
    private void initializeWebView(){
        //Configuración que permite que las páginas que se abran dentro del WebView se visualicen dentro del WebView,
        //y así evitar que se inicie otra aplicación.
        mWbEnlace.setWebViewClient(new MyWebViewClient());

        WebSettings webSettings = mWbEnlace.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        mWbEnlace.loadUrl(url);
    }

    /**
     * <p>Método que permite configurar el comportamiento del WebView al presionar el botón de back.</p>
     */
    @Override
    public void onBackPressed() {
        //Condición para revisar si el WebView puede regresar a otra página en el historial, o si nos encotramos en la primer página que se abrió.
        if (mWbEnlace.canGoBack()){
            mWbEnlace.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * <p>Clase que permite definir el comportamiento que se debe seguir cuando se abra un link dentro del WebView.</p>
     */
    private class MyWebViewClient extends WebViewClient{

        private static final String DOMINIO_PAGINA_WEB = RequestManager.API_URL;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            /*//Si la página web que se quiere abrir pertenece al dominio, el WebView cargará la página web.
            if (request.getUrl().getHost().equals(DOMINIO_PAGINA_WEB)){
                return false;
            }
            //Si la página web no pertenece al dominio, entonces se abrirá otra Activity que pueda manejarla.
            Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
            startActivity(intent);
            return true;*/
            return false;
        }
    }
}
