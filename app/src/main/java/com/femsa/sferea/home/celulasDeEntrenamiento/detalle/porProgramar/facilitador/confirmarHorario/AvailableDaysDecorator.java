package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * <p>Decorator que permite habilitar ciertos días en el calendario.</p>
 */
public class AvailableDaysDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public AvailableDaysDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    /**
     * <p>Método que decora únicamente los días disponibles.</p>
     * @param day
     * @return
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(false); //Habilita el día en el calendario.
        //view.setSelectionDrawable();
        //view.addSpan(new DotSpan(5, color)); //Adds small dot to bottom of day number text.
    }
}
