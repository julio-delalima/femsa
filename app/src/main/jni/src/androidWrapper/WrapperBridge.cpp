//
// Created by MasterKiwi-Sferea on 13/09/2017.
//

#include "WrapperBridge.h"
#include "Wrapper.hpp"


extern "C"
{
    wHandler createWrapper()
    {
        return new Wrapper();
    }

    void freeWrapper(wHandler wrapper)
    {
        delete wrapper;
    }

    void wrapperNaviSetGame(wHandler wrapper,int projectID)
    {
        wrapper->NaviSetGame(projectID);
    }

    bool wrapperNaviHasGameData(wHandler wrapper,int projectID)
    {
        return wrapper->NaviHasGameData(projectID);
    }

    bool wrapperNaviHasGameDataPath(wHandler wrapper,int projectID, const char* path)
    {
        return wrapper->NaviHasGameDataPath(projectID, path);
    }

    void wrapperNaviAskDataDate(wHandler wrapper, int projectID)
    {
        wrapper->NaviAskDataDate(projectID);
    }

    void wrapperNaviMoveFile(wHandler wrapper,const char* from,const char* to)
    {
        wrapper->NaviMoveFile(from,to);
    }

    void wrapperNaviChangeAudio(wHandler wrapper)
    {
        wrapper->NaviChangeAudio();
    }

    bool wrapperNaviIsAudioActive(wHandler wrapper)
    {
        return wrapper->NaviIsAudioActive();
    }

    void wrapperNaviSetJsonSquirrel(wHandler wrapper, const char* json)
    {
        showDebug("Le pongo valores a navi ");
        showDebug(json);
        wrapper->NaviSetJsonSquirrel(json);
    }

    void wrapperNaviSetDataGusanosYEscaleras(wHandler wrapper, int usado_,int posJ1_,int posJ2,int mapa_,int turno_)
    {
        wrapper->NaviSetDataGusanosYEscaleras(usado_, posJ1_, posJ2, mapa_, turno_);
    }
}