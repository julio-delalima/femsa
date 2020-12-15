package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.DetalleSolicitudData;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.DatosDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.FechaDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.MostrarHorarioData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.DetallePaisesResponseData;
import com.femsa.requestmanager.Response.CelulasDeEntrenamiento.PaisesResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.LogManager;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters.HorarioAdapter;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.CelulasEntrenamientoDialogFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ConfirmarHorarioActivity extends AppCompatActivity implements View.OnClickListener,
        HorarioAdapter.OnItemClickListener, OnDateSelectedListener, OnMonthChangedListener,
        CelulasEntrenamientoDialogFragment.RechazarDialogListener, ConfirmarHorarioView{

    private ConstraintLayout mClLayout;
    private Toolbar mToolbar;
    private Loader mLoader;
    private TextView mTvMes;
    private TextView mTvAnio;
    private TextView mTvHorario; //TextView donde se coloca el texto "Horario".
    private MaterialCalendarView mMcvCalendario;
    private RecyclerView mRvHorario;
    private Button mBtnConfirmar;
    private int DIA_GLOBAL_PICADO = -1;
    private ArrayList<Horario> mListHorario; //Lista de horas que se muestran en la línea de tiempo. VARIABLE OBLIGATORIA
    private HorarioAdapter mHorarioAdapter;
    ArrayList<FechaDTO> mfechasReducidas = new ArrayList<>();
    private ArrayList<FechaProgramada> mListNotAvailable; //Lista de horarios que no están disponibles porque fueron seleccionados por otro facilitador.

    private HashSet<CalendarDay> enabledDates; //Lista de fechas para habilitar en el calendario.
    //private HashSet<CalendarDay> unavailableDates; //Lista de fechas para inhabilitar en el calendario.

    private int idSolicitud;
    private int idFacilitador;
    private ArrayList<FechaDTO> listFechasProgramadas; //ArrayList donde se guardarán las fechas que se van a programar.
    private String temaEspecifico;
    private String tipoSolicitud;
    private String nombreFacilitador;
    private int tiempoSolicitud;
    private String fechaInicio;
    private String fechaFin;
    private String temaGeneral;

    private CalendarDay fechaSeleccionada; //Día en el calendario que se seleccionó.
    private int cantidadHorarios = 0; //Cantidad de horarios que se han seleccionado.
    private ArrayList<String> horariosAProgramar;

    private ConfirmarHorarioPresenter mPresenter;
    private static Datum DiaInicioGlobal = null;
    private static long diffGeneral;
    static final int PICK_CONTACT_REQUEST = 1;  // The request code


    private HashMap<Integer, ArrayList<Horario>> matrizHorarios = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obteniendo el id de la solicitud.
        idSolicitud = getIntent().getIntExtra("idSolicitud", 0);
        setContentView(R.layout.activity_confirmar_horario);
        bindViews();
        bindListeners();
        customizeCalendar();
        initializeToolbar();
        //Creando el intervalo de horas que se mostrarán en la línea de tiempo.
        mListHorario = crearListaDeHoras(6, 18);

        initializeAdapters();
        initializePresenter();
    }

    private void bindViews() {
        mClLayout = findViewById(R.id.cl_activity_confirmar_horario);
        mToolbar = findViewById(R.id.toolbar_confirmar_horario_activity);
        mLoader = Loader.newInstance();
        mTvMes = findViewById(R.id.tv_activity_confirmar_horario_mes);
        mTvAnio = findViewById(R.id.tv_activity_confirmar_horario_anio);
        mTvHorario = findViewById(R.id.tv_horario);
        mMcvCalendario = findViewById(R.id.mcv_activity_confirmar_horario);
        mRvHorario = findViewById(R.id.rv_activity_confirmar_horario);
        mBtnConfirmar = findViewById(R.id.btn_activity_confirmar_horario);
    }

    private void bindListeners(){
        mBtnConfirmar.setOnClickListener(this);
        mMcvCalendario.setOnDateChangedListener(this);
        mMcvCalendario.setOnMonthChangedListener(this);
    }

    /**
     * <p>Método que configura el calendario.</p>
     */
    private void customizeCalendar(){
        mMcvCalendario.setTopbarVisible(false); //Oculta el mes y las flechas de la parte superior.

        enabledDates = new HashSet<>(); //Creando la lista de fechas que se van a habilitar en el calendario.
        //unavailableDates = new HashSet<>(); //Creando lista de fechas que no estarán disponibles en el calendario.

        //Asignando la fecha actual a los TextView de mes y año que se encuentran arriba del calendario..
        actualizarFecha(mMcvCalendario.getCurrentDate());
    }

    /**
     * <p>Método que permite configurar la Toolbar de la Activity.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * <p>Método que genera una lista de Horario para la línea de tiempo, con los cuales se podrá
     * seleccionar una hora.</p>
     * @return ArrayList de datos dummy.
     */
    private ArrayList<Horario> crearListaDeHoras(int inicio, int ultimo){
        ArrayList<Horario> lista = new ArrayList<>();
        for(float i=(float)inicio; i<=(float)ultimo; i=(float)(i+0.5)){
            lista.add(new Horario(Float.toString(i), false, true));
        }
        //lista.add(new Horario("23.5", false, true));
        return lista;
    }

    /**
     * <p>Método que inicializa el adaptador y manager para el RecyclerView donde se visualizan los
     * horarios.</p>
     */
    private void initializeAdapters(){
        mHorarioAdapter = new HorarioAdapter(mListHorario, this);
        mHorarioAdapter.setOnItemClickListener(this);
        mRvHorario.setAdapter(mHorarioAdapter);
        mRvHorario.setLayoutManager(new LinearLayoutManager(this));

        horariosAProgramar = new ArrayList<>();
    }

    /**
     * <p>Método que inicializa el Presenter para obtener la información del horario.</p>
     */
    private void initializePresenter(){
        mPresenter = new ConfirmarHorarioPresenter(this, new ConfirmarHorarioInteractor());
        mPresenter.iniciarProcesoMostrarHorario(idSolicitud, SharedPreferencesUtil.getInstance().getIdEmpleado());
    }

    /**
     * <p>Método que habilita en el calendario sólo los días en los que se puede programar una solicitud.</p>
     * @param fechasAHabilitar
     */
    private void habilitarDias(ArrayList<String> fechasAHabilitar) throws ParseException {
        Datum fechaParaCalendario=null;
        CalendarDay fechaFinCalendar = null, fechaInicioCalendar = null
                ;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date fechaInicio = null, fechaFin = null;
        Datum fechaInicioDatum = null;
        //Creando la lista de objetos de tipo Datum para separar la información del String.
        for (int i=0; i<fechasAHabilitar.size(); i++){
            Datum datum = new Datum(fechasAHabilitar.get(i));
            if (i==0){
                fechaParaCalendario = datum;
                fechaInicio = sdf.parse(datum.getDay()+"/"+datum.getMonth()+"/"+datum.getYear());
                fechaInicioDatum = datum;
                fechaInicioCalendar = CalendarDay.from(datum.getYear(), datum.getMonth(), datum.getDay());
                DiaInicioGlobal = fechaInicioDatum;
            }
            else
                {
                    fechaFin = sdf.parse(datum.getDay()+"/"+datum.getMonth()+"/"+datum.getYear());
                    fechaFinCalendar = CalendarDay.from(datum.getYear(), datum.getMonth(), datum.getDay());
                }
            enabledDates.add(CalendarDay.from(datum.getYear(), datum.getMonth(), datum.getDay()));
        }


        long diffInMillies = Math.abs(fechaFin.getTime() - fechaInicio.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        int mes = fechaInicioDatum.getMonth();
        int dia = fechaInicioDatum.getDay();
        for(int i = 0; i < diff; i++)
            {
                try
                    {
                        CalendarDay nuevodia = CalendarDay.from(fechaInicioDatum.getYear(),  mes , dia);
                        if(nuevodia.isInRange(fechaInicioCalendar, fechaFinCalendar))
                            {
                                enabledDates.add(nuevodia);
                            }
                        LogManager.d("Meses", mes + "/"+dia);
                        dia++;
                    }
                catch(Exception ex)
                    {
                        mes++;
                        dia = 1;
                        LogManager.d("Meses", mes + "/"+dia);
                    }
            }

        //unavailableDates.add(CalendarDay.from(2019, 7, 4));

        //Asignando un Decorator que permite deshabilitar todos los días del calendario.
        mMcvCalendario.addDecorator(new AllDaysDisabledDecorator());

        //Asignando un Decorator que permite habilitar solo los días que se encuentran dentro de enabledDates.
        mMcvCalendario.addDecorator(new AvailableDaysDecorator(R.color.femsa_red_b, enabledDates));

        //Asignando un Decorator que permite deshabilitar los días que ya hayan sido seleccionados por otro facilitador.
        //mMcvCalendario.addDecorator(new UnavailableDaysDecorator(R.color.femsa_red_b, unavailableDates));

        //Colocando en el calendario la primer fecha que se va a programar.
        mMcvCalendario.setCurrentDate(CalendarDay.from(fechaParaCalendario.getYear(), fechaParaCalendario.getMonth(), fechaParaCalendario.getDay()));

        mMcvCalendario.setAllowClickDaysOutsideCurrentMonth(false);

        diffGeneral = diff;

        for (int i=0; i <= diff; i++){
            matrizHorarios.put(i, crearListaDeHoras(6, 18));
        }
    }

    /**
     * <p>Método que actualiza el mes y el año que se encuentran arriba del calendario.</p>
     * @param fecha FechaProgramada actual.
     */
    private void actualizarFecha(CalendarDay fecha){
        //Actualizando el mes.
        switch (fecha.getMonth()){
            case 1:
                mTvMes.setText(getString(R.string.january));
                break;
            case 2:
                mTvMes.setText(getString(R.string.february));
                break;
            case 3:
                mTvMes.setText(getString(R.string.march));
                break;
            case 4:
                mTvMes.setText(getString(R.string.april));
                break;
            case 5:
                mTvMes.setText(getString(R.string.may));
                break;
            case 6:
                mTvMes.setText(getString(R.string.june));
                break;
            case 7:
                mTvMes.setText(getString(R.string.july));
                break;
            case 8:
                mTvMes.setText(getString(R.string.august));
                break;
            case 9:
                mTvMes.setText(getString(R.string.september));
                break;
            case 10:
                mTvMes.setText(getString(R.string.october));
                break;
            case 11:
                mTvMes.setText(getString(R.string.november));
                break;
            case 12:
                mTvMes.setText(getString(R.string.december));
                break;
        }
        //Actualizando el año.
        mTvAnio.setText(Integer.toString(fecha.getYear()));
    }

    /**
     * <p>Método que crea el DialogFragment que se genera cuando se presiona el botón "Confirmar".</p>
     */
    private void initializeDialogFragment(){
        CelulasEntrenamientoDialogFragment dialogFragment = CelulasEntrenamientoDialogFragment.newInstance(getString(R.string.horario_cumfirmado));
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(), "participacionConfirmada");
    }

    /**
     * <p>Método que valida que se hayan seleccionado el total de horas. Este método se manda a llamar al presionar
     * el botón "Aceptar".</p>
     */
    private void validarDatos(){
        if(listFechasProgramadas.size() > 0)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    listFechasProgramadas.sort(new FechaComparator());
                }
                for(FechaDTO fechita: listFechasProgramadas)
                    {
                        LogManager.d("Cumfirmar", fechita.toString());
                    }
               // agregarAGoogleCalendar();
                mPresenter.iniciarProcesoProgramarSolicitud(SharedPreferencesUtil.getInstance().getIdEmpleado(), idSolicitud, listFechasProgramadas);
            }
        else
            {
                DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.minimo_opciones_horario));
            }
    }

    /**
     * <p>Método que agrega al calendario los días, y a la línea de tiempo las horas que no se encuentren
     * disponibles.</p>
     * @param listFechas Lista de fechas (inicio y fin) que ya no están disponibles.
     */
    private void setBusySchedule(ArrayList<FechaDTO> listFechas){

        for (int i=0; i<listFechas.size(); i++){


            //ArrayList de Horario donde se almacenarán los horarios que ya están reservados.

            //El par de fechas que se traen del servicio son iguales. Por lo tanto solo se tiene que
            //elegir una instancia.
            Datum datum = new Datum(listFechas.get(i).getFechaInicio());

            Date fechaInicio = null, fechaFin = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            try {
                fechaInicio = sdf.parse(DiaInicioGlobal.getDay()+"/"+DiaInicioGlobal.getMonth()+"/"+DiaInicioGlobal.getYear());
                fechaFin = sdf.parse(datum.getDay()+"/"+datum.getMonth()+"/"+datum.getYear());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diffInMillies = Math.abs(fechaFin.getTime() - fechaInicio.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);



            ArrayList<Horario> horariosNoDisponibles = matrizHorarios.get((int)diff);

            Horario horario = new Horario(String.valueOf(datum.getHour()), true, false);

            //horariosNoDisponibles la hora está en formato 4.0
            for (int j=0; j<horariosNoDisponibles.size(); j++){
                if (Float.parseFloat(horariosNoDisponibles.get(j).getHora())==(float)datum.getHour() + ((datum.getMinute() == 30) ? 0.5 : 0.0 )){
                    horariosNoDisponibles.get(j).setDisponible(false);
                    horariosNoDisponibles.get(j).setmTema(listFechas.get(i).getmTema());
                    horariosNoDisponibles.get(j).setmFacilitador(listFechas.get(i).getmFacilitador());
                    horariosNoDisponibles.get(j).setSeleccionado(true);
                    break;
                }
            }


            matrizHorarios.put((int)diff, horariosNoDisponibles);

            /*FechaDTO fechaDTO = listFechas.get(i);

            Datum fechaInicio = new Datum(fechaDTO.getFechaInicio());
            Datum fechaFin = new Datum(fechaDTO.getFechaFin());

            //Verificando que el día de la fecha de inicio y fin sea la misma para crear un CalendarDay.
            if (fechaInicio.equals(fechaFin)){
                CalendarDay calendarDay = CalendarDay.from(fechaInicio.getYear(), fechaInicio.getMonth(), fechaInicio.getDay());

                ArrayList<Horario> listHorario = new ArrayList<>();
                //Creando un arreglo de horas con base en un inicio y un fin. Estos datos se obtienen de la hora de inicio y fin.
                for (int j=fechaInicio.getHour(); j<=fechaFin.getHour(); i++){
                    listHorario.add(new Horario(Integer.toString(i), true, false));
                }
                //El identificador del id será el día de CalendarDay.
                mListNotAvailable.add(new FechaProgramada(calendarDay, listHorario));

                //Limpiando el Adapter
                mHorarioAdapter.setListHorario(listHorario);
                mHorarioAdapter.notifyDataSetChanged();
            }*/
        }


        //mHorarioAdapter.setListHorario(horariosNoDisponibles);
        //mHorarioAdapter.notifyDataSetChanged();
    }

    /**
     * <p>Método que permite obtener las horas, minutos o segundos de una hora completa. Estos datos se pueden obtener mediante
     * índices: 0->horas, 1->minutos, 2->segundos. Se usa para saber el total de horas que se deben programar.</p>
     * @param tiempoSolicitud Tiempo de la solicitud en forma de String que incluye las horas, minutos y segundos.
     * @return Int que representa el número de horas.
     */
    private int obtenerTiempoSolicitud(String tiempoSolicitud, int dato){
        //Obteniendo las horas, minutos y segundos del tiempo de la solicitud. Estos se obtienen separando
        //los datos que son divididos por un símbolo ":".
        String[] tiempo = tiempoSolicitud.split(":");
        if (dato==0){
            return Integer.parseInt(tiempo[0]);
        } else if (dato==1){
            return Integer.parseInt(tiempo[1]);
        } else if (dato==2){
            return Integer.parseInt(tiempo[2]);
        } else {
            return Integer.parseInt(tiempo[0]);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_activity_confirmar_horario){
            validarDatos();
        }
    }

    /**
     * <p>Listener para seleccionar/deseleccionar un elemento del listado del listado de horarios.</p>
     * Al hacer click existen dos posibles casos:
     *      1) Si el elemento no está seleccionado se cambiará el color de fondo y se mostrarán los
     *         textos con información respecto a la solicitud.
     *      2) Si el elemento está seleccionado se cambiará el color de fondo y se ocultarán los
     *         textos con información respecto a la solicitud.
     * La cantidad de elementos que se pueden seleccionar están limitados por la variable tiempoSolicitud.
     */
    @Override
    public void onHorarioSeleccionado(View view, Horario horario) {
        ArrayList<Horario> diaActial = matrizHorarios.get(DIA_GLOBAL_PICADO);

        if (horario.isDisponible()){
            TextView tipo = view.findViewById(R.id.tv_item_horario_tipo);
            TextView titulo = view.findViewById(R.id.tv_item_horario_tema_especifico);
            TextView tema = view.findViewById(R.id.tv_item_nombre_facilitador);
            ConstraintLayout contenedorInterior = view.findViewById(R.id.cl_item_horario_contenido);
            if (!horario.isSeleccionado()){ //no está seleccionado
                for(int i = 0; i < 48; i++)
                    {
                        if(diaActial.get(i).getHora().equals(horario.getHora()))
                            {
                                diaActial.get(i).setSeleccionado(false);
                                break;
                            }
                    }
              //  if (cantidadHorarios<2 * tiempoSolicitud){
                    contenedorInterior.setBackgroundColor(getResources().getColor(R.color.femsa_gray_a));
                    tipo.setVisibility(View.VISIBLE);
                    titulo.setVisibility(View.VISIBLE);
                    tema.setVisibility(View.VISIBLE);
                    horario.setSeleccionado(true);
                    Datum datum = new Datum(fechaSeleccionada, horario);
                    Horario sigHoraio = new Horario("", true, true);
                    sigHoraio.setHora(datum.sumaMediaHora(horario));
                    Datum datumSiguiente = new Datum(fechaSeleccionada, sigHoraio);
                    //Agregar el elemento que se seleccionó.
                    listFechasProgramadas.add(new FechaDTO(datum.toString(), datumSiguiente.toString()));
                    cantidadHorarios++;
                    //Toast.makeText(this, String.valueOf(cantidadHorarios), Toast.LENGTH_SHORT).show();
                //} else{
                    //SnackbarManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.max_horas), getResources().getString(R.string.femsa));
                //}
            } else{ //true
                for(int i = 0; i < 48; i++)
                {
                    if(diaActial.get(i).getHora().equals(horario.getHora()))
                    {
                        diaActial.get(i).setSeleccionado(true);
                        break;
                    }
                }
                Datum datum = new Datum(fechaSeleccionada, horario);
                Horario sigHoraio = new Horario("", true, true);
                sigHoraio.setHora(datum.sumaMediaHora(horario));
                Datum datumSiguiente = new Datum(fechaSeleccionada, sigHoraio);
                //Agregar el elemento que se seleccionó.
                for(FechaDTO fechita: listFechasProgramadas){
                    if(fechita.getFechaInicio().equals(datum.toString()) && fechita.getFechaFin().equals(datumSiguiente.toString())){
                        listFechasProgramadas.remove(fechita);
                        break;
                    }
                }
                contenedorInterior.setBackground(getResources().getDrawable(R.drawable.border_item_horario));
                tipo.setVisibility(View.INVISIBLE);
                titulo.setVisibility(View.INVISIBLE);
                tema.setVisibility(View.INVISIBLE);
                horario.setSeleccionado(false);
                //Eliminar el elemento que se seleccionó.
                cantidadHorarios--;
                //Toast.makeText(this, String.valueOf(cantidadHorarios), Toast.LENGTH_SHORT).show();
            }
            matrizHorarios.put(DIA_GLOBAL_PICADO, diaActial);
        }
        /*if (cantidadHorarios<tiempoSolicitud ){
            TextView tipo = view.findViewById(R.id.tv_item_horario_tipo);
            TextView titulo = view.findViewById(R.id.tv_item_horario_nombre);
            TextView tema = view.findViewById(R.id.tv_item_horario_tema_especifico);
            ConstraintLayout contenedorInterior = view.findViewById(R.id.cl_item_horario_contenido);
            if (!horario.isSeleccionado()){
                if (horario.isDisponible()){
                    contenedorInterior.setBackgroundColor(getResources().getColor(R.color.femsa_gray_a));
                    tipo.setVisibility(View.VISIBLE);
                    titulo.setVisibility(View.VISIBLE);
                    tema.setVisibility(View.VISIBLE);
                    horario.setSeleccionado(true);
                    Datum datum = new Datum(fechaSeleccionada, horario);
                    //Agregar el elemento que se seleccionó.
                    listFechasProgramadas.add(new FechaDTO(datum.toString(), datum.toString()));
                    cantidadHorarios++;
                    Toast.makeText(this, String.valueOf(cantidadHorarios), Toast.LENGTH_SHORT).show();
                }
            } else{
                contenedorInterior.setBackground(getResources().getDrawable(R.drawable.border_item_horario));
                tipo.setVisibility(View.INVISIBLE);
                titulo.setVisibility(View.INVISIBLE);
                tema.setVisibility(View.INVISIBLE);
                horario.setSeleccionado(false);
                //Eliminar el elemento que se seleccionó.
                cantidadHorarios--;
                Toast.makeText(this, String.valueOf(cantidadHorarios), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Has seleccionado el número máximo de horarios.", Toast.LENGTH_SHORT).show();
        }*/
    }

    /**
     * <p>Listener para escuchar cuando se selecciona un día en el calendario.</p>
     * @param widget Vista asociada al listener.
     * @param date La fecha que fue seleccionada o deseleccionada.
     * @param selected True si el día fue seleccionado; False en caso contrario.
     */
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        Date fechaInicio = null, fechaFin = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            fechaInicio = sdf.parse(DiaInicioGlobal.getDay()+"/"+DiaInicioGlobal.getMonth()+"/"+DiaInicioGlobal.getYear());
            fechaFin = sdf.parse(date.getDay()+"/"+date.getMonth()+"/"+date.getYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffInMillies = Math.abs(fechaFin.getTime() - fechaInicio.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        DIA_GLOBAL_PICADO = (int) diff;
        ArrayList<Horario> horarioActual = new ArrayList<>();
        horarioActual.addAll(matrizHorarios.get(DIA_GLOBAL_PICADO));
        fechaSeleccionada = date;
        if (mTvHorario.getVisibility()!=View.VISIBLE) { //Solo se ejecuta la primera vez que se selecciona un día del calendario para mostrar la línea de tiempo.
            mTvHorario.setVisibility(View.VISIBLE);
            mRvHorario.setVisibility(View.VISIBLE);
            mBtnConfirmar.setEnabled(true);
        }
        //mHorarioAdapter.clear();
        mHorarioAdapter.setListHorario(horarioActual);
        mHorarioAdapter.notifyDataSetChanged();
        //Resetear los elementos que se encuentran en el Adapter de horarios.
        //Colocarle al Adapter un arreglo de los elementos que se encuentran
    }

    /**
     * <p>Listener para cambiar el texto del mes que se muestra en el calendario.</p>
     * @param widget Vista asociada al listener.
     * @param date Mes que se seleccionó, como el primer día del mes.
     */
    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        actualizarFecha(date);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * <p>Método que finaliza la Activity cuando se presiona el botón "Aceptar" del DialogFragment.</p>
     */
    @Override
    public void onFinishRechazar() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /**
     * <p>Método que se ejecuta cuando se programó una solicitud exitosamente.</p>
     */
    @Override
    public void onViewSolicitudProgramada() {
        //Agregar fehcas al calendario de android
        reduceIntents();
        agregarAGoogleCalendar();
        initializeDialogFragment();
    }


    private void reduceIntents(){
        FechaDTO fechita = new FechaDTO(listFechasProgramadas.get(0).getFechaInicio(), listFechasProgramadas.get(0).getFechaFin());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listFechasProgramadas.sort(new FechaComparator());
        }
        for(int i = 1; i < listFechasProgramadas.size(); i++){
            if(listFechasProgramadas.get(i).getFechaInicio().equals(fechita.getFechaFin())){
                fechita.setFechaFin(listFechasProgramadas.get(i).getFechaFin());
            }
            else{
                mfechasReducidas.add(fechita);
                fechita = new FechaDTO(listFechasProgramadas.get(i).getFechaInicio(), listFechasProgramadas.get(i).getFechaFin());
            }
        }
        mfechasReducidas.add(fechita);
        for(int j = 0; j < mfechasReducidas.size(); j++){
            LogManager.d("Klendario", "-" + mfechasReducidas.get(j).toString());
        }

    }


    /**
     * Método semi recursiva para agregar las fechas ya ordenadas y agrupadas por intervalos a Google Calendar,
     * llamando al calendario por cada grupo de fechas.
     * */
    private void agregarAGoogleCalendar(){
        if(mfechasReducidas.size() > 0)
            {
                Intent Calendarint = new Intent(Intent.ACTION_INSERT);
                //ciclo para recorrer el arreglo de fechas
                Calendar begingtime = Calendar.getInstance();
                Calendar endtime = Calendar.getInstance();
                Calendarint.setType("vnd.android.cursor.item/event");
                Date fecha1 = null, fecha2 = null;
                fecha1 = mfechasReducidas.get(0).getFechaFinDate();
                fecha2 = mfechasReducidas.get(0).getFechaInicioDate();
                //  begingtime.set(fecha.getYear(), fecha.getMonth(), fecha.getDay(), fecha.getHours(), fecha.getMinutes());//metodo que asigna el
                //endtime.set(fecha1.getYear(), fecha1.getMonth(), fecha1.getDay(), fecha1.getHours(), fecha1.getMinutes());
                long endtime1 = fecha2.getTime();//endtime.getTimeInMillis();
                long starttime = fecha1.getTime();//begingtime.getTimeInMillis();
                Calendarint.putExtra(CalendarContract.Events.TITLE, temaEspecifico);
                Calendarint.putExtra(CalendarContract.Events.DESCRIPTION, tipoSolicitud + getResources().getString(R.string.tema_general, temaGeneral) + getResources().getString(R.string.tema_especifico_2temaEspecifico, temaEspecifico));
                Calendarint.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                Calendarint.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, starttime);
                Calendarint.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                Calendarint.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endtime1);
                Calendarint.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(Calendarint, PICK_CONTACT_REQUEST);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                // Do something with the contact here (bigger example below)
            }
            else{
                if(mfechasReducidas.size() > 0)
                {
                    mfechasReducidas.remove(0);
                    agregarAGoogleCalendar();
                }
            }
        }
        else
            {
                LogManager.d("Klendario", "Todo doblemente malisimo");
            }
    }



    /**
     * <p>Método que se ejecuta cuando se obtiene la información del horario exitosamente.</p>
     * @param data Información para poder llevar a cabo la programación de la solicitud.
     */
    @Override
    public void onViewMostrarHorario(MostrarHorarioData data) {
        DatosDTO datosDTO = data.getDatos();

        //a) Habilitar en el calendario solo los días que se pueden seleccionar
        // 1 De fechaInicio/fechaFin se debe obtener el día, mes y año
        // 2 Se crea un ArrayList de Strings donde se va a agregar fechaInicio y fechaFin y se va a pasar al método habilitarDías para agregarlo al arreglo enabledDates
        ArrayList<String> listFechas = new ArrayList<>();
        listFechas.add(datosDTO.getFechaInicio());
        listFechas.add(datosDTO.getFechaFin());
        try {
            habilitarDias(listFechas);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //b) Si hay días que ya estén reservados se deben colocar en el calendario con sus respectivas horas
        listFechasProgramadas = new ArrayList<>();
        //listFechasProgramadas = datosDTO.getListFechas();
        // 1 Verificar que el arreglo de FechaDTO (fechas) no venga vacío
        if (datosDTO.getListFechas().size()!=0){ //Si existen fechas que ya estén ocupadas se van a colocar en el calendario.
            setBusySchedule(datosDTO.getListFechas());
        }
        // 2 Por cada objeto FechaDTO se debe crear un objeto tipo FechaProgramada debido a que:
        // 2.1 Cada objeto FechaDTO contiene dos fechas, el día/mes/año es el mismo, lo único que varían son las horas así que se crea un único CalendarDay
        // 2.2 Un arreglo de objetos Horario. El objeto de FechaDTO contiene una hora inicio y una hora fin; con esos datos se debe usar un ciclo for para crear las horas con un intervalo.
        // 2.3 Cada objeto FechaProgramada se crea con un id con el fin de poder recuperarlos del ArrayList
        //Crear un ArrayList de tipo FechaProgramada para almacenar los elementos

        // c) Se debe modificar el Adapter donde se muestran los horarios para que cuando se seleccione una fecha:
        // 1 La fecha seleccionada se almacene en el objeto fechaSeleccionada.
        // 2 Vaciar el Adapter y colocar en él las horas


        //c) Guardar la otra información que se obtuvo de la petición
        temaEspecifico = datosDTO.getTemaEspecifico();
        tipoSolicitud = datosDTO.getTipoSolicitud();
        tiempoSolicitud = obtenerTiempoSolicitud(datosDTO.getTiempoSolicitud(), 0); //Obteniendo el total de horas que se deben programar.
        temaGeneral = datosDTO.getTemaGeneral();
        idFacilitador = datosDTO.getIdFacilitador();
        nombreFacilitador = datosDTO.getNombreFacilitador();
        //Compartiendo el tipo de solicitud, tema general y tema específico con el Adapter.
        mHorarioAdapter.setTemaEspecifico(temaEspecifico);
        mHorarioAdapter.setTituloSolicitud(temaGeneral);
        mHorarioAdapter.setTipoSolicitud(tipoSolicitud);
        mHorarioAdapter.setNombreFacilitador(SharedPreferencesUtil.getInstance().getNombreSP());

        mClLayout.setVisibility(View.VISIBLE);
    }

    /**
     * <p>Método que no se implementará en esta Activity ya que esta no representa el detalle de una solicitud.</p>
     * @param data Información no necesaria en esta Activity.
     */
    @Override
    public void onViewObtenerDetalleSolicitud(DetalleSolicitudData data) { }

    @Override
    public void onViewMostrarMensaje(int mensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje);
    }

    @Override
    public void onViewMostrarMensaje(int mensaje, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(), mensaje, codigo);
    }

    @Override
    public void onViewMostrarLoader() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mLoader.show(fragmentManager, "loader");
    }

    @Override
    public void onViewOcultarLoader() {
        if (mLoader!=null && mLoader.isAdded()){
            mLoader.dismiss();
        }
    }

    @Override
    public void onViewTokenInvalido() {
        GlobalActions globalActions = new GlobalActions(this);
        globalActions.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onViewMostrarDetallePais(DetallePaisesResponseData data) {

    }

    @Override
    public void onViewDesplegarlistadoPaises(PaisesResponseData data) {

    }
}