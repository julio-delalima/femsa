//
// Created by MasterKiwi-Sferea on 13/09/2017.
//
#include "Wrapper.hpp"

Wrapper::Wrapper()
{
    if (gNavi == 0)
    {
        gNavi = new NaVi();
    }
}

void Wrapper::NaviSetGame(int gameID)
{
    gNavi->current_project->id = gameID;
    //gNavi->current_project = gNavi->getProjectWithType(gameID);
}

bool Wrapper::NaviHasGameData(int gameID)
{
    return gNavi->hasGameData(gameID);
}

bool Wrapper::NaviHasGameDataPath(int gameID, const char* path)
{
    return gNavi->hasGameDataPath(gameID, path);
}

void Wrapper::NaviAskDataDate(int gameID)
{
    gNavi->sendRequestForDataDate(gameID);
}

void Wrapper::NaviMoveFile(const char* source, const char* target)
{
    gNavi->renameFileForMasterKiwiUse(source,target);
}

void Wrapper::NaviChangeAudio()
{
    gNavi->changeAudioState();
}

bool Wrapper::NaviIsAudioActive()
{
    return gNavi->isAudioActive();
}

void Wrapper::NaviSetJsonSquirrel(const char* json)
{
    showDebug("Navi ya vale ");
    showDebug(json);
        gNavi->set_jsonToSquirrelData(json);
}

void Wrapper::NaviSetDataGusanosYEscaleras(int usado_,int posJ1_,int posJ2,int mapa_,int turno_)
{
    gNavi->set_dataGusanosYEscaleras(usado_, posJ1_, posJ2, mapa_, turno_);
}