//
// Created by MasterKiwi-Sferea on 13/09/2017.
//

#include "NetWrapperBridge.h"
#include "NetWrapper.hpp"


extern "C"
{
    netHandler createNetWrapper()
    {
        return new NetWrapper();
    }

    void freeNetWrapper(netHandler wrapper)
    {
        delete wrapper;
    }

    void wrapperRequestDownloadCompleted(netHandler wrapper,const char* download)
    {
        wrapper->downloadRequestCompleted(download);
    }

    bool wrapperRequestLogin(netHandler wrapper,const char* usr,const char* pass,bool checkLocal)
    {
        return wrapper->RequestLogin(usr,pass,checkLocal);
    }

    void wrapperRequestCompressAtlas(netHandler wrapper,int project)
    {
        wrapper->RequestCompressAtlas(project);
    }

    void wrapperRequestCompressToRTTEXT(netHandler wrapper)
    {
        wrapper->RequestCompressToRTTEXT();
    }

    void wrapperRequestGetAssetsList(netHandler wrapper)
    {
        wrapper->RequestGetAssetsList();
    }

    void wrapperRequestLoadGameData(netHandler wrapper)
    {
        wrapper->RequestLoadGameData();
    }

    void wrapperOnRequestCompleted(netHandler wrapper,const char* result)
    {
        wrapper->onRequestResponse(result);
    }
}