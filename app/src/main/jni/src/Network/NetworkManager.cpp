#include "NetworkManager.h"
#include "navi.h"
#include "util/NetUtils.h"

void NetworkManager::sendStringRequest(eNetworkActions type)
{
    SDL_Log("Request: %s",urlStream.str().c_str());
    network_action = type;
    externalStringRequest(urlStream.str().c_str());
}

void NetworkManager::sendImageDownloadRequest(eNetworkActions type)
{
    SDL_Log("Request: %s",urlStream.str().c_str());
    network_action = type;
    externalImageDownloadRequest(urlStream.str().c_str());
}

void NetworkManager::sendAudioDownloadRequest(eNetworkActions type)
{
    SDL_Log("Request: %s",urlStream.str().c_str());
    network_action = type;
    externalAudioDownloadRequest(urlStream.str().c_str());
}

void NetworkManager::sendUploadRequest(eNetworkActions type)
{
    SDL_Log("Request: %s",urlStream.str().c_str());
    network_action = type;
    externalUploadRequest(urlStream.str().c_str());
}

void NetworkManager::createRequest(int instruction)
{
    setInstruction(instruction);
    PostDefaultData();
}

void NetworkManager::setInstruction(int instruction)
{
    urlStream.str("");
    urlStream << SERVERIP;
    urlStream << "/index.php/apiv3/?a=";
    urlStream << NumberToString(instruction);
}

void NetworkManager::PostDefaultData()
{
    AddToPostData("p",gNavi->p_.c_str(),false);
    AddToPostData("u",gNavi->u_.c_str(),false);
    AddToPostData("version",gNavi->version.c_str());
}

void NetworkManager::AddToPostData(std::string postID,std::string postData,bool encrypt)
{
    if(encrypt)
    {
        postData = gNavi->naviEncrypt(postData);
    }
    URLEncoder encoder;
    string result = "";
    encoder.encodeData((const byte*)postID.c_str(),postID.length(),result);
    urlStream << "&";
    urlStream << result;
    urlStream << "=";
    result = "";
    encoder.encodeData((const byte*)postData.c_str(),postData.length(),result);
    urlStream << result;
}

void NetworkManager::moveFileToMasterKiwiUse(string fileToMove)
{
    char ch;
    FILE *source, *target;
    std::string nFile(getPreferencedPath());
    if(currentDownload.size() > 0)
    {
        nFile = nFile + "/interface/data/" + currentDownload;
    }
    else
    {
        SDL_Log("Theres no current Download");
    }

    source = fopen(fileToMove.c_str(), "r");
    if( source == NULL )
    {
        exit(EXIT_FAILURE);
    }

    target = fopen(nFile.c_str(), "w");
    if( target == NULL )
    {
        fclose(source);
        exit(EXIT_FAILURE);
    }

    fseek(source, 0L, SEEK_END);
    int pos = ftell(source);
    fseek(source, 0L, SEEK_SET);
    while (pos--)
    {
        ch = fgetc(source);
        fputc(ch, target);
    }

    fclose(source);
    fclose(target);
    remove(fileToMove.c_str());
    onRequestCompleted("");
}

string NetworkManager::getRequestResult()
{
    return requestResult;
}

void NetworkManager::sendLoginRequest()
{
    createRequest(LOGIN_INSTRUCTION);
    sendStringRequest(LOGINING_IN);
}

void NetworkManager::askForGameDataDate(int projectId, long date)
{
    setInstruction(ASK_FOR_DATA_DATE);
    urlStream.str("");
    urlStream << SERVERIP;
    urlStream << "/prueba?pid=";
    urlStream << NumberToString(projectId);
    urlStream << "&fechaUltima=";
    urlStream << NumberToString(date);
    gNavi->current_project->id = projectId;
    sendStringRequest(ASK_FOR_DATA_DATE);
}

void NetworkManager::askForQuestions(int version,int projectID)
{
    setInstruction(ASK_FOR_QUESTIONS);
    urlStream.str("");
    urlStream << NRM_SERVERIP;
    urlStream << "getTrivias.php?appId=";
    URLEncoder encoder;
    string result = "";
    string userName(gNavi->u_.c_str());
    encoder.encodeData((const byte*)userName.c_str(),userName.length(),result);
    urlStream << result;
    urlStream << "&version=";
    urlStream << NumberToString(version);
    urlStream << "&pid=";
    urlStream << NumberToString(projectID);
    sendStringRequest(ASK_FOR_QUESTIONS);
}

void NetworkManager::download_assets(int pid)
{
    setInstruction(COMPRESS_INSTRUCTION);
    PostDefaultData();
    AddToPostData("pid",NumberToString(pid));
    gNavi->current_project->id = pid;
    sendStringRequest(COMPRESSING_ATLAS);
}

void NetworkManager::compress_tex()
{
    setInstruction(TEXTURES_INSTRUCTION);
    PostDefaultData();
    AddToPostData("pid",NumberToString(gNavi->current_project->id));
    sendStringRequest(COMPRESSING_TO_RTTEXT);
}

void NetworkManager::load_file_list()
{
    setInstruction(ASSETS_LIST_INSTRUCTION);
    PostDefaultData();
    AddToPostData("pid",NumberToString(gNavi->current_project->id));
    sendStringRequest(GENERATING_DOWNLOAD_LINKS);
}

void NetworkManager::download_file(int asset_id, std::string name)
{
    std::vector<std::string> res = StringTokenize(name, "|");
    name = res[1];
    RemoveFile(name);
    setInstruction(DOWNLOAD_ASSET_INSTRUCTION);
    PostDefaultData();
    AddToPostData("asset_id",NumberToString(asset_id));
    AddToPostData("fname",name);
    AddToPostData("n",NumberToString(gNavi->current_project->id));
    std::string ext = FilterToValidAscii((GetFileExtension(name)),false);
    if (ext == "mkinfo")
    {
        sendStringRequest(DOWNLOADING_FILES);
    }
    else if (ext == "wav" || ext == "mp3")
    {
        currentDownload = NumberToString(gNavi->current_project->id) + "_" + NumberToString(gNavi->currentDownload->fileID) + "_" + res[1];
        sendAudioDownloadRequest(DOWNLOADING_FILES);
    }
    else
    {
        currentDownload = NumberToString(gNavi->current_project->id) + "_" + NumberToString(gNavi->currentDownload->fileID) + "_" + res[1];
        sendImageDownloadRequest(DOWNLOADING_FILES);
    }
}

void NetworkManager::load_menus_and_assets()
{
    setInstruction(DOWNLOAD_UDATA_INSTRUCTION);
    PostDefaultData();
    AddToPostData("pid",NumberToString(gNavi->current_project->id));
    sendStringRequest(DOWNLOADING_GAME_DATA);
}

void NetworkManager::upload_data()
{
    createRequest(UPLOAD_DATA_INSTRUCTION);

    stringstream ss3;
    ss3 << "interface/data/" << gNavi->current_project->id << "_sv.txt";
    TextScanner b2(ss3.str());
    int i = 0;
    for (i = 0; i < 50; i++)
    {
        if(i < b2.lineList.size())
        {
            if(b2.lineList[i] != "")
            {
                string data = b2.lineList[i];
                stringstream ss4;
                ss4 << "data" << i;
                AddToPostData(ss4.str(),data,false);
            }

        }
    }
    string id = NumberToString(gNavi->current_project->id);
    AddToPostData("pid",id);
    sendUploadRequest(UPLOADING_DATA);
}

void NetworkManager::upload_data_insecure()
{
    std::stringstream ss3;
    ss3 << "interface/data/" << gNavi->current_project << "_sv.txt";
    TextScanner b2(ss3.str());
    if (b2.lineList.size() == 0 || b2.lineList[0] == "")
    {
        return;
    }
    createRequest(UPLOAD_DATA_INSECURE_INSTRUCTION);
    std::string v = gNavi->naviEncrypt(NumberToString(gNavi->current_project->id+322));
    AddToPostData("version",v);
    int i = 0;
    for (i = 0; i < 50; i++) //Cuando temrina el request borro 20 de la lista y guardo el archivo
    {
        if (i >= b2.lineList.size())
        {
            break;
        }
        if (b2.lineList[i] == "")
        {
            continue;
        }

        std::string data = b2.lineList[i];
        std::stringstream ss4;
        ss4 << "data" << i;
        AddToPostData(ss4.str(),data);
    }
    std::string id = gNavi->naviEncrypt(NumberToString(gNavi->current_project->id));
    AddToPostData("pid",id);
    sendUploadRequest(UPLOADING_DATA);
}

void NetworkManager::get_top10_insecure()
{
    createRequest(GET_TOP_10_INSECURE_INSTRUCTION);
    std::string v = gNavi->naviEncrypt(NumberToString(gNavi->current_project->id+797));
    gNavi->version = v;
    std::string id = gNavi->naviEncrypt(NumberToString(gNavi->current_project->id));
    AddToPostData("pid",id);
    sendUploadRequest(INSECURE_UPLOADING); //es 101 pero 100 para que maneje de la misma forma
}

void NetworkManager::onRequestCompleted(const char* response)
{
    requestResult = string(response);
    gNavi->NetInstructionComplete();
}