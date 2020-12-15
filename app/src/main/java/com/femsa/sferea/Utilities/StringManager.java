package com.femsa.sferea.Utilities;

import android.content.Context;

import com.femsa.sferea.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.CHECKLIST;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.COMIC;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.CONFERENCIA;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.DOC_PDF;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.DOC_WORD;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.EBOOK;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.ENCOESTA;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.ENLACE;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.EVALUACION;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.EXCEL;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.GIF;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.JUEGO;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.JUEGO_MULTIJUGADOR;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.POWER_POINT;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.VEDEO;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.VEDEO_ASESORIA_CON_EXPERTO;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.VEDEO_INTERACTIVO;

public class StringManager {

    public static final int NO_INTERNET = 0;

    public static final int TIMEOUT = 1;

    public static final int SERVER_ERROR = 2;

    public static final int EXPIRED_TOKEN = 3;

    public static final int NO_CONTENT = 4;

    public static final int UNEXPECTED_ERROR = 5;

    public static final int EMPTY = 6;

    public static final int EMPTY_QUERY = 7;

    public static final int MENSAJES_CHIDOS = 8;

    public static final int SIN_FACILITADOR = 9;

    public static final int SIN_WS = 10;

    public static final int SELECCIONA_FACILITADOR = 11;

    public static final int MINIMO_PARTICIPANTES = 12;

    public static final int AREAS_MINIMAS = 13;

    public static final int ID_IDIOMA_ESPANOL= 1;

    public static final int ID_IDIOMA_INGLES = 2;

    public static final int ID_IDIOMA_PORTUGUES = 3;

    public static final String WORD_TYPE = "word";
    public static final String EXCEL_TYPE = "excel";
    public static final String POWERPOINT_TYPE = "powerpoint";

    public static final String WORD_EXTENSION = ".docx";
    public static final String EXCEL_EXTENSION = ".xlsx";
    public static final String POWERPOINT_EXTENSION = ".pptx";

    public static final String WORD_MIME_TYPE = "application/msword";
    public static final String EXCEL_MIME_TYPE = "application/vnd.ms-excel";
    public static final String POWERPOINT_MIME_TYPE = "application/vnd.ms-powerpoint";

    public static int messagechooser(int messageType)
    {
        switch(messageType)
        {
            case NO_INTERNET: //no internet
                return R.string.no_internet;
            case TIMEOUT: //timeout
                return R.string.timeout;
            case SERVER_ERROR: //500
                return R.string.server_error;
            case EXPIRED_TOKEN: //noAuth
                return R.string.expired_token;
            case NO_CONTENT: //no CONTENT
                return R.string.no_content;
            case EMPTY: //Empty
                return R.string.sin_contenido;
            case EMPTY_QUERY:
                return R.string.consulta_vacia;
            case MENSAJES_CHIDOS:
                return R.string.mensaje_chido;
            case SIN_FACILITADOR:
                return R.string.sin_facilitador;
            case SIN_WS:
                return R.string.sinws;
            case SELECCIONA_FACILITADOR:
                return R.string.selecciona_facilitador;
            case MINIMO_PARTICIPANTES:
                return R.string.minimo_facilitadores;
            case AREAS_MINIMAS:
                return R.string.minima_areas;
            default:
                return R.string.unexpected_error;
        }
    }

    public static int obtienePaises(int idPais)
        {
            switch(idPais)
                {
                    case 1:
                        return R.string.Mexico;
                    case 2:
                        return R.string.Brasil;
                    case 3:
                        return R.string.Colombia;
                    case 4:
                        return R.string.OtrosPaises;
                    default:
                        return R.string.OtrosPaises;
                }
        }

    public static String parseFecha(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

    public static String parseFecha(String inputDateString, SimpleDateFormat inputDateFormat, int idIdioma) {
        Date date = null;
        SimpleDateFormat outputDateFormat = null;
        switch (idIdioma){
            case ID_IDIOMA_ESPANOL:
                outputDateFormat = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
            break;
            case ID_IDIOMA_INGLES:
                outputDateFormat = new SimpleDateFormat("MMM/dd/yyyy", Locale.getDefault());
            break;
            case ID_IDIOMA_PORTUGUES:
                outputDateFormat = new SimpleDateFormat("MMM/dd/yyyy", new Locale("pt"));
            break;
        }
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

    public static String parseFecha(String fechaEntrada, String formatoEntrada, String formatoSalida) {
        Date date;
        SimpleDateFormat viejo, nuevo;
        viejo = new SimpleDateFormat(formatoEntrada, Locale.getDefault());
        nuevo = new SimpleDateFormat(formatoSalida, Locale.getDefault());
        String outputDateString;
        try {
            date = viejo.parse(fechaEntrada);
            outputDateString = nuevo.format(date);
        } catch (ParseException e) {
            return fechaEntrada;
        }
        return outputDateString;
    }

    public static String getTipe(Context mContext, int tipo){
        String tempType;
        switch(tipo)
        {
            case GIF:
                tempType = mContext.getResources().getString(R.string.gif);
                break;
            case DOC_PDF:
                tempType = mContext.getResources().getString(R.string.pdf);
                break;
            case EBOOK:
                tempType = mContext.getResources().getString(R.string.ebook);
                break;
            case VEDEO:
                tempType = mContext.getResources().getString(R.string.video);
                break;
            case VEDEO_INTERACTIVO:
                tempType = mContext.getResources().getString(R.string.videoInteractivo);
                break;
            case DOC_WORD:
                tempType = mContext.getResources().getString(R.string.word);
                break;
            case EXCEL:
                tempType = mContext.getResources().getString(R.string.excek);
                break;
            case POWER_POINT:
                tempType = mContext.getResources().getString(R.string.powerpoint);
                break;
            case CHECKLIST:
                tempType = mContext.getResources().getString(R.string.checklist);
                break;
            case EVALUACION:
                tempType = mContext.getResources().getString(R.string.evaluacion);
                break;
            case ENCOESTA:
                tempType = mContext.getResources().getString(R.string.encuesta_toolbar_title);
                break;
            case CONFERENCIA:
                tempType = mContext.getResources().getString(R.string.videoconferencia);
                break;
            case VEDEO_ASESORIA_CON_EXPERTO:
                tempType = mContext.getResources().getString(R.string.charla_con_experto);
                break;
            case JUEGO:
                tempType = mContext.getResources().getString(R.string.juegos);
                break;
            case ENLACE:
                tempType = mContext.getResources().getString(R.string.enlace);
                break;
            case COMIC:
                tempType = mContext.getResources().getString(R.string.comic);
                break;
            case JUEGO_MULTIJUGADOR:
                tempType = mContext.getResources().getString(R.string.juego_multijugador);
                break;
            default:
                tempType = mContext.getResources().getString(R.string.indeterminado);
        }
        return tempType;
    }

    public static Date parseFecha2(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        Date outputDateString = null;
        try {
            date = inputDateFormat.parse(String.valueOf(inputDateString));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
