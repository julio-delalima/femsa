//
// Created by MasterKiwi-Sferea on 13/09/2017.
//
#include "NetWrapper.hpp"

NetWrapper::NetWrapper()
{
}

void NetWrapper::downloadRequestCompleted(const char* result)
{
    gNavi->netManager.moveFileToMasterKiwiUse(result);
}

bool NetWrapper::RequestLogin(const char * nUser, const char * nPassword,bool checkLocal)
{
    return gNavi->tryLogin(nUser,nPassword,checkLocal);
}

void NetWrapper::RequestCompressAtlas(int projectID)
{
    gNavi->LoadUserData();
    gNavi->netManager.download_assets(projectID);
}

void NetWrapper::RequestCompressToRTTEXT()
{
    gNavi->netManager.compress_tex();
}

void NetWrapper::RequestGetAssetsList()
{
    gNavi->netManager.load_file_list();
}

void NetWrapper::RequestLoadGameData()
{
    gNavi->netManager.load_menus_and_assets();
}

void NetWrapper::onRequestResponse(const char* response)
{
    gNavi->netManager.onRequestCompleted(response);
}