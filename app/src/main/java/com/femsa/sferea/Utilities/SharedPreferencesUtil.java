package com.femsa.sferea.Utilities;

import android.content.SharedPreferences;

import com.femsa.requestmanager.DTO.User.Login.UserInnerData;

import static android.content.Context.MODE_PRIVATE;

/**
 * Clase que trabaja los datos que se guardan en el smartphone.
 */
public class SharedPreferencesUtil {

    public static final String NAME_UTILS = "preferences";
    public static final String IS_LOGGED = "isLogged";
    public static final String ID_EMPLEADO_KEY = "idEmpleado";
    public static final String NUM_EMPLEADO_KEY = "numEmpleado";
    public static final String NUM_RED_KEY = "numRed";
    public static final String NOMBRE_KEY = "nombre";
    //    public static final String A_PATERNO_KEY = "apellidoPaterno";
//  public static final String A_MATERNO_KEY = "apellidoMaterno";
    public static final String CORREO_KEY = "correo";
    public static final String FOTO_PERFIL_KEY = "fotoPerfil";
    public static final String ID_IDIOMA_KEY = "idIdioma";
    public static final String ID_PAIS_KEY = "idPais";
    public static final String DESCARGA__CONTENIDO_KEY = "descargaContenidoWifi";
    public static final String NOTIFICATIONES_KEY = "notificaciones";
    public static final String FECHA_NACIMIENTO_KEY = "fechaNacimiento";
    public static final String ACTIVO_KEY = "activo";
    public static final String BORRADO_KEY = "borrado";
    public static final String ACTIVE_DIRECTORY_KEY = "activeDirectory";
    public static final String TOKEN_USUARIO_KEY = "tokenUsuario";
    public static final String TOKEN_FIREBASE_KEY = "tokenFirebase";
    public static final String SOCIETY_NAME_KEY = "nombreSociedad";
    public static final String TEXTO_DIV_P_KEY = "textoDivp";
    public static final String TEXTO_SDIV_PER_KEY = "textoSDivPer";
    public static final String POSITION_KEY = "posicion";
    public static final String DESCRIPTION_POSITION_KEY = "descPosicion";
    public static final String DESC_UN_ORG_KEY = "descUnOrg";
    public static final String NUM_PERSONAL_SUPERIOR_KEY = "numPersonalSuperior";
    public static final String NOMBRE_PERSONAL_SUPERIOR_KEY = "nombrePersonalSuperior";
    public static final String CONTRIBUTION_LV_KEY = "nivContr";
    public static final String DESCR_NV_CONTRIBUTION_KEY = "descNivContri";
    public static final String FUNCTIONAL_AREA_KEY = "descArFun";
    public static final String DESC_POSITION_SUPERIOR_KEY = "descPosicionSuperior";
    public static final String EXTERNAL_KEY = "externo";
    public static final String REGISTRY_DATE_KEY = "fechaRegistro";
    public static final String VIDEO_CURRENT_KEY = "video";
    public static final String FECHA_CIERRE_SESION_KEY = "fechaCierreSesion";
    public static final String CORCHOLATAS_KEY = "corcholatas";
    public static final String STATUS_SESION_KEY = "statusSesion";
    public static final String CREDENCIAL_KEY = "credencial";
    public static final String INGRESO_SESION_KEY = "ingresoSesion";
    public static final String ROL_KEY = "rol";
    public static final String PAIS_NOMBRE_KEY = "pais";
    public static final String PAIS_BANDERA_KEY = "imgPais";
    public static final String NOMBRE_ADMIN_KEY = "nombreAdministrador";
    private static SharedPreferencesUtil singleton;
    private static SharedPreferences.Editor editor;
    private SharedPreferences sharedpreferences;

    /**
     * Constructor que genera un sharedPreferences.
     */
    private SharedPreferencesUtil() {
        this.sharedpreferences = AppTalentoRHApplication.getApplication().getSharedPreferences(NAME_UTILS, MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    /**
     * Método que permite crear una instancia para los sharedPreferences en caso de que sea nulo.
     */
    private static synchronized void createInstance() {
        if (singleton == null) {
            singleton = new SharedPreferencesUtil();
        }
    }

    /**
     * Método que permite obtener una instancia del SharedPreferencesUtils.
     *
     * @return Instancia del SharedPrefefencesUtils
     */
    public static SharedPreferencesUtil getInstance() {
        if (singleton == null) {
            createInstance();
        }
        return singleton;
    }

    /**
     * Metodo encargado de guardar la sesión de usuario.
     */
    public final void createLoginSession(UserInnerData usuario) {
        editor.putBoolean(IS_LOGGED, true);
        editor.putInt(ID_EMPLEADO_KEY, usuario.getmIdEmpleado());
        editor.putString(NUM_EMPLEADO_KEY, usuario.getmNumEmpleado());
        editor.putString(NUM_RED_KEY, usuario.getmNUmRed());
        editor.putString(NOMBRE_KEY, usuario.getmNombre());
    //    editor.putString(A_PATERNO_KEY, usuario.getmApellidoPaterno());
      //  editor.putString(A_MATERNO_KEY, usuario.getmApellidoMaterno());
        editor.putString(CORREO_KEY, usuario.getmCorreo().trim());
        editor.putString(FOTO_PERFIL_KEY, usuario.getmFotoPerfil());
        editor.putInt(ID_IDIOMA_KEY, usuario.getmIdIdioma());
        editor.putInt(ID_PAIS_KEY, usuario.getmIdPais());
        editor.putBoolean(DESCARGA__CONTENIDO_KEY, usuario.isDescargaContenidoWifi());
        editor.putBoolean(NOTIFICATIONES_KEY, usuario.ismNotificaciones());
        editor.putString(FECHA_NACIMIENTO_KEY, usuario.getmFechaNacimiento());
        editor.putBoolean(ACTIVO_KEY, usuario.ismActivo());
        editor.putBoolean(BORRADO_KEY, usuario.ismBorrado());
        editor.putBoolean(ACTIVE_DIRECTORY_KEY, usuario.ismActiveDirectory());
        editor.putString(TOKEN_USUARIO_KEY, usuario.getmTokenUsuario());
        editor.putString(TOKEN_FIREBASE_KEY, "5464654");//usuario.getTokenFirebase());
        editor.putString(SOCIETY_NAME_KEY, usuario.getSocietyName());
        editor.putString(TEXTO_DIV_P_KEY, usuario.getTextDivp());
        editor.putString(TEXTO_SDIV_PER_KEY, usuario.getTextoSDiv());
        editor.putInt(POSITION_KEY, usuario.getPosition());
        editor.putString(DESCRIPTION_POSITION_KEY, usuario.getDescPosition());
        editor.putString(DESC_UN_ORG_KEY, usuario.getDescUnOrg());
        editor.putInt(NUM_PERSONAL_SUPERIOR_KEY, usuario.getNumPersonalSuperior());
        editor.putString(NOMBRE_PERSONAL_SUPERIOR_KEY, usuario.getNombrePersonalSuperor());
        editor.putInt(CONTRIBUTION_LV_KEY, usuario.getNvContribucion());
        editor.putString(DESCR_NV_CONTRIBUTION_KEY, usuario.getDescNvContribution());
        editor.putString(FUNCTIONAL_AREA_KEY, usuario.getFunctionalArea());
        editor.putString(DESC_POSITION_SUPERIOR_KEY, usuario.getDescPositionSuperior());
        editor.putBoolean(EXTERNAL_KEY, usuario.isExterno());
        editor.putString(REGISTRY_DATE_KEY, usuario.getRegistryDate());
        editor.putString(FECHA_CIERRE_SESION_KEY, usuario.getmFechaCierreSesion());
        editor.putInt(CORCHOLATAS_KEY, usuario.getCorcholatas());
        editor.putBoolean(STATUS_SESION_KEY, usuario.getmStatusSesion());
        editor.putString(CREDENCIAL_KEY, usuario.getmCcredencial());
        editor.putInt(INGRESO_SESION_KEY, usuario.getmNumeroTotalIngresoSesion());
        editor.putString(ROL_KEY, usuario.getRol());
        editor.putString(PAIS_NOMBRE_KEY, usuario.getNombrePais());
        editor.putString(PAIS_BANDERA_KEY, usuario.getRutaBanderaPais());
        editor.putString(NOMBRE_ADMIN_KEY, usuario.getNombreAdmin());
        editor.apply();
    }

    public final void setVideoValue(int value)
    {
        editor.putInt(VIDEO_CURRENT_KEY, value);
    }

    public final void closeSession() {
        editor.putBoolean(IS_LOGGED, false);
        editor.putInt(ID_EMPLEADO_KEY, -1);
        editor.putString(NUM_EMPLEADO_KEY, "");
        editor.putString(NUM_RED_KEY, "");
        editor.putString(NOMBRE_KEY, "");
       // editor.putString(A_PATERNO_KEY, "");
      //  editor.putString(A_MATERNO_KEY, "");
        editor.putString(CORREO_KEY, "");
        editor.putString(FOTO_PERFIL_KEY, "");
        editor.putInt(ID_IDIOMA_KEY, -1);
        editor.putInt(ID_PAIS_KEY, -1);
        editor.putBoolean(DESCARGA__CONTENIDO_KEY, false);
        editor.putBoolean(NOTIFICATIONES_KEY, false);
        editor.putString(FECHA_NACIMIENTO_KEY, "");
        editor.putBoolean(ACTIVO_KEY, false);
        editor.putBoolean(BORRADO_KEY, false);
        editor.putBoolean(ACTIVE_DIRECTORY_KEY, false);
        editor.putString(TOKEN_USUARIO_KEY, "");
        editor.putString(TOKEN_FIREBASE_KEY, "");

        editor.putString(SOCIETY_NAME_KEY, "");
        editor.putString(TEXTO_DIV_P_KEY, "");
        editor.putString(TEXTO_SDIV_PER_KEY, "");
        editor.putInt(POSITION_KEY, -1);
        editor.putString(DESCRIPTION_POSITION_KEY, "");
        editor.putString(DESC_UN_ORG_KEY, "");
        editor.putInt(NUM_PERSONAL_SUPERIOR_KEY, -1);
        editor.putString(NOMBRE_PERSONAL_SUPERIOR_KEY, "");
        editor.putInt(CONTRIBUTION_LV_KEY, -1);
        editor.putString(DESCR_NV_CONTRIBUTION_KEY, "");
        editor.putString(FUNCTIONAL_AREA_KEY, "");
        editor.putString(DESC_POSITION_SUPERIOR_KEY, "");
        editor.putBoolean(EXTERNAL_KEY, false);
        editor.putString(REGISTRY_DATE_KEY, "");
        editor.putString(FECHA_CIERRE_SESION_KEY, "");
        editor.putInt(CORCHOLATAS_KEY, -1);
        editor.putBoolean(STATUS_SESION_KEY, false);
        editor.putString(CREDENCIAL_KEY, "");
        editor.putInt(INGRESO_SESION_KEY, -1);
        editor.putString(ROL_KEY, "");
        editor.putString(PAIS_NOMBRE_KEY, "");
        editor.putString(PAIS_BANDERA_KEY, "");
        editor.putString(NOMBRE_ADMIN_KEY, "");
        editor.apply();
    }

    public SharedPreferences getSharedpreferences() {
        return sharedpreferences;
    }

    public void setSharedpreferences(SharedPreferences sharedpreferences) {
        this.sharedpreferences = sharedpreferences;
    }

    public static SharedPreferences.Editor getEditor() {
        return editor;
    }

    public static void setEditor(SharedPreferences.Editor editor) {
        SharedPreferencesUtil.editor = editor;
    }

    public int getVideoValue()
    {
        return  this.sharedpreferences.getInt(VIDEO_CURRENT_KEY, 0);
    }

    public boolean isLogged() {
        return this.sharedpreferences.getBoolean(IS_LOGGED, false);
    }

    public int getIdEmpleado() {
        return this.sharedpreferences.getInt(ID_EMPLEADO_KEY, -1);
    }

    public String getNumEmpleado() {
        return this.sharedpreferences.getString(NUM_EMPLEADO_KEY, "");
    }

    public String getNumRed() {
        return this.sharedpreferences.getString(NUM_RED_KEY, "");
    }

    public String getNombreSP() {
        return this.sharedpreferences.getString(NOMBRE_KEY, "");
    }

  /*  public String getPaterno() {
        return this.sharedpreferences.getString(A_PATERNO_KEY, "");
    }

    public String getMaterno() {
        return this.sharedpreferences.getString(A_MATERNO_KEY, "");
    }*/

    public String getCorreo() {
        return this.sharedpreferences.getString(CORREO_KEY, "");
    }

    public String getFotoPerfil() {
        return this.sharedpreferences.getString(FOTO_PERFIL_KEY, "");
    }

    public int getIdioma() {
        return this.sharedpreferences.getInt(ID_IDIOMA_KEY, -1);
    }

    public int getIDPais() {
        return this.sharedpreferences.getInt(ID_PAIS_KEY, -1);
    }

    public boolean isContenidoWifi() {
        return this.sharedpreferences.getBoolean(DESCARGA__CONTENIDO_KEY, false);
    }

    public boolean isNotificaciones() {
        return this.sharedpreferences.getBoolean(NOTIFICATIONES_KEY, false);
    }

    public boolean isActivo() {
        return this.sharedpreferences.getBoolean(ACTIVO_KEY, false);
    }

    public boolean isBorrado() {
        return this.sharedpreferences.getBoolean(BORRADO_KEY, false);
    }

    public boolean isActiveDirectory() {
        return this.sharedpreferences.getBoolean(ACTIVE_DIRECTORY_KEY, false);
    }

    public String getFechaNacimiento() {
        return this.sharedpreferences.getString(FECHA_NACIMIENTO_KEY, "-");
    }

    public String getTokenUsuario() {
        return this.sharedpreferences.getString(TOKEN_USUARIO_KEY, "");
    }

    public String getTokenFirebase() {
        return this.sharedpreferences.getString(TOKEN_FIREBASE_KEY, "");
    }

    public String getSocietyName() {
        return this.sharedpreferences.getString(SOCIETY_NAME_KEY, "-");
    }

    public String getTextoDivP() {
        return this.sharedpreferences.getString(TEXTO_DIV_P_KEY, "-");
    }

    public String getTextoSDivP() {
        return this.sharedpreferences.getString(TEXTO_SDIV_PER_KEY, "-");
    }

    public int getPosition() {
        return this.sharedpreferences.getInt(POSITION_KEY, -1);
    }

    public String getDescriptionPosition() {
        return this.sharedpreferences.getString(DESCRIPTION_POSITION_KEY, "-");
    }

    public String getDescUnOrg() {
        return this.sharedpreferences.getString(DESC_UN_ORG_KEY, "-");
    }

    public int getNumPersonalSuperior() {
        return this.sharedpreferences.getInt(NUM_PERSONAL_SUPERIOR_KEY, -1);
    }

    public String getNombrePersonalSuperior() {
        return this.sharedpreferences.getString(NOMBRE_PERSONAL_SUPERIOR_KEY, "-");
    }

    public int getContributionLevel() {
        return this.sharedpreferences.getInt(CONTRIBUTION_LV_KEY, -1);
    }

    public String getDescrNvContribution() {
        return this.sharedpreferences.getString(DESCR_NV_CONTRIBUTION_KEY, "-");
    }

    public String getFunctionalArea() {
        return this.sharedpreferences.getString(FUNCTIONAL_AREA_KEY, "-");
    }

    public String getDescPositionSuperior() {
        return this.sharedpreferences.getString(DESC_POSITION_SUPERIOR_KEY, "-");
    }

    public boolean isExternal() {
        return this.sharedpreferences.getBoolean(EXTERNAL_KEY, false);
    }

    public String getRegistryDate() {
        return this.sharedpreferences.getString(REGISTRY_DATE_KEY, "-");
    }

    public String getLogOutDate() {
        return this.sharedpreferences.getString(FECHA_CIERRE_SESION_KEY, "-");
    }
    public Boolean isStatusSession() {
        return this.sharedpreferences.getBoolean(STATUS_SESION_KEY, false);
    }

    public int getCorcholatas(){
        return this.sharedpreferences.getInt(CORCHOLATAS_KEY, -1);
    }

    public String getCredencial(){
        return this.sharedpreferences.getString(CREDENCIAL_KEY, null);
    }

    public String getNombrePais(){
        return this.sharedpreferences.getString(PAIS_NOMBRE_KEY, null);
    }

    public String getImagenPais(){
        return this.sharedpreferences.getString(PAIS_BANDERA_KEY, null);
    }

    public String getNombreAdmin(){
        return this.sharedpreferences.getString(NOMBRE_ADMIN_KEY, null);
    }

    public int getTotalSesiones(){
        return this.sharedpreferences.getInt(INGRESO_SESION_KEY, -1);
    }
}