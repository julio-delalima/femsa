//
// Created by MasterKiwi-Sferea on 13/09/2017.
//
#include "SDL.h"
#include "NetWrapperBridge.h"
#include "androidWrapper/MasterKiwiWrapper.h"
#include <jni.h>

#ifndef NEWENGINE_NETWORKWRAPPER_H
#define NEWENGINE_NETWORKWRAPPER_H

    #ifdef __cplusplus
        extern "C"
        {
    #endif
            void initializeNetVM(JNIEnv*,jobject);
            void initializeNet();
            void initializeNetMethods();

            void wrapperLogin(const char*,const char*,bool);
            void wrapperCompressAtlas(int);
            void wrapperCompressToRTTEXT();
            void wrapperGetAssetsList();
            void wrapperLoadGameData();
            void wrapperOnRequestComplete(const char*);
            void wrapperOnDownloadComplete(const char*);

            void externalStringRequest(const char* requestURL);
            void externalImageDownloadRequest(const char *requestURL);
            void externalAudioDownloadRequest(const char *requestURL);
            void externalUploadRequest(const char* requestURL);
    #ifdef __cplusplus
        }
    #endif
#endif
