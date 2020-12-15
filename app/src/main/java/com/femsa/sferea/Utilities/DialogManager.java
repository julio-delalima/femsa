package com.femsa.sferea.Utilities;

import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;
import android.view.View;

import com.femsa.sferea.R;

public class DialogManager extends StringManager {


    /**
     * <p>Método estático que crea un DialogManager personalizado y define sus valores internos mediante polimorfismo</p>
     * @param fm FragmentManager que va a gestionar el DialogFragment.
     * @param messageType ID del mensaje a mostrar.
     * */
    public static void displaySnack(FragmentManager fm, int messageType)
        {
            DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, AppTalentoRHApplication.getApplication().getString(R.string.femsa),
                    AppTalentoRHApplication.getApplication().getString(messagechooser(messageType)), AppTalentoRHApplication.getApplication().getString(R.string.ok), "", true);
                    fragment.show(fm, "Error");
        }

    /**
     * <p>Método estático que crea un DialogManager personalizado y define sus valores internos mediante polimorfismo</p>
     * @param fm FragmentManager que va a gestionar el DialogFragment.
     * @param messageType ID del mensaje a mostrar.
     * @param errorCode código de error que se va a mostrar cuando haya mensajes de error del servidor.
     * */
    public static void displaySnack(FragmentManager fm, int messageType, int errorCode)
    {
        String message = AppTalentoRHApplication.getApplication().getResources().getString(messagechooser(messageType), errorCode);
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, AppTalentoRHApplication.getApplication().getString(R.string.femsa),
                message, AppTalentoRHApplication.getApplication().getString(R.string.ok), "", true);
        fragment.show(fm, "Error");
    }
    /**
     * <p>Método estático que crea un DialogManager personalizado y define sus valores internos mediante polimorfismo</p>
     * @param fm FragmentManager que va a gestionar el DialogFragment.
     * @param messageType ID del mensaje a mostrar.
     * @param cadena texto que se mostrará en el cuerpo del mensaje.
     * */
    public static void displaySnack(FragmentManager fm, int messageType, String cadena)
    {
        String message = AppTalentoRHApplication.getApplication().getResources().getString(messagechooser(messageType), cadena);
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, AppTalentoRHApplication.getApplication().getString(R.string.femsa),
                message, AppTalentoRHApplication.getApplication().getString(R.string.ok), "", true);
        fragment.show(fm, "Error");
    }

    /**
     * <p>Método estático que crea un DialogManager personalizado y define sus valores internos mediante polimorfismo.
     * Aqui no hay mensajes de error, solo se muestra lo que viene en el string de cadena.</p>
     * @param fm FragmentManager que va a gestionar el DialogFragment.
     * @param cadena cuerpo del mensaje que se va a mostrar.
     * */
    public static void displaySnack(FragmentManager fm, String cadena)
    {
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, AppTalentoRHApplication.getApplication().getString(R.string.femsa),
                cadena, AppTalentoRHApplication.getApplication().getString(R.string.aceptar), "", true);
        fragment.show(fm, "Error");
    }

    /**
     * <p>Método estático que crea un DialogManager personalizado y define sus valores internos mediante polimorfismo</p>
     * @param fm FragmentManager que va a gestionar el DialogFragment.
     * @param cadena Texto a mostrar como cuerpo del mensaje.
     * @param titulo cadena de texto que se pondrá como título.
     * */
    public static void displaySnack(FragmentManager fm, String cadena, String titulo)
    {
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, titulo,
                cadena, AppTalentoRHApplication.getApplication().getString(R.string.ok), "", true);
        fragment.show(fm, "Error");
    }

    /**
     * <p>Método estático que crea un DialogManager personalizado y define sus valores internos mediante polimorfismo</p>
     * */
    public static void displaySnack(FragmentManager fm, String cadena, String titulo, boolean cerrar, DialogFragmentManager.OnDialogManagerButtonActions listener)
    {
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(false, true, false, titulo,
                cadena, AppTalentoRHApplication.getApplication().getString(R.string.ok), AppTalentoRHApplication.getApplication().getResources().getString(R.string.cancel), cerrar);
        fragment.setListener(listener);
        fragment.show(fm, "Reto");
    }

    /**
     * <p>Método estático que crea un DialogManager personalizado y define sus valores internos mediante polimorfismo</p>
     * @param fm FragmentManager que va a gestionar el DialogFragment.
     * @param cadena texto del mensaje a mostrar.
     * @param soloCerrar bandera que permite definir el comportamiento de la app al aceptar o cancelar.
     *                   En caso de ser true el Dialog solo se cerrará.
     *                   En caso de ser false se deberá implementar la Interfaz OnDialogManagerButtonActions para
     *                    definir las acciones a realizar con cada botón.
     * @param listener listener o referencia de la clase que está implementando la interfaz de OnDialogManagerButtonActions.
     * */
    public static void displaySnack(FragmentManager fm, String cadena, boolean soloCerrar, DialogFragmentManager.OnDialogManagerButtonActions listener)
    {
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, AppTalentoRHApplication.getApplication().getString(R.string.femsa),
                cadena, AppTalentoRHApplication.getApplication().getString(R.string.ok), "", soloCerrar);
        fragment.setListener(listener);
        fragment.show(fm, "Error");
    }
}
