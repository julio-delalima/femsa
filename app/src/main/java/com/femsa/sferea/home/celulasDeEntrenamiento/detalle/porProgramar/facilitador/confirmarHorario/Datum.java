package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * <p>Clase que encapsula el año, mes, día, horas, minutos y segundos de un objeto tipo String que
 * contiene el formato yyyy-MM-dd hh:mm-ss.</p>
 */
public class Datum {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int seconds;

    public Datum(String datum){
        if (datum.length()==19 || datum.length()==17 || datum.length()==18 || datum.length()==16) { //Cuando la cadena que se obtiene proviene del arreglo de fechas ya reservadas.
            //Separando año-mes-día y horas:minutos:segundos.
            String[] fechaCompleta = datum.split(" ");

            if (fechaCompleta[0].contains("/")) {
                //Separando el día/mes/año.
                String[] dayMonthYear = fechaCompleta[0].split("/");
                //Asignando el día, mes y año a los miembros de la clase.
                day = Integer.parseInt(dayMonthYear[0]);
                month = Integer.parseInt(dayMonthYear[1]);
                year = Integer.parseInt(dayMonthYear[2]);

            }else{
                //Separando el año-mes-día.
                String[] yearMonthDay = fechaCompleta[0].split("-");
                //Asignando el año, mes y día a los miembros de la clase.
                year = Integer.parseInt(yearMonthDay[0]);
                month = Integer.parseInt(yearMonthDay[1]);
                day = Integer.parseInt(yearMonthDay[2]);
            }

            //Separando horas:minutos:segundos.
            String[] hourMinuteSecond = fechaCompleta[1].split(":");
            //Asignando las horas, minutos, segundos a los miembros de la clase.
            hour = Integer.parseInt(hourMinuteSecond[0]);
            minute = Integer.parseInt(hourMinuteSecond[1]);
            //seconds = Integer.parseInt(hourMinuteSecond[2]);
        } else { //Cuando la cadena que se obtiene proviene de las fechas que se indicaron en la creación de la solicitud.
            //Separando dia/mes/año y horas:minutos.
            String[] fechaCompleta = datum.split(" ");

            //Separando el día/mes/año.
            String[] dayMonthYear = fechaCompleta[0].split("/");
            //Asignando el día, mes y año a los miembros de la clase.
            day = Integer.parseInt(dayMonthYear[0]);
            month = Integer.parseInt(dayMonthYear[1]);
            year = Integer.parseInt(dayMonthYear[2]);

            //Separando horas:minutos.
            String[] hourMinuteSecond = fechaCompleta[1].split(":");
            //Asignando las horas y minutos a los miembros de la clase.
            hour = Integer.parseInt(hourMinuteSecond[0]);
            minute = Integer.parseInt(hourMinuteSecond[1]);
        }
    }

    public Datum(CalendarDay calendarDay, Horario horario){
        year = calendarDay.getYear();
        month = calendarDay.getMonth();
        day = calendarDay.getDay();
        hour = Integer.parseInt(horario.getHora().split("\\.")[0]);
        minute = Integer.parseInt(horario.getHora().split("\\.")[1].equals("5") ? "30" : "0");
    }

    public String sumaMediaHora(Horario horario)
        {
            return String.valueOf(Float.parseFloat(horario.getHora()) + 0.5);
        }

    /**
     * <p>Método para comparar que dos objetos de tipo Datum tengan el mismo día, mes y año.</p>
     * @param obj Objeto de tipo Datum con el que se va a comparar la fecha.
     * @return True si las fechas son las mismas.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Datum){
            return this.day == ((Datum) obj).day && this.month == ((Datum) obj).month && this.year == ((Datum) obj).year;
        } else {
            return false;
        }
    }

    /**
     * <p>Método que permite obtener la versión en String de un objeto de tipo Datum.</p>
     * @return String con formato para realizar una petición.
     */
    @Override
    public String toString() {
        return year+"-"+month+"-"+day+" "+(hour < 10 ? "0"+hour : hour)+":"+(minute < 10 ? "0"+minute : minute);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSeconds() {
        return seconds;
    }
}
