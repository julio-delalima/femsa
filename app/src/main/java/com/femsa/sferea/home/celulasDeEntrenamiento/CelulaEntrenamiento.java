package com.femsa.sferea.home.celulasDeEntrenamiento;

import android.content.res.Resources;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.StringManager;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * <p>Clase abstracta que contiene métodos que comparten todos los detalles de solicitudes.</p>
 */
public abstract class CelulaEntrenamiento {

    //Constantes que se usan para distinguir a una solicitud.
    public static final String INDUCCION = "1";//"Inducción";
    public static final String CELULA = "2";//"Célula";

    //Constantes que se usan para distinguir el rol de un usuario.
    public static final String AUTORIZADOR = "3";//"Autorizador";
    public static final String FACILITADOR = "4";//"Facilitador";
    public static final String SOLICITANTE = "1";//"Solicitante";
    public static final String PARTICIPANTE = "2";//"Participante";

    //Constantes que se usan para distinguir el estatus de una solicitud.
    public static final String POR_AUTORIZAR = "1";//"Por Autorizar";
    public static final String POR_PROGRAMAR = "2";//"Por Programar";
    public static final String PROGRAMADO = "Programado";

    //Constantes que se usan para distinguir los distintos países.
    private static final String MEXICO = "México";
    private static final String BRASIL = "Brasil";
    private static final String COLOMBIA = "Colombia";
    private static final String ARGENTINA = "Argentina";
    private static final String GUATEMALA = "Guatemala";
    private static final String URUGUAY = "Uruguay";
    private static final String VENEZUELA = "Venezuela";
    private static final String NICARAGUA = "Nicaragua";
    private static final String COSTA_RICA = "Costa Rica";
    private static final String PANAMA = "Panamá";

    //Objeto que permite acceder a los recursos.
    private static Resources resources = AppTalentoRHApplication.getApplication().getResources();

    //Regex para identificar los diferentes formatos de fecha que se envían desde el servidor.
    private static final Pattern fechaPattern = Pattern.compile("\\d{4}-{1}\\d{2}-{1}\\d{2}");

    /**
     * <p>Método que retorna el texto adecuado para el detalle de la solicitud dependiendo si esta
     * es una Célula o una Inducción.</p>
     * @param tipoSolicitud Tipo de solicitud.
     * @return Cadena con formato que indica el tipo de la solicitud.
     */
    public static String obtenerTipoSolicitud(String tipoSolicitud){
        if (tipoSolicitud.equals(CelulaEntrenamiento.INDUCCION)){
            return resources.getString(R.string.layout_informacion_induccion_titulo);
        } else {
            return resources.getString(R.string.layout_informacion_celula_titulo);
        }
    }


    /**
     * <p>Método que retorna el texto adecuado para el detalle de la solicitud dependiendo si esta
     * es una Célula o una Inducción.</p>
     * @param tipoSolicitud Tipo de solicitud.
     * @return Cadena con formato que indica el tipo de la solicitud.
     */
    public static String obtenerTipoSolicitudCorto(String tipoSolicitud){
        if (tipoSolicitud.equals(CelulaEntrenamiento.INDUCCION)){
            return resources.getString(R.string.Celula);
        } else {
            return resources.getString(R.string.layout_informacion_celula_titulo);
        }
    }

    /**
     * <p>Método que, con base en un id de país, agrega a un TextView el nombre y la bandera del país.
     * Se utiliza en: Datos de solicitante, en el detalle de una solicitud.</p>
     * @param textView TextView en el que se colocará el nombre y la bandera del país.
     * @param idPais Id del país.
     */
    public static void asignarPais(TextView textView, int idPais){
        switch (idPais){
            case 1:
                textView.setText(resources.getString(R.string.Corporativo));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.otros_square, 0,0,0);
                break;
            case 2:
                textView.setText(resources.getString(R.string.Mexico));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mexico_square, 0,0,0);
                break;
            case 3:
                textView.setText(resources.getString(R.string.Brasil));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.brasil_square, 0,0,0);
                break;
            case 4:
                textView.setText(resources.getString(R.string.Colombia));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.colombia_square, 0,0,0);
                break;
            case 5:
                textView.setText(resources.getString(R.string.Argentina));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.argentina_square, 0,0,0);
                break;
            case 6:
                textView.setText(resources.getString(R.string.Guatemala));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.guatemala_square, 0,0,0);
                break;
            case 7:
                textView.setText(resources.getString(R.string.Uruguay));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uruguay_square, 0,0,0);
                break;
            case 8:
                textView.setText(resources.getString(R.string.Venezuela));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.venezuela_square, 0,0,0);
                break;
            case 9:
                textView.setText(resources.getString(R.string.Nicaragua));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nicaragua_square, 0,0,0);
                break;
            case 10:
                textView.setText(resources.getString(R.string.Costa_Rica));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.costa_rica_square, 0,0,0);
                break;
            case 11:
                textView.setText(resources.getString(R.string.Panama));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.panama_square, 0,0,0);
                break;
            default:
                textView.setText(resources.getString(R.string.OtrosPaises));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.otros_square, 0,0,0);
                break;
        }
    }

    /**
     * <p>Método que, con base en un id de país, agrega a un ImageView la bandera del país correspondiente.
     * Se utiliza en: listado de Células de Entrenamiento.</p>
     * @param imageView
     * @param idPais
     */
    public static void asignarPais(ImageView imageView, int idPais, int x){
        switch (idPais){
            case 1:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.otros_square));
                break;
            case 2:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.mexico_square));
                break;
            case 3:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.brasil_square));
                break;
            case 4:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.colombia_square));
                break;
            case 5:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.argentina_square));
                break;
            case 6:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.guatemala_square));
                break;
            case 7:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.uruguay_square));
                break;
            case 8:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.venezuela_square));
                break;
            case 9:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.nicaragua_square));
                break;
            case 10:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.costa_rica_square));
                break;
            case 11:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.panama_square));
                break;
            default:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.otros_square));
                break;
        }
    }

    /**
     * <p>Método que, con base en el nombre de un país, agrega a un ImageView el nombre y la bandera del país.
     * Se utiliza en: listado de autorizaciones en el detalle de una solicitud.</p>
     * @param imageView ImageView donde se colocará la bandera del país.
     * @param nombrePais Id del país.
     */
    public static void asignarPais(ImageView imageView, String nombrePais, int x){
        switch (nombrePais){
            case MEXICO:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.mexico_square));
                break;
            case BRASIL:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.brasil_square));
                break;
            case COLOMBIA:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.colombia_square));
                break;
            case ARGENTINA:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.argentina_square));
                break;
            case GUATEMALA:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.guatemala_square));
                break;
            case URUGUAY:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.uruguay_square));
                break;
            case VENEZUELA:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.venezuela_square));
                break;
            case NICARAGUA:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.nicaragua_square));
                break;
            case COSTA_RICA:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.costa_rica_square));
                break;
            case PANAMA:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.panama_square));
                break;
            default:
                imageView.setImageDrawable(resources.getDrawable(R.drawable.otros_square));
                break;
        }
    }

    /**
     * <p>Método que, con base en un String, permite obtener un formato de fecha adecuado.
     * Se utiliza en: fechas del detalle de solicitud.</p>
     * @param fecha
     * @return
     */
    public static String obtenerFechaSolicitud(String fecha){
        //Separando el día, mes y año de la hora.
        String[] informacion = fecha.split(" ");

        String[] dayMonthYear;

        //Bandera para saber qué tipo de formato de fecha se envió desde el servidor.
        boolean fechaConGuion = false;

        //Identificando si el tipo de separador del día, mes y año es "-" o "\"
        if (fechaPattern.matcher(informacion[0]).matches()){
            dayMonthYear = informacion[0].split("-");
            fechaConGuion = true;
        } else {
            dayMonthYear = informacion[0].split("/");
        }

        //Arreglo de Strings donde se almacena la versión corta de los meses.
        String[] monthArray = resources.getStringArray(R.array.short_months);
        String month = monthArray[Integer.parseInt(dayMonthYear[1])-1];

        SimpleDateFormat inpur = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
        if (fechaConGuion){
            return StringManager.parseFecha(dayMonthYear[2]+"/"+month+"/"+dayMonthYear[0], inpur, SharedPreferencesUtil.getInstance().getIdioma());
        }
        else {
            return StringManager.parseFecha(dayMonthYear[0]+"/"+month+"/"+dayMonthYear[2], inpur,  SharedPreferencesUtil.getInstance().getIdioma());
        }

    }

    /**
     * <p>Método que le da formato al tiempo sugerido.
     * Se utiliza en: tiempo sugerido del detalle de solicitud.</p>
     * @param fecha Hora con la terminación "H"
     * @return
     */
    public static String obtenerTiempoSugerido(String fecha){
        String[] fechas = fecha.split(":");
        if (Integer.valueOf(fechas[1])>0){
            return fechas[0].charAt(1)+"."+"5 H";
        } else {
            return fechas[0].charAt(1)+" H";
        }
    }
}
