package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VedeoCharlaConExperto;

public class IntervaloHorasSensuales {

    private String mHoraInicio;

    private String  mHoraFinal;

    private String URLSesion;

    private int mIDHora;

    private String mDiaApartado;

    private int status;

    public IntervaloHorasSensuales(String horaInicio, String horaFinal, String URLSesion, int IDHora) {
        mHoraInicio = horaInicio;
        mHoraFinal = horaFinal;
        this.URLSesion = URLSesion;
        mIDHora = IDHora;
    }

    public IntervaloHorasSensuales(String horaInicio, String horaFinal, String URLSesion, int IDHora, String diaApartado, int status) {
        mHoraInicio = horaInicio;
        mHoraFinal = horaFinal;
        this.URLSesion = URLSesion;
        mIDHora = IDHora;
        mDiaApartado = diaApartado;
        this.status = status;
    }

    public IntervaloHorasSensuales(String horaInicio, String horaFinal, String URLSesion, int IDHora, String diaApartado) {
        mHoraInicio = horaInicio;
        mHoraFinal = horaFinal;
        this.URLSesion = URLSesion;
        mIDHora = IDHora;
        mDiaApartado = diaApartado;
    }

    public IntervaloHorasSensuales(String horaInicio, String horaFinal, String URLSesion, int IDHora, int status) {
        mHoraInicio = horaInicio;
        mHoraFinal = horaFinal;
        this.URLSesion = URLSesion;
        mIDHora = IDHora;
        this.status = status;
    }

    public String getHoraInicio() {
        return mHoraInicio;
    }

    public String getHoraFinal() {
        return mHoraFinal;
    }

    public String getURLSesion() {
        return URLSesion;
    }

    public int getIDHora() {
        return mIDHora;
    }

    public String getDiaApartado() {
        return mDiaApartado;
    }

    public int getStatus() {
        return status;
    }
}
