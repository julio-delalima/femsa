//
// Created by MasterKiwi-Sferea on 13/09/2017.
//
#include "NetworkWrapper.h"

const char * JAVA_MASTERKIWI_NET_CLASS = "com/femsa/sferea/masterkiwi/MasterKiwiNetworkManager";
const char * JAVA_REQUEST_STRING_URL = "requestString";
const char * JAVA_REQUEST_IMAGE_DOWNLOAD_URL = "requestImageDownload";
const char * JAVA_REQUEST_AUDIO_DOWNLOAD_URL = "requestAudioDownload";
const char * JAVA_REQUEST_UPLOAD_STATS_URL = "uploadStatsData";

bool javaNetMethodsInitialized = false;
bool javaNetVMInitialized = false;

JavaVM*  javaNetVM;
jclass  javaNetClassRef;
jobject javaNetObject;

jmethodID requestString;
jmethodID requestProcess;
jmethodID requestImageDownload;
jmethodID requestAudioDownload;
jmethodID requestUpload;

netHandler wrap;

JNIEXPORT void JNICALL Java_com_femsa_sferea_masterkiwi_MasterKiwiNetworkManager_networkResponse(JNIEnv *env,jobject instance,jstring result_)
{
    const char *result = (*env)->GetStringUTFChars(env, result_, 0);

    wrapperOnRequestComplete(result);

    (*env)->ReleaseStringUTFChars(env, result_, result);
}

JNIEXPORT void JNICALL Java_com_femsa_sferea_masterkiwi_MasterKiwiNetworkManager_networkDownloadComplete(JNIEnv *env,jobject instance,jstring fileDownloaded_)
{
    initializeNet();
    initializeNetVM(env,instance);

    const char *fileDownloaded = (*env)->GetStringUTFChars(env, fileDownloaded_, 0);

    wrapperOnDownloadComplete(fileDownloaded);

    (*env)->ReleaseStringUTFChars(env, fileDownloaded_, fileDownloaded);
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiNetworkManager_masterKiwiLoginWithData(JNIEnv *env, jobject instance, jstring user_, jstring password_, jboolean checkLocal)
{
    initializeNet();
    initializeNetVM(env,instance);

    const char *user = (*env)->GetStringUTFChars(env, user_, 0);
    const char *password = (*env)->GetStringUTFChars(env, password_, 0);

    char str[128];
    strcpy (str,"Usuario: ");
    strcat (str,user);
    strcat (str," , Password: ");
    strcat (str,password);
    puts (str);
    showDebug(str);
    wrapperLogin(user,password,checkLocal);

    (*env)->ReleaseStringUTFChars(env, user_, user);
    (*env)->ReleaseStringUTFChars(env, password_, password);
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiNetworkManager_masterKiwiCompressAtlas(JNIEnv *env,jobject instance,jint gameID)
{
    showDebug("Comprimiendo Atlas!!!");
    wrapperCompressAtlas((int)gameID);
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiNetworkManager_masterKiwiCompressToRTTEXT(JNIEnv *env, jobject instance)
{
    showDebug("Comprimiendo con texture packer");
    wrapperCompressToRTTEXT();
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiNetworkManager_masterKiwiGetAssetsList(JNIEnv *env, jobject instance)
{
    showDebug("Generando lista de assets");
    wrapperGetAssetsList();
}

JNIEXPORT void JNICALL
Java_com_femsa_sferea_masterkiwi_MasterKiwiNetworkManager_masterKiwiDownloadGameData(JNIEnv *env, jobject instance)
{
    showDebug("Por desgargar gamedata");
    wrapperLoadGameData();
}

void initializeNetVM(JNIEnv* env,jobject obj)
{
    if(!javaNetVMInitialized)
    {
        (*env)->GetJavaVM(env,&javaNetVM);
        javaNetObject = (*env)->NewGlobalRef(env,obj);
        javaNetVMInitialized = true;
    }
    initializeNetMethods();
}

void initializeNet()
{
    if(wrap == 0)
    {
        showDebug("Creating wrap");
        wrap = createNetWrapper();
    }
}

void initializeNetMethods()
{
    if(!javaNetMethodsInitialized)
    {
        JNIEnv* env;
        (*javaNetVM)->AttachCurrentThread(javaNetVM,&env,NULL);
        jclass dataClass = (*env)->FindClass(env,JAVA_MASTERKIWI_NET_CLASS);
        javaNetClassRef = (jclass)(*env)->NewGlobalRef(env,dataClass);
        requestString = (*env)->GetMethodID(env,javaNetClassRef,JAVA_REQUEST_STRING_URL,"(Ljava/lang/String;)V");
        requestImageDownload = (*env)->GetMethodID(env,javaNetClassRef,JAVA_REQUEST_IMAGE_DOWNLOAD_URL,"(Ljava/lang/String;)V");
        requestUpload = (*env)->GetMethodID(env,javaNetClassRef,JAVA_REQUEST_UPLOAD_STATS_URL,"(Ljava/lang/String;)V");
        javaNetMethodsInitialized = true;
    }
}

void wrapperLogin(const char* wUser,const char* wPassword,bool checkLocal)
{
    wrapperRequestLogin(wrap,wUser,wPassword,checkLocal);
}

void wrapperCompressAtlas(int projectID)
{
    wrapperRequestCompressAtlas(wrap,projectID);
}

void wrapperCompressToRTTEXT()
{
    wrapperRequestCompressToRTTEXT(wrap);
}

void wrapperGetAssetsList()
{
    wrapperRequestGetAssetsList(wrap);
}

void wrapperLoadGameData()
{
    wrapperRequestLoadGameData(wrap);
}

void wrapperOnRequestComplete(const char* response)
{
    wrapperOnRequestCompleted(wrap,response);
}

void wrapperOnDownloadComplete(const char* result)
{
    wrapperRequestDownloadCompleted(wrap,result);
}

void externalStringRequest(const char* requestURL)
{
    JNIEnv* env;
    (*javaNetVM)->AttachCurrentThread(javaNetVM,&env,NULL);
    jstring netMsg = (*env)->NewStringUTF(env,requestURL);
    (*env)->CallVoidMethod(env,javaNetObject,requestString,netMsg);
}

void externalImageDownloadRequest(const char *requestURL)
{
    JNIEnv* env;
    (*javaNetVM)->AttachCurrentThread(javaNetVM,&env,NULL);
    jstring netMsg = (*env)->NewStringUTF(env,requestURL);
    (*env)->CallVoidMethod(env,javaNetObject,requestImageDownload,netMsg);
}

void externalAudioDownloadRequest(const char *requestURL)
{
    JNIEnv* env;
    (*javaNetVM)->AttachCurrentThread(javaNetVM,&env,NULL);
    jstring netMsg = (*env)->NewStringUTF(env,requestURL);
    (*env)->CallVoidMethod(env,javaNetObject,requestAudioDownload,netMsg);
}

void externalUploadRequest(const char* requestURL)
{
    JNIEnv* env;
    (*javaNetVM)->AttachCurrentThread(javaNetVM,&env,NULL);
    jstring netMsg = (*env)->NewStringUTF(env,requestURL);
    (*env)->CallVoidMethod(env,javaNetObject,requestUpload,netMsg);
}