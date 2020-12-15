package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.MostrarHorarioData;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.DetalleSolicitudView;

public interface ConfirmarHorarioView extends DetalleSolicitudView {
    void onViewSolicitudProgramada();
    void onViewMostrarHorario(MostrarHorarioData data);
}
