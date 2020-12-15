package com.femsa.requestmanager.DTO.User.Login;

import org.json.JSONObject;

public class UserInnerData {

    /**
     * These are the keys used to catch variables from the JSON, names must match exactly
     **/
    private final String ID_EMPLEADO_KEY = "idEmpleado";
    private final String NUM_EMPELADO_KEY = "numEmpleado";
    private final String NUM_RED_KEY = "numRed";
    private final String NOMBRE_KEY = "nombre";
    //private final String APELLIDO_PATERNO_KEY = "apellidoPaterno";
    //private final String APELLIDO_MATERNO_KEY = "apellidoMaterno";
    private final String CORRREO_KEY = "correo";
    private final String FOTO_PERFIL_KEY = "fotoPerfil";
    private final String ID_IDIOMA_KEY = "idIdioma";
    private final String ID_PAIS_KEY = "idPais";
    private final String DESCARGA_CONTENIDO_WIFI_KEY = "descargaContenidoWifi";
    private final String NOTIFICACIONES_KEY = "notificaciones";
    private final String FECHA_NACIMIENTO_KEY = "fechaNacimiento";
    private final String ACTIVO_KEY = "activo";
    private final String BORRADO_KEY = "borrado";
    private final String ACTIVE_DIRECTORY_KEY = "activeDirectory";
    private final String TOKEN_USUARIO_KEY = "tokenUsuario";
    private final String TOKEN_FIREBASE_KEY = "tokenFirebase";
    private final String SOCIETY_NAME_KEY = "nombreSociedad";
    private final String TEXTO_DIV_P_KEY = "textoDivp";
    private final String TEXTO_SDIV_PER_KEY = "textoSDivPer";
    private final String POSITION_KEY = "posicion";
    private final String DESCRIPTION_POSITION_KEY = "descPosicion";
    private final String DESC_UN_ORG_KEY = "descUnOrg";
    private final String NUM_PERSONAL_SUPERIOR_KEY = "numPersonalSuperior";
    private final String NOMBRE_PERSONAL_SUPERIOR_KEY = "nombrePersonalSuperior";
    private final String CONTRIBUTION_LV_KEY = "nivContr";
    private final String DESCR_NV_CONTRIBUTION_KEY = "descNivContri";
    private final String FUNCTIONAL_AREA_KEY = "descArFun";
    private final String DESC_POSITION_SUPERIOR_KEY = "descPosicionSuperior";
    private final String EXTERNAL_KEY = "externo";
    private final String REGISTRY_DATE_KEY = "fechaRegistro";
    private final String FECHA_CIERRE_SESION_KEY = "fechaCierreSesion";
    private final String CORCHOLATAS_KEY = "corcholatas";
    private final String STATUS_SESION_KEY = "statusSesion";
    private final String CREDENCIAL_KEY = "credencial";
    private final String INGRESO_SESION_KEY = "ingresoSesion";
    private final String ROL_KEY = "rol";
    private final String STATUS_CREDENCIAL_KEY = "statusCredencial";
    private final String PAIS_NOMBRE_KEY = "pais";
    private final String PAIS_BANDERA_KEY = "imgPais";
    public final String NOMBRE_ADMIN_KEY = "nombreAdministrador";


    private int mIdEmpleado;
    private String mNumEmpleado;
    private String mNUmRed;
    private String mNombre;
    private String mApellidoPaterno;
    private String mApellidoMaterno;
    private String mCorreo;
    private String mFotoPerfil;
    private int mIdIdioma;
    private int mIdPais;
    private boolean mDescargaContenidoWifi;
    private boolean mNotificaciones;
    private String mFechaNacimiento;
    private boolean mActivo;
    private boolean mBorrado;
    private boolean mActiveDirectory;
    private String mTokenUsuario;
    private String mTokenFirebase;
    private String mSocietyName;
    private String mTextDivp;
    private String mTextoSDiv;
    private int mPosition;
    private String mDescPosition;
    private String mDescUnOrg;
    private int mNumPersonalSuperior;
    private String mNombrePersonalSuperor;
    private int mNvContribucion;
    private String mDescNvContribution;
    private String mFunctionalArea;
    private String mDescPositionSuperior;
    private boolean mExterno;
    private String mRegistryDate;
    private String mFechaCierreSesion;
    private int mCorcholatas;
    private Boolean mStatusSesion;
    private String mCcredencial;
    private int mNumeroTotalIngresoSesion;
    private String mRol;
    private boolean mStatusCredencial;
    private String mNombrePais;
    private String mRutaBanderaPais;
    private String mNombreAdmin;



    /**
     * Empty Constructor
     * */

    public UserInnerData() {
    }

    /**
     * Constructor which takes data from the JSON object
     * and puts it into the variables to be manipulated
     * */

    public UserInnerData(JSONObject data)
    {
        mIdEmpleado = data.optInt(ID_EMPLEADO_KEY);
        mNumEmpleado = data.optString(NUM_EMPELADO_KEY);
        mNUmRed = data.optString(NUM_RED_KEY);
        mNombre = data.optString(NOMBRE_KEY);
        //mApellidoPaterno = data.optString(APELLIDO_PATERNO_KEY);
        //mApellidoMaterno = data.optString(APELLIDO_MATERNO_KEY);
        mCorreo = data.optString(CORRREO_KEY);
        mFotoPerfil = data.optString(FOTO_PERFIL_KEY);
        mIdIdioma = data.optInt(ID_IDIOMA_KEY);
        mIdPais = data.optInt(ID_PAIS_KEY);
        mDescargaContenidoWifi = data.optBoolean(DESCARGA_CONTENIDO_WIFI_KEY);
        mNotificaciones = data.optBoolean(NOTIFICACIONES_KEY);
        mFechaNacimiento = data.optString(FECHA_NACIMIENTO_KEY);
        mActivo = data.optBoolean(ACTIVO_KEY);
        mBorrado = data.optBoolean(BORRADO_KEY);
        mActiveDirectory = data.optBoolean(ACTIVE_DIRECTORY_KEY);
        mTokenUsuario = data.optString(TOKEN_USUARIO_KEY);
        mTokenFirebase = data.optString(TOKEN_FIREBASE_KEY);
        mSocietyName = data.optString(SOCIETY_NAME_KEY);
        mTextDivp = data.optString(TEXTO_DIV_P_KEY);
        mTextoSDiv = data.optString(TEXTO_SDIV_PER_KEY);
        mPosition = data.optInt(POSITION_KEY);
        mDescPosition = data.optString(DESCRIPTION_POSITION_KEY);
        mDescUnOrg = data.optString(DESC_UN_ORG_KEY);
        mNumPersonalSuperior = data.optInt(NUM_PERSONAL_SUPERIOR_KEY);
        mNombrePersonalSuperor = data.optString(NOMBRE_PERSONAL_SUPERIOR_KEY);
        mNvContribucion = data.optInt(CONTRIBUTION_LV_KEY);
        mDescNvContribution = data.optString(DESCR_NV_CONTRIBUTION_KEY);
        mFunctionalArea = data.optString(FUNCTIONAL_AREA_KEY);
        mDescPositionSuperior = data.optString(DESC_POSITION_SUPERIOR_KEY);
        mExterno = data.optBoolean(EXTERNAL_KEY);
        mRegistryDate = data.optString(REGISTRY_DATE_KEY);
        mFechaCierreSesion = data.optString(FECHA_CIERRE_SESION_KEY);
        mCorcholatas = data.optInt(CORCHOLATAS_KEY);
        mStatusSesion = data.optBoolean(STATUS_SESION_KEY);
        mCcredencial = data.optString(CREDENCIAL_KEY);
        mNumeroTotalIngresoSesion = data.optInt(INGRESO_SESION_KEY);
        mRol = data.optString(ROL_KEY);
        mStatusCredencial = data.optBoolean(STATUS_CREDENCIAL_KEY);
        mNombrePais = data.optString(PAIS_NOMBRE_KEY);
        mRutaBanderaPais = data.optString(PAIS_BANDERA_KEY);
        mNombreAdmin = data.optString(NOMBRE_ADMIN_KEY);
    }

    public String getTokenFirebase() {
        return mTokenFirebase;
    }

    public void setTokenFirebase(String tokenFirebase) {
        mTokenFirebase = tokenFirebase;
    }

    public String getSocietyName() {
        return mSocietyName;
    }

    public void setSocietyName(String societyName) {
        mSocietyName = societyName;
    }

    public String getTextDivp() {
        return mTextDivp;
    }

    public void setTextDivp(String textDivp) {
        mTextDivp = textDivp;
    }

    public String getTextoSDiv() {
        return mTextoSDiv;
    }

    public void setTextoSDiv(String textoSDiv) {
        mTextoSDiv = textoSDiv;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public String getDescPosition() {
        return mDescPosition;
    }

    public void setDescPosition(String descPosition) {
        mDescPosition = descPosition;
    }

    public String getDescUnOrg() {
        return mDescUnOrg;
    }

    public void setDescUnOrg(String descUnOrg) {
        mDescUnOrg = descUnOrg;
    }

    public int getNumPersonalSuperior() {
        return mNumPersonalSuperior;
    }

    public void setNumPersonalSuperior(int numPersonalSuperior) {
        mNumPersonalSuperior = numPersonalSuperior;
    }

    public String getNombrePersonalSuperor() {
        return mNombrePersonalSuperor;
    }

    public void setNombrePersonalSuperor(String nombrePersonalSuperor) {
        mNombrePersonalSuperor = nombrePersonalSuperor;
    }

    public int getNvContribucion() {
        return mNvContribucion;
    }

    public void setNvContribucion(int nvContribucion) {
        mNvContribucion = nvContribucion;
    }

    public String getDescNvContribution() {
        return mDescNvContribution;
    }

    public void setDescNvContribution(String descNvContribution) {
        mDescNvContribution = descNvContribution;
    }

    public String getFunctionalArea() {
        return mFunctionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        mFunctionalArea = functionalArea;
    }

    public String getDescPositionSuperior() {
        return mDescPositionSuperior;
    }

    public void setDescPositionSuperior(String descPositionSuperior) {
        mDescPositionSuperior = descPositionSuperior;
    }

    public boolean isExterno() {
        return mExterno;
    }

    public void setExterno(boolean externo) {
        mExterno = externo;
    }

    public String getRegistryDate() {
        return mRegistryDate;
    }

    public void setRegistryDate(String registryDate) {
        mRegistryDate = registryDate;
    }

    /**
         * Getters and setters
         * */

    public int getmIdEmpleado() {
        return mIdEmpleado;
    }

    public void setmIdEmpleado(int mIdEmpleado) {
        this.mIdEmpleado = mIdEmpleado;
    }

    public String getmNumEmpleado() {
        return mNumEmpleado;
    }

    public void setmNumEmpleado(String mNumEmpleado) {
        this.mNumEmpleado = mNumEmpleado;
    }

    public String getmNUmRed() {
        return mNUmRed;
    }

    public void setmNUmRed(String mNUmRed) {
        this.mNUmRed = mNUmRed;
    }

    public String getmNombre() {
        return mNombre;
    }

    public void setmNombre(String mNombre) {
        this.mNombre = mNombre;
    }

    public String getmApellidoPaterno() {
        return mApellidoPaterno;
    }

    public void setmApellidoPaterno(String mApellidoPaterno) {
        this.mApellidoPaterno = mApellidoPaterno;
    }

    public String getmApellidoMaterno() {
        return mApellidoMaterno;
    }

    public void setmApellidoMaterno(String mApellidoMaterno) {
        this.mApellidoMaterno = mApellidoMaterno;
    }

    public String getmCorreo() {
        return mCorreo;
    }

    public void setmCorreo(String mCorreo) {
        this.mCorreo = mCorreo;
    }

    public String getmFotoPerfil() {
        return mFotoPerfil;
    }

    public void setmFotoPerfil(String mFotoPerfil) {
        this.mFotoPerfil = mFotoPerfil;
    }

    public int getmIdIdioma() {
        return mIdIdioma;
    }

    public void setmIdIdioma(int mIdIdioma) {
        this.mIdIdioma = mIdIdioma;
    }

    public int getmIdPais() {
        return mIdPais;
    }

    public void setmIdPais(int mIdPais) {
        this.mIdPais = mIdPais;
    }

    public boolean ismDescargaContenidoWifi() {
        return mDescargaContenidoWifi;
    }

    public void setmDescargaContenidoWifi(boolean mDescargaContenidoWifi) {
        this.mDescargaContenidoWifi = mDescargaContenidoWifi;
    }

    public boolean ismNotificaciones() {
        return mNotificaciones;
    }

    public void setmNotificaciones(boolean mNotificaciones) {
        this.mNotificaciones = mNotificaciones;
    }

    public String getmFechaNacimiento() {
        return mFechaNacimiento;
    }

    public void setmFechaNacimiento(String mFechaNacimiento) {
        this.mFechaNacimiento = mFechaNacimiento;
    }

    public boolean ismActivo() {
        return mActivo;
    }

    public void setmActivo(boolean mActivo) {
        this.mActivo = mActivo;
    }

    public boolean ismBorrado() {
        return mBorrado;
    }

    public void setmBorrado(boolean mBorrado) {
        this.mBorrado = mBorrado;
    }

    public boolean ismActiveDirectory() {
        return mActiveDirectory;
    }

    public void setmActiveDirectory(boolean mActiveDirectory) {
        this.mActiveDirectory = mActiveDirectory;
    }

    public String getmTokenUsuario() {
        return mTokenUsuario;
    }

    public void setmTokenUsuario(String mTokenUsuario) {
        this.mTokenUsuario = mTokenUsuario;
    }

    public boolean isDescargaContenidoWifi() {
        return mDescargaContenidoWifi;
    }

    public void setDescargaContenidoWifi(boolean descargaContenidoWifi) {
        mDescargaContenidoWifi = descargaContenidoWifi;
    }

    public boolean isNotificaciones() {
        return mNotificaciones;
    }

    public void setNotificaciones(boolean notificaciones) {
        mNotificaciones = notificaciones;
    }

    public String getFechaNacimiento() {
        return mFechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        mFechaNacimiento = fechaNacimiento;
    }

    public boolean isActivo() {
        return mActivo;
    }

    public void setActivo(boolean activo) {
        mActivo = activo;
    }

    public boolean isBorrado() {
        return mBorrado;
    }

    public void setBorrado(boolean borrado) {
        mBorrado = borrado;
    }

    public boolean isActiveDirectory() {
        return mActiveDirectory;
    }

    public void setActiveDirectory(boolean activeDirectory) {
        mActiveDirectory = activeDirectory;
    }

    public String getTokenUsuario() {
        return mTokenUsuario;
    }

    public void setTokenUsuario(String tokenUsuario) {
        mTokenUsuario = tokenUsuario;
    }

    public String getmFechaCierreSesion() {
        return mFechaCierreSesion;
    }

    public int getCorcholatas(){
        return mCorcholatas;
    }

    public Boolean getmStatusSesion() {
        return mStatusSesion;
    }

    public String getmCcredencial() {
        return mCcredencial;
    }

    public int getmNumeroTotalIngresoSesion() {
        return mNumeroTotalIngresoSesion;
    }

    public boolean isStatusCredencial() {
        return mStatusCredencial;
    }

    public String getRol() {
        return mRol;
    }

    public String getNombrePais() {
        return mNombrePais;
    }

    public String getRutaBanderaPais() {
        return mRutaBanderaPais;
    }

    public String getNombreAdmin() {
        return mNombreAdmin;
    }
}



