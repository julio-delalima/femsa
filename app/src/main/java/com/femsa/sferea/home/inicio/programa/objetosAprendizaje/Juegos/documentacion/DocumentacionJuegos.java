package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.documentacion;

import com.femsa.sferea.Utilities.DownloadManager;
import com.femsa.sferea.Utilities.FileManager;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.GameLoadingDialogFragment;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;
import com.femsa.sferea.masterkiwi.MasterKiwiWrapper;

/**
 * Aqui no hay nada solo la documentación :3
 * */
public class DocumentacionJuegos {

    /**
     * Este es el dialogFragment que aparece mientras se descarga, extrae y carga un juego de MK.
     * */
    GameLoadingDialogFragment g;

    /**
     * En esta clase se maneja la descarga de los juegos de manera asíncrona, pasándole de parámetros los datos
     * de la clase JuegosTaskParams, ya que el Web Service retorna un InputStream con el .zip del juego.
     * */
    DownloadManager d;
    ObjetoAprendizajeActivity.JuegosTaskParams t;

    /**
     * Con esta clase se inicia l extracción del .zip que se descargó en  "Android/data/com.femsa.sferea/files/Download/"
     * para que MK pueda acceder a los datos del juego.
     * */
    FileManager f;

    /**
     * El flujo para los juegos es el siguiente:
     * 1.- ObjetoAprendizajeActivity recibe el tipo Juegos
     * 2.- Cuando se hace click sobre el botón de iniciar, se llama al WS de requestObtenerJuegosZip("/api/objetos/obtenerUrlArchivo/")
     * 3.- El WS nos envia el archivo y se recibe como un InputString el cual se envía a DownloadManager para comenzar a descargarlo.
     * 4.- Cuando ya fue descargado se envia a FileManager para hacer la descompresión del mismo.
     * 5.- Al término de la descompresión y ya con la ruta del archivo, se le envián los datos a GameLoadingActivity la cual se
     *     encargará de inicializar MasterKiwi y todos los datos necesarios como el ID y el GameType(Script).
     * 6.- Con la inicialziación de MasterKiwi se manda a llamar a SDLACtivity que es la clase que se encarga de cargar los datos
     *     y las librerías de C++ para corrre el juego.
     *
     * Clases importantes de C++ a considerar son:
     * a) navi.cpp
     *      407:naviEncrypt y 414:naviDecrypt sirven para obtener la información de los .bin como el script del juego que se encuentra
     *      interface/g/typeID.bin
     *      768:LoadGameData, 779:setGameData son funciones dentro del flujo interno para cargar los juegos.
     *      925:init es donde se inicia el flujo de cargar los datos de juegos.
     *      1383:setGame es donde se le carga el script del juego a MasterKiwi
     * b) game.cpp
     * c) main.cpp
     * d) MasterKiwiWrapper.cpp
     *      Aquí se realiza la conexión con su homónimo hecho en Java para conectar las clases de C++ con las de Java,
     *      266:getPreferencedPath tiene harcodeada la ruta para que siempre buque en la memoria interna del dispositivo.
     *
     * Todos éstos están dentro de la ruta cpp/main/src
     * */
    ObjetoAprendizajeActivity ob;


    /**
     * El flujo para comunicar Android con MasterKiwi es el siguiente
     * 1.- Se declara en MasterKiwiWrapper una función de la forma siguiente:
     *          public native void nombreFuncion(tipo Argumentos);
     *
     * 2.- Dentro del MasterKiwiWrapper.C se declara la misma función de la siguiente forea:
     *          Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_nombreFuncion(JNIEnv *env,jobject instance, jArgumentos omnbre)
     *              Donde JArgumentos vale lo siguiente:
     *              String -> jstring
     *                  Y se tiene que convertir dentro de la función así: const char *nombre = (*env)->GetStringUTFChars(env, nombre, 0);
     *              int ->  jint
     *              float -> jfloat**
     *              double -> jdouble**
     *              boolean -> jboole**
     *              **Necesita confirmarse, no uso ninguno de esos pero pues, ahí chécale :3
     * 2.1- Se declara un JMethod en la parte superior
     *          jmethodID onNombreJMethod;
     * 2.2.- Se define in nombre para la función en forma de constante const char * JAVA_NOMBRE_FUNCION = "nombreFuncion";
     * 2.3 Dentro de la función initializeMethods de MasterKiwiWrapper.cpp se agrega la declaración del método que es como MasterKiwi
     *     llamará la funcion
     *          onNombreJMethod = (*env)->GetMethodID(env,javaClassRef,JAVA_NOMBRE_FUNCION,"(Ljava/lang/String;)V");*
     *
     * 3.- Se crea una nueva función en MasterKiwiWrapper que ya reciba los parámetros convertidos (en el caso del String)
     *          void wrapperNombreFuncion(Argumentos) y de esa misma manera se debe declarar en el MasterKiwiWrapper.h
     *
     * 4.- Se declara una nueva función dentro del WrapperBridge.h y se implementa en el WrapperBridge.cpp
     *          void wrapperNaviNombreFuncion(wHandler, args);
     *
     * 5.- Se declara una nueva funcion en el wrapper.hpp
     *          void NaviNombreFuncion(args);
     *
     * 6.- La implementación dentro de Wrapper.cpp debe lucir así:
     *          void Wrapper::NaviNombreFuncion(Args){}
     *
     * 7.- Dentro de navi.h se declara una nueva función cerca del grupo de funciones localizaadas en la línea 450 y cerca de las 429 se declaran las variables que
     *     se van a inicializar o llevar desde la función o método.
     *          void nombreFuncion(args);
     *
     * 8.- La implementación en Navi.cpp lleva la inicialización de variables/argumentos que estamos enviando a través de la función, la cual es tomada por MasterKiwi
     *
     * 9.- Dentro de script_environment.cpp en la función bindSquirrel() se inicializa los nombres de los métodos como los va a leer squirrel
     *          Sqrat::RootTable().Func<std::string(*)()>("nombre_dentro_del_codigo_de_mk", &nombre_funcion_declarada);
     *          en donde nombre_funcion_declarada es el nombre de la función que hemos creado la cual retorna los datos guardados de nuestro método, los cuales viven en Navi y fueron
     *          guardados en el paso 8.
     *          std::tipo  nombre_funcion_declarada()
     *              {
     *                  return gNavi->variable_paso_8;
     *              }
     * */
    MasterKiwiWrapper wrapper;

}
