//
// Created by MasterKiwi-Sferea on 13/09/2017.
//

#ifndef NEWENGINE_WRAPPERBRIDGE_H
#define NEWENGINE_WRAPPERBRIDGE_H

#include <stdbool.h>

struct Wrapper;

#ifdef __cplusplus
    extern "C"
    {
#endif
        typedef struct Wrapper * wHandler;

        wHandler createWrapper();
        void freeWrapper(wHandler);

        void wrapperNaviSetGame(wHandler,int);
        bool wrapperNaviHasGameData(wHandler,int);
        bool wrapperNaviHasGameDataPath(wHandler,int, const char*);
        void wrapperNaviAskDataDate(wHandler, int);
        void wrapperNaviMoveFile(wHandler,const char*,const char*);
        void wrapperNaviChangeAudio(wHandler);
        bool wrapperNaviIsAudioActive(wHandler);
        void wrapperNaviSetJsonSquirrel(wHandler, const char*);
        void wrapperNaviSetDataGusanosYEscaleras(wHandler, int, int, int, int, int);
#ifdef __cplusplus
    }
#endif

#endif //NEWENGINE_WRAPPERBRIDGE_H
