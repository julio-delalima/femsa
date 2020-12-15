//
// Created by MasterKiwi-Sferea on 13/09/2017.
//
#include "MasterKiwiWrapper.h"

const char * JAVA_MASTERKIWI_CLASS = "com/femsa/sferea/masterkiwi/MasterKiwiWrapper";
const char * JAVA_START_SDL = "startSDL";
const char * JAVA_USER_JSON = "onUsersDataReceived";
const char * JAVA_USER_LOGOUT = "onUserLogOut";
const char * JAVA_GET_PREF_PATH = "getStoragePath";
const char * JAVA_ON_NET_ERROR = "netErrorReported";
const char * JAVA_ATLAS_COMPRESSED = "onAtlasCompressed";
const char * JAVA_RTTEXT_READY = "onTexturesReady";
const char * JAVA_ASSETS_LIST = "assetsToDownload";
const char * JAVA_ASSET_DOWNLOADED = "assetDownloaded";
const char * JAVA_GET_GAME_DATA = "readGameDataJSON";
const char * JAVA_DOWNLOADING_FINISHED = "allAssetsDownloaded";
const char * JAVA_ON_START_GAME = "startingGame";
const char * JAVA_ON_GAMEOVER = "gameOver";
const char * JAVA_ON_EXIT_GAME = "exitGame";
const char * JAVA_NOT_LOGGED = "onNotLoggedIn";
const char * JAVA_NO_GAME_DATA = "onNotGameData";
const char * JAVA_MANDAR_DATOS = "mandarDatos";
bool javaMethodsInitialized = false;
bool javaVMInitialized = false;
bool activateDebugMsg = true;

JavaVM*  javaVM;
jclass  javaClassRef;
jobject javaObject;

jmethodID  startSDL;
jmethodID onUsersData;
jmethodID onUserLogOut;
jmethodID getPath;
jmethodID onNetError;
jmethodID onAtlasCompressed;
jmethodID onTexturesReady;
jmethodID onAssetsList;
jmethodID onAssetDownloaded;
jmethodID allAssetsDownloded;
jmethodID getGameJSON;
jmethodID onStartingGame;
jmethodID onGameOver;
jmethodID onExitGame;
jmethodID notLogged;
jmethodID noGameData;
jmethodID mandarDatos;
jmethodID onSendJsonSQ;

wHandler wrap;

const char* androidPath = "";

void showDebug(const char* message)
{
    if(activateDebugMsg)
    {
        SDL_Log("%s",message);
    }
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_nativeWrapperInit(JNIEnv *env, jobject instance)
{
    showDebug("Se ha inicializado Navi wrapper");
    initialize();
    initializeVM(env,instance);
    getPreferencedPath();
    getGameDataJSON();
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_nativeWrapperStop(JNIEnv *env, jobject instance)
{
    showDebug("Se destruira el wrapper!!");
    freeWrapper(wrap);
    wrap = 0;
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_masterKiwiSetGame(JNIEnv *env, jobject instance,jint gameID)
{
    initialize();
    initializeVM(env,instance);
    wrapperSetGame((int)gameID);
}

JNIEXPORT jboolean JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_masterKiwiHasGameData(JNIEnv *env, jobject instance, jint gameID)
{
    initialize();
    initializeVM(env,instance);
    return wrapperHasGameData((int)gameID);
}

JNIEXPORT jboolean JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_masterKiwiHasGameDataPath(JNIEnv *env, jobject instance, jint gameID, jstring path)
{
    initialize();
    initializeVM(env,instance);
    return wrapperHasGameDataPath((int)gameID, path);
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_masterKiwiAskDataDate(JNIEnv *env, jobject instance, jint gameID)
{
    initialize();
    initializeVM(env,instance);
    wrapperSendDataDateRequest((int) gameID);
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_moveFile(JNIEnv *env,jobject instance,jstring from_,jstring to_)
{
    initialize();
    initializeVM(env,instance);
    const char *from = (*env)->GetStringUTFChars(env, from_, 0);
    const char *to = (*env)->GetStringUTFChars(env, to_, 0);

    wrapperMoveFile(from,to);

    (*env)->ReleaseStringUTFChars(env, from_, from);
    (*env)->ReleaseStringUTFChars(env, to_, to);
}


JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_jsonToSquirrel(JNIEnv *env,jobject instance,jstring json_)
{
    showDebug("*******************************  ESTOY EN C ");
    showDebug(json_);
    const char *json = (*env)->GetStringUTFChars(env, json_, 0);
    wrapperSetJsonSquirrel(json);
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_sendDatosGusnaosYEscaleras(
        JNIEnv *env,jobject instance,jint usado_, jint posJ1_, jint posJ2, jint mapa_, jint turno_)
{
    wrapperSetDataGusanosYEscaleras(usado_, posJ1_, posJ2, mapa_, turno_);
}



JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_changeAudioState(JNIEnv *env,jobject instance)
{
    wrapperChangeAudioState();
}

JNIEXPORT jboolean JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiWrapper_isAudioActivated(JNIEnv *env,jobject instance)
{
    return wrapperIsAudioActive();
}

void initializeVM(JNIEnv* env,jobject obj)
{
    if(!javaVMInitialized)
    {
        (*env)->GetJavaVM(env,&javaVM);
        javaObject = (*env)->NewGlobalRef(env,obj);
        javaVMInitialized = true;
    }
    initializeMethods();
}

void initialize()
{
    if(wrap == 0)
    {
        showDebug("Creating wrap");
        wrap = createWrapper();
    }
}

/**
 * There are two parts to the signature.
 * The first part is enclosed within the parentheses and represents the method's arguments.
 * The second portion follows the closing parenthesis and represents the return type.
 * The mapping between the Java type and C type is
    Type     Character
    boolean      Z
    byte         B
    char         C
    double       D
    float        F
    int          I
    long         J
    object       L
    short        S
    void         V
    array        [
    Note that to specify an object, the "L" is followed by the object's class name and ends with a semi-colon, ';' .
 * */
void initializeMethods()
{
    if(!javaMethodsInitialized)
    {
        JNIEnv* env;
        (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
        jclass dataClass = (*env)->FindClass(env,JAVA_MASTERKIWI_CLASS);
        javaClassRef = (jclass)(*env)->NewGlobalRef(env,dataClass);
        startSDL = (*env)->GetStaticMethodID(env,javaClassRef,JAVA_START_SDL,"(I)V");
        onUsersData = (*env)->GetStaticMethodID(env,javaClassRef,JAVA_USER_JSON,"(Ljava/lang/String;)V");
        onUserLogOut = (*env)->GetMethodID(env,javaClassRef,JAVA_USER_LOGOUT,"()V");
        getPath= (*env)->GetMethodID(env,javaClassRef,JAVA_GET_PREF_PATH,"()Ljava/lang/String;");
        onNetError= (*env)->GetMethodID(env,javaClassRef,JAVA_ON_NET_ERROR,"(Ljava/lang/String;Ljava/lang/String;)V");
        onAtlasCompressed = (*env)->GetMethodID(env,javaClassRef,JAVA_ATLAS_COMPRESSED,"()V");
        onTexturesReady = (*env)->GetMethodID(env,javaClassRef,JAVA_RTTEXT_READY,"()V");
        onAssetsList= (*env)->GetMethodID(env,javaClassRef,JAVA_ASSETS_LIST,"(I)V");
        onAssetDownloaded = (*env)->GetMethodID(env,javaClassRef,JAVA_ASSET_DOWNLOADED,"(I)V");
        allAssetsDownloded = (*env)->GetMethodID(env,javaClassRef,JAVA_DOWNLOADING_FINISHED,"()V");
        getGameJSON = (*env)->GetMethodID(env,javaClassRef,JAVA_GET_GAME_DATA,"()Ljava/lang/String;");
        onStartingGame = (*env)->GetMethodID(env,javaClassRef,JAVA_ON_START_GAME,"()V");
        onGameOver = (*env)->GetMethodID(env,javaClassRef,JAVA_ON_GAMEOVER,"()V");
        mandarDatos = (*env)->GetMethodID(env,javaClassRef,JAVA_MANDAR_DATOS,"(IIIII)V");
        onExitGame = (*env)->GetMethodID(env,javaClassRef,JAVA_ON_EXIT_GAME,"()V");
        notLogged= (*env)->GetMethodID(env,javaClassRef,JAVA_NOT_LOGGED,"()V");
        noGameData= (*env)->GetMethodID(env,javaClassRef,JAVA_NO_GAME_DATA,"()V");
       // onSendJsonSQ= (*env)->GetMethodID(env,javaClassRef,JAVA_SEND_TO_SQUIRREL,"(Ljava/lang/String;)V");
        javaMethodsInitialized = true;
    }
}

void wrapperSetGame(int gameID)
{
    wrapperNaviSetGame(wrap,gameID);
}

bool wrapperHasGameData(int gameID)
{
    return wrapperNaviHasGameData(wrap,gameID);
}

bool wrapperHasGameDataPath(int gameID, const char* path)
{
    return wrapperNaviHasGameDataPath(wrap,gameID, path);
}


void wrapperSendDataDateRequest(int gameID)
{
    wrapperNaviAskDataDate(wrap, gameID);
}

void wrapperMoveFile(const char* source,const char* target)
{
    wrapperNaviMoveFile(wrap,source,target);
}


void wrapperSetJsonSquirrel(const char* json)
{
    showDebug("Estoy en el wrapper set json y vale ");
    showDebug(json);
    wrapperNaviSetJsonSquirrel(wrap, json);
}

void wrapperSetDataGusanosYEscaleras(int usado_,int posJ1_,int posJ2,int mapa_,int turno_)
{
    wrapperNaviSetDataGusanosYEscaleras(wrap, usado_, posJ1_, posJ2, mapa_, turno_);
}

void wrapperChangeAudioState()
{
    wrapperNaviChangeAudio(wrap);
}

bool wrapperIsAudioActive()
{
    return wrapperNaviIsAudioActive(wrap);
}

void callStartSDL(int download)
{
    JNIEnv* env;
    (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
    jint  intParam = download;
    (*env)->CallStaticVoidMethod(env,javaClassRef,startSDL,intParam);
}

void sendUserDataToJava(const char* uData)
{
    JNIEnv* env;
    (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
    jstring stringParam = (*env)->NewStringUTF(env,uData);
    (*env)->CallStaticVoidMethod(env,javaClassRef,onUsersData,stringParam);
}

void sendLogOut()
{
    sendVoidVoidMethod(onUserLogOut);
}


const char* getPreferencedPath()
{
    if(androidPath == "")
    {
    /*    JNIEnv* env;
        (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
        jobject nPath = (*env)->CallObjectMethod(env,javaObject,getPath);

        androidPath = (*env)->GetStringUTFChars(env,nPath,0);
        showDebug(androidPath);*/
        androidPath = "/data/data/com.femsa.sferea/files/Android/data/com.femsa.sferea/files/Download/";
    }
    //
    return androidPath;
}

void sendNetError(const char* netError,const char* masterKiwiError)
{
    JNIEnv* env;
    (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
    jstring netMsg = (*env)->NewStringUTF(env,netError);
    jstring masterKiwiMsg = (*env)->NewStringUTF(env,masterKiwiError);
    (*env)->CallVoidMethod(env,javaObject,onNetError,netMsg,masterKiwiMsg);
}

void sendAtlasCompressed()
{
    sendVoidVoidMethod(onAtlasCompressed);
}

void sendTexturesReady()
{
    sendVoidVoidMethod(onTexturesReady);
}

void sendAssetsListCount(int count)
{
    JNIEnv* env;
    (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
    jint intParam = count;
    (*env)->CallVoidMethod(env,javaObject,onAssetsList,intParam);
}

void sendRemainingAssets(int remaining)
{
    showDebug("Termino textrura!!!!!!!");
    JNIEnv* env;
    (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
    jint intParam = remaining;
    (*env)->CallVoidMethod(env,javaObject,onAssetDownloaded,intParam);
}

void sendFinishDownloading()
{
    sendVoidVoidMethod(allAssetsDownloded);
}

const char* getGameDataJSON()
{
    JNIEnv* env;
    (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
    jobject nPath = (*env)->CallObjectMethod(env,javaObject,getGameJSON);

    const char* result = (*env)->GetStringUTFChars(env,nPath,0);
    return result;
}

void sendStartingGame()
{
    sendVoidVoidMethod(onStartingGame);
}

void sendGameOver()
{
    SDL_Log("MasterKiwiWrapper.c: report game over");
    sendVoidVoidMethod(onGameOver);
}

void sendDataGusanos(int usado_, int posJ1_, int posJ2_, int mapa_, int turno_){
    JNIEnv* env;
    (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
    jint  usado = usado_;
    jint posj1 = posJ1_;
    jint posj2 = posJ2_;
    jint mapa = mapa_;
    jint turno = turno_;
    SDL_Log("MasterKiwiWrapper.c: sendDataGusanos");
    (*env)->CallVoidMethod(env,javaObject,mandarDatos,usado, posj1, posj2, mapa, turno);
}


void sendExitGame()
{
    sendVoidVoidMethod(onExitGame);
}

void sendNoLoggedIn()
{
    sendVoidVoidMethod(notLogged);
}

void sendNoGameData()
{
    sendVoidVoidMethod(noGameData);
}

void sendJsonSquirrel(const char* json)
{
    JNIEnv* env;
    (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
    jstring stringParam = (*env)->NewStringUTF(env,json);
    (*env)->CallStaticVoidMethod(env,javaClassRef,onSendJsonSQ,stringParam);
}

void sendVoidVoidMethod(jmethodID jID)
{
    SDL_Log("MasterKiwiWrapper.c: sendVoidVoidMethod");
    JNIEnv* env;
    (*javaVM)->AttachCurrentThread(javaVM,&env,NULL);
    (*env)->CallVoidMethod(env,javaObject,jID);
}
