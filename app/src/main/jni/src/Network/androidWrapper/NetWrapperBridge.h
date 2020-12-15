//
// Created by MasterKiwi-Sferea on 13/09/2017.
//

#ifndef NEWENGINE_NETWRAPPERBRIDGE_H
#define NEWENGINE_NETWRAPPERBRIDGE_H

#include <stdbool.h>

struct NetWrapper;

#ifdef __cplusplus
    extern "C"
    {
#endif
        typedef struct NetWrapper * netHandler;

        netHandler createNetWrapper();
        void freeNetWrapper(netHandler);

        void wrapperRequestDownloadCompleted(netHandler ,const char*);
        bool wrapperRequestLogin(netHandler,const char*,const char*,bool);
        void wrapperRequestCompressAtlas(netHandler,int);
        void wrapperRequestCompressToRTTEXT(netHandler);
        void wrapperRequestGetAssetsList(netHandler);
        void wrapperRequestLoadGameData(netHandler);
        void wrapperOnRequestCompleted(netHandler ,const char*);
#ifdef __cplusplus
    }
#endif

#endif
