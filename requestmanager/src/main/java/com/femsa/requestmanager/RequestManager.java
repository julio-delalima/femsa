
package com.femsa.requestmanager;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * ___________♥ ♥ ♥ ♥       P E R R I T O      ♥ ♥ ♥ ♥
 * ______________________$$$$$$$$
 * _______________$$$$$$$________$$$$$$$$$
 * _____________$$________________________$$$$
 * ____________$$_____________________________$$
 * ___________$__________________________________$$
 * ___________$$___________________________________$$
 * __________$$__$$______________________$$__________$$
 * ________$$__$$___$$$$_________$$$$____$$__________$$$$
 * ______$$___$$__$$$$__$$_____$$$$__$$_$$_____________$$$
 * ______$$___$$____$$$$_________$$$$___$$_______________$$
 * ______$$___$$________________________$$_______________$$
 * ______$$____$$_______________________$$_____________$$
 * ________$$__$$____$$$$$$_____________$$___________$$$
 * ________$$__$$__$$______$$___________$$_________$$
 * ________$$__$$__$$______$$___________$$_______$$
 * __________$$$$____$$$$$$_____________$$$$____$$$$
 * __________$$$$_____________________$$__$$____$$$
 * ___________$$_$$$$$$$$$$$$_____$$$$______$$$$_$$
 * _____________$$___$$______$$$$$_______________$$
 * _____________$$_____$$$$$$$____________________$$
 * _____________$$________________________________$$
 * ____________$$_________________________________$$
 * ____________$$_________________________________$$
 * ____________$$___________________________________$
 * ____________$$___________________________________$$
 * __________$$_________________________$$___________$
 * __________$$__________$$___________$$_____________$$
 * ________$$__$$________$$_________$$_______________$$
 * ______$$____$$__________$$_______$$_______________$$
 * ______$$____$$____________$$___$$_________________$$
 * ____$$______$$_____________$$_$$_______$$_________$$
 * ____$$______$$________$$____$$$________$$_________$$
 * ____$$______$$________$$____$$$_______$$__________$$
 * ____$$______$$________$$_______________$$__________$$
 * ____$$______$$________$$_______________$$____________$
 * _$$$$_______$$________$$_______________$$____________$$
 * $___$$______$$________$$$$___________$$$$____________$$
 * $___$$______$$________$$__$$_______$$__$$____________$$
 * _$$$$$______$$________$$____$$___$$_____$$___________$$
 * ____$$______$$________$$______$$_______$$___________$$
 * ____$$______$$________$$_____$$________$$___________$$
 * __$$________$$________$$$$$$$$___$$$$$$__$$_________$$
 * __$$________$$________$$______$$$______$$$$_________$$
 * $$________$$__________$$_________$$$$$$__$$__________$
 * $$______$$__________$$$$$$$$$$$$$$$______$$__________$
 * $$_$$_$$$__________$$_____________$$$$$$$__$$_________$
 * _$$$$$$$___________$$______________________$$________$$
 * _____$$__$$__$$__$$_$______________________$$__________$$
 * ______$$$$__$___$__$$______________________$$____________$
 * _______$$___$___$__$________________________$$_$__$$__$$__$
 * _________$$$$$$$$$$__________________________$$_$_$$$$$$$$
 * */


/**
 * <p>Clase que representa al API de peticiones que implementarán las </p>
 */
public final class RequestManager {

    //public static final String API_URL = "http://femsa.sferea.com/"; //QA
    //public static final String API_URL =  "https://qas.mykoflearning.com/"; //QAS
    //public static final String API_URL = "http://naranja.sferea.com:4848/"; //Desarrollo
    public static final String API_URL = "https://mykoflearning.com/"; //Productivo
    //public static final String API_SUBURL = "/apifemsadev";
    public static final String API_SUBURL = "/apifemsa";//Productivo

    public static final String IMAGE_BASE_URL = RequestManager.API_URL + API_SUBURL + "/api/usuarios/obtenerFotoPerfil";
    public static final String IMAGE_PROGRAM_BASE_URL = RequestManager.API_URL + API_SUBURL + "/api/usuarios/obtenerImagenPrograma/";
    public static final String ARCHIVO_PROGRAMA_URL = RequestManager.API_URL + API_SUBURL+"/api/admin/Programas/archivoTipo/"; // +tipo + nombrearchivo
    public static final String CONTENT_TYPE_JSON = "Content-Type: application/json";
    public static final String CONTENT_TYPE_TOKEN = "tokenUsuario";
    public static final String CONTENT_TYPE_ID_PAIS = "idPais";
    public static final String CONTENT_TYPE_ID_IDIOMA = "idIdioma";
    public static final String CONTENT_TYPE_TOKEN_2 = "token";
    public static final String APP_JSON = "application/json";
    public static final String ARCHIVO_URL_KEY = "urlArchivo";
    public static final String IMAGEN_CUADRADA_PAIS = RequestManager.API_URL + API_SUBURL + "/webapp/img/img_paises/cuadradas/";
    public static final String IMAGEN_circular_PAIS = RequestManager.API_URL + API_SUBURL + "/webapp/img/img_paises/circulares/";

    private static Retrofit mRetrofitInstance;

    private static Retrofit mRetrofitInstanceRxJava;

    private static final int TIMEOUT_REQUEST = 90;
    private static OkHttpClient mOkHttpClient;

    /**
     * <p>Código de error mandado por el servidor.</p>
     */
    public interface CODIGO_SERVIDOR {
        // Respuestas informativas
        int CONTINUE = 100;
        int SWITCHING_PROTOCOL = 101;
        int PROCESSING = 102;
        // Respuestas satisfactorias
        int OK = 200;
        int CREATED = 201;
        int ACCEPTED = 202;
        int NON_AUTHORITATIVE_INFORMATION = 203;
        int NO_CONTENT = 204;
        int RESET_CONTENT = 205;
        int PARTIAL_CONTENT = 206;
        int MULTI_STATUS = 207;
        int IM_USER = 226;
        int MULTIPLE_CHOICES = 300;
        int MOVED_PERMANENTLY = 301;
        int FOUND = 302;
        int SEE_OTHER = 303;
        int NOT_MODIFIED = 304;
        int USE_PROXY = 305;
        int UNUSED = 306;
        int TEMPORARY_REDIRECT = 307;
        int PERMANENT_REDIRECT = 308;
        // Errores del cliente
        int BAD_REQUEST = 400;
        int UNAUTHORIZED = 401;
        int PAYMENT_REQUIRED = 402;
        int FORBIDDEN = 403;
        int NOT_FOUND = 404;
        int METHOD_NOT_ALLOWED = 405;
        int NOT_ACCEPTABLE = 406;
        int PROXY_AUTHENTICATION_REQUIRED = 407;
        int REQUEST_TIMEOUT = 408;
        int CONFLICT = 409;
        int GONE = 410;
        int LENGTH_REQUIRED = 411;
        int PRECONDITION_FAILED = 412;
        int PAYLOAD_TOO_LARGE = 413;
        int URI_TOO_LONG = 414;
        int UNSUPPORTED_MEDIA_TYPE = 415;
        int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
        int EXPECTATION_FAILED = 417;
        int TEAPOT = 418;
        int MISDIRECTED_REQUEST = 421;
        int UNPROCESSABLE_ENTITY = 422;
        int LOCKED = 423;
        int FAILED_DEPENDENCY = 424;
        int UPGRADE_REQUIRED = 426;
        int PRECONDITION_REQUIRED = 428;
        int TOO_MANY_REQUEST = 429;
        int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
        int UNAVAILABLE_FOR_LEGAL_REASONS = 451;
        // Errores del servidor
        int INTERNAL_SERVER_ERROR = 500;
        int NOT_IMPLEMENTED = 501;
        int BAD_GATEWAY = 502;
        int SERVICE_UNAVAILABLE = 503;
        int GATEWAY_TIMEOUT = 504;
        int HTTP_VERSION_NOT_SUPPORTED = 505;
        int VARIANT_ALSO_NEGOTIATES = 506;
        int INSUFFICIENT_STORAGE =507;
        int LOOP_DETECTED = 508;
        int NOT_EXTENDED = 510;
        int NETWORK_AUTHENTICATION_REQUIRED = 511;
    }

    /**
     * <p>Código de error mandado por la respuesta del servidor.</p>
     */
    public interface CODIGO_RESPUESTA {
        int RESPONSE_OK = 200;
        int RESPONSE_CREDENCIALES_INCORRECTAS = 204;
        int RESPONSE_ERROR_CREACION = 3;
    }

    /**
     * <p>Método que permite obtener al cliente de la petición Retrofit.</p>
     * @return Objeto de la petición tipo Retrofit.
     */
    public static Retrofit getCliente() {
        if (mOkHttpClient == null) {
            initOkHttp();

        }

        if (mRetrofitInstanceRxJava == null) {
            mRetrofitInstanceRxJava = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(mOkHttpClient)
                    .build();
        }

        return mRetrofitInstanceRxJava;
    }

    /**
     * <p>Método que permite obtener al cliente de la petición Retrofit.</p>
     * @return Objeto de la petición tipo Retrofit.
     */
    public static Retrofit getClienteRxJava() {
        if (mOkHttpClient == null) {
            initOkHttp();
        }

        if (mRetrofitInstance == null) {
            mRetrofitInstance = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(mOkHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return mRetrofitInstance;
    }

    /**
     * <p>Método que inicializa el cliente Http con parámetros como el tiempo máximo de respuesta de
     * la petición.</p>
     */
    private static void initOkHttp() {
        mOkHttpClient = getUnsafeOkHttpClient().build();
    }

    /**
     * <p>Método que permite validar la petición al servidor, con rutas de tipo HTTPS.</p>
     * @return Cliente con la validación al servidor.
     */
    private static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.connectTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

//para debug ->
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
//            <-
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Interfaces de tipo GET utilizadas para realizar las peticiones al servidor web</p>
     * */
    public interface GetMethods {
        @GET(API_SUBURL+"/api/usuarios/logout/")
        Call<ResponseBody> requestLogOut(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/home/obtenerTerminosCondiciones/")
        Call<ResponseBody> requestTermsAndConditions(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/home/obtenerAvisoPrivacidad/")
        Call<ResponseBody> requestPrivacyAdvice(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/home/obtenerCategorias/")
        Call<ResponseBody> requestCategories(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/home/obtenerProgramasVistos/")
        Call<ResponseBody> requestFeatured(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/home/obtenerPorqueViste/")
        Call<ResponseBody> requestSeen(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/home/obtenerProgramas/")
        Call<ResponseBody> requestPrograms(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/home/obtenerRanking/")
        Call<ResponseBody> requestRanking(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/home/obtenerProgamaRanking/")
        Call<ResponseBody> requestProgramsRanking(@Header(CONTENT_TYPE_TOKEN)  String token);

        // Peticiones para mi actividad

        @GET(API_SUBURL+"/api/actividad/obtenerMiActividad/")
        Call<ResponseBody> requestMiActividad(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/actividad/obtenerLogros/")
        Call<ResponseBody> requestObtenerAllLogros(@Header(CONTENT_TYPE_TOKEN)  String token);

        // Peticiones para notificaciones

        @GET(API_SUBURL+"/api/solicitudes/obtenernotificaciones/")
        Call<ResponseBody> requestObtenerTodasLasNotificaciones(@Header(CONTENT_TYPE_TOKEN)  String token);

        // PETICIONES DE CÉLULAS DE ENTRENAMIENTO **********************************************

        //Petición para obtener el listado de solicitudes.
        @GET(API_SUBURL+"/api/solicitudes/obtenerSolicitudes/")
        Call<ResponseBody> requestObtenerListadoSolicitudes(@Header(CONTENT_TYPE_TOKEN)  String token);

        //PETICION DE OBTENER AREAS
        @GET(API_SUBURL+"/api/solicitudes/obtenerAreas/")
        Call<ResponseBody> requestObtenerArea(@Header(CONTENT_TYPE_TOKEN)  String token);

        @GET(API_SUBURL+"/api/paises/obtenerPaises/")
        Call<ResponseBody> requestTraePaises(@Header(CONTENT_TYPE_TOKEN) String token);

        @GET(API_SUBURL+"/api/paises/obtenerPaises/")
        Call<ResponseBody> requestDetallePais(@Header(CONTENT_TYPE_TOKEN)  String token, @Header(CONTENT_TYPE_ID_PAIS) int idPais);

        @GET(API_SUBURL + "/api/idiomas/obtener/{idIdioma}")
        Call<ResponseBody> requestTraeIdiomas(@Path("idIdioma") int idIdioma, @Header(CONTENT_TYPE_TOKEN)  String token);

        //PETICIONES DE MIS RECOMPENSAS ********************************************************

        //Petición para obtener el listado de recompensas
        @GET(API_SUBURL+"/api/recompensas/obtenerRecompensas/")
        Call<ResponseBody> requestObtenerListadoRecompensas(@Header(CONTENT_TYPE_TOKEN)  String token);

        //Petición para obtener el historial de recompensas
        @GET(API_SUBURL+"/api/recompensas/obtenerHistorialRecompensaPorUsuario/")
        Call<ResponseBody> requestObtenerHistorialRecompensas(@Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);
    }

    /**
     * <p>Interfaces de tipo POST utilizadas para realizar las peticiones al servidor web</p>
     * */
    public interface PostMethods {
        /// Peticiones de configuración
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/conf/obtenerDescargaContenido/")
        Call<ResponseBody> requestDescargaWifi(@Header(CONTENT_TYPE_TOKEN) String token, @Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/conf/obtenerConfNotificaciones/")
        Call<ResponseBody> requestHabilitarNotificaciones(@Header(CONTENT_TYPE_TOKEN) String token, @Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/login/")
        Call<ResponseBody> requestLogin(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/actualizarFotoPerfil/")
        Call<ResponseBody> updateProfilePic(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/obtenerStatusUsuario/")
        Call<ResponseBody> checkEmployeeExistance(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerProgramasVistosSubCategoria/")
        Call<ResponseBody> requestMasVistosSubCategoria(@Header(CONTENT_TYPE_TOKEN) String token, @Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerPorqueVisteSubcategorias/")
        Call<ResponseBody> requestPorqueVisteSubCategoria(@Header(CONTENT_TYPE_TOKEN)  String token, @Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerDetallePrograma/")
        Call<ResponseBody> requestProgramDetail(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/programasApp/inscribirmePrograma/")
        Call<ResponseBody> requestInscripcionPrograma(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerTerminosCondicionesST/")
        Call<ResponseBody> requestTermsNoToken(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerAvisoPrivacidadST/")
        Call<ResponseBody> requestAdviceNoToken(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/loginUnico/")
        Call<ResponseBody> requestLoginUnico(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/buscarFecha/")
        Call<ResponseBody> requestCheckDate(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/insertaCredencial/")
        Call<ResponseBody> requestUpdateCredential(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/correo/")
        Call<ResponseBody> requesrSendMail(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/editaCorreoElectronico/")
        Call<ResponseBody> requestEditarCorreo(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerProgramasPorBusqueda/")
        Call<ResponseBody> requestBusquedaProgramas(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN) String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerlike/")
        Call<ResponseBody> requestLike(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerlikeObjeto/")
        Call<ResponseBody> requestObjectLike(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerProgramasPorBusqueda/")
        Call<ResponseBody> requestBrowser(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        //--------------------// Peticiones parra objetos de aprendizaje (Leer con la voz de Muriel de Coraje)

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerObjetos/")
        Call<ResponseBody> requestLearningObject(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerDetalleObjetos/")
        Call<ResponseBody> requestDetailObject(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerBanderaBonusObjetos/")
        Call<ResponseBody> requestMarkAsRead(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        /////////////////// Peticiones para CheckList
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerCheckList/")
        Call<ResponseBody> requestCheckList(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/insertaCheckList/")
        Call<ResponseBody> requestInsertaCheckList(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        //////////////////////// Peticiones para Evaluacion
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerEvaluacion/")
        Call<ResponseBody> requestObtenerEvaluacion(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/insertaEvaluacion/")
        Call<ResponseBody> requestInsertaEvaluacion(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        /////////////////////// Peticiones para Encuesta

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/objetos/obtenerEncuesta/")
        Call<ResponseBody> requestObtenerEncuesta(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/objetos/insertaEncuesta/")
        Call<ResponseBody> requestInsertaEncuesta(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);


        // ----------------------------- Peticiones para Ranking
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerProgamaRankingDetalle/")
        Call<ResponseBody> requestRankingTierlist(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerlikeEmpleadoRanking/")
        Call<ResponseBody> requestRankinglistLike(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerProgramasPorSub/")
        Call<ResponseBody> requestProgramasFiltrados(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerSubCategoriasPorCategoria/")
        Call<ResponseBody> requestCategoriasFiltrados(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerVideoInteractivo/")
        Call<ResponseBody> requestVideoInteractivo(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/home/obtenerBonusObjetos/")
        Call<ResponseBody> requestObtenerBonusObjeto(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        //------------------------------ Peticiones para Notificaciones

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/borrarNotificacion/")
        Call<ResponseBody> requestBorrarNotificacion(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/leerNotificacion/")
        Call<ResponseBody> requestMarcarNotificacionVista(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/importanteNotificacion/")
        Call<ResponseBody> requestNotificacionImportante(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        //PETICIONES DE CÉLULAS DE ENTRENAMIENTO ***********************************************

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/obtenerDetalleSolicitud/")
        Call<ResponseBody> obtenerDetalleSolicitud(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String token);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/obtenerParticipante/")
        Call<ResponseBody> requestObtenerParticipantes(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/obtenerFacilitador/")
        Call<ResponseBody> requestObtenerFacilitadorPorArea(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        // Petición para obtener la autorización de una solicitud junto con los participantes.
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/obtenerAutorizacionParticipante/")
        Call<ResponseBody> requestAutorizarSolicitud(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        //Petición para obtener la autorización y programación de horarios de un facilitador.
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/programarSolicitud/")
        Call<ResponseBody> requestProgramarSolicitud(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/mostrarHorario/")
        Call<ResponseBody> requestMostrarHorario(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/iniciarSolicitud/")
        Call<ResponseBody> requestNuevasolicitud(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/eliminarSolicitud/")
        Call<ResponseBody> requestEliminarSolicitud(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/actualizarSolicitud/")
        Call<ResponseBody> requestActualizarSolicitud(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/enviarCorreoAdmin/")
        Call<ResponseBody> requestContactarAdministrador(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/obtenerPaises/")
        Call<ResponseBody> requestTraePaises(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        /**
         * <p>Petición para agregar/eliminar el participante de una solicitud.</p>
         *
         * @param body         Parámetros de la petición.
         * @param tokenUsuario Token del usuario que realiza la petición.
         */
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/solicitudes/editarSolicitudParticipante/")
        Call<ResponseBody> requestEditarParticipante(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        // Peticiones para Configuración ***********************************************************

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/conf/obtenerConfIdioma/")
        Call<ResponseBody> requestCambiarIdioma(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/actualizaToken/")
        Call<ResponseBody> requestSetTokenFirebase(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        //PETICIONES DE MIS RECOMPENSAS ************************************************************

        //Petición para obtener el detalle de una recompensa.
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/recompensas/obtenerDetalleRecompensa/")
        Call<ResponseBody> requestObtenerDetalleRecompensa(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        //Petición para canjear una recompensa.
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/recompensas/obtenerRecompensaPorUsuario/")
        Call<ResponseBody> requestObtenerRecompensa(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        //Peticiones para VedeoConferencia y vedeocharla con experto.
        //Petición para la vedeoconferencia.
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/objetos/obtenerVideoConferencia/")
        Call<ResponseBody> requestObtenerVedeoConferencia(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        //Peticiones para la vedeocharla con vedeoexperto
        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/objetos/obtenerCharlaConExperto/")
        Call<ResponseBody> requestObtenerVedeoCharlaExperto(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/objetos/insertaCharlaConExperto/")
        Call<ResponseBody> requestInsertarVedeoCharlaExperto(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/objetos/obtenerUrlArchivo/")
        Call<ResponseBody> requestObtenerJuegoZip(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/conf/obtenerConfIdioma/")
        Call<ResponseBody> requestCambiaridioma(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN)  String tokenUsuario);

        /**
         * Peticiones poara Gusanos y Escaleras
         * */

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/admin/Programas/enviarDatosGE/")
        Call<ResponseBody> requestEnviarDatosGusanosEscaleras(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN_2)  String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/usuarios/actualizaSesion/")
        Call<ResponseBody> requestActualizaSesion(@Body RequestBody body);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/games/obtenerJugadores/")
        Call<ResponseBody> requestGetListadoJugadores(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN) String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/games/retarJugador/")
        Call<ResponseBody> requestRetarJugador(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN) String tokenUsuario);

        @Headers(CONTENT_TYPE_JSON)
        @POST(API_SUBURL+"/api/games/aceptarReto/")
        Call<ResponseBody> requestAceptarReto(@Body RequestBody body, @Header(CONTENT_TYPE_TOKEN) String tokenUsuario);
    }


    /**
     * <p>Interfaces de tipo PUT utilizadas para realizar las peticiones al servidor web</p>
     * */
    public interface PutMethods {
        /*@Headers(CONTENT_TYPE_JSON)
        @PUT("api/clientes/detalleCliente/{idCliente}")
        Call<ResponseBody> requestEditarInformacionCliente(@Path("idCliente") int idCliente, @Body RequestBody body);*/
    }

    /**
     * <p>Interfaces de tipo DELETE utilizadas para realizar las peticiones al servidor web</p>
     * */
    public interface DeleteMethods {
       /* @Headers(CONTENT_TYPE_JSON)
        @HTTP(method = "DELETE", path = "api/metodoCobro", hasBody = true)
        Call<ResponseBody> requestEliminarMetodoCobro(@Body RequestBody body);*/
    }


}
