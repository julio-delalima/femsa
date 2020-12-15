package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

/**
 * <p>
 * Objeto que encapsula los siguientes elementos:
 *      id = entero para poder recuperar un objeto en un ArrayList.
 *      calendarDay = objeto que permitirá identificar un día en el calendario.
 *      listHorario = lista de objetos que contienen las horas que están reservadas por horario.
 * </p>
 */
public class FechaProgramada {

    private CalendarDay calendarDay;
    private ArrayList<Horario> listHorario;

    public FechaProgramada(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
        listHorario = new ArrayList<>();
    }

    public FechaProgramada(CalendarDay calendarDay, ArrayList<Horario> list){
        this.calendarDay = calendarDay;
        listHorario = list;
    }

    public CalendarDay getCalendarDay() {
        return calendarDay;
    }

    public ArrayList<Horario> getListHorario() {
        return listHorario;
    }

    public void setHorario(Horario horario){
        listHorario.add(horario);
    }
}
