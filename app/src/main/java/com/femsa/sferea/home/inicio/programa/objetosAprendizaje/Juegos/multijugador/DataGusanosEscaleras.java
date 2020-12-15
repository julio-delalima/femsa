package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador;

import java.io.Serializable;

public class DataGusanosEscaleras implements Serializable {

    private int usado, posJ1, posJ2, mapa, turno, mIdEmpleadoRetado, mIdPartida, mIDPrograma;

    public DataGusanosEscaleras(int usado, int posJ1, int posJ2, int mapa, int turno, int idEmpleadoRetado, int idPartida, int IDPrograma) {
        this.usado = usado;
        this.posJ1 = posJ1;
        this.posJ2 = posJ2;
        this.mapa = mapa;
        this.turno = turno;
        mIdEmpleadoRetado = idEmpleadoRetado;
        mIdPartida = idPartida;
        mIDPrograma = IDPrograma;
    }

    public int getIdPartida() {
        return mIdPartida;
    }

    public void setIdPartida(int idPartida) {
        mIdPartida = idPartida;
    }

    public int getUsado() {
        return usado;
    }

    public void setUsado(int usado) {
        this.usado = usado;
    }

    public int getPosJ1() {
        return posJ1;
    }

    public void setPosJ1(int posJ1) {
        this.posJ1 = posJ1;
    }

    public int getPosJ2() {
        return posJ2;
    }

    public void setPosJ2(int posJ2) {
        this.posJ2 = posJ2;
    }

    public int getMapa() {
        return mapa;
    }

    public void setMapa(int mapa) {
        this.mapa = mapa;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getIdEmpleadoRetado() {
        return mIdEmpleadoRetado;
    }

    public void setIdEmpleadoRetado(int idEmpleadoRetado) {
        mIdEmpleadoRetado = idEmpleadoRetado;
    }

    public int getIDPrograma() {
        return mIDPrograma;
    }
}
