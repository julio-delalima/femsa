#ifndef INC_09MASTERKIWI_NETWORKMANAGER_H
#define INC_09MASTERKIWI_NETWORKMANAGER_H

#include <string>
#include <sstream>
#include <iostream>
#include "Network/androidWrapper/NetworkWrapper.h"

using namespace std;

class NetworkManager
{
public:
    enum eNetworkActions
    {
        LOGINING_IN,
        ASK_FOR_DATA_DATE,
        ASK_FOR_QUESTIONS,
        COMPRESSING_ATLAS,
        COMPRESSING_TO_RTTEXT,
        GENERATING_DOWNLOAD_LINKS,
        DOWNLOADING_FILES,
        DOWNLOADING_GAME_DATA,
        UPLOADING_DATA,
        INSECURE_UPLOADING,
        NONE
    };

protected:
    const int LOGIN_INSTRUCTION = 1;
    const int COMPRESS_INSTRUCTION = 2;
    const int TEXTURES_INSTRUCTION = 3;
    const int ASSETS_LIST_INSTRUCTION = 4;
    const int DOWNLOAD_ASSET_INSTRUCTION = 5;
    const int DOWNLOAD_UDATA_INSTRUCTION = 6;
    const int UPLOAD_DATA_INSTRUCTION = 100;
    const int UPLOAD_DATA_INSECURE_INSTRUCTION = 101;
    const int GET_TOP_10_INSECURE_INSTRUCTION = 102;

    stringstream urlStream;
    string requestResult;
    string currentDownload;

    void sendStringRequest(eNetworkActions type);
    void sendImageDownloadRequest(eNetworkActions type);
    void sendAudioDownloadRequest(eNetworkActions type);
    void sendUploadRequest(eNetworkActions type);
    void createRequest(int instruction);
    void setInstruction(int instruction);
    void PostDefaultData();
    void AddToPostData(std::string postID,std::string postData,bool encrypt = true);
public:
    eNetworkActions network_action;

    void moveFileToMasterKiwiUse(string fileToMove);
    string getRequestResult();
    void sendLoginRequest();
    void askForGameDataDate(int projectId,long date);
    //Empezamos haciendo el request 2, que es comprimir atlas - ok
    //Cuando termine hacemos el request 3, que es comprimir a rttext - ok
    //Cuando termine hacemos el request 4, que es listar todos los archivos y los paths para descargar
    //Cuando termine hacemos el request 5 multiples veces, que es descargar cada archivo que nos dio el pasado
    //Terminamos con todos los archivos guardados
    void download_assets(int pid);
    void compress_tex();
    void load_file_list();
    void download_file(int asset_id,string name);
    void load_menus_and_assets();
    void upload_data();
    void upload_data_insecure();
    void get_top10_insecure();

    void onRequestCompleted(const char* response);

    void askForQuestions(int version,int projectID);
};


#endif
