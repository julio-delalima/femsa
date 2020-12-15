#include "SDL.h"
#include "navi.h"

struct NetWrapper
{
    NetWrapper();

    void downloadRequestCompleted(const char*);
    bool RequestLogin(const char*,const char*,bool);
    void RequestCompressAtlas(int);
    void RequestCompressToRTTEXT();
    void RequestGetAssetsList();
    void RequestLoadGameData();

    void onRequestResponse(const char*);
};