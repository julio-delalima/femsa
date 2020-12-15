#ifndef NAVI_H
#define NAVI_H

#include <list>

#ifdef __WINPHONE__
#include <SDL.h>
#else
#ifdef __ANDROID__

#include "SDL.h"
#include "SDL_mixer.h"
#include "androidWrapper/MasterKiwiWrapper.h"

#elif __APPLE__
#include "SDL.h"
#include "SDL_mixer.h"
#else
#include "SDL2/SDL.h"
#ifndef EMSCRIPTEN
#include "SDL_mixer.h"
#endif
#endif
#endif

#include "SDL_ttf.h"
#include "miscutils.h"
#include "mk_gui.h"
#include "mk_button.h"
#include "mk_label.h"
#include "mkimage.h"
#include <map>
#include <vector>
#include <sstream>
#include <util/cJSON.h>
#include <NRMSpecifics/NRMSpecifics.h>
#include "util/NetHTTP.h"
#include "Network/NetworkManager.h"
#include "Menus/CustomMenu.h"
#include "textscanner.h"
#include "NRMSpecifics/NRMSpecifics.h"
#include "NRMSpecifics/Trivia/NRMTrivia.h"

#define USE_ENCRYPTION 1
#define IS_DOWNLOAD_APP 1
//#define IS_WEB_PREVIEW 1
//#define SCREEN_KEYBOARD 1
//#define DEBUG_PHYSICS 1
//#define SCREEN_PRINT 1
//#define HORIZONTAL_MODE 1
//#define FULL_SCREEN_WINDOWS 1

//#define SERVERIP "52.2.182.152"
//#define SERVERIP "52.1.42.222"
//#define SERVERIP "http://www.master.kiwi.test.com/"
#define SERVERIP "https://www.masterkiwi.com"
//#define NRM_SERVERIP "http://apinrm.sferea.com/Servicios/";
#define NRM_SERVERIP "http://nrm.com.mx/apps/api/Servicios/";


#include "debugWindow.h"

//This class is the base, it loads other classes
enum states
    {
    STARTING,
    LOADING,
    GAME,
    APP,
    PRELOAD_GAME,
    PRELOAD_GAME2,
    ANY_ERROR
    };
enum app_buttons
    {
    EMPTY,
    LOGIN_BUTTON,
    GAMES_SCREEN
    };

#ifdef SCREEN_PRINT
class message
{
public:
    message (std::string _msg = "", Uint32 _die = 0)
    {
        msg = _msg;
        die_time = _die;
    }
    std::string msg;
    Uint32 die_time;
};
#endif

//estas son las imagenes que cargamos con sus tamaños (como son atlas, estas son las imagenes dentro del atlas)
class Frames
    {
public:
    Frames()
        {
        x = 0;
        y = 0;

        width = 0;
        height = 0;
        }

    Frames(float _x, float _y, float _width, float _height, std::string _txname)
        {
        x = _x;
        y = _y;
        width = _width;
        height = _height;
        txname = _txname;
        }

    float x;
    float y;
    float width;
    float height;
    std::string txname;
    };

//Information about the assets, we will use this to actually generate the assets :)
class Asset_Data
    {
public:
    Asset_Data()
        {
        type_ = -1;
        fps_ = 0;
        type_2_ = 0;
        id_ = -1000;
        }

    int type_; //is it animation? static?
    int type_2_; //is it a game asset? character?
    int id_; //the id
    int fps_; //if its an animation the fps
    std::vector<std::string> files_; //the list of files the asset has, 1 if static, N if animation
    };

/*
 * This is the actual in game asset
 */
class Asset
    {
public:
    Asset()
        {
        img_ = 0;
        d_ = 0;
        scale_x = 1.0f;
        scale_y = 1.0f;
        last_frame_tick_ = SDL_GetTicks();
        }

    ~Asset()
        {
        SAFE_DELETE(img_);
        }

    float get_height(float _default = 0)
        {
        return (float) img_->GetHeight();
        }

    float get_width(float _default = 0)
        {
        return (float) img_->GetWidth();
        }

    void move(float x, float y)
        {
        img_->SetX(x);
        img_->SetY(y);
        }

    float get_y()
        {
        return img_->y;
        }

    float get_x()
        {
        return img_->x;
        }

    float scale_x;
    float scale_y;

    void draw();

    void load_asset(int id);

    void load_asset_by_type_2(int type2);

    int id_;
    Mk_Image *img_;
    Asset_Data *d_;
    int total_frames_;
    int current_frame_;
    float frame_rate;
    long last_frame_tick_;
    };

class Mk_Font
    {
    std::string FontLocation;
    std::map<int, TTF_Font *> FontSizes;

public:
    //Save the path to the TTF file and do some error checking string cant be empty
    bool Load(const std::string FileLocation)
        {
        FontLocation = FileLocation;

        if(FileLocation == "")
            {
            return false;
            }
        return true;
        }

    // loads and returns a TTF_Font.
    // looks in the map if the font already exists if not create a new font with the requested size
    TTF_Font *GetFont(int FontSize)
        {
        std::map<int, TTF_Font *>::iterator it;
        it = FontSizes.find(FontSize);

        if(it != FontSizes.end())
            {
            return it->second;
            }
        else
            {
            TTF_Font *mFont = TTF_OpenFont(FontLocation.c_str(), FontSize);
            FontSizes.insert(std::pair<int, TTF_Font *>(FontSize, mFont));
            return mFont;
            }
        }

    ~Mk_Font()
        {
        //i think i should delete the loaded fonts
        for(std::map<int, TTF_Font *>::iterator it = FontSizes.begin(); it != FontSizes.end(); ++it)
            {
            TTF_CloseFont(it->second);
            }
        FontSizes.clear();
        }
    };

class point
    {
public:
    point(float _x = 0, float _y = 0)
        {
        x = _x;
        y = _y;
        color.r = 255;
        color.g = 255;
        color.b = 255;
        color.a = 255;
        }

    float x;
    float y;
    SDL_Color color;
    };

class NaVi
    {
    bool checkIfLoginExpired();

    bool hasFile(const char *);

    void Login(const char *name, const char *pass);

    void setUserData(const char *n, const char *p);

public:
    NaVi();

    string naviEncrypt(string data);

    string naviDecrypt(string data);

    string decrypt_file(string file);

    bool hasUserData();

    bool hasGameData(int gameID);

    bool hasGameDataPath(int gameID, const char* path);

    void sendRequestForDataDate(int gameID);

    bool initGame();

    bool initGameWithPath(const char*);

    void Init();

    void registerDirectories();

    void logout(bool _exit = true);

    void registerProjectsFromData(cJSON *jsonData);

    int createAndGetBoolOnJson(cJSON *jsonData, string name, int initialVal);

    string createAndGetStringOnJSON(cJSON *jsonData, string name, string initialVal);

    bool dataHasErrors(cJSON *jsonData);

    void registerFilesToDownload(cJSON *files);

    void clearAssetsFile(bool stringTokenize);

    void erraseGameData(int gameID);

    void refresh_list();

    void loadGameFiles();

    void loadGameFilesWithPath(const char*);

    void loadGameAssets(cJSON *gameData);

    void loadGameMenus(cJSON *gameData);

    void setStats(cJSON *gameData);

    void setGame(int gameID,cJSON* gameData);

    void setGame(int gameID,cJSON* gameData, std::string);

    const char *getGameScript(int gameID);

    void setSetting(cJSON *gameData);

    void setMusic(cJSON *gameData);

    void setEffects(cJSON *gameData);

    void reportGameOver();

    bool LoadProjectFiles();

    bool LoadProjectFileswithPath(const char*);

    void AddMenu(int menu_id);

    void LoadProject();

    int current_state;
    int current_menu_id;

    bool loading_network;

    bool tryLogin(const char *name, const char *pass,bool checkLocal);

    void setRegisterText();

    int MeasureTextWidth(std::string t, float font_size);

    vector<MK_Gui *> gui_buttons;
    project *current_project;
    bool has_game_over_score;
    int game_over_score_x;
    int game_over_score_y;
    int default_menu;
    int game_over_menu;
    map<string, SDL_Texture *> loaded_textures;
    map<string, Frames> frames;
    map<long, Asset_Data> assets_data;
    map<int, Menu> menus_;

#if !defined(EMSCRIPTEN) && !defined(__WINPHONE__)
    map<int, Mix_Music *> music_;
    Mix_Music *current_music_;
#else
    std::map<int, std::string> music_;
#endif

    void registerAudioMix();

    int gameplay_music;

    std::vector<point> game_over_scores_;
    bool already_clicked;
    int game_over_score_;
    std::vector<Asset*> loaded_assets;
    int keyboard_type;


    int index_project;

    struct fileData
        {
        fileData(int id, string name)
            {
            fileName = name.c_str();
            fileID = id;
            }

        fileData(const fileData &originalInfo)
            {
            fileName = originalInfo.fileName.c_str();
            fileID = originalInfo.fileID;
            }

        string fileName;
        int fileID;
        };

    vector<fileData> project_files_to_download_;
    int file_count_;
    int current_file_down_;
    bool has_been_updated;

    std::vector<std::pair<MK_Label *, MK_Label *> > top10_labels;

    std::map<int, Uint32> call_function_after_time;
    std::vector<Uint32> goto_menu_after_time;

    std::string json_to_squirrel;

    /**
     * Estas variables son para gusados y escaleras :3
     * */
     int posJ1, posJ2, mapaEnTurno, turno, usado;

#ifdef DEBUG_WINDOW
    debugWindow dw;
#endif

    std::vector<project> projects_;
    bool started_text_input;

    int game_has_stats;
    int game_stats_begin;
    int game_stats_mode;
    int menu_after_upload;

    void ShowProjectScreen();

    void load_register_data();

    unsigned long register_error;

    bool load_top10_next;

    std::string p_;
    std::string u_;

    Mk_Font global_font;
    int page;

    void SaveUserData();

    void mandarDatos();

    void reiniciaDatosGE();

    void SaveGamesData(std::string str);

    void SaveCurrentProjects();

    void setValueToNumberJSON(cJSON *jsonData, string name, int newValue);

    void setValueToStringJSON(cJSON *jsonData, string name, string newValue);

    project *getProjectWithType(int gameID);

    project *getProjectWithID(int gameID);

    void changeAudioState();

    bool isAudioActive();

    int menu_stats;
    int menu_stats_fields;
    bool next_is_play; //if put a field is at the beggining

    std::string number_to_commastring(int number);

    void LoadUserData();

    const char *LoadGameData();

    cJSON *GetGameData();

    void RemoveUploadData();

    void processTop10(std::string str);
    void processForm();
    void set_jsonToSquirrelData(std::string str);
    void set_dataGusanosYEscaleras(int usado, int posJ1, int posJ2, int mapa, int turno);

#ifdef SCREEN_PRINT
    std::list<message> print_list;
#endif

    int fps;

    SDL_Color white;
    SDL_Color black;

    std::string version = "1.4";

    NetworkManager netManager;

    void NetInstructionComplete();

    void evaluateDateForDownload(cJSON *data);

    void configureToDownload(cJSON *data);

    bool continueToDownload();

    void saveDownload();

    void askForGameSpecificData();

    void workWithSpecificResponse();

    vector<fileData>::iterator currentDownload;

    CustomMenu customMenuGenerator;
    Menu currentMenu;

    string dateToRegister;

    void updateDownloadData(int projectID, string newDate);

    long getGameDataDate(int projectID);

    void renameFileForMasterKiwiUse(string fileToMove, string newFile);

    void saveTime();

    bool loadingFiles;
    bool canInitGames;

    void registerNRMSpecifications();

    int getCurrentSpecificationIndex();

    vector<NRMSpecifics *> nrmSpecifics;
    };

extern NaVi *gNavi;

#endif
