package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;

import android.content.Context;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.LogManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class Truster {

    public static void trustFemsa(String baseURL, Context c) {
        try{
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From http://femsa.sferea.com/STAR_mykoflearning_com.crt
            //URL resource =


            InputStream caInput = c.getResources().openRawResource(R.raw.mykoflearning_com);// new BufferedInputStream(new FileInputStream("/res/raw/mykoflearning_com.crt"));
            Certificate ca;
            LogManager.d("Media","ca input");
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                LogManager.d("Media","ca generate");
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
            LogManager.d("Media","ssl init");
            // Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL(baseURL);
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            LogManager.d("Media","conexión establecida");
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            //InputStream in = urlConnection.getInputStream();
            //copyInputStreamToOutputStream(in, System.out);
            LogManager.d("Media","Si llega aquí ya chingamos :3");
        }
        catch (Exception ex){
            LogManager.d("Media", ex.getMessage());
        }
    }
}
