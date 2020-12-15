package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

/**
 * <p>Decorator que permite de deshabilitar todos los días en el calendario.</p>
 */
public class AllDaysDisabledDecorator implements DayViewDecorator {

    /**
     * <p>Para indicar si un día en específico se debe decorar.</p>
     * @param day Día que posiblemente podría ser decorado.
     * @return True si el día provisto debe ser decorado.
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    /**
     * <p>Método que configura que se deben deshabilitar todos los días en el calendario.</p>
     * @param view
     */
    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true); //Deshabilitando todos los días.
    }
}
