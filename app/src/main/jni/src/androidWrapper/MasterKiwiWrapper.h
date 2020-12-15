//
// Created by MasterKiwi-Sferea on 13/09/2017.
//
#include "SDL.h"
#include "WrapperBridge.h"
#include <jni.h>
#include <stdbool.h>

#ifndef NEWENGINE_MASTERKIWIWRAPPER_H
#define NEWENGINE_MASTERKIWIWRAPPER_H

    #ifdef __cplusplus
        extern "C"
        {
    #endif
            void initializeVM(JNIEnv*,jobject);
            void initialize();
            void initializeMethods();
            void showDebug(const char*);
            void wrapperSetGame(int);
            bool wrapperHasGameData(int);
            bool wrapperHasGameDataPath(int, const char*);
            void wrapperSendDataDateRequest(int);
            void wrapperMoveFile(const char*,const char*);
            void wrapperChangeAudioState();
            bool wrapperIsAudioActive();
            void wrapperSetJsonSquirrel(const char*);
            void wrapperSetDataGusanosYEscaleras(int ,int ,int ,int ,int);

            void callStartSDL(int);
            void sendUserDataToJava(const char*);
            void sendLogOut();
            const char* getPreferencedPath();
            void sendNetError(const char*,const char*);
            void sendAtlasCompressed();
            void sendTexturesReady();
            void sendAssetsListCount(int);
            void sendRemainingAssets(int);
            void sendFinishDownloading();
            const char* getGameDataJSON();
            void sendStartingGame();
            void sendGameOver();
            void sendExitGame();
            void sendNoLoggedIn();
            void sendNoGameData();
            void sendVoidVoidMethod(jmethodID);
            void sendDataGusanos(int, int, int, int, int);

    #ifdef __cplusplus
        }
    #endif
#endif //NEWENGINE_MASTERKIWIWRAPPER_H
