package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.FechaDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class FechaComparator implements Comparator<FechaDTO>
    {
        /***
         * Método que compara dos fechas de tipo FechaDTO las cuales vienen en formato yyyy-MM-dd HH:mm para
         * poder realizar un ordenamiento de más antiguo a más futuro.
         * */
        public int compare(FechaDTO left, FechaDTO right) {
            Date fechaInicio = null, fechaFin = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            try {
                fechaInicio = sdf.parse(left.getFechaInicio());
                fechaFin = sdf.parse(right.getFechaInicio());
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            return fechaFin != null ? fechaInicio.compareTo(fechaFin) : 0;
        }
    }
