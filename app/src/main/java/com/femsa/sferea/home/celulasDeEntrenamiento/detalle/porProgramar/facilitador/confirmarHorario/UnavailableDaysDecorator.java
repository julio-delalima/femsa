package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * <p>Decorator que permite marcar los días que ya han sido ocupados por otro facilitador.</p>
 */
public class UnavailableDaysDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public UnavailableDaysDecorator(int color, Collection<CalendarDay> dates){
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    /**
     * <p>Método que va a decorar los días que no estén disponibles debido a que ya fueron escogidos
     * por otro facilitador.</p>
     * @param day Días que deben decorarse.
     * @return
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    /**
     * <p>Método que permite configurar el Decorator.</p>
     * @param view
     */
    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true); //Deshabilita el día del calendario.
        view.addSpan(new DotSpan(5, color));
    }
}
