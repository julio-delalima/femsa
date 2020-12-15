package com.femsa.sferea.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;

import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.home.inicio.componentes.activity.HomeActivity;
import com.femsa.sferea.home.inicio.componentes.activity.HomeInteractor;
import com.femsa.sferea.home.inicio.componentes.activity.HomePresenter;
import com.femsa.sferea.home.inicio.componentes.activity.HomeView;
import com.femsa.sferea.Login.LoginActivity;

import java.util.Locale;

public class GlobalActions implements HomeView, HomeInteractor.OnHomeInteractorListener {

    private Loader loader;

    private Activity mActivity;

    private boolean mIrALogin = true;

    public GlobalActions(Activity activity)
    {
        mActivity = activity;
        loader = Loader.newInstance();
    }


    public void SetTokenFirebase(String token, String tipo)
        {
            HomeInteractor home = new HomeInteractor();
            home.callTokenFirebase(token, SharedPreferencesUtil.getInstance().getTokenUsuario(), tipo, this);
        }

    /**
     * Método para cerrar la sesión del usuario cuando el token de usuario haya expirado.
     * */
    public void OnNoAuthCloseSession(String token)
    {
        HomePresenter mPresenter = new HomePresenter(this, new HomeInteractor());
        mPresenter.OnInteractorLogOut(token);
        SetTokenFirebase("", "");
    }

    public void setIrALogin(boolean irALogin){
        mIrALogin = irALogin;
    }

    public void setLocale(String lang, int idIdioma) {
        Locale myLocale = new Locale(lang);
        Resources res = mActivity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(mActivity, HomeActivity.class);
        mActivity.startActivity(refresh);
        mActivity.finish();
        SharedPreferencesUtil.getEditor().putInt(SharedPreferencesUtil.ID_IDIOMA_KEY,idIdioma).apply();
    }

    public void setLocaleHome(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = mActivity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(mActivity, HomeActivity.class);
        mActivity.startActivity(refresh);
    }

    /**
     * Cuando se cierre exitosamente la sesión se ejecutará éste método.
     * */
    @Override
    public void onLogOutSuccess() {
        SharedPreferencesUtil.getInstance().closeSession();
        if(mIrALogin){
            Intent sendTo =  new Intent(AppTalentoRHApplication.getApplication(), LoginActivity.class);
            sendTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mActivity.startActivity(sendTo);
            SetTokenFirebase("", "");
            mActivity.finish();
        }
    }

    /**
     * Método para mostrar el loader en la pantalla.
     * */
    @Override
    public void onShowLoader() {
        if(mActivity != null)
            {
                FragmentManager fm = ((FragmentActivity)mActivity).getSupportFragmentManager();
                loader.show(fm, "Loader");
            }
    }

    /**
     * Método para ocultar el loader en la pantalla.
     * */
    @Override
    public void onHideLoader() {
        if(loader != null && loader.isAdded()) {
            loader.dismiss();
        }
    }

    @Override
    public void onShowMessage(int tipoMensaje) {

    }

    @Override
    public void onShowMessage(int tipoMensaje, int codigoRespuesta) {

    }

    /**
     * Función estática que le da formato al texto ingresado para poder mostrarlo correctamente dentro de un WebView.
     * @param text Texto al que se le dará formato.
     * @return String texto en formato WebView justificado.
     **/
    public static String webViewConfigureText(String text)
        {
            return "<html><body scroll=\"no\"><p align=\"justify\">" +  text + "</p></body></html>";
        }

    /**
     * Función estática que regresa las dos primeras palabras de un nombre completo.
     * @param nombre Nombre completo que va a ser recortado.
     * @return String el nombre cortado, solo las primeras 2 palabras.
     * */
    public static String cutFullName(String nombre)
    {
        StringBuilder newName = new StringBuilder();

        int spaceCount = 0;

        for(int i = 0; i < nombre.length(); i++)
            {
                if(spaceCount == 2)
                    {
                        break;
                    }
                String c = ""  + nombre.charAt(i);
                if(c.equals(" ") || c.equals(""))
                {
                    spaceCount++;
                }
                newName.append(c);
            }

        return newName.toString();
    }


    /**
     * Funcion para extraer el tiempo deseado de un String de la forma hh:mm:ss
     * @param  time el String de entrada en formato hh:mm:ss
     * @param returnType el valor de retorno que queremos, 0 la hora, 1 minutos y 2 los segundos.
     * @return String el tiempo indicado.
     * */
    public static String getTimeFromstring(String time, int returnType)
    {
        String hour = "", minute = "", second = "";
        int pointCounter = 0;
        for(int i = 0; i < time.length(); i++)
        {
            String c = time.charAt(i) + "";
            if(c.equals(":"))
            {
                //hour += c;
                pointCounter++;
                continue;
            }
            switch(pointCounter)
                {
                    case 0:
                        hour += c;
                    break;
                    case 1:
                        minute += c;
                    break;
                    case 2:
                        second += c;
                    break;
                }

            /*if(!c.equals(":"))
                {
                    hour += c;
                }
            else
                {
                    minute = time.charAt(i + 1) + "" + time.charAt(i + 2);

                    second = time.charAt(i + 4) + "" + time.charAt(i + 5);
                    break;
                }*/
        }
       /* switch(returnType)
            {
                case 0:
                    return hour;
                case 1:
                    return minute;
                case 2:
                    return second;
                default:
                    return "0";
            }*/
        int monutostotales = (Integer.parseInt(hour) * 60) + Integer.parseInt(minute) + Integer.parseInt(second)/60;
        return monutostotales + "";
    }

    /**
     * Función para validar si un correo electrónico es válido.
     * @param target correo ingresado.
     * @return true si el correo es correcto, false si no cumple las características de un correo.
     * */
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void OnInteractorLogOut(String token) {

    }

    @Override
    public void OnInteractorServerError() {

    }

    @Override
    public void OnInteractorTimeout() {

    }

    @Override
    public void OnInteractorNoInternet() {

    }

    @Override
    public void OnInteractorRequestFailure() {

    }

    @Override
    public void OnInteractorNoAuth() {

    }

    /***
     * Success
     */
    @Override
    public void OnInteractorSuccessfulLogout() {

    }

    @Override
    public void OnInteractorMensajeInesperado(int codigoRespuesta) {

    }

    @Override
    public void OnInteractorSetTokenFirebase(String tokenFirebase, String tipoDispositivo) {

    }

    @Override
    public void OnInteractorTokenFirebaseDefinido() {

    }
}
