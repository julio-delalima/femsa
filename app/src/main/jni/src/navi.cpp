#include <ctime>
#include <iostream>
#include "script_enviroment.h"
#include "navi.h"
#include "miscutils.h"
#include "mk_textinput.h"
#include "util/cJSON.h"
#include "game.h"

#include "aes/Blowfish.h"

#include "SDL2_gfx/SDL2_gfxPrimitives.h"

#ifdef EMSCRIPTEN
extern void playSoundEM(std::string str);
    extern void stopSoundEM();
    extern void playEffectEM(std::string str);
    extern void stopEffectEM();
    extern void enviar_datos_upload(std::map<string,string> &mapa);
    extern void descargar_top10(std::map<string,string> &mapa);

#endif // EMSCRIPTEN

#ifdef __ANDROID__

#include <sys/stat.h>
#include <android/log.h>

#endif // __ANDROID__

#ifdef __APPLE__
#include <sys/stat.h>
#endif

#ifdef __WINPHONE__
extern "C" const char *GetRootPath();
extern "C" const char *GetInstallPath();
extern "C" void RateApp();
#define fopen(a,b) _fsopen((a), (b), 0x40)
#endif

const std::string currentDateTime()
    {
    time_t now = time(0);
    struct tm tstruct;
    char buf[80];
    tstruct = *localtime(&now);
    // Visit http://en.cppreference.com/w/cpp/chrono/c/strftime
    // for more information about date/time format
    strftime(buf, sizeof(buf), "%Y-%b-%d %X", &tstruct);

    return buf;
    }

static const std::string base64_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        "abcdefghijklmnopqrstuvwxyz"
        "0123456789+/";

void click_list_item(int action, int x, int y);

static inline bool is_base64(unsigned char c)
    {
    return (isalnum(c) || (c == '+') || (c == '/'));
    }

std::string base64_encode(unsigned char const *bytes_to_encode, unsigned int in_len)
    {
    std::string ret;
    int i = 0;
    int j = 0;
    unsigned char char_array_3[3];
    unsigned char char_array_4[4];

    while(in_len--)
        {
        char_array_3[i++] = *(bytes_to_encode++);
        if(i == 3)
            {
            char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
            char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
            char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
            char_array_4[3] = char_array_3[2] & 0x3f;

            for(i = 0; (i < 4); i++)
                {
                ret += base64_chars[char_array_4[i]];
                }
            i = 0;
            }
        }

    if(i)
        {
        for(j = i; j < 3; j++)
            {
            char_array_3[j] = '\0';
            }
        char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
        char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
        char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
        char_array_4[3] = char_array_3[2] & 0x3f;
        for(j = 0; (j < i + 1); j++)
            {
            ret += base64_chars[char_array_4[j]];
            }
        while((i++ < 3))
            {
            ret += '=';
            }
        }
    return ret;
    }

std::string base64_decode(std::string const &encoded_string)
    {
    int in_len = encoded_string.size();
    int i = 0;
    int j = 0;
    int in_ = 0;
    unsigned char char_array_4[4], char_array_3[3];
    std::string ret;

    while(in_len-- && (encoded_string[in_] != '=') && is_base64(encoded_string[in_]))
        {
        char_array_4[i++] = encoded_string[in_];
        in_++;
        if(i == 4)
            {
            for(i = 0; i < 4; i++)
                {
                char_array_4[i] = base64_chars.find(char_array_4[i]);
                }

            char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
            char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
            char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];

            for(i = 0; (i < 3); i++)
                {
                ret += char_array_3[i];
                }
            i = 0;
            }
        }

    if(i)
        {
        for(j = i; j < 4; j++)
            {
            char_array_4[j] = 0;
            }

        for(j = 0; j < 4; j++)
            {
            char_array_4[j] = base64_chars.find(char_array_4[j]);
            }

        char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
        char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
        char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];

        for(j = 0; (j < i - 1); j++)
            {
            ret += char_array_3[j];
            }
        }

    return ret;
    }

#ifndef EMSCRIPTEN

void Asset::draw()
    {
    if(total_frames_ > 1)
        {
        if(last_frame_tick_ + frame_rate < SDL_GetTicks())
            {
            SDL_RendererFlip flip = img_->flip;

            Frames *f = &gNavi->frames[d_->files_[current_frame_]];
            img_->ChangeImage(f->txname);

            img_->flip = flip;

            img_->tx_x = f->x;
            img_->tx_y = f->y;
            img_->SetWidth(f->width);
            img_->SetHeight(f->height);
            current_frame_++; //1 fps
            if(current_frame_ >= total_frames_)
                {
                current_frame_ = 0;
                }
            last_frame_tick_ = SDL_GetTicks();
            }
        }
    img_->Draw();
    }

void Asset::load_asset(int id)
    {
    d_ = &gNavi->assets_data[id];
    if(id == -1) //the -1 is always score at gameover
        {
        return;
        }
    else if(id == 1)//the one is always! credits
        {
        return;
        }
    else if(d_->type_ == -1 && d_->id_ == -1000)
        {
        return;
        }
    else if(d_->type_ != 2 && d_->files_.size() > 0) //it is not scml :P
        {
        Frames *f = &gNavi->frames[d_->files_[0]];
        total_frames_ = d_->files_.size();
        current_frame_ = 0;
        img_ = new Mk_Image(f->txname);
        frame_rate = 1000.0f / d_->fps_;

        img_->tx_x = f->x;
        img_->tx_y = f->y;
        img_->SetWidth(f->width);
        img_->SetHeight(f->height);
        }
    }

void Asset::load_asset_by_type_2(int type2)
    {
    for(std::map<long, Asset_Data>::iterator it = gNavi->assets_data.begin(); it != gNavi->assets_data.end(); ++it)
        {
        if(it->second.type_ != -1 && it->second.type_2_ == type2)
            {
            d_ = &it->second;
            id_ = d_->id_;
            if((d_->type_ == 1 || d_->type_ == 0) && d_->files_.size() > 0) //it is not scml :P
                {
                Frames *f = &gNavi->frames[d_->files_[0]];
                total_frames_ = d_->files_.size();
                current_frame_ = 0;
                img_ = new Mk_Image(f->txname);
                frame_rate = 1000.0f / d_->fps_;

                img_->tx_x = f->x;
                img_->tx_y = f->y;
                img_->SetWidth(f->width);
                img_->SetHeight(f->height);
                }
            }
        }
    }

#endif

NaVi *gNavi = 0;

NaVi::NaVi()
    {
    next_is_play = false;
    current_project = new project("", 0, 0, 0);
    keyboard_type = 0;
    srand(time(NULL));
    loading_network = false;
    page = 0;
    }

void click_use_register_data(int action, int x, int y)
    {
    if(g_game->user_data.size() == 0)
        {
        return;
        }
    ((MK_Textinput *) gNavi->gui_buttons[0])->input_text = g_game->user_data[0];
    ((MK_Textinput *) gNavi->gui_buttons[1])->input_text = g_game->user_data[1];
    ((MK_Textinput *) gNavi->gui_buttons[2])->input_text = g_game->user_data[2];
    }

void click_register_data(int action, int x, int y)
    {
    g_game->user_data.clear();

    if(((MK_Textinput *) gNavi->gui_buttons[0])->input_text.length() == 0)
        {
        gNavi->register_error = SDL_GetTicks() + 5000;
        return;
        }

    if(((MK_Textinput *) gNavi->gui_buttons[1])->input_text.length() == 0)
        {
        gNavi->register_error = SDL_GetTicks() + 5000;
        return;
        }

    if(((MK_Textinput *) gNavi->gui_buttons[2])->input_text.length() == 0)
        {
        gNavi->register_error = SDL_GetTicks() + 5000;
        return;
        }
    g_game->user_data.push_back(((MK_Textinput *) gNavi->gui_buttons[0])->input_text);
    g_game->user_data.push_back(((MK_Textinput *) gNavi->gui_buttons[1])->input_text);
    g_game->user_data.push_back(((MK_Textinput *) gNavi->gui_buttons[2])->input_text);

    for(std::vector<MK_Gui *>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end(); ++it)
        {
        delete (*it);
        }
    gNavi->gui_buttons.clear();
    gNavi->top10_labels.clear();
    g_game->state_ = PLAYING;
    g_game->show_register_data = false;
    g_game->start_level(0);
    gNavi->already_clicked = true;
    g_game->show_register_data = true;
    }

void NaVi::load_register_data()
    {
    MK_Textinput *itxt = new MK_Textinput("", 125, 440, 460, 80);
    gui_buttons.push_back(itxt);

    MK_Textinput *itxt2 = new MK_Textinput("", 125, 600, 460, 80);
    gui_buttons.push_back(itxt2);

    MK_Textinput *itxt3 = new MK_Textinput("", 125, 760, 460, 80);
    gui_buttons.push_back(itxt3);

    MK_Button *btn_click = new MK_Button(120, 904, 480, 118);
    btn_click->clickCallback = &click_register_data;
    gui_buttons.push_back(btn_click);

    MK_Button *btn_click2 = new MK_Button(120, 1090, 480, 118);
    btn_click2->clickCallback = &click_use_register_data;
    gui_buttons.push_back(btn_click2);
    }

int NaVi::MeasureTextWidth(std::string t, float font_size)
    {
    int biggest_width = 0;
    TTF_Font *myFont = global_font.GetFont(font_size);
    if(myFont == 0)
        {
        return 0;
        }

    std::vector<std::string> strs = StringTokenize(t, "\n");

    for(std::vector<std::string>::iterator it = strs.begin(); it != strs.end(); ++it)
        {
        t = (*it);

        // This creates the text surface
        SDL_Surface *stext = TTF_RenderText_Blended(myFont, t.c_str(), SDL_Color());

        // If it worked, then blit it to the main screen
        // If you wrap this function
        if(stext)
            {
            SDL_Texture *texture = SDL_CreateTextureFromSurface(g_game->renderer, stext);
            int w, h;
            SDL_QueryTexture(texture, NULL, NULL, &w, &h);
            if(w > biggest_width)
                {
                biggest_width = w;
                }
            SDL_DestroyTexture(texture);
            }
        SDL_FreeSurface(stext);
        }
    return biggest_width;
    }

void NaVi::setRegisterText()
    {
    if(gNavi->loaded_assets.begin() != gNavi->loaded_assets.end())
        {
        for(std::vector<Asset *>::iterator it = gNavi->loaded_assets.begin() + 1; it != gNavi->loaded_assets.end(); ++it)
            {
            delete (*it);
            it = gNavi->loaded_assets.erase(it);
            it--;
            }
        }

    if(gNavi->gui_buttons.begin() != gNavi->gui_buttons.end())
        {
        for(std::vector<MK_Gui *>::iterator it = gNavi->gui_buttons.begin() + 1; it != gNavi->gui_buttons.end(); ++it)
            {

            delete (*it);
            it = gNavi->gui_buttons.erase(it);
            it--;
            }
        }
    MK_Label *lbl = (MK_Label *) (*gNavi->gui_buttons.begin());
    lbl->text = "Registrando...";
    gNavi->netManager.upload_data_insecure();

    gNavi->top10_labels.clear();
    gNavi->already_clicked = true;
    }

#ifndef EMSCRIPTEN

string NaVi::naviEncrypt(string data)
    {
    string result = encrypt_text("!origin_OS_V1.0!", (char *) data.c_str(), data.length());
    result = base64_encode((byte *) result.c_str(), result.length());
    return result;
    }

string NaVi::naviDecrypt(string data)
    {
    string result = base64_decode(data.c_str());
    result = decrypt_text("!origin_OS_V1.0!", (char *) result.c_str(), result.length());
    return result;
    }


void NaVi::refresh_list()
    {
    netManager.sendLoginRequest();
    page = 0;
    }

bool NaVi::tryLogin(const char *name, const char *pass,bool checkLocal)
    {
    registerDirectories();

    if(hasUserData())
        {
        LoadUserData();
        string strName = naviDecrypt(gNavi->u_);
        if(strcmp(name,strName.c_str()) == 0 && checkLocal)
            {
            const char *data = LoadGameData();
            sendUserDataToJava(data);
            return false;
            }
        else
            {
            gNavi->logout(false);
            Login(name, pass);
            }
        }
    else
        {
        Login(name, pass);
        }
    return true;
    }

void NaVi::Login(const char *name, const char *pass)
    {
    gNavi->setUserData(name, pass);
    netManager.sendLoginRequest();
    }

#endif

void NaVi::setUserData(const char *n, const char *p)
    {
    std::string u_name(n);
    std::string u_pass(p);
    if(u_name.length() == 0 || u_pass.length() == 0)
        {
        return;
        }
    gNavi->u_ = naviEncrypt(u_name);
    gNavi->p_ = naviEncrypt(u_pass);
    }

std::string MakeFileName(const char *path, bool bUserDir = false)
    {
    return path;
    }

void click_app_gui(int action, int x, int y)
    {
    if(!gNavi->loading_network)
        {
        if(action == LOGIN_BUTTON && x > 210 && x < 510 && y > 800 && y < 890)
            {
            gNavi->tryLogin("", "",false);
            }
        else if(action == GAMES_SCREEN && x > 210 && x < 510 && y > 1160 && y < 1230)
            {
#ifndef EMSCRIPTEN
            gNavi->refresh_list();
#endif
            }
        else if(action == GAMES_SCREEN && x > 590 && x < 720 && y > 0 && y < 100)
            {
            gNavi->logout();
            }
        }
    }

void NaVi::logout(bool _exit)
    {
    std::stringstream ss2;
    ss2 << "interface/data/d.txt";
    //RemoveFile(ss2.str());

    std::stringstream ss3;
    ss3 << "interface/data/d2.txt";
    //RemoveFile(ss3.str());

    std::stringstream ss4;
    ss4 << "interface/data/d3.txt";
    //RemoveFile(ss4.str());

    if(_exit)
        {
        //sendLogOut();
        }
    }

void NaVi::ShowProjectScreen()
    {
    for(std::vector<MK_Gui *>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end(); ++it)
        {
        delete (*it);
        }
    gui_buttons.clear();
    gNavi->top10_labels.clear();

    MK_Button *btn = new MK_Button("interface/games.png", 360, 640);
    btn->clickCallback = &click_app_gui;
    btn->action = GAMES_SCREEN;
    gui_buttons.push_back(btn);
    MK_Label *lbl = 0;
    for(int i = 0; i < 10; i++)
        {
        if(i + (page * 10) >= projects_.size())
            {
            break;
            }
        lbl = new MK_Label(projects_[i + (page * 10)].name, 25, 118 + i * 100);
        lbl->separator = true;
        lbl->SeparatorColor.a = 55;
        lbl->SeparatorColor.r = 164;
        lbl->SeparatorColor.g = 190;
        lbl->SeparatorColor.b = 54;
        gui_buttons.push_back(lbl);

        MK_Button *btn2 = new MK_Button("interface/go.png", 680, 154 + i * 100);
        gui_buttons.push_back(btn2);

        MK_Button *btn_click = new MK_Button(0, 100 + i * 100, 720, 100);
        btn_click->clickCallback = &click_list_item;
        btn_click->action = i + (page * 10);
        gui_buttons.push_back(btn_click);
        }
    gNavi->already_clicked = true;

    if(projects_.size() >= (page + 1) * 10)
        {
        //we have an extra page
        MK_Button *btn2 = new MK_Button("interface/next.png", 640, 1200);
        btn2->action = 0;
        btn2->scale = 2.0f;
        gui_buttons.push_back(btn2);
        }
    if(page > 0)
        {
        //we have a prev page
        MK_Button *btn2 = new MK_Button("interface/prev.png", 80, 1200);
        btn2->action = 1;
        btn2->scale = 2.0f;
        gui_buttons.push_back(btn2);
        }
    }

void click_stats_screen(int action, int x, int y)
    {
    if(x < 360 && y > 100 && y < 200)
        {
        click_list_item(-1, 0, 0);
        gNavi->already_clicked = true;
        }
    else if(x < 160 && y < 100)
        {
        gNavi->ShowProjectScreen();
        }

    }

void click_upload_data(int action, int x, int y)
{
    gNavi->netManager.upload_data();
}

void click_test_screen(int action, int x, int y)
    {
    if(x > 360 && y > 100 && y < 200)
        {
        for(std::vector<MK_Gui *>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end(); ++it)
            {
            delete (*it);
            }
        gNavi->gui_buttons.clear();
        gNavi->top10_labels.clear();

        MK_Button *btn = new MK_Button("interface/mkstats.png", 360, 640);
        btn->clickCallback = &click_stats_screen;
        btn->action = 0;
        gNavi->gui_buttons.push_back(btn);

        MK_Label *lbl = new MK_Label(gNavi->projects_[gNavi->index_project].name, 360, 20);
        lbl->center = true;
        lbl->color.r = 255;
        lbl->color.g = 255;
        lbl->color.b = 255;
        gNavi->gui_buttons.push_back(lbl);

        lbl = new MK_Label("Numero de registros:", 360, 220);
        lbl->center = true;
        gNavi->gui_buttons.push_back(lbl);

        std::stringstream ss3;
        ss3 << "interface/data/" << gNavi->current_project->id << "_sv.txt";
        TextScanner b2(ss3.str());

        lbl = new MK_Label(NumberToString(b2.lineList.size()), 360, 290);
        lbl->center = true;
        gNavi->gui_buttons.push_back(lbl);

        if(b2.lineList.size() > 0)
            {
            MK_Button *btn2 = new MK_Button("interface/uploaddata.png", 360, 700);
            btn2->clickCallback = &click_upload_data;
            btn2->action = 100;
            gNavi->gui_buttons.push_back(btn2);
            }
        gNavi->already_clicked = true;
        }
    else if(x < 160 && y < 100)
        {
        gNavi->ShowProjectScreen();
        gNavi->already_clicked = true;
        }
    else if(y > 910 && y < 991) //870 920
        {
        gNavi->netManager.download_assets(gNavi->index_project);
        gNavi->already_clicked = true;
        for(std::vector<MK_Gui *>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end(); ++it)
            {
            delete (*it);
            }
        gNavi->gui_buttons.clear();
        gNavi->top10_labels.clear();
        }
    else if(y > 1060 && y < 1140 && gNavi->has_been_updated == true) //870 920
        {
        gNavi->already_clicked = true;

        for(std::vector<MK_Gui *>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end(); ++it)
            {
            delete (*it);
            }
        gNavi->gui_buttons.clear();
        gNavi->top10_labels.clear();
        gNavi->current_state = PRELOAD_GAME;
        }
    }

void click_list_item(int action, int x, int y)
    {
    if(action != -1)
        {
        gNavi->index_project = action;
        gNavi->current_project->id = gNavi->projects_[gNavi->index_project].id;
        }

    for(std::vector<MK_Gui *>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end(); ++it)
        {
        delete (*it);
        }
    gNavi->gui_buttons.clear();
    gNavi->top10_labels.clear();

    MK_Button *btn = new MK_Button("interface/mktest.png", 360, 640);
    btn->clickCallback = &click_test_screen;
    btn->action = 0;
    gNavi->gui_buttons.push_back(btn);

    MK_Label *lbl = new MK_Label(gNavi->projects_[gNavi->index_project].name, 360, 20);
    lbl->center = true;
    lbl->color.r = 255;
    lbl->color.g = 255;
    lbl->color.b = 255;
    gNavi->gui_buttons.push_back(lbl);

    lbl = new MK_Label("Presiona Update Data\npara descargar el juego\n \n \nPresiona Play Game\npara jugar", 360, 220);
    lbl->center = true;
    gNavi->gui_buttons.push_back(lbl);

    //we search for the file

    std::stringstream ss3;
    ss3 << "interface/data/" << gNavi->current_project->id << "_dt.txt";
    TextScanner b2(ss3.str());

    lbl = new MK_Label("Last Data Update: Never", 360, 850);
    gNavi->has_been_updated = false;
    if(b2.lineList.size() > 0)
        {

        lbl->text = "Last Data Update:\n";
        lbl->text.append(FilterToValidAscii(b2.lineList[0], false));
        lbl->y -= 50;

        //we add the btn
        MK_Button *btn2 = new MK_Button("interface/testbtn.png", 360, 1100);
        btn2->clickCallback = &click_test_screen;
        btn2->action = 100;
        gNavi->gui_buttons.push_back(btn2);
        gNavi->has_been_updated = true;

        }
    lbl->center = true;
    gNavi->gui_buttons.push_back(lbl);

    gNavi->already_clicked = true;
    }

bool NaVi::checkIfLoginExpired()
    {
    TextScanner tm;
    std::stringstream ssTm;
    ssTm << "interface/data/d3.txt";
    tm.LoadFile(ssTm.str());
    if(tm.lineList.size() == 0)
        {
        gNavi->logout(false);
        sendNoLoggedIn();
        return true;
        }
    std::string _time = naviDecrypt(tm.GetLine(0));
    unsigned long int sec = time(0);
    unsigned long int savedTime = strtoul(_time.c_str(), 0, 0);
    if(sec < savedTime || (sec - savedTime) > 259200)  //3 days in seconds
        {
        gNavi->logout(true);
        return true;
        }
    return false;
    }

void NaVi::LoadUserData()
    {
    TextScanner t;
    std::stringstream ss2;
    ss2 << "interface/data/d.txt";
    t.LoadFile(ss2.str());
    if(t.lineList.size() == 0)
        {
        sendNoLoggedIn();
        return;
        }
    gNavi->u_ = naviDecrypt(t.GetLine(0));
    gNavi->p_ = t.GetLine(1);
    }

const char *NaVi::LoadGameData()
    {
    cJSON *gameData = gNavi->GetGameData();
    if(gameData != 0)
        {
        registerProjectsFromData(gameData);
        return cJSON_Print(gameData);
        }
    return "";
    }

cJSON *NaVi::GetGameData()
    {
    TextScanner i;
    std::stringstream ss3;
    ss3 << "interface/data/d2.txt";
    i.LoadFile(ss3.str());
    std::string asd = naviDecrypt(i.GetAll());
    cJSON *root = cJSON_Parse((char *) asd.c_str());
    return root;
    }

void NaVi::Init()
    {
    register_error = 0;
    current_state = LOADING;
    global_font.Load(MakeFileName("interface/app.ttf"));
#ifdef EMSCRIPTEN
#else
#ifndef IS_DOWNLOAD_APP
    LoadProject();
#else
    std::stringstream ss3;
    ss3 << "interface/data/d.txt";
    TextScanner b2(ss3.str());
    current_state = STARTING;
    if(b2.lineList.size() > 0)
        {
        LoadUserData();
        LoadGameData();
        }
#endif
#endif

    white.r = 255;
    white.g = 255;
    white.b = 255;
    white.a = 255;

    black.r = 0;
    black.g = 0;
    black.b = 0;
    black.a = 255;
    }

void NaVi::registerAudioMix()
    {
#if !defined(EMSCRIPTEN) && !defined(__WINPHONE__)
    //mixer
#ifdef __APPLE__
    Mix_Init(MIX_INIT_OGG);
#else
    //Mix_Init(MIX_INIT_MP3);
    Mix_Init(MIX_INIT_MODPLUG);
#endif
    int audio_rate = 22050;
    Uint16 audio_format = MIX_DEFAULT_FORMAT;
    int audio_channels = 2;
    int audio_buffers = 4096;
    if(Mix_OpenAudio(audio_rate, audio_format, audio_channels, audio_buffers))
        {
        exit(1);
        }
    Mix_QuerySpec(&audio_rate, &audio_format, &audio_channels);
#endif
    }

void NaVi::registerDirectories()
    {
#ifdef __ANDROID__
#ifdef NEWENGINE_MASTERKIWIWRAPPER_H
    std::string s = getPreferencedPath();
#else
    std::string s = SDL_GetPrefPath("","");
#endif
#endif
    std::string s2 = s;
    std::string s3 = s;
    s.append("/interface");
    s2.append("/interface/data");
    s3.append("/interface/g");
    mkdir(s.c_str(), S_IRUSR | S_IWUSR | S_IXUSR);
    mkdir(s2.c_str(), S_IRUSR | S_IWUSR | S_IXUSR);
    mkdir(s3.c_str(), S_IRUSR | S_IWUSR | S_IXUSR);
    }

void NaVi::LoadProject()
    {
    TextScanner t("interface/data/b.txt");
    current_project->id = StringToInt(t.GetLine(1));
    LoadProjectFiles();
    }

std::string NaVi::decrypt_file(std::string file)
    {
    TextScanner b3(file);
    std::string _s = naviDecrypt(b3.GetAll());
    return _s;
    }

bool NaVi::hasFile(const char *file)
    {
    std::string f(file);
#ifdef __ANDROID__
#ifdef NEWENGINE_MASTERKIWIWRAPPER_H
    std::string path(getPreferencedPath());
    f = path + "/" + f;
#else
    f = SDL_GetPrefPath("","") + f;
#endif
#elif __APPLE__
    f = SDL_GetPrefPath("","") + f;
#endif
    FILE *fp = fopen(f.c_str(), "r");
    if(!fp)
        {
        return false;
        }
    fclose(fp);
    return true;
    }

bool NaVi::hasUserData()
    {
    std::string str = "interface/data/d.txt";
    return hasFile(str.c_str());
    }

bool NaVi::hasGameData(int gameID)
    {
        std::string str = "interface/data/" + NumberToString(gameID) + "_m_a.txt";
        return hasFile(str.c_str());
    }

bool NaVi::hasGameDataPath(int gameID, const char* path)
{
    std::string rootPath = std::string(path);
    std::string str = rootPath + "interface/data/" + NumberToString(gameID) + "_m_a.txt";
    return hasFile(str.c_str());
}

void NaVi::sendRequestForDataDate(int gameID)
    {
    long date = getGameDataDate(gameID);
    netManager.askForGameDataDate(gameID, date);
    }

bool NaVi::initGame()
    {
    gNavi->loaded_textures.clear();
    gNavi->LoadUserData();
    gNavi->LoadGameData();
    return gNavi->LoadProjectFiles();
    }

bool NaVi::initGameWithPath(const char* path)
{
    gNavi->loaded_textures.clear();
    gNavi->LoadUserData();
    gNavi->LoadGameData();
    return gNavi->LoadProjectFileswithPath(path);
}

bool NaVi::LoadProjectFileswithPath(const char* path)
{
    registerAudioMix();

    g_script_enviroment = new script_enviroment();
    g_script_enviroment->BindSquirrel();

    has_game_over_score = false;
    default_menu = 0;
    gNavi->loadGameFilesWithPath(path);

    game_over_menu = -1;
    std::stringstream ss3;
    std::string pathsito = std::string(path);
    ss3 << pathsito + "/interface/data/" << current_project->id << "_m_a.txt";
    TextScanner b2(ss3.str());
    cJSON *root = cJSON_Parse((char *) b2.GetAll().c_str());
    if(root == 0)
    {
        current_state = ANY_ERROR;
        gNavi->updateDownloadData(gNavi->current_project->id, "0");
        sendNoGameData();
        return false;
    }
    if(gNavi == NULL)
    {
        return false;
    }
    gNavi->current_project->game_type_ = StringToInt(cJSON_GetObjectItem(root, "game_type")->valuestring);
    loadGameAssets(root);
    menu_stats = 0;
    loadGameMenus(root);
    setStats(root);

#if defined(EMSCRIPTEN)
    game_stats_mode = 1;
#endif
    //LOS CREDITOS SON IGNORADOS POR EL ENGINE

    gNavi->setGame(gNavi->current_project->id,root);
    gNavi->setSetting(root);
    gNavi->setMusic(root);
    gNavi->setEffects(root);

    g_game->show_register_data = false;
    gameplay_music = 0;
    cJSON *g_music = cJSON_GetObjectItem(root, "gameplay_music");
    if(g_music != 0)
    {
        gameplay_music = StringToInt(g_music->valuestring);
    }
#if !defined(EMSCRIPTEN) && !defined(__WINPHONE__)
    current_music_ = 0;
#endif
    if(default_menu != 0)
    {
        AddMenu(default_menu);
    }
    SAFE_DELETE(root);
#ifdef __APPLE__
    g_game->game_font.Load(MakeFileName("interface/test.ttf"));
#else
    g_game->game_font.Load("interface/test.ttf");
#endif
    game_over_score_ = 0;
    return true;
}

bool NaVi::LoadProjectFiles()
    {
    registerAudioMix();

    g_script_enviroment = new script_enviroment();
    g_script_enviroment->BindSquirrel();

    has_game_over_score = false;
    default_menu = 0;
    gNavi->loadGameFiles();

    game_over_menu = -1;
    std::stringstream ss3;
    std::stringstream script;
    ss3 << "interface/data/" << current_project->id << "_m_a.txt";
    TextScanner b2(ss3.str());
    cJSON *root = cJSON_Parse((char *) b2.GetAll().c_str());
    if(root == 0)
        {
        current_state = ANY_ERROR;
        gNavi->updateDownloadData(gNavi->current_project->id, "0");
        sendNoGameData();
        return false;
        }
    if(gNavi == NULL)
        {
        return false;
        }
    gNavi->current_project->game_type_ = StringToInt(cJSON_GetObjectItem(root, "game_type")->valuestring) - 1;
    //script << "interface/g/" << current_project->game_type_ << ".txt";
    script << "interface/g/" << current_project->game_type_ << ".bin";
    TextScanner scriptTexto(script.str());
    loadGameAssets(root);
    menu_stats = 0;
    loadGameMenus(root);
    setStats(root);

#if defined(EMSCRIPTEN)
    game_stats_mode = 1;
#endif
    //LOS CREDITOS SON IGNORADOS POR EL ENGINE

    gNavi->setGame(gNavi->current_project->id,root, scriptTexto.GetAll());
    gNavi->setSetting(root);
    gNavi->setMusic(root);
    gNavi->setEffects(root);

    g_game->show_register_data = false;
    gameplay_music = 0;
    cJSON *g_music = cJSON_GetObjectItem(root, "gameplay_music");
    if(g_music != 0)
        {
        gameplay_music = StringToInt(g_music->valuestring);
        }
#if !defined(EMSCRIPTEN) && !defined(__WINPHONE__)
    current_music_ = 0;
#endif
    if(default_menu != 0)
        {
        AddMenu(default_menu);
        }
    SAFE_DELETE(root);
#ifdef __APPLE__
    g_game->game_font.Load(MakeFileName("interface/test.ttf"));
#else
    g_game->game_font.Load("interface/test.ttf");
#endif
    game_over_score_ = 0;
    return true;
    }

void NaVi::loadGameFiles()
    {
    std::stringstream ss2;
    ss2 << "interface/data/" << current_project->id << ".txt";
    TextScanner b(ss2.str());

    std::string music = "";
    int i = 0;
    while(b.GetLine(i) != "" && gNavi != NULL)
        {
        std::string f = b.GetLine(i++);
        std::vector<std::string> res = StringTokenize(f, "|");
        if(res.size() > 1)
            {
            f = res[1];
            }
        res.clear();
        std::string ext = FilterToValidAscii((GetFileExtension(f)), false);
        if(ext == "mp3")
            {
            music = f;
            }
        else if(ext == "mkinfo")
            {
            f = FilterToValidAscii(f, false);
            StringReplace("interface/", "", f);
            TextScanner ts("interface/" + f);
            cJSON *root = cJSON_Parse((char *) ts.GetAll().c_str());
            for(int i = 0; i < cJSON_GetArraySize(root); i++)
                {
                cJSON *subitem = cJSON_GetArrayItem(root, i);
                if(subitem == 0)
                    {
                    continue;
                    }
                cJSON *x1 = cJSON_GetObjectItem(subitem, "x1");
                if(x1 == 0)
                    {
                    continue;
                    }
                cJSON *y1 = cJSON_GetObjectItem(subitem, "y1");
                cJSON *x2 = cJSON_GetObjectItem(subitem, "w");
                cJSON *y2 = cJSON_GetObjectItem(subitem, "h");
                cJSON *path = cJSON_GetObjectItem(subitem, "path");
                int _x1 = x1->valueint;
                int _y1 = y1->valueint;
                int _x2 = x2->valueint;
                int _y2 = y2->valueint;
                std::string p = path->valuestring;
                std::vector<std::string> tok = StringTokenize(p, "/");
                p = tok[tok.size() - 3] + "_" + tok[tok.size() - 1];
                tok.clear();
                std::string fname = ModifyFileExtension(GetFileNameFromString(f), "mktx");
                std::vector<std::string> tok2 = StringTokenize(fname, "_");
                StringReplace("info", "tx", tok2[tok2.size() - 1]);
                fname = "";
                for(int i = 0; i < tok2.size() - 1; i++)
                    {
                    fname += tok2[i] + "_";
                    }
                fname += tok2[tok2.size() - 1];
                Mk_Image *load_img = new Mk_Image("interface/data/" + fname);
                SAFE_DELETE(load_img);
                frames[p] = Frames(_x1, _y1, _x2, _y2, fname);
                }
            SAFE_DELETE(root);
            }
        }
    }


void NaVi::loadGameFilesWithPath(const char* path)
{
    std::stringstream ss2;
    std::string rootPath = std::string(path);
    ss2 << rootPath + "/interface/data/" << current_project->id << ".txt";
    TextScanner b(ss2.str());

    std::string music = "";
    int i = 0;
    while(b.GetLine(i) != "" && gNavi != NULL)
    {
        std::string f = b.GetLine(i++);
        std::vector<std::string> res = StringTokenize(f, "|");
        if(res.size() > 1)
        {
            f = res[1];
        }
        res.clear();
        std::string ext = FilterToValidAscii((GetFileExtension(f)), false);
        if(ext == "mp3")
        {
            music = f;
        }
        else if(ext == "mkinfo")
        {
            f = FilterToValidAscii(f, false);
            StringReplace("interface/", "", f);
            TextScanner ts("interface/" + f);
            cJSON *root = cJSON_Parse((char *) ts.GetAll().c_str());
            for(int i = 0; i < cJSON_GetArraySize(root); i++)
            {
                cJSON *subitem = cJSON_GetArrayItem(root, i);
                if(subitem == 0)
                {
                    continue;
                }
                cJSON *x1 = cJSON_GetObjectItem(subitem, "x1");
                if(x1 == 0)
                {
                    continue;
                }
                cJSON *y1 = cJSON_GetObjectItem(subitem, "y1");
                cJSON *x2 = cJSON_GetObjectItem(subitem, "w");
                cJSON *y2 = cJSON_GetObjectItem(subitem, "h");
                cJSON *path = cJSON_GetObjectItem(subitem, "path");
                int _x1 = x1->valueint;
                int _y1 = y1->valueint;
                int _x2 = x2->valueint;
                int _y2 = y2->valueint;
                std::string p = path->valuestring;
                std::vector<std::string> tok = StringTokenize(p, "/");
                p = tok[tok.size() - 3] + "_" + tok[tok.size() - 1];
                tok.clear();
                std::string fname = ModifyFileExtension(GetFileNameFromString(f), "mktx");
                std::vector<std::string> tok2 = StringTokenize(fname, "_");
                StringReplace("info", "tx", tok2[tok2.size() - 1]);
                fname = "";
                for(int i = 0; i < tok2.size() - 1; i++)
                {
                    fname += tok2[i] + "_";
                }
                fname += tok2[tok2.size() - 1];
                Mk_Image *load_img = new Mk_Image("interface/data/" + fname);
                SAFE_DELETE(load_img);
                frames[p] = Frames(_x1, _y1, _x2, _y2, fname);
            }
            SAFE_DELETE(root);
        }
    }
}

void NaVi::loadGameAssets(cJSON *gameData)
    {
    cJSON *assets = cJSON_GetObjectItem(gameData, "assets");
    if(assets != 0)
        {
        for(int i = 0; i < cJSON_GetArraySize(assets); i++)
            {
            cJSON *subitem = cJSON_GetArrayItem(assets, i);
            cJSON *type = cJSON_GetObjectItem(subitem, "type");
            cJSON *a_id = cJSON_GetObjectItem(subitem, "asset_id");
            cJSON *_type2 = cJSON_GetObjectItem(subitem, "type2");
            int type2 = StringToInt(_type2->valuestring);
            cJSON *fps = cJSON_GetObjectItem(subitem, "fps");
            int id = StringToInt(a_id->valuestring);
            assets_data[id].type_ = type->valueint;
            assets_data[id].id_ = id;
            assets_data[id].type_2_ = type2;
            if(fps != 0)
                {
                assets_data[id].fps_ = StringToInt(fps->valuestring);
                }
            int c = 1;
            cJSON *fname = cJSON_GetObjectItem(subitem, std::string("fname_" + NumberToString(c)).c_str());
            while(fname != 0)
                {
                assets_data[id].files_.push_back(fname->valuestring);
                fname = cJSON_GetObjectItem(subitem, std::string("fname_" + NumberToString(++c)).c_str());
                }
            }
        }
    }

void NaVi::loadGameMenus(cJSON *gameData)
    {
    cJSON *menus = cJSON_GetObjectItem(gameData, "menus");
        SDL_Log("mid es %d", *menus);
    if(menus != 0)
        {
        for(int i = 0; i < cJSON_GetArraySize(menus); i++)
            {
            cJSON *subitem = cJSON_GetArrayItem(menus, i);
            cJSON *saved_text = cJSON_GetObjectItem(subitem, "saved_text");
            cJSON *m_id = cJSON_GetObjectItem(subitem, "menu_id");
            cJSON *m_music = cJSON_GetObjectItem(subitem, "music");
            cJSON *m_special = cJSON_GetObjectItem(subitem, "special");
            int mid = StringToInt(m_id->valuestring);
            menus_[mid].id_ = mid;
            if(m_music != 0)
                {
                menus_[mid].music_id_ = StringToInt(m_music->valuestring);
                }
            menus_[mid].text_ = saved_text->valuestring;
            if(m_special != 0)
                {
                menus_[mid].special_ = m_special->valueint;
                if(menus_[mid].special_ == 3) //soy stats y necesito que me llamen al gameover
                    {
                    menu_stats = mid;
                    cJSON *m_special_text = cJSON_GetObjectItem(subitem, "special_text");
                    menus_[mid].desc_ = m_special_text->valuestring;
                    cJSON *m_special_extra_1 = cJSON_GetObjectItem(subitem, "special_extra_1");
                    menus_[mid].extra_1 = m_special_extra_1->valuestring;
                    cJSON *m_special_extra_2 = cJSON_GetObjectItem(subitem, "special_extra_2");
                    menus_[mid].extra_2 = m_special_extra_2->valuestring;
                    cJSON *m_special_extra_3 = cJSON_GetObjectItem(subitem, "special_extra_3");
                    menus_[mid].extra_3 = m_special_extra_3->valuestring;
                    cJSON *m_special_extra_4 = cJSON_GetObjectItem(subitem, "special_extra_4");
                    menus_[mid].extra_4 = m_special_extra_4->valuestring;
                    }
                else if(menus_[mid].special_ == 4) //soy stats y necesito que me llamen al gameover
                    {
                    cJSON *m_special_text = cJSON_GetObjectItem(subitem, "special_text");
                    menus_[mid].desc_ = m_special_text->valuestring;
                    }
                }
            SDL_Log("i es %d", i);
            SDL_Log("mid es %d", mid);
            if(i == 0)
                {
                    default_menu = mid;
                }
            if(i == 1)
                {
                    game_over_menu = mid;
                }
            }
        }
    }

void NaVi::setStats(cJSON *gameData)
    {
    cJSON *m_stats = cJSON_GetObjectItem(gameData, "has_stats");
    game_has_stats = 0;
    if(m_stats != 0)
        {
        game_has_stats = StringToInt(m_stats->valuestring);
        }

    m_stats = cJSON_GetObjectItem(gameData, "stats_begin");
    game_stats_begin = 0;
    if(m_stats != 0)
        {
        game_stats_begin = StringToInt(m_stats->valuestring);
        }

    m_stats = cJSON_GetObjectItem(gameData, "stats_mode");
    game_stats_mode = 0;
    if(m_stats != 0)
        {
        game_stats_mode = StringToInt(m_stats->valuestring);
        if(game_stats_mode == 0)
            game_stats_mode = 1;
        else
            game_stats_mode = 0;
        }
    }

void NaVi::setGame(int gameID,cJSON* gameData)
    {
    for(int i = 0; i < gNavi->projects_.size(); i++)
        {
        if(gNavi->projects_[i].id == gameID)
            {
            gNavi->current_project = new project(gNavi->projects_[i]);
            break;
            }
        }
    g_game->game_id = gNavi->current_project->id;
    g_game->isAudioAvailable = (gNavi->current_project->audioActivated == 1);
    g_game->init_game();
    if(gNavi->current_project->pjct_script == "")
    {
        if (gNavi->current_project->game_type_ == 1) //jumper
        {
            gNavi->current_project->pjct_script = "local width = 720;\nlocal height = 1280;\n\nlocal character;\nlocal background;\nlocal a_platform;\nlocal move_platform;\nlocal foreground;\nlocal fake_platform;\nlocal one_use_platform;\nlocal jump_item;\nlocal score;\nlocal gravity = 0.9;\nlocal has_moving_platform;\nlocal has_fake_platform;\nlocal has_one_use_platform;\nlocal has_jump_item;\n\nlocal base_y;\n\nlocal item_height;\n\nlocal player_vx;\nlocal player_vy;\nlocal player_width;\nlocal player_height;\nlocal player_is_moving_left;\nlocal player_is_moving_right;\nlocal player_x;\nlocal player_y;\nlocal position;\nlocal there_is_jump_item;\nlocal platforms = [];\n\nlocal score_position;\nlocal score_color;\nlocal font_size;\n\nlocal was_moving;\nlocal num_platforms = 8;\n\nlocal broken;\nlocal char_shape;\n\nclass Platform\n{\npWidth = null;\npHeight = null;\npX = null;\npY = null;\npFlag = null;\npState = null;\npType = null;\npMoved = null;\npVx = null;\npJump_item = null;\npShape = null;\npShape_item = null;\n\nconstructor(p_c)\n{\npState = 0;\npFlag = 0;\npWidth = asset_get_width(a_platform);\npHeight = asset_get_height(a_platform);\npX = random_int(0,width+1);\npY = position;\nposition += height / p_c;\npType = 0;\npJump_item = false;\npMoved = 0;\npVx = 8;\nsetType();\nevaluateBroken();\nsetItem();\npShape = phy_create_rect_shape(pX,pY,pWidth,pHeight,true);\nphy_set_sensor(pShape,true);\nphy_shape_set_collision_type(pShape,2);\n}\n\nfunction setType()\n{\nif(score > 300)\n{\nif(has_moving_platform)\n{\npType = random_int(0,2);\n}\nelse\n{\npType = 0;\n}\n}\nif(score > 700)\n{\nif(has_moving_platform && has_fake_platform)\n{\npType = random_int(0,3);\n}\nelse if(has_moving_platform)\n{\npType = random_int(0,2);\n}\nelse if(has_fake_platform)\n{\npType = random_int(0,2);\nif(pType == 1)\n{\npType = 2;\n}\n}\nelse\n{\npType = 0;\n}\n}\nif(score > 1200)\n{\nif(has_moving_platform && has_fake_platform && has_one_use_platform)\n{\npType = random_int(0,4);\n}\nelse if(has_moving_platform && has_one_use_platform)\n{\npType = random_int(0,3); \nif(pType == 2)\n{\npType = 3;\n}\n}\nelse if(has_fake_platform && has_one_use_platform)\n{\npType = random_int(0,3);\nif(pType == 1)\n{\npType = 3;\n}\n}\nelse if(has_moving_platform && has_fake_platform)\n{\npType = random_int(0,3);\n}\nelse if(has_moving_platform)\n{\npType = random_int(0,2);\n}\nelse if(has_fake_platform)\n{\npType = random_int(0,2);\nif (pType == 1)\n{\npType = 2;\n}\n}\nelse if(has_one_use_platform)\n{\npType = random_int(0,2);\nif(pType == 1)\n{\npType = 3;\n}\n}\nelse\n{\npType = 0;\n}\n}\n}\n\nfunction evaluateBroken()\n{\nif (pType == 2 && broken < 1)\n{\nbroken++;\n}\nelse if (pType == 2 && broken >= 1)\n{\npType = 0;\nbroken = 0;\n}\n}\n\nfunction setItem()\n{\nif (has_jump_item && (pType == 0 || pType == 1) && !there_is_jump_item)\n{\nlocal res =random_int(0,100);\nif (res < 10)\n{\nthere_is_jump_item = true;\npJump_item = true;\npShape_item = phy_create_rect_shape(pX,pY-(pWidth*0.5),asset_get_width(jump_item),asset_get_height(jump_item),true);\nphy_set_sensor(pShape_item,true);\nphy_shape_set_collision_type(pShape_item,3);\n}\n}\n}\n}\n\nfunction init()\n{\nwas_moving = 0;\nbackground = load_asset(1,false);\nmove_asset(background,width*0.5,height*0.5);\nforeground = load_asset(2,true);\nmove_asset(foreground,width*0.5,height*0.5);\n\ncharacter = load_asset(3,false);\na_platform = load_asset(10,false);\n\ninitCustomPlatforms();\n}\n\nfunction initCustomPlatforms()\n{\nmove_platform = load_asset(11,true);\nhas_moving_platform = false;\nif (move_platform != null)\n{\nhas_moving_platform = true;\n}\n\nfake_platform = load_asset(12,true);\nhas_fake_platform = false;\nif (fake_platform != null)\n{\nhas_fake_platform = true;\n}\n\none_use_platform = load_asset(13,true);\nhas_one_use_platform = false;\nif (one_use_platform != null)\n{\nhas_one_use_platform = true;\n}\n\njump_item = load_asset(14,true);\nhas_jump_item = false;\nif (jump_item != null)\n{\nhas_jump_item = true;\nitem_height = asset_get_height(jump_item);\n}\n}\n\nfunction start_level(level_id)\n{\nscore = 0;\nbase_y = height-1;\nplayer_vx = 0;\nplayer_vy = 0.01;\nplayer_height = asset_get_height(character);\nplayer_width = asset_get_width(character);\nplayer_is_moving_left = false;\nplayer_is_moving_right = false;\nplayer_x = width * 0.5;\nplayer_y = height -(player_height * 0.5);\nmove_asset(character,player_x,player_y);\nbroken = 0;\nposition = 0;\nthere_is_jump_item = false;\nplatforms.clear();\n\nfor(local i = 0;i < num_platforms;i++)\n{\n        platforms.push(Platform(num_platforms));\n}\n\n    char_shape = phy_create_shape_for_asset(character,false);\n    phy_set_sensor(char_shape,true);\n    phy_shape_set_collision_type(char_shape,1);\n\n    phy_create_collision_handler_begin(1,2,begin);\n    phy_create_collision_handler_end(1,2,colliding);\n    phy_create_collision_handler_begin(1,3,begin);\n    phy_create_collision_handler_end(1,3,colliding);\n}\n\nfunction update()\n{\nif(player_is_moving_left)\n{\nplayer_vx -= 0.8;\n}\nelse if(player_vx < 0)\n{\nplayer_vx += 0.8;\nif(player_vx > 0)\n{\nplayer_vx = 0;\n}\n}\nif(player_is_moving_right)\n{\nplayer_vx += 0.8;\n}\nelse if(player_vx > 0)\n{\nplayer_vx -= 0.8;\nif(player_vx < 0)\n{\nplayer_vx = 0;\n}\n}\nif(player_vx < -20)\n{\nplayer_vx = -20;\n}\nif(player_vx > 20)\n{\nplayer_vx = 20;\n}\n    player_x += player_vx;\nif((player_y + player_height) > base_y && base_y <= height)\n{\njump();\n}\nif(base_y > height && (player_y + player_height) > height)\n{\ngame_over();\nreturn;\n    }\nif(player_x > width)\n{\nplayer_x = -player_width;\n}\nelse if(player_x < -player_width)\n{\nplayer_x = width;\n}\nif(player_y >= ((height*0.5)-(player_height*0.5)))\n{\nplayer_y += player_vy;\nplayer_vy += gravity;\n}\nelse\n{\nfor(local i = 0;i < platforms.len();i++)\n{\nif(player_vy < 0)\n{\nplatforms[i].pY -= player_vy;\n}\nif(platforms[i].pY > height)\n{\nif(platforms[i].pJump_item)\n{\nthere_is_jump_item = false;\n}\nlocal _y = platforms[i].pY;\nplatforms.remove(i);\ni--;\nplatforms.push(Platform(num_platforms));\nplatforms[platforms.len()-1].pY = _y - height;\n}\n}\nbase_y -= player_vy;\nplayer_vy += gravity;\nif(player_vy >= 0)\n{\nplayer_y += player_vy;\nplayer_vy += gravity;\n}\nscore++;\n}\nif(player_vx < 0 && was_moving == 0)\n{\nflip_asset(character,true);\nwas_moving = -1;\n}\nelse if(player_vx > 0 && was_moving == -1)\n{\nflip_asset(character,false);\nwas_moving = 0;\n}\nfor(local i = 0;i < platforms.len();i++)\n{\nif(platforms[i].pType == 1)\n{\nif(platforms[i].pX < 0 || platforms[i].pX > width)\n{\nplatforms[i].pVx *= -1;\n}\nplatforms[i].pX += platforms[i].pVx;\n}\nphy_move_shape(platforms[i].pShape,platforms[i].pX,platforms[i].pY);\nif(platforms[i].pShape_item != null)\n{\nphy_move_shape(platforms[i].pShape_item,platforms[i].pX * 0.5,platforms[i].pY * 0.5);\n}\n}\nmove_asset(character,player_x,player_y+(player_height*0.5));\nphy_move_shape(char_shape,player_x,player_y+(player_height*0.5));\n}\n\nfunction draw()\n{\ndraw_asset(background);\n\nfor(local i = 0;i  < platforms.len();i++)\n{\nif(platforms[i].pState == 1)\n{\ncontinue;\n}\nif(platforms[i].pType == 0)\n{\nmove_asset(a_platform,platforms[i].pX,platforms[i].pY);\ndraw_asset(a_platform);\n}\nelse if(platforms[i].pType == 1)\n{\nmove_asset(move_platform,platforms[i].pX,platforms[i].pY);\ndraw_asset(move_platform);\n}\nelse if(platforms[i].pType == 2)\n{\nmove_asset(fake_platform,platforms[i].pX,platforms[i].pY);\ndraw_asset(fake_platform);\n}\nelse if(platforms[i].pType == 3)\n{\nmove_asset(one_use_platform,platforms[i].pX,platforms[i].pY);\ndraw_asset(one_use_platform);\n}\nif(platforms[i].pJump_item && has_jump_item)\n{\nif(platforms[i].pType == 0)\n{\nmove_asset(jump_item,platforms[i].pX,platforms[i].pY-(asset_get_height(a_platform)*0.5)-(asset_get_height(jump_item)*0.5));\n}\nelse\n{\nmove_asset(jump_item,platforms[i].pX,platforms[i].pY-(asset_get_height(move_platform)*0.5)-(asset_get_height(jump_item)*0.5));\n}\ndraw_asset(jump_item);\n}\n}\nif(foreground != null)\n{\ndraw_asset(foreground);\n}\ndraw_asset(character);\n\ndraw_text_center(width*0.5,height*0.01,2,score);\n}\n\nfunction game_over()\n{\nplatforms.clear();\nsave_highscore_gameover(score);\n    goto_menu_after_ms(\"gameover\", 1250);\n}\n\nfunction get_arrow_keys(key,state)\n{\n    if (key == 0 && state == 1)\n{\nplayer_is_moving_left = true;\n}\n    else if (key == 0)\n{\nplayer_is_moving_left = false;\n}\n    if (key == 1 && state == 1)\n{\nplayer_is_moving_right = true;\n}\n    else if (key == 1)\n{\nplayer_is_moving_right = false;\n}\n}\n\nfunction on_accelerometer_update(x,y,z) \n{\nif (x < -0.1)\n{\nplayer_vx = x*3;\n}\nelse if (x > 0.1)\n{\nplayer_vx = x*3;\n}\nif(player_vx > 8)\n{\nplayer_vx = 8;\n}\nelse if(player_vx < -8)\n{\nplayer_vx = -8;\n}\n}\n\nfunction jump()\n{\nplay_effect(1);\nplayer_vy = -27;\n}\n\nfunction jumpHigh()\n{\nplay_effect(2);\nplayer_vy = -36.66*2;\n}\n\nfunction begin(shape_1,shape_2)\n{\nif(player_vy < 0)\n{\nreturn 1;\n}\nfor(local i = 0;i < platforms.len();i++)\n{\nif(compare_shapes(platforms[i].pShape,shape_2))\n{\nif(platforms[i].pType == 2 || platforms[i].pType == 3)\n{\n    if(platforms[i].pState != 1)\n    {\n                    play_effect(3);\n    }\nplatforms[i].pState = 1;\n}\nif(platforms[i].pType != 2)\n{\njump();\n}\nbreak;\n}\nelse if(platforms[i].pShape_item != null)\n{\nif(compare_shapes(platforms[i].pShape_item,shape_2))\n{\njumpHigh();\n}\n}\n}\n}\n\nfunction colliding(shape_1,shape_2)\n{\nif(player_vy < 0)\n{\nreturn 1;\n}\nfor(local i = 0;i < platforms.len();i++)\n{\nif(compare_shapes(platforms[i].pShape,shape_2))\n{\nif(platforms[i].pType == 2 || platforms[i].pType == 3)\n{\n    if(platforms[i].pState != 1)\n    {\n                    play_effect(3);\n    }\nplatforms[i].pState = 1;\n}\nif(platforms[i].pType != 2)\n{\njump();\n}\nbreak;\n}\nelse if(platforms[i].pShape_item != null)\n{\nif(compare_shapes(platforms[i].pShape_item,shape_2))\n{\njumpHigh();\n}\n}\n}\n}";
        }
        else if (gNavi->current_project->game_type_ == 2) //shoot-them-up
        {
            gNavi->current_project->pjct_script = "local enemy_asset;\nlocal player;\nlocal bullet;\nlocal background;\nlocal game_over;\nlocal enemyasset_h;\nlocal playerasset_h;\nlocal playerasset_w;\n\nlocal is_pressing_left = false;\nlocal is_pressing_right = false;\nlocal next_player_bullet = 0;\nlocal puntos = 0;\n\nlocal enemigos_count = 0;\n\n//enemy array\nclass enemy {\n    constructor() {\n        pos = vector_2();\n        pos.x = random_int(100, 620);\n        pos.y = -50;\n        next_enemy_bullet = get_time() + random_int(1500, 4000);\n        goto_y = random_int(200, 450);\n        enemigos_count = enemigos_count + 1;\n    }\n    pos = null;\n    shape = null;\n    next_enemy_bullet = 0;\n    goto_y = 100;\n\n    function move(new_x, new_y) {\n        pos.x = new_x;\n        pos.y = new_y;\n        if (shape != null)\n            phy_move_shape(shape, pos.x, pos.y - enemyasset_h);\n    }\n\n}\n\nclass bullet_obj {\n    constructor() {\n        pos = vector_2();\n        pos.x = 0;\n        pos.y = 0;\n    }\n    pos = null;\n    shape = null;\n\n    function move(new_x, new_y) {\n        pos.x = new_x;\n        pos.y = new_y;\n        if (shape != null)\n            phy_move_shape(shape, pos.x, pos.y);\n    }\n\n}\n\n//empty array\nlocal enemies = [];\nlocal bullets_player = [];\nlocal bullets_enemy = [];\n\nlocal x_shape = null;\nlocal x_;\n\nfunction init() {\n    //phy_set_gravity(0,9.8);\n\n    background = load_asset(1, true);\n    enemy_asset = load_asset(100, true);\n    bullet = load_asset(10, true);\n    player = load_asset(3, true);\n\n    enemyasset_h = asset_get_height(enemy_asset) / 2;\n    playerasset_h = asset_get_height(player) / 2;\n    playerasset_w = asset_get_width(player) / 4;\n\n    if (background != null)\n        move_asset(background, 720 / 2, 1280 / 2);\n\n    //finish_game_with_error(\"Errrostototote\");}\n\n    phy_create_collision_handler_begin(1, 2, collision);\n    phy_create_collision_handler_begin(3, 4, kill_player);\n\n}\n\nfunction collision(shape_1, shape_2) {\n    print(\"inicio colision \");\n\n    foreach(idx, val in enemies) {\n        if (compare_shapes(shape_1, val.shape) || compare_shapes(val.shape, shape_2)) {\n            phy_remove_shape(val.shape);\n            val.shape = null;\n            enemies.remove(idx);\n            enemigos_count--;\n            puntos = puntos + 10;\n            //mate un enemigo\n            play_effect(3);\n\n        }\n    }\n\n    foreach(idx, val in bullets_player) {\n        if (compare_shapes(shape_1, val.shape) || compare_shapes(val.shape, shape_2)) {\n            phy_remove_shape(val.shape);\n            val.shape = null;\n            bullets_player.remove(idx);\n        }\n    }\n}\n\nfunction kill_player(shape_1, shape_2) {\n    print(\"se meure el jugador\");\n\n    game_over = true;\n    save_highscore_gameover(puntos);\n    goto_menu_after_ms(\"gameover\", 1250);\n    play_effect(4);\n\n\n}\n\n//only used on windows. mac or linux\nfunction get_arrow_keys(key, state) {\n    if (key == 0 && state == 1) {\n        is_pressing_left = true;\n    } else if (key == 0) {\n        is_pressing_left = false;\n    } else if (key == 1 && state == 1) {\n        is_pressing_right = true;\n    } else if (key == 1) {\n        is_pressing_right = false;\n    }\n\n}\n\nfunction on_accelerometer_update(x, y, z) {\n    //print(\"Yaay\");\n    is_pressing_right = false;\n    is_pressing_left = false;\n    if (x < 1.5 && x > -1.5)\n        return; //muy lento\n    else if (x < 0) {\n        is_pressing_left = true;\n        //player_vx_ = _x*3;\n    } else if (x > 0) {\n        is_pressing_right = true;\n        //player_vx_ = _x*3;\n    }\n}\n\nfunction start_level(level_num) {\n    game_over = false;\n    foreach(idx, val in enemies) {\n        phy_remove_shape(val.shape);\n        //enemies.remove(idx);\n    }\n\n    foreach(idx, val in bullets_player) {\n        phy_remove_shape(val.shape);\n        //bullets_player.remove(idx);\n    }\n\n    foreach(idx, val in bullets_enemy) {\n        phy_remove_shape(val.shape);\n        //bullets_enemy.remove(idx);\n    }\n\n    enemies.clear();\n    bullets_enemy.clear();\n    bullets_player.clear();\n\n    if (x_shape != null)\n        phy_remove_shape(x_shape);\n\n    move_asset(player, 360, 1080 + playerasset_h);\n    x_shape = phy_create_shape_for_asset(player, true);\n    x_ = 360;\n    phy_shape_set_collision_type(x_shape, 4);\n\n    next_player_bullet = 0;\n    enemigos_count = 0;\n    puntos = 0;\n}\n\nfunction add_enemy() {\n    local e = enemy();\n    e.shape = phy_create_shape_for_asset(enemy_asset, true);\n    phy_shape_set_collision_type(e.shape, 2);\n    e.move(e.pos.x, e.pos.y);\n    enemies.push(e);\n}\n\nfunction player_shoot(start_x, start_y) {\n    local e = bullet_obj();\n    e.shape = phy_create_shape_for_asset(bullet, false);\n    phy_shape_set_collision_type(e.shape, 1);\n    e.move(start_x, start_y);\n    bullets_player.push(e);\n    play_effect(1);\n}\n\nfunction enemy_shoot(start_x, start_y) {\n    local e = bullet_obj();\n    e.shape = phy_create_shape_for_asset(bullet, false);\n    phy_shape_set_collision_type(e.shape, 3);\n    e.move(start_x, start_y);\n    bullets_enemy.push(e);\n    play_effect(2);\n}\n\nfunction update() {\n\n    if (game_over == true) {\n        return;\n    }\n    if (enemigos_count < 3) {\n        add_enemy();\n    }\n\n    if (is_pressing_left == true) {\n        x_ -= 7;\n        if (x_ < 0)\n            x_ = 0;\n        phy_move_shape(x_shape, x_, 1080 + playerasset_h);\n    } else if (is_pressing_right == true) {\n        x_ += 7;\n        if (x_ > 720)\n            x_ = 720;\n        phy_move_shape(x_shape, x_, 1080 + playerasset_h);\n    }\n    move_asset(player, x_, 1080);\n    //we move all enemies\n    foreach(idx, val in enemies) {\n        if (val.pos.y < val.goto_y) {\n            val.move(val.pos.x, val.pos.y + 3);\n        }\n\n        //we shoot bullet\n        if (val.next_enemy_bullet < get_time()) {\n            enemy_shoot(val.pos.x, val.pos.y);\n\n            val.next_enemy_bullet = get_time() + random_int(1500, 4000);\n        }\n    }\n\n    //we move bullets\n    foreach(idx, val in bullets_player) {\n        val.move(val.pos.x, val.pos.y - 30);\n        if (val.pos.y < 0) {\n            phy_remove_shape(val.shape);\n            val.shape = null;\n            bullets_player.remove(idx);\n            //print(\"borrado\");\n        }\n    }\n\n    foreach(idx, val in bullets_enemy) {\n        val.move(val.pos.x, val.pos.y + 30);\n        if (val.pos.y > 1280) {\n            phy_remove_shape(val.shape);\n            val.shape = null;\n            bullets_enemy.remove(idx);\n            //print(\"borrado\");\n        }\n    }\n\n    if (next_player_bullet <= get_time()) {\n        next_player_bullet = get_time() + 1000;\n        //print(\"shoot\");\n        player_shoot(x_, 1080);\n    }\n}\n\nfunction draw() {\n    draw_asset(background);\n\n    foreach(idx, val in bullets_player) {\n        move_asset(bullet, val.pos.x, val.pos.y);\n        draw_asset(bullet);\n    }\n\n    foreach(idx, val in bullets_enemy) {\n        move_asset(bullet, val.pos.x, val.pos.y);\n        draw_asset(bullet);\n    }\n\n    if (game_over == false) {\n        draw_asset(player);\n    }\n\n    //we draw all enemies\n    foreach(idx, val in enemies) {\n        move_asset(enemy_asset, val.pos.x, val.pos.y);\n        draw_asset(enemy_asset);\n    }\n\n    draw_text_center(720 / 2, 35, 3, puntos);\n\n}";
        }
        else if (gNavi->current_project->game_type_ == -999) //gato
        {
            gNavi->current_project->pjct_script= "local gato_x;\nlocal gato_o;\nlocal cross_line;\nlocal background;\nlocal turn;\nlocal game_over;\n\n//number of x or o on the board\nlocal marks;\n\nlocal fichas = [0, 0, 0, 0, 0, 0, 0, 0, 0];\n\nfunction init() {\n    //we load the assets in here\n    background = load_asset(1, true);\n    gato_x = load_asset(10, true);\n    gato_o = load_asset(11, true);\n    cross_line = load_asset(12, true);\n\n    if (background != null)\n        move_asset(background, 720 / 2, 1280 / 2);\n\n    //finish_game_with_error(\"Errrostototote\");}\n\n    add_click_area(\"click_1\", 170, 430, 160, 160, click);\n    add_click_area(\"click_2\", 170 + 200, 430, 160, 160, click);\n    add_click_area(\"click_3\", 170 + 400, 430, 160, 160, click);\n    add_click_area(\"click_4\", 170, 630, 160, 160, click);\n    add_click_area(\"click_5\", 170 + 200, 630, 160, 160, click);\n    add_click_area(\"click_6\", 170 + 400, 630, 160, 160, click);\n    add_click_area(\"click_7\", 170, 840, 160, 160, click);\n    add_click_area(\"click_8\", 170 + 200, 840, 160, 160, click);\n    add_click_area(\"click_9\", 170 + 400, 840, 160, 160, click);\n\n    turn = random_int(1, 2);\n\n\n}\n\nfunction start_level(level_num) {\n    fichas = [0, 0, 0, 0, 0, 0, 0, 0, 0];\n    marks = 0;\n    game_over = false;\n}\n\nfunction update() {\n    //not needed for this game\n}\n\nfunction click(pos_x, pos_y, name) {\n\n    local val = substr(name, 6, 1).tointeger() - 1;\n    if (fichas[val] != 0)\n        return;\n\n    if (turn == 1)\n    play_effect(1);\n   else\n   play_effect(2);\n\n    marks++;\n    fichas[val] = turn;\n    turn++;\n    if (turn > 2) {\n        turn = 1;\n    }\n\n    print(\"check_win()\" + check_win());\n\n\n    if (check_win()) {\n        game_over = true;\n        goto_menu_after_ms(\"gameover\", 1250);\n        play_effect(3);\n    } else if (marks == 9) { //draw\n        game_over = true;\n        goto_menu(\"gameover\");\n        play_effect(4);\n    }\n}\n\nfunction check_win() {\n    if (fichas[0] != 0 && fichas[0] == fichas[1] && fichas[0] == fichas[2]) //linea arriba\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2 - 200);\n        rotate_asset(cross_line, 0);\n        return true;\n    } else if (fichas[3] != 0 && fichas[3] == fichas[4] && fichas[3] == fichas[5]) //linea medio\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2);\n        rotate_asset(cross_line, 0);\n        return true;\n    } else if (fichas[6] != 0 && fichas[6] == fichas[7] && fichas[6] == fichas[8]) //linea abajo\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2 + 200);\n        rotate_asset(cross_line, 0);\n        return true;\n    } else if (fichas[0] != 0 && fichas[0] == fichas[4] && fichas[0] == fichas[8]) //diagonal \\\\ .\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2);\n        rotate_asset(cross_line, 45);\n        return true;\n    } else if (fichas[2] != 0 && fichas[2] == fichas[4] && fichas[2] == fichas[6]) //diagonal /\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2);\n        rotate_asset(cross_line, 135);\n        return true;\n    } else if (fichas[0] != 0 && fichas[0] == fichas[3] && fichas[0] == fichas[6]) //vertical 1\n    {\n        move_asset(cross_line, 720 / 2 - 200, 1280 / 2);\n        rotate_asset(cross_line, 90);\n        return true;\n    } else if (fichas[1] != 0 && fichas[1] == fichas[4] && fichas[1] == fichas[7]) //vertical 2\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2);\n        rotate_asset(cross_line, 90);\n        return true;\n    } else if (fichas[2] != 0 && fichas[2] == fichas[5] && fichas[2] == fichas[8]) //vertical 3\n    {\n        move_asset(cross_line, 720 / 2 + 200, 1280 / 2);\n        rotate_asset(cross_line, 90);\n        return true;\n    }\n\n    return false;\n}\n\nfunction draw() {\n    draw_asset(background);\n\n    local i = 0;\n    local x = 170;\n    local y = 430;\n    for (i = 0; i < 9; i++) {\n        if (i % 3 == 0 && i != 0) {\n            x = 170;\n            y += 200;\n        }\n        if (fichas[i] == 1) {\n            move_asset(gato_x, x, y);\n            draw_asset(gato_x);\n        } else if (fichas[i] == 2) {\n            move_asset(gato_o, x, y);\n            draw_asset(gato_o);\n        }\n        x += 200;\n    }\n\n    if (game_over == true) {\n        draw_asset(cross_line);\n    }\n}";
        }
        else if (gNavi->current_project->game_type_ == 1022) //flappy
        {
            gNavi->current_project->pjct_script="local game_over;\nlocal background;\nlocal char;\nlocal char_shape;\nlocal wall;\nlocal walls = [];\nlocal wall_height;\nlocal wall_w;\nlocal puntos = 0;\nclass wall_data {\n    constructor() {\n\n    }\n    shape_top = null;\n    shape_bot = null;\n    passed_char = false;\n\n}\n\n\n\nfunction init() {\n\n    background = load_asset(1, true);\n    if (background != null)\n        move_asset(background, 720 / 2, 1280 / 2);\n    char = load_asset(2, true);\n    wall = load_asset(3, true);\n    phy_set_gravity(0, 980);\n    move_asset(char, 720 / 2, 1280 / 2);\n    char_shape = phy_create_shape_for_asset(char, false);\n    phy_shape_set_collision_type(char_shape, 1);\n\n    wall_height = asset_get_height(wall);\n    wall_w = asset_get_width(wall);\n\n    add_click_area(\"click_\", 360, 1280 / 2, 720, 1280, click);\n    phy_create_collision_handler_begin(1, 2, collision);\n\n}\n\nfunction click(pos_x, pos_y, name) {\n    phy_shape_set_velocity(char_shape, 0, 0);\n    phy_shape_add_impulse(char_shape, 0, -500);\n    play_effect(1);\n}\n\nfunction start_level(level_num) {\n    generate_walls();\n    phy_shape_set_angle(char_shape, 0);\n    phy_reset_forces(char_shape);\n    phy_move_shape(char_shape, 720 / 2, 1280 / 2);\n    phy_shape_set_velocity(char_shape, 0, 0);\n    phy_shape_add_impulse(char_shape, 0, -600);\n\n    foreach(idx, val in walls) {\n        phy_remove_shape(val.shape_top);\n        phy_remove_shape(val.shape_bot);\n    }\n\n    walls = [];\n    puntos = 0;\n}\n\nlocal steps = 0;\n\nfunction update() {\n    phy_shape_set_angle(char_shape, 0);\n    //not needed for this game\n    foreach(idx, val in walls) {\n\n        local v = phy_get_shape_position(val.shape_top);\n        phy_move_shape(val.shape_top, v.x - 6.5, v.y);\n\n\n        local v = phy_get_shape_position(val.shape_bot);\n        phy_move_shape(val.shape_bot, v.x - 6.5, v.y);\n\n        if (v.x < 300 && val.passed_char == false) {\n            val.passed_char = true;\n            puntos += 1;\n        }\n\n\n\n    }\n    steps++;\n    if (steps == 75) {\n        steps = 0;\n        generate_walls();\n    }\n\n    foreach(idx, val in walls) {\n\n        local v = phy_get_shape_position(val.shape_top);\n        if (v.x < 0 - wall_w / 2.0) {\n            phy_remove_shape(val.shape_top);\n            phy_remove_shape(val.shape_bot);\n            walls.remove(idx);\n            return;\n        }\n\n    }\n\n    local v = phy_get_shape_position(char_shape);\n    if (v.y < 0 || v.y > 1280) {\n    play_effect(2);\n        goto_menu_after_ms(\"gameover\", 1250);\n    }\n}\n\nfunction generate_walls() {\n\n    local diff_y = random_int(-400, 400);\n\n    local e = wall_data();\n\n    move_asset(wall, 1000, -15 + diff_y);\n    e.shape_top = phy_create_shape_for_asset(wall, true);\n\n\n    move_asset(wall, 1000, 1300 + diff_y);\n    e.shape_bot = phy_create_shape_for_asset(wall, true);\n    //phy_shape_set_collision_type(e.shape, 2);\n\n    phy_shape_set_collision_type(e.shape_top, 2);\n    phy_shape_set_collision_type(e.shape_bot, 2);\n\n    walls.push(e);\n}\n\nfunction collision(shape_1, shape_2) {\n    print(\"game over\");\n    goto_menu_after_ms(\"gameover\", 1250);\n    save_highscore_gameover(puntos);\n    play_effect(2);\n}\n\n\nfunction draw() {\n    draw_asset(background);\n    local v = phy_get_shape_position(char_shape);\n    move_asset(char, v.x, v.y);\n    draw_asset(char);\n\n    foreach(idx, val in walls) {\n\n        local v = phy_get_shape_position(val.shape_top);\n        move_asset(wall, v.x, v.y);\n        rotate_asset(wall, 180);\n        draw_asset(wall);\n\n        local v = phy_get_shape_position(val.shape_bot);\n        rotate_asset(wall, 0);\n        move_asset(wall, v.x, v.y);\n        draw_asset(wall);\n    }\n\n    draw_text_center(720 / 2, 35, 3, puntos);\n\n\n}";
        }
        else if (gNavi->current_project->game_type_ == 1021) //memorama
        {
            gNavi->current_project->pjct_script = "local game_over;\nlocal background;\nlocal piece_closed;\nlocal piece_1;\nlocal piece_2;\nlocal piece_3;\nlocal piece_4;\nlocal piece_5;\nlocal piece_6;\nlocal map;\nlocal hidden;\nlocal can_click = 1;\nlocal start_time = 0;\nlocal tiempo = 0;\n\nfunction init() {\n\n    background = load_asset(1, true);\n    if (background != null)\n        move_asset(background, 720 / 2, 1280 / 2);\n\n    piece_closed = load_asset(2, true);\n    piece_1 = load_asset(11, true);\n    piece_2 = load_asset(12, true);\n    piece_3 = load_asset(13, true);\n    piece_4 = load_asset(14, true);\n    piece_5 = load_asset(15, true);\n    piece_6 = load_asset(16, true);\n\n    local x = 0;\n    local y = 0;\n    for (local i = 0; i < 12; i++) {\n        add_click_area(\"click_\" + i, 144 + 215 * x, 220 + y * 256, asset_get_width(piece_closed), asset_get_height(piece_closed), click);\n        x++;\n        if (x >= 3) {\n            x = 0;\n            y++;\n        }\n    }\n}\n\nfunction check_win() {\n    local put = 0;\n\n    while (put < 12) {\n        if (map[put] != 2) {\n            return false;\n        }\n        put++;\n    }\n    return true;\n}\n\n\n\nlocal del_1;\nlocal del_2;\n\nfunction close_after_fail() {\n    print(\"loool!\");\n    map[del_1] = 0;\n    map[del_2] = 0;\n    can_click = 1;\n}\n\nlocal last = 0;\nlocal last_pos = -1;\n\nfunction click(pos_x, pos_y, name) {\n    if (can_click == 0)\n        return;\n    \n    local val = substr(name, 6, 5).tointeger();\n    if (map[val] == 0) {\n        map[val] = 1;\n        if (last_pos == -1) {\n            last_pos = val;\n            last = hidden[val] - 1;\n            play_effect(1);\n        } else {\n            //ya le dimos last_pos es el segundo click\n            local v = last / 2;\n\n            if (last % 2 != 0) {\n                v = (last - 1) / 2;\n            }\n            print(\"l \" + last + \" \" + last % 2 + \" \" + v);\n\n            last = hidden[val] - 1;\n            local v2 = last / 2;\n            if (last % 2 != 0) {\n                v2 = (last - 1) / 2;\n            }\n\n            print(v + \" \" + v2);\n\n            if (v == v2) {\n                map[val] = 2;\n                map[last_pos] = 2;\n                play_effect(2);\n                if (check_win() == true) {\n                    game_over = true;\n                    goto_menu_after_ms(\"gameover\", 1250);\n                    print(\"gameover\");\n                    tiempo = 1000 - floor((get_time() - start_time) / 750);\n                    if (tiempo < 0)\n                        tiempo = 0;\n                    save_highscore_gameover(tiempo);\n                }\n            } else {\n            play_effect(1);\n                call_function_after_time(close_after_fail, 1000);\n                can_click = 0;\n                //map[val] = 0;\n                //map[last_pos] = 0;\n                del_1 = val;\n                del_2 = last_pos;\n            }\n\n            last = 0;\n            last_pos = -1;\n        }\n    }\n}\n\nfunction start_level(level_num) {\n    map = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];\n\n\n    //generamos el random\n    hidden = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];\n\n    local put = 0;\n\n    while (put < 12) {\n        map[put] = 0;\n        hidden[put] = 0;\n        put++;\n\n    }\n\n    local put = 0;\n    local cosa = 1;\n    while (put < 12) {\n        local val = random_int(0, 11);\n        if (hidden[val] == 0) {\n            put++;\n            hidden[val] = cosa;\n            cosa++;\n        }\n    }\n\n    //print(hidden[0] + \" \" + hidden[1] + \" \"+ hidden[2]);\n    //print(hidden[3] + \" \" + hidden[4] + \" \"+ hidden[5]);\n    //print(hidden[6] + \" \" + hidden[7] + \" \"+ hidden[8]);\n    //print(hidden[9] + \" \" + hidden[10] + \" \"+ hidden[11]);\n\n\n    game_over = false;\n    start_time = get_time();\n}\n\nfunction update() {\n    //not needed for this game\n}\n\n\nfunction draw() {\n    draw_asset(background);\n\n    local x = 0;\n    local y = 0;\n    for (local i = 0; i < 12; i++) {\n        if (map[i] == 0) {\n            move_asset(piece_closed, 144 + 215 * x, 220 + y * 256);\n            draw_asset(piece_closed);\n        } else {\n            if (hidden[i] == 1 || hidden[i] == 2) {\n                move_asset(piece_1, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_1);\n            } else if (hidden[i] == 3 || hidden[i] == 4) {\n                move_asset(piece_2, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_2);\n            } else if (hidden[i] == 5 || hidden[i] == 6) {\n                move_asset(piece_3, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_3);\n            } else if (hidden[i] == 7 || hidden[i] == 8) {\n                move_asset(piece_4, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_4);\n            } else if (hidden[i] == 9 || hidden[i] == 10) {\n                move_asset(piece_5, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_5);\n            } else if (hidden[i] == 11 || hidden[i] == 12) {\n                move_asset(piece_6, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_6);\n            }\n        }\n        x++;\n        if (x >= 3) {\n            x = 0;\n            y++;\n        }\n    }\n    if (game_over == false) {\n        tiempo = 1000 - floor((get_time() - start_time) / 750);\n        if (tiempo < 0)\n            tiempo = 0;\n    }\n    draw_text_center(720 / 2, 35, 3, tiempo);\n}";
        }
        else
        {

            gNavi->current_project->pjct_script = gNavi->naviDecrypt(cJSON_GetObjectItem(gameData, "script")->valuestring);
            //gNavi->current_project->pjct_script = getGameScript(gNavi->current_project->game_type_);
        }
    }
    //gNavi->nrmSpecifics[gNavi->getCurrentSpecificationIndex()]->modifyGameProject(*gNavi->current_project);
    g_script_enviroment->load_game_from_raw(gNavi->current_project->pjct_script);
    }


void NaVi::setGame(int gameID,cJSON* gameData, std::string script)
{
    SDL_Log("set game");
    for(int i = 0; i < gNavi->projects_.size(); i++)
    {
        if(gNavi->projects_[i].id == gameID)
        {
            gNavi->current_project = new project(gNavi->projects_[i]);
            break;
        }
    }
    g_game->game_id = gNavi->current_project->id;
    g_game->isAudioAvailable = (gNavi->current_project->audioActivated == 1);
    g_game->init_game();
    if(gNavi->current_project->pjct_script == "")
    {
        if (gNavi->current_project->game_type_ == 1) //jumper
        {
            gNavi->current_project->pjct_script = "local width = 720;\nlocal height = 1280;\n\nlocal character;\nlocal background;\nlocal a_platform;\nlocal move_platform;\nlocal foreground;\nlocal fake_platform;\nlocal one_use_platform;\nlocal jump_item;\nlocal score;\nlocal gravity = 0.9;\nlocal has_moving_platform;\nlocal has_fake_platform;\nlocal has_one_use_platform;\nlocal has_jump_item;\n\nlocal base_y;\n\nlocal item_height;\n\nlocal player_vx;\nlocal player_vy;\nlocal player_width;\nlocal player_height;\nlocal player_is_moving_left;\nlocal player_is_moving_right;\nlocal player_x;\nlocal player_y;\nlocal position;\nlocal there_is_jump_item;\nlocal platforms = [];\n\nlocal score_position;\nlocal score_color;\nlocal font_size;\n\nlocal was_moving;\nlocal num_platforms = 8;\n\nlocal broken;\nlocal char_shape;\n\nclass Platform\n{\npWidth = null;\npHeight = null;\npX = null;\npY = null;\npFlag = null;\npState = null;\npType = null;\npMoved = null;\npVx = null;\npJump_item = null;\npShape = null;\npShape_item = null;\n\nconstructor(p_c)\n{\npState = 0;\npFlag = 0;\npWidth = asset_get_width(a_platform);\npHeight = asset_get_height(a_platform);\npX = random_int(0,width+1);\npY = position;\nposition += height / p_c;\npType = 0;\npJump_item = false;\npMoved = 0;\npVx = 8;\nsetType();\nevaluateBroken();\nsetItem();\npShape = phy_create_rect_shape(pX,pY,pWidth,pHeight,true);\nphy_set_sensor(pShape,true);\nphy_shape_set_collision_type(pShape,2);\n}\n\nfunction setType()\n{\nif(score > 300)\n{\nif(has_moving_platform)\n{\npType = random_int(0,2);\n}\nelse\n{\npType = 0;\n}\n}\nif(score > 700)\n{\nif(has_moving_platform && has_fake_platform)\n{\npType = random_int(0,3);\n}\nelse if(has_moving_platform)\n{\npType = random_int(0,2);\n}\nelse if(has_fake_platform)\n{\npType = random_int(0,2);\nif(pType == 1)\n{\npType = 2;\n}\n}\nelse\n{\npType = 0;\n}\n}\nif(score > 1200)\n{\nif(has_moving_platform && has_fake_platform && has_one_use_platform)\n{\npType = random_int(0,4);\n}\nelse if(has_moving_platform && has_one_use_platform)\n{\npType = random_int(0,3); \nif(pType == 2)\n{\npType = 3;\n}\n}\nelse if(has_fake_platform && has_one_use_platform)\n{\npType = random_int(0,3);\nif(pType == 1)\n{\npType = 3;\n}\n}\nelse if(has_moving_platform && has_fake_platform)\n{\npType = random_int(0,3);\n}\nelse if(has_moving_platform)\n{\npType = random_int(0,2);\n}\nelse if(has_fake_platform)\n{\npType = random_int(0,2);\nif (pType == 1)\n{\npType = 2;\n}\n}\nelse if(has_one_use_platform)\n{\npType = random_int(0,2);\nif(pType == 1)\n{\npType = 3;\n}\n}\nelse\n{\npType = 0;\n}\n}\n}\n\nfunction evaluateBroken()\n{\nif (pType == 2 && broken < 1)\n{\nbroken++;\n}\nelse if (pType == 2 && broken >= 1)\n{\npType = 0;\nbroken = 0;\n}\n}\n\nfunction setItem()\n{\nif (has_jump_item && (pType == 0 || pType == 1) && !there_is_jump_item)\n{\nlocal res =random_int(0,100);\nif (res < 10)\n{\nthere_is_jump_item = true;\npJump_item = true;\npShape_item = phy_create_rect_shape(pX,pY-(pWidth*0.5),asset_get_width(jump_item),asset_get_height(jump_item),true);\nphy_set_sensor(pShape_item,true);\nphy_shape_set_collision_type(pShape_item,3);\n}\n}\n}\n}\n\nfunction init()\n{\nwas_moving = 0;\nbackground = load_asset(1,false);\nmove_asset(background,width*0.5,height*0.5);\nforeground = load_asset(2,true);\nmove_asset(foreground,width*0.5,height*0.5);\n\ncharacter = load_asset(3,false);\na_platform = load_asset(10,false);\n\ninitCustomPlatforms();\n}\n\nfunction initCustomPlatforms()\n{\nmove_platform = load_asset(11,true);\nhas_moving_platform = false;\nif (move_platform != null)\n{\nhas_moving_platform = true;\n}\n\nfake_platform = load_asset(12,true);\nhas_fake_platform = false;\nif (fake_platform != null)\n{\nhas_fake_platform = true;\n}\n\none_use_platform = load_asset(13,true);\nhas_one_use_platform = false;\nif (one_use_platform != null)\n{\nhas_one_use_platform = true;\n}\n\njump_item = load_asset(14,true);\nhas_jump_item = false;\nif (jump_item != null)\n{\nhas_jump_item = true;\nitem_height = asset_get_height(jump_item);\n}\n}\n\nfunction start_level(level_id)\n{\nscore = 0;\nbase_y = height-1;\nplayer_vx = 0;\nplayer_vy = 0.01;\nplayer_height = asset_get_height(character);\nplayer_width = asset_get_width(character);\nplayer_is_moving_left = false;\nplayer_is_moving_right = false;\nplayer_x = width * 0.5;\nplayer_y = height -(player_height * 0.5);\nmove_asset(character,player_x,player_y);\nbroken = 0;\nposition = 0;\nthere_is_jump_item = false;\nplatforms.clear();\n\nfor(local i = 0;i < num_platforms;i++)\n{\n        platforms.push(Platform(num_platforms));\n}\n\n    char_shape = phy_create_shape_for_asset(character,false);\n    phy_set_sensor(char_shape,true);\n    phy_shape_set_collision_type(char_shape,1);\n\n    phy_create_collision_handler_begin(1,2,begin);\n    phy_create_collision_handler_end(1,2,colliding);\n    phy_create_collision_handler_begin(1,3,begin);\n    phy_create_collision_handler_end(1,3,colliding);\n}\n\nfunction update()\n{\nif(player_is_moving_left)\n{\nplayer_vx -= 0.8;\n}\nelse if(player_vx < 0)\n{\nplayer_vx += 0.8;\nif(player_vx > 0)\n{\nplayer_vx = 0;\n}\n}\nif(player_is_moving_right)\n{\nplayer_vx += 0.8;\n}\nelse if(player_vx > 0)\n{\nplayer_vx -= 0.8;\nif(player_vx < 0)\n{\nplayer_vx = 0;\n}\n}\nif(player_vx < -20)\n{\nplayer_vx = -20;\n}\nif(player_vx > 20)\n{\nplayer_vx = 20;\n}\n    player_x += player_vx;\nif((player_y + player_height) > base_y && base_y <= height)\n{\njump();\n}\nif(base_y > height && (player_y + player_height) > height)\n{\ngame_over();\nreturn;\n    }\nif(player_x > width)\n{\nplayer_x = -player_width;\n}\nelse if(player_x < -player_width)\n{\nplayer_x = width;\n}\nif(player_y >= ((height*0.5)-(player_height*0.5)))\n{\nplayer_y += player_vy;\nplayer_vy += gravity;\n}\nelse\n{\nfor(local i = 0;i < platforms.len();i++)\n{\nif(player_vy < 0)\n{\nplatforms[i].pY -= player_vy;\n}\nif(platforms[i].pY > height)\n{\nif(platforms[i].pJump_item)\n{\nthere_is_jump_item = false;\n}\nlocal _y = platforms[i].pY;\nplatforms.remove(i);\ni--;\nplatforms.push(Platform(num_platforms));\nplatforms[platforms.len()-1].pY = _y - height;\n}\n}\nbase_y -= player_vy;\nplayer_vy += gravity;\nif(player_vy >= 0)\n{\nplayer_y += player_vy;\nplayer_vy += gravity;\n}\nscore++;\n}\nif(player_vx < 0 && was_moving == 0)\n{\nflip_asset(character,true);\nwas_moving = -1;\n}\nelse if(player_vx > 0 && was_moving == -1)\n{\nflip_asset(character,false);\nwas_moving = 0;\n}\nfor(local i = 0;i < platforms.len();i++)\n{\nif(platforms[i].pType == 1)\n{\nif(platforms[i].pX < 0 || platforms[i].pX > width)\n{\nplatforms[i].pVx *= -1;\n}\nplatforms[i].pX += platforms[i].pVx;\n}\nphy_move_shape(platforms[i].pShape,platforms[i].pX,platforms[i].pY);\nif(platforms[i].pShape_item != null)\n{\nphy_move_shape(platforms[i].pShape_item,platforms[i].pX * 0.5,platforms[i].pY * 0.5);\n}\n}\nmove_asset(character,player_x,player_y+(player_height*0.5));\nphy_move_shape(char_shape,player_x,player_y+(player_height*0.5));\n}\n\nfunction draw()\n{\ndraw_asset(background);\n\nfor(local i = 0;i  < platforms.len();i++)\n{\nif(platforms[i].pState == 1)\n{\ncontinue;\n}\nif(platforms[i].pType == 0)\n{\nmove_asset(a_platform,platforms[i].pX,platforms[i].pY);\ndraw_asset(a_platform);\n}\nelse if(platforms[i].pType == 1)\n{\nmove_asset(move_platform,platforms[i].pX,platforms[i].pY);\ndraw_asset(move_platform);\n}\nelse if(platforms[i].pType == 2)\n{\nmove_asset(fake_platform,platforms[i].pX,platforms[i].pY);\ndraw_asset(fake_platform);\n}\nelse if(platforms[i].pType == 3)\n{\nmove_asset(one_use_platform,platforms[i].pX,platforms[i].pY);\ndraw_asset(one_use_platform);\n}\nif(platforms[i].pJump_item && has_jump_item)\n{\nif(platforms[i].pType == 0)\n{\nmove_asset(jump_item,platforms[i].pX,platforms[i].pY-(asset_get_height(a_platform)*0.5)-(asset_get_height(jump_item)*0.5));\n}\nelse\n{\nmove_asset(jump_item,platforms[i].pX,platforms[i].pY-(asset_get_height(move_platform)*0.5)-(asset_get_height(jump_item)*0.5));\n}\ndraw_asset(jump_item);\n}\n}\nif(foreground != null)\n{\ndraw_asset(foreground);\n}\ndraw_asset(character);\n\ndraw_text_center(width*0.5,height*0.01,2,score);\n}\n\nfunction game_over()\n{\nplatforms.clear();\nsave_highscore_gameover(score);\n    goto_menu_after_ms(\"gameover\", 1250);\n}\n\nfunction get_arrow_keys(key,state)\n{\n    if (key == 0 && state == 1)\n{\nplayer_is_moving_left = true;\n}\n    else if (key == 0)\n{\nplayer_is_moving_left = false;\n}\n    if (key == 1 && state == 1)\n{\nplayer_is_moving_right = true;\n}\n    else if (key == 1)\n{\nplayer_is_moving_right = false;\n}\n}\n\nfunction on_accelerometer_update(x,y,z) \n{\nif (x < -0.1)\n{\nplayer_vx = x*3;\n}\nelse if (x > 0.1)\n{\nplayer_vx = x*3;\n}\nif(player_vx > 8)\n{\nplayer_vx = 8;\n}\nelse if(player_vx < -8)\n{\nplayer_vx = -8;\n}\n}\n\nfunction jump()\n{\nplay_effect(1);\nplayer_vy = -27;\n}\n\nfunction jumpHigh()\n{\nplay_effect(2);\nplayer_vy = -36.66*2;\n}\n\nfunction begin(shape_1,shape_2)\n{\nif(player_vy < 0)\n{\nreturn 1;\n}\nfor(local i = 0;i < platforms.len();i++)\n{\nif(compare_shapes(platforms[i].pShape,shape_2))\n{\nif(platforms[i].pType == 2 || platforms[i].pType == 3)\n{\n    if(platforms[i].pState != 1)\n    {\n                    play_effect(3);\n    }\nplatforms[i].pState = 1;\n}\nif(platforms[i].pType != 2)\n{\njump();\n}\nbreak;\n}\nelse if(platforms[i].pShape_item != null)\n{\nif(compare_shapes(platforms[i].pShape_item,shape_2))\n{\njumpHigh();\n}\n}\n}\n}\n\nfunction colliding(shape_1,shape_2)\n{\nif(player_vy < 0)\n{\nreturn 1;\n}\nfor(local i = 0;i < platforms.len();i++)\n{\nif(compare_shapes(platforms[i].pShape,shape_2))\n{\nif(platforms[i].pType == 2 || platforms[i].pType == 3)\n{\n    if(platforms[i].pState != 1)\n    {\n                    play_effect(3);\n    }\nplatforms[i].pState = 1;\n}\nif(platforms[i].pType != 2)\n{\njump();\n}\nbreak;\n}\nelse if(platforms[i].pShape_item != null)\n{\nif(compare_shapes(platforms[i].pShape_item,shape_2))\n{\njumpHigh();\n}\n}\n}\n}";
        }
        else if (gNavi->current_project->game_type_ == 2) //shoot-them-up
        {
            gNavi->current_project->pjct_script = "local enemy_asset;\nlocal player;\nlocal bullet;\nlocal background;\nlocal game_over;\nlocal enemyasset_h;\nlocal playerasset_h;\nlocal playerasset_w;\n\nlocal is_pressing_left = false;\nlocal is_pressing_right = false;\nlocal next_player_bullet = 0;\nlocal puntos = 0;\n\nlocal enemigos_count = 0;\n\n//enemy array\nclass enemy {\n    constructor() {\n        pos = vector_2();\n        pos.x = random_int(100, 620);\n        pos.y = -50;\n        next_enemy_bullet = get_time() + random_int(1500, 4000);\n        goto_y = random_int(200, 450);\n        enemigos_count = enemigos_count + 1;\n    }\n    pos = null;\n    shape = null;\n    next_enemy_bullet = 0;\n    goto_y = 100;\n\n    function move(new_x, new_y) {\n        pos.x = new_x;\n        pos.y = new_y;\n        if (shape != null)\n            phy_move_shape(shape, pos.x, pos.y - enemyasset_h);\n    }\n\n}\n\nclass bullet_obj {\n    constructor() {\n        pos = vector_2();\n        pos.x = 0;\n        pos.y = 0;\n    }\n    pos = null;\n    shape = null;\n\n    function move(new_x, new_y) {\n        pos.x = new_x;\n        pos.y = new_y;\n        if (shape != null)\n            phy_move_shape(shape, pos.x, pos.y);\n    }\n\n}\n\n//empty array\nlocal enemies = [];\nlocal bullets_player = [];\nlocal bullets_enemy = [];\n\nlocal x_shape = null;\nlocal x_;\n\nfunction init() {\n    //phy_set_gravity(0,9.8);\n\n    background = load_asset(1, true);\n    enemy_asset = load_asset(100, true);\n    bullet = load_asset(10, true);\n    player = load_asset(3, true);\n\n    enemyasset_h = asset_get_height(enemy_asset) / 2;\n    playerasset_h = asset_get_height(player) / 2;\n    playerasset_w = asset_get_width(player) / 4;\n\n    if (background != null)\n        move_asset(background, 720 / 2, 1280 / 2);\n\n    //finish_game_with_error(\"Errrostototote\");}\n\n    phy_create_collision_handler_begin(1, 2, collision);\n    phy_create_collision_handler_begin(3, 4, kill_player);\n\n}\n\nfunction collision(shape_1, shape_2) {\n    print(\"inicio colision \");\n\n    foreach(idx, val in enemies) {\n        if (compare_shapes(shape_1, val.shape) || compare_shapes(val.shape, shape_2)) {\n            phy_remove_shape(val.shape);\n            val.shape = null;\n            enemies.remove(idx);\n            enemigos_count--;\n            puntos = puntos + 10;\n            //mate un enemigo\n            play_effect(3);\n\n        }\n    }\n\n    foreach(idx, val in bullets_player) {\n        if (compare_shapes(shape_1, val.shape) || compare_shapes(val.shape, shape_2)) {\n            phy_remove_shape(val.shape);\n            val.shape = null;\n            bullets_player.remove(idx);\n        }\n    }\n}\n\nfunction kill_player(shape_1, shape_2) {\n    print(\"se meure el jugador\");\n\n    game_over = true;\n    save_highscore_gameover(puntos);\n    goto_menu_after_ms(\"gameover\", 1250);\n    play_effect(4);\n\n\n}\n\n//only used on windows. mac or linux\nfunction get_arrow_keys(key, state) {\n    if (key == 0 && state == 1) {\n        is_pressing_left = true;\n    } else if (key == 0) {\n        is_pressing_left = false;\n    } else if (key == 1 && state == 1) {\n        is_pressing_right = true;\n    } else if (key == 1) {\n        is_pressing_right = false;\n    }\n\n}\n\nfunction on_accelerometer_update(x, y, z) {\n    //print(\"Yaay\");\n    is_pressing_right = false;\n    is_pressing_left = false;\n    if (x < 1.5 && x > -1.5)\n        return; //muy lento\n    else if (x < 0) {\n        is_pressing_left = true;\n        //player_vx_ = _x*3;\n    } else if (x > 0) {\n        is_pressing_right = true;\n        //player_vx_ = _x*3;\n    }\n}\n\nfunction start_level(level_num) {\n    game_over = false;\n    foreach(idx, val in enemies) {\n        phy_remove_shape(val.shape);\n        //enemies.remove(idx);\n    }\n\n    foreach(idx, val in bullets_player) {\n        phy_remove_shape(val.shape);\n        //bullets_player.remove(idx);\n    }\n\n    foreach(idx, val in bullets_enemy) {\n        phy_remove_shape(val.shape);\n        //bullets_enemy.remove(idx);\n    }\n\n    enemies.clear();\n    bullets_enemy.clear();\n    bullets_player.clear();\n\n    if (x_shape != null)\n        phy_remove_shape(x_shape);\n\n    move_asset(player, 360, 1080 + playerasset_h);\n    x_shape = phy_create_shape_for_asset(player, true);\n    x_ = 360;\n    phy_shape_set_collision_type(x_shape, 4);\n\n    next_player_bullet = 0;\n    enemigos_count = 0;\n    puntos = 0;\n}\n\nfunction add_enemy() {\n    local e = enemy();\n    e.shape = phy_create_shape_for_asset(enemy_asset, true);\n    phy_shape_set_collision_type(e.shape, 2);\n    e.move(e.pos.x, e.pos.y);\n    enemies.push(e);\n}\n\nfunction player_shoot(start_x, start_y) {\n    local e = bullet_obj();\n    e.shape = phy_create_shape_for_asset(bullet, false);\n    phy_shape_set_collision_type(e.shape, 1);\n    e.move(start_x, start_y);\n    bullets_player.push(e);\n    play_effect(1);\n}\n\nfunction enemy_shoot(start_x, start_y) {\n    local e = bullet_obj();\n    e.shape = phy_create_shape_for_asset(bullet, false);\n    phy_shape_set_collision_type(e.shape, 3);\n    e.move(start_x, start_y);\n    bullets_enemy.push(e);\n    play_effect(2);\n}\n\nfunction update() {\n\n    if (game_over == true) {\n        return;\n    }\n    if (enemigos_count < 3) {\n        add_enemy();\n    }\n\n    if (is_pressing_left == true) {\n        x_ -= 7;\n        if (x_ < 0)\n            x_ = 0;\n        phy_move_shape(x_shape, x_, 1080 + playerasset_h);\n    } else if (is_pressing_right == true) {\n        x_ += 7;\n        if (x_ > 720)\n            x_ = 720;\n        phy_move_shape(x_shape, x_, 1080 + playerasset_h);\n    }\n    move_asset(player, x_, 1080);\n    //we move all enemies\n    foreach(idx, val in enemies) {\n        if (val.pos.y < val.goto_y) {\n            val.move(val.pos.x, val.pos.y + 3);\n        }\n\n        //we shoot bullet\n        if (val.next_enemy_bullet < get_time()) {\n            enemy_shoot(val.pos.x, val.pos.y);\n\n            val.next_enemy_bullet = get_time() + random_int(1500, 4000);\n        }\n    }\n\n    //we move bullets\n    foreach(idx, val in bullets_player) {\n        val.move(val.pos.x, val.pos.y - 30);\n        if (val.pos.y < 0) {\n            phy_remove_shape(val.shape);\n            val.shape = null;\n            bullets_player.remove(idx);\n            //print(\"borrado\");\n        }\n    }\n\n    foreach(idx, val in bullets_enemy) {\n        val.move(val.pos.x, val.pos.y + 30);\n        if (val.pos.y > 1280) {\n            phy_remove_shape(val.shape);\n            val.shape = null;\n            bullets_enemy.remove(idx);\n            //print(\"borrado\");\n        }\n    }\n\n    if (next_player_bullet <= get_time()) {\n        next_player_bullet = get_time() + 1000;\n        //print(\"shoot\");\n        player_shoot(x_, 1080);\n    }\n}\n\nfunction draw() {\n    draw_asset(background);\n\n    foreach(idx, val in bullets_player) {\n        move_asset(bullet, val.pos.x, val.pos.y);\n        draw_asset(bullet);\n    }\n\n    foreach(idx, val in bullets_enemy) {\n        move_asset(bullet, val.pos.x, val.pos.y);\n        draw_asset(bullet);\n    }\n\n    if (game_over == false) {\n        draw_asset(player);\n    }\n\n    //we draw all enemies\n    foreach(idx, val in enemies) {\n        move_asset(enemy_asset, val.pos.x, val.pos.y);\n        draw_asset(enemy_asset);\n    }\n\n    draw_text_center(720 / 2, 35, 3, puntos);\n\n}";
        }
        else if (gNavi->current_project->game_type_ == -999) //gato
        {
            gNavi->current_project->pjct_script= "local gato_x;\nlocal gato_o;\nlocal cross_line;\nlocal background;\nlocal turn;\nlocal game_over;\n\n//number of x or o on the board\nlocal marks;\n\nlocal fichas = [0, 0, 0, 0, 0, 0, 0, 0, 0];\n\nfunction init() {\n    //we load the assets in here\n    background = load_asset(1, true);\n    gato_x = load_asset(10, true);\n    gato_o = load_asset(11, true);\n    cross_line = load_asset(12, true);\n\n    if (background != null)\n        move_asset(background, 720 / 2, 1280 / 2);\n\n    //finish_game_with_error(\"Errrostototote\");}\n\n    add_click_area(\"click_1\", 170, 430, 160, 160, click);\n    add_click_area(\"click_2\", 170 + 200, 430, 160, 160, click);\n    add_click_area(\"click_3\", 170 + 400, 430, 160, 160, click);\n    add_click_area(\"click_4\", 170, 630, 160, 160, click);\n    add_click_area(\"click_5\", 170 + 200, 630, 160, 160, click);\n    add_click_area(\"click_6\", 170 + 400, 630, 160, 160, click);\n    add_click_area(\"click_7\", 170, 840, 160, 160, click);\n    add_click_area(\"click_8\", 170 + 200, 840, 160, 160, click);\n    add_click_area(\"click_9\", 170 + 400, 840, 160, 160, click);\n\n    turn = random_int(1, 2);\n\n\n}\n\nfunction start_level(level_num) {\n    fichas = [0, 0, 0, 0, 0, 0, 0, 0, 0];\n    marks = 0;\n    game_over = false;\n}\n\nfunction update() {\n    //not needed for this game\n}\n\nfunction click(pos_x, pos_y, name) {\n\n    local val = substr(name, 6, 1).tointeger() - 1;\n    if (fichas[val] != 0)\n        return;\n\n    if (turn == 1)\n    play_effect(1);\n   else\n   play_effect(2);\n\n    marks++;\n    fichas[val] = turn;\n    turn++;\n    if (turn > 2) {\n        turn = 1;\n    }\n\n    print(\"check_win()\" + check_win());\n\n\n    if (check_win()) {\n        game_over = true;\n        goto_menu_after_ms(\"gameover\", 1250);\n        play_effect(3);\n    } else if (marks == 9) { //draw\n        game_over = true;\n        goto_menu(\"gameover\");\n        play_effect(4);\n    }\n}\n\nfunction check_win() {\n    if (fichas[0] != 0 && fichas[0] == fichas[1] && fichas[0] == fichas[2]) //linea arriba\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2 - 200);\n        rotate_asset(cross_line, 0);\n        return true;\n    } else if (fichas[3] != 0 && fichas[3] == fichas[4] && fichas[3] == fichas[5]) //linea medio\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2);\n        rotate_asset(cross_line, 0);\n        return true;\n    } else if (fichas[6] != 0 && fichas[6] == fichas[7] && fichas[6] == fichas[8]) //linea abajo\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2 + 200);\n        rotate_asset(cross_line, 0);\n        return true;\n    } else if (fichas[0] != 0 && fichas[0] == fichas[4] && fichas[0] == fichas[8]) //diagonal \\\\ .\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2);\n        rotate_asset(cross_line, 45);\n        return true;\n    } else if (fichas[2] != 0 && fichas[2] == fichas[4] && fichas[2] == fichas[6]) //diagonal /\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2);\n        rotate_asset(cross_line, 135);\n        return true;\n    } else if (fichas[0] != 0 && fichas[0] == fichas[3] && fichas[0] == fichas[6]) //vertical 1\n    {\n        move_asset(cross_line, 720 / 2 - 200, 1280 / 2);\n        rotate_asset(cross_line, 90);\n        return true;\n    } else if (fichas[1] != 0 && fichas[1] == fichas[4] && fichas[1] == fichas[7]) //vertical 2\n    {\n        move_asset(cross_line, 720 / 2, 1280 / 2);\n        rotate_asset(cross_line, 90);\n        return true;\n    } else if (fichas[2] != 0 && fichas[2] == fichas[5] && fichas[2] == fichas[8]) //vertical 3\n    {\n        move_asset(cross_line, 720 / 2 + 200, 1280 / 2);\n        rotate_asset(cross_line, 90);\n        return true;\n    }\n\n    return false;\n}\n\nfunction draw() {\n    draw_asset(background);\n\n    local i = 0;\n    local x = 170;\n    local y = 430;\n    for (i = 0; i < 9; i++) {\n        if (i % 3 == 0 && i != 0) {\n            x = 170;\n            y += 200;\n        }\n        if (fichas[i] == 1) {\n            move_asset(gato_x, x, y);\n            draw_asset(gato_x);\n        } else if (fichas[i] == 2) {\n            move_asset(gato_o, x, y);\n            draw_asset(gato_o);\n        }\n        x += 200;\n    }\n\n    if (game_over == true) {\n        draw_asset(cross_line);\n    }\n}";
        }
        else if (gNavi->current_project->game_type_ == 1022) //flappy
        {
            gNavi->current_project->pjct_script="local game_over;\nlocal background;\nlocal char;\nlocal char_shape;\nlocal wall;\nlocal walls = [];\nlocal wall_height;\nlocal wall_w;\nlocal puntos = 0;\nclass wall_data {\n    constructor() {\n\n    }\n    shape_top = null;\n    shape_bot = null;\n    passed_char = false;\n\n}\n\n\n\nfunction init() {\n\n    background = load_asset(1, true);\n    if (background != null)\n        move_asset(background, 720 / 2, 1280 / 2);\n    char = load_asset(2, true);\n    wall = load_asset(3, true);\n    phy_set_gravity(0, 980);\n    move_asset(char, 720 / 2, 1280 / 2);\n    char_shape = phy_create_shape_for_asset(char, false);\n    phy_shape_set_collision_type(char_shape, 1);\n\n    wall_height = asset_get_height(wall);\n    wall_w = asset_get_width(wall);\n\n    add_click_area(\"click_\", 360, 1280 / 2, 720, 1280, click);\n    phy_create_collision_handler_begin(1, 2, collision);\n\n}\n\nfunction click(pos_x, pos_y, name) {\n    phy_shape_set_velocity(char_shape, 0, 0);\n    phy_shape_add_impulse(char_shape, 0, -500);\n    play_effect(1);\n}\n\nfunction start_level(level_num) {\n    generate_walls();\n    phy_shape_set_angle(char_shape, 0);\n    phy_reset_forces(char_shape);\n    phy_move_shape(char_shape, 720 / 2, 1280 / 2);\n    phy_shape_set_velocity(char_shape, 0, 0);\n    phy_shape_add_impulse(char_shape, 0, -600);\n\n    foreach(idx, val in walls) {\n        phy_remove_shape(val.shape_top);\n        phy_remove_shape(val.shape_bot);\n    }\n\n    walls = [];\n    puntos = 0;\n}\n\nlocal steps = 0;\n\nfunction update() {\n    phy_shape_set_angle(char_shape, 0);\n    //not needed for this game\n    foreach(idx, val in walls) {\n\n        local v = phy_get_shape_position(val.shape_top);\n        phy_move_shape(val.shape_top, v.x - 6.5, v.y);\n\n\n        local v = phy_get_shape_position(val.shape_bot);\n        phy_move_shape(val.shape_bot, v.x - 6.5, v.y);\n\n        if (v.x < 300 && val.passed_char == false) {\n            val.passed_char = true;\n            puntos += 1;\n        }\n\n\n\n    }\n    steps++;\n    if (steps == 75) {\n        steps = 0;\n        generate_walls();\n    }\n\n    foreach(idx, val in walls) {\n\n        local v = phy_get_shape_position(val.shape_top);\n        if (v.x < 0 - wall_w / 2.0) {\n            phy_remove_shape(val.shape_top);\n            phy_remove_shape(val.shape_bot);\n            walls.remove(idx);\n            return;\n        }\n\n    }\n\n    local v = phy_get_shape_position(char_shape);\n    if (v.y < 0 || v.y > 1280) {\n    play_effect(2);\n        goto_menu_after_ms(\"gameover\", 1250);\n    }\n}\n\nfunction generate_walls() {\n\n    local diff_y = random_int(-400, 400);\n\n    local e = wall_data();\n\n    move_asset(wall, 1000, -15 + diff_y);\n    e.shape_top = phy_create_shape_for_asset(wall, true);\n\n\n    move_asset(wall, 1000, 1300 + diff_y);\n    e.shape_bot = phy_create_shape_for_asset(wall, true);\n    //phy_shape_set_collision_type(e.shape, 2);\n\n    phy_shape_set_collision_type(e.shape_top, 2);\n    phy_shape_set_collision_type(e.shape_bot, 2);\n\n    walls.push(e);\n}\n\nfunction collision(shape_1, shape_2) {\n    print(\"game over\");\n    goto_menu_after_ms(\"gameover\", 1250);\n    save_highscore_gameover(puntos);\n    play_effect(2);\n}\n\n\nfunction draw() {\n    draw_asset(background);\n    local v = phy_get_shape_position(char_shape);\n    move_asset(char, v.x, v.y);\n    draw_asset(char);\n\n    foreach(idx, val in walls) {\n\n        local v = phy_get_shape_position(val.shape_top);\n        move_asset(wall, v.x, v.y);\n        rotate_asset(wall, 180);\n        draw_asset(wall);\n\n        local v = phy_get_shape_position(val.shape_bot);\n        rotate_asset(wall, 0);\n        move_asset(wall, v.x, v.y);\n        draw_asset(wall);\n    }\n\n    draw_text_center(720 / 2, 35, 3, puntos);\n\n\n}";
        }
        else if (gNavi->current_project->game_type_ == 1021) //memorama
        {
            gNavi->current_project->pjct_script = "local game_over;\nlocal background;\nlocal piece_closed;\nlocal piece_1;\nlocal piece_2;\nlocal piece_3;\nlocal piece_4;\nlocal piece_5;\nlocal piece_6;\nlocal map;\nlocal hidden;\nlocal can_click = 1;\nlocal start_time = 0;\nlocal tiempo = 0;\n\nfunction init() {\n\n    background = load_asset(1, true);\n    if (background != null)\n        move_asset(background, 720 / 2, 1280 / 2);\n\n    piece_closed = load_asset(2, true);\n    piece_1 = load_asset(11, true);\n    piece_2 = load_asset(12, true);\n    piece_3 = load_asset(13, true);\n    piece_4 = load_asset(14, true);\n    piece_5 = load_asset(15, true);\n    piece_6 = load_asset(16, true);\n\n    local x = 0;\n    local y = 0;\n    for (local i = 0; i < 12; i++) {\n        add_click_area(\"click_\" + i, 144 + 215 * x, 220 + y * 256, asset_get_width(piece_closed), asset_get_height(piece_closed), click);\n        x++;\n        if (x >= 3) {\n            x = 0;\n            y++;\n        }\n    }\n}\n\nfunction check_win() {\n    local put = 0;\n\n    while (put < 12) {\n        if (map[put] != 2) {\n            return false;\n        }\n        put++;\n    }\n    return true;\n}\n\n\n\nlocal del_1;\nlocal del_2;\n\nfunction close_after_fail() {\n    print(\"loool!\");\n    map[del_1] = 0;\n    map[del_2] = 0;\n    can_click = 1;\n}\n\nlocal last = 0;\nlocal last_pos = -1;\n\nfunction click(pos_x, pos_y, name) {\n    if (can_click == 0)\n        return;\n    \n    local val = substr(name, 6, 5).tointeger();\n    if (map[val] == 0) {\n        map[val] = 1;\n        if (last_pos == -1) {\n            last_pos = val;\n            last = hidden[val] - 1;\n            play_effect(1);\n        } else {\n            //ya le dimos last_pos es el segundo click\n            local v = last / 2;\n\n            if (last % 2 != 0) {\n                v = (last - 1) / 2;\n            }\n            print(\"l \" + last + \" \" + last % 2 + \" \" + v);\n\n            last = hidden[val] - 1;\n            local v2 = last / 2;\n            if (last % 2 != 0) {\n                v2 = (last - 1) / 2;\n            }\n\n            print(v + \" \" + v2);\n\n            if (v == v2) {\n                map[val] = 2;\n                map[last_pos] = 2;\n                play_effect(2);\n                if (check_win() == true) {\n                    game_over = true;\n                    goto_menu_after_ms(\"gameover\", 1250);\n                    print(\"gameover\");\n                    tiempo = 1000 - floor((get_time() - start_time) / 750);\n                    if (tiempo < 0)\n                        tiempo = 0;\n                    save_highscore_gameover(tiempo);\n                }\n            } else {\n            play_effect(1);\n                call_function_after_time(close_after_fail, 1000);\n                can_click = 0;\n                //map[val] = 0;\n                //map[last_pos] = 0;\n                del_1 = val;\n                del_2 = last_pos;\n            }\n\n            last = 0;\n            last_pos = -1;\n        }\n    }\n}\n\nfunction start_level(level_num) {\n    map = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];\n\n\n    //generamos el random\n    hidden = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];\n\n    local put = 0;\n\n    while (put < 12) {\n        map[put] = 0;\n        hidden[put] = 0;\n        put++;\n\n    }\n\n    local put = 0;\n    local cosa = 1;\n    while (put < 12) {\n        local val = random_int(0, 11);\n        if (hidden[val] == 0) {\n            put++;\n            hidden[val] = cosa;\n            cosa++;\n        }\n    }\n\n    //print(hidden[0] + \" \" + hidden[1] + \" \"+ hidden[2]);\n    //print(hidden[3] + \" \" + hidden[4] + \" \"+ hidden[5]);\n    //print(hidden[6] + \" \" + hidden[7] + \" \"+ hidden[8]);\n    //print(hidden[9] + \" \" + hidden[10] + \" \"+ hidden[11]);\n\n\n    game_over = false;\n    start_time = get_time();\n}\n\nfunction update() {\n    //not needed for this game\n}\n\n\nfunction draw() {\n    draw_asset(background);\n\n    local x = 0;\n    local y = 0;\n    for (local i = 0; i < 12; i++) {\n        if (map[i] == 0) {\n            move_asset(piece_closed, 144 + 215 * x, 220 + y * 256);\n            draw_asset(piece_closed);\n        } else {\n            if (hidden[i] == 1 || hidden[i] == 2) {\n                move_asset(piece_1, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_1);\n            } else if (hidden[i] == 3 || hidden[i] == 4) {\n                move_asset(piece_2, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_2);\n            } else if (hidden[i] == 5 || hidden[i] == 6) {\n                move_asset(piece_3, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_3);\n            } else if (hidden[i] == 7 || hidden[i] == 8) {\n                move_asset(piece_4, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_4);\n            } else if (hidden[i] == 9 || hidden[i] == 10) {\n                move_asset(piece_5, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_5);\n            } else if (hidden[i] == 11 || hidden[i] == 12) {\n                move_asset(piece_6, 144 + 215 * x, 220 + y * 256);\n                draw_asset(piece_6);\n            }\n        }\n        x++;\n        if (x >= 3) {\n            x = 0;\n            y++;\n        }\n    }\n    if (game_over == false) {\n        tiempo = 1000 - floor((get_time() - start_time) / 750);\n        if (tiempo < 0)\n            tiempo = 0;\n    }\n    draw_text_center(720 / 2, 35, 3, tiempo);\n}";
        }
        else
        {
            /*
             * Solo importa este, los demás de arriba no, pero como dicen:
             * "Si ya jala, ni le muevas"
             * Por eso no los borro. c:
             * */
            gNavi->current_project->pjct_script = gNavi->naviDecrypt(script);
            //gNavi->current_project->pjct_script = getGameScript(gNavi->current_project->game_type_);
        }
    }
    //gNavi->nrmSpecifics[gNavi->getCurrentSpecificationIndex()]->modifyGameProject(*gNavi->current_project);
    g_script_enviroment->load_game_from_raw(gNavi->current_project->pjct_script);
}

void NaVi::setSetting(cJSON *gameData)
    {
    cJSON *settings = cJSON_GetObjectItem(gameData, "settings");
    if(settings != 0)
        {
        for(int i = 0; i < cJSON_GetArraySize(settings); i++)
            {
            cJSON *subitem = cJSON_GetArrayItem(settings, i);
            cJSON *type = cJSON_GetObjectItem(subitem, "type");
            cJSON *value = cJSON_GetObjectItem(subitem, "value");
            int _type = StringToInt(type->valuestring);
            int _value = StringToInt(value->valuestring);
            if(_type == 1 || _type == 2 || _type == 3 || _type == 4)
                {
                g_game->settings_int_[_type] = _value;
                }
            }
        }
    }

void NaVi::setMusic(cJSON *gameData)
    {
    cJSON *j_music = cJSON_GetObjectItem(gameData, "music");
    if(j_music != 0)
        {
        for(int i = 0; i < cJSON_GetArraySize(j_music); i++)
            {
            cJSON *subitem = cJSON_GetArrayItem(j_music, i);
            cJSON *j_id = cJSON_GetObjectItem(subitem, "id");
            cJSON *j_name = cJSON_GetObjectItem(subitem, "name");
#if !defined(EMSCRIPTEN) && !defined(__WINPHONE__)
            std::string music_name = "interface/data/";
            music_name.append(j_name->valuestring);
#ifdef __APPLE__
            music_name = MakeFileName(music_name.c_str(),true);
#endif
            Mix_Music *music = Mix_LoadMUS(music_name.c_str());
            if(!music)
                {
                SDL_Log("Mix error");
                }
            if(music)
                {
                gNavi->music_[StringToInt(j_id->valuestring)] = music;
                }
#else
            {
                std::string music_name = "";
                music_name.append(j_name->valuestring);
                music_[StringToInt(j_id->valuestring)] = music_name;
            }
#endif
            }
        }
    }

void NaVi::setEffects(cJSON *gameData)
    {
    cJSON *j_music_effects = cJSON_GetObjectItem(gameData, "music_effect");
    if(j_music_effects != 0)
        {
        for(int i = 0; i < cJSON_GetArraySize(j_music_effects); i++)
            {
            cJSON *subitem = cJSON_GetArrayItem(j_music_effects, i);
            cJSON *j_id = cJSON_GetObjectItem(subitem, "id");
            cJSON *j_name = cJSON_GetObjectItem(subitem, "name");
#if !defined(EMSCRIPTEN) && !defined(__WINPHONE__)
            g_game->addSoundEffect(StringToInt(j_id->valuestring), j_name->valuestring);
#else
            g_game->addSoundEffect(StringToInt(j_id->valuestring), j_name->valuestring);
#endif
            }
        }
    }

void NaVi::AddMenu(int menu_id)
    {
    SDL_Log("navi.cpp: Add menu (%d)", menu_id);
    current_menu_id = menu_id;
    currentMenu = menus_[menu_id];
    customMenuGenerator.createMenu(currentMenu);
    }

void NaVi::reportGameOver()
    {
    SDL_Log("navi.cpp: report game over");
    if(gNavi->loaded_assets.begin() != gNavi->loaded_assets.end())
        {
        for(std::vector<Asset *>::iterator it = gNavi->loaded_assets.begin() + 1; it != gNavi->loaded_assets.end(); ++it)
            {
            SDL_DestroyTexture((*it)->img_->sdl_texture);
            delete (*it);
            it = gNavi->loaded_assets.erase(it);
            it--;
            }
        }
    sendGameOver();
    }

#ifndef EMSCRIPTEN

void NaVi::SaveUserData()
    {
    TextScanner t;
    std::string id = naviEncrypt(u_);
    t.lineList.push_back(id);
    id = naviEncrypt(p_);
    t.lineList.push_back(p_);
    t.lineList.push_back("");
    std::stringstream ss2;
    ss2 << "interface/data/d.txt";
    t.SaveFile(ss2.str());

    //saveTime();
    }

void NaVi::mandarDatos()
{
    sendDataGusanos(usado, posJ1, posJ2, mapaEnTurno, turno);
    reiniciaDatosGE();
}

void NaVi::reiniciaDatosGE(){
    usado = 0;
    posJ1 = 0;
    posJ2 = 0;
    mapaEnTurno = 0;
    turno = 0;
}

void NaVi::SaveGamesData(std::string str)
    {
    SDL_Log("navi.cpp: SaveGamesData()");
    TextScanner i;
    std::stringstream ss3;
    ss3 << "interface/data/d2.txt";
    str = gNavi->naviEncrypt(str);
    //i.lineList.push_back(str);
    //i.SaveFile(ss3.str());
    }

void NaVi::SaveCurrentProjects()
    {
    SDL_Log("navi.cpp: SaveCurrentProjects");
//    cJSON *onFileData = gNavi->GetGameData();
//    cJSON *projects = cJSON_GetObjectItem(onFileData, "projects");
//    project *projectData = nullptr;
//    gNavi->nrmSpecifics[gNavi->getCurrentSpecificationIndex()]->saveCurrentVersion();
//    for(int i = 0; i < cJSON_GetArraySize(projects); i++)
//        {
//        cJSON *subitem = cJSON_GetArrayItem(projects, i);
//        cJSON *pid = cJSON_GetObjectItem(subitem, "pid");
//        projectData = gNavi->getProjectWithID(pid->valueint);
//        if(projectData != 0)
//            {
//            gNavi->setValueToNumberJSON(subitem, "audioActivated", projectData->audioActivated);
//            gNavi->setValueToNumberJSON(subitem, "hasShownManual", projectData->hasShownManual);
//            gNavi->setValueToNumberJSON(subitem, "gameVersion", projectData->gameVersion);
//            gNavi->setValueToStringJSON(subitem, "gameSpecificData", projectData->gameSpecificData);
//            }
//        }
//    gNavi->SaveGamesData(cJSON_Print(onFileData));
    }

void NaVi::setValueToNumberJSON(cJSON *jsonData, string name, int newValue)
    {
    cJSON *jsonNumber = cJSON_GetObjectItem(jsonData, name.c_str());
    if(jsonNumber == 0)
        {
        jsonNumber = cJSON_CreateNumber(1);
        cJSON_AddItemToObject(jsonData, name.c_str(), jsonNumber);
        }
    jsonNumber->valuedouble = jsonNumber->valueint = newValue;
    }

void NaVi::setValueToStringJSON(cJSON *jsonData, string name, string newValue)
    {
    cJSON *jsonText = cJSON_GetObjectItem(jsonData, name.c_str());
    if(jsonText == 0)
        {
        jsonText = cJSON_CreateString("");
        cJSON_AddItemToObject(jsonData, name.c_str(), jsonText);
        }
    jsonText->valuestring = (char *) newValue.c_str();
    }

project *NaVi::getProjectWithType(int gameID)
    {
    project *result = new project("", 0, 0, 0);
    for(int i = 0; i < gNavi->projects_.size(); i++)
        {
        if(gNavi->projects_[i].game_type_ == gameID)
            {
            result = new project(gNavi->projects_[i]);
            break;
            }
        }
    return result;
    }

project *NaVi::getProjectWithID(int gameID)
    {
    project *result = new project("", 0, 0, 0);
    for(int i = 0; i < gNavi->projects_.size(); i++)
        {
        if(gNavi->projects_[i].id == gameID)
            {
            result = new project(gNavi->projects_[i]);
            break;
            }
        }
    return result;
    }

void NaVi::changeAudioState()
    {
    gNavi->current_project->audioActivated = gNavi->current_project->audioActivated ? 0 : 1;
    g_game->isAudioAvailable = gNavi->current_project->audioActivated ? true : false;
    for(int i = 0; i < gNavi->projects_.size(); i++)
        {
        if(gNavi->projects_[i].id == gNavi->current_project->id)
            {
            gNavi->projects_[i].audioActivated = gNavi->current_project->audioActivated;
            break;
            }
        }
    }

bool NaVi::isAudioActive()
    {
    project *currentData = gNavi->getProjectWithID(gNavi->current_project->id);
    return (currentData->audioActivated == 1);
    }
//En este punto hace la conecxion con el servidor con las acciones declaradas
void NaVi::NetInstructionComplete()
    {
    cJSON *root = cJSON_Parse(gNavi->netManager.getRequestResult().c_str());
    gNavi->loading_network = false;
    if(root != 0)
        {
        if(dataHasErrors(root))
            {
            SAFE_DELETE(root);
            return;
            }
        }
    switch(gNavi->netManager.network_action)
        {
        //login al servidor masterkiwi
        case (NetworkManager::LOGINING_IN):
            {
            registerProjectsFromData(root);
            SaveUserData();
            SaveGamesData(cJSON_Print(root));
            sendUserDataToJava(cJSON_Print(root));
            }
            break;
        //pregunta la fecha del servidor
        case (NetworkManager::ASK_FOR_DATA_DATE):
            {
            evaluateDateForDownload(root);
            }
            break;
        //comprime el atlas de texturas
        case (NetworkManager::COMPRESSING_ATLAS):
            {
            sendAtlasCompressed();
            }
            break;
        //Comprime las texturas y textos
        case (NetworkManager::COMPRESSING_TO_RTTEXT):
            {
            sendTexturesReady();
            }
            break;
        //genera los los links para poder descargar
        case (NetworkManager::GENERATING_DOWNLOAD_LINKS):
            {
            configureToDownload(root);
            sendAssetsListCount(gNavi->file_count_);
            continueToDownload();
            SAFE_DELETE(root);
            }
            break;
        //descarga los archivos con los links generados
        case (NetworkManager::DOWNLOADING_FILES):
            {
            sendRemainingAssets(gNavi->file_count_ - gNavi->current_file_down_);
            saveDownload();
            gNavi->currentDownload++;
            if(!continueToDownload())
                {
                gNavi->netManager.load_menus_and_assets();
                }
            }
            break;
        //descarga los datos del juego como los archivos _m_a.txt y _dt.txt
        case (NetworkManager::DOWNLOADING_GAME_DATA):
            {
            erraseGameData(gNavi->current_project->id);
            std::string ss2("interface/data/" + NumberToString(gNavi->current_project->id) + "_m_a.txt");
            std::string ssm2("interface/data/" + NumberToString(gNavi->current_project->id) + "_dt.txt");
            TextScanner b;
            b.SetupFromMemoryAddress(gNavi->netManager.getRequestResult().c_str());
            b.SaveFile(ss2);
            TextScanner b2;
            b2.lineList.push_back(currentDateTime());
            b2.SaveFile(ssm2);
            SAFE_DELETE(root);
            updateDownloadData(current_project->id, dateToRegister);
            gNavi->askForGameSpecificData();
            }
            break;
        case (NetworkManager::UPLOADING_DATA):
            {
            gNavi->loading_network = false;
            RemoveUploadData();
            g_game->state_ = WAITING;
            if(game_stats_begin == 1)
            {
                AddMenu(0);
            }
            else
            {
                AddMenu(game_over_menu);
            }
            }
            break;
        case (NetworkManager::INSECURE_UPLOADING):
            {
            processTop10(gNavi->netManager.getRequestResult().c_str());
            gNavi->loading_network = false;
            }
            break;
        case (NetworkManager::ASK_FOR_QUESTIONS):
            {
            gNavi->workWithSpecificResponse();
            }
            break;
        }
    }

void NaVi::evaluateDateForDownload(cJSON *data)
    {
    cJSON *isNewer = cJSON_GetObjectItem(data, "descargar");
    if(isNewer->valueint)
        {
        cJSON *newDate = cJSON_GetObjectItem(data, "fechaUltima");
        dateToRegister = newDate->valuestring;
        }
    else
        {
        gNavi->askForGameSpecificData();
        return;
        }
    callStartSDL(isNewer->valueint);
    }

#endif

void NaVi::configureToDownload(cJSON *data)
    {
    gNavi->file_count_ = 0;
    gNavi->project_files_to_download_.clear();
    cJSON *files = cJSON_GetObjectItem(data, "files");
    if(files != 0)
        {
        gNavi->registerFilesToDownload(files);
        }
    if(gNavi->project_files_to_download_.size() == 0)
        {
        clearAssetsFile(false);
        SAFE_DELETE(data);
        return;
        }
    clearAssetsFile(true);
    erraseGameData(gNavi->current_project->id);
    gNavi->current_file_down_ = 0;

    string fileName("interface/data/" + NumberToString(gNavi->current_project->id) + "_m_a.txt");
    RemoveFile(fileName);

    TextScanner t;
    for(vector<fileData>::iterator it = gNavi->project_files_to_download_.begin(); it != gNavi->project_files_to_download_.end(); ++it)
        {
        std::vector<std::string> res = StringTokenize(it->fileName, "|");
        fileName = res[0] + "|interface/data/" + NumberToString(gNavi->current_project->id) + "_" + NumberToString(it->fileID) + "_" + res[1];
        t.lineList.push_back(fileName);
        }
    fileName = "interface/data/" + NumberToString(gNavi->current_project->id) + ".txt";
    t.SaveFile(fileName);
    }

bool NaVi::continueToDownload()
    {
    bool result = false;
    if(gNavi->current_file_down_ < gNavi->file_count_)
        {
        gNavi->current_file_down_++;
        gNavi->netManager.download_file(gNavi->currentDownload->fileID, gNavi->currentDownload->fileName);
        result = true;
        }
    return result;
    }

void NaVi::saveDownload()
    {
    string str = gNavi->netManager.getRequestResult();
    if(str.size() > 0)
        {
        vector<char> receibedData(str.begin(), str.end());
        vector<string> res = StringTokenize(gNavi->currentDownload->fileName, "|");
        string fileName(getPreferencedPath());
        fileName = fileName + "/interface/data/" + NumberToString(gNavi->current_project->id) + "_" + NumberToString(gNavi->currentDownload->fileID) + "_" + res[1];
        FILE *targetFile = fopen(fileName.c_str(), "wb");
        if(!targetFile)
            {
            SDL_Log("SAVING FILE ERROR!!!!!");
            }
        else
            {
            int bytesWritten = fwrite(&receibedData[0], 1, receibedData.size(), targetFile);
            if(bytesWritten != receibedData.size())
                {
                SDL_Log("Error Al escribir data");
                }
            receibedData.clear();
            fclose(targetFile);
            }
        }
    }

void NaVi::askForGameSpecificData()
    {
    if(gNavi->nrmSpecifics.size() > 0)
        {
        if(!gNavi->nrmSpecifics[gNavi->getCurrentSpecificationIndex()]->networkRequest(gNavi->netManager))
            {
            sendFinishDownloading();
            }
        }
    else
        {
        if(gNavi->current_project->id != 0)
            {
            gNavi->registerNRMSpecifications();
            gNavi->askForGameSpecificData();
            }
        }
    }

void NaVi::workWithSpecificResponse()
    {
    gNavi->nrmSpecifics[gNavi->getCurrentSpecificationIndex()]->networkResponse(gNavi->netManager);
    sendFinishDownloading();
    }

void NaVi::registerProjectsFromData(cJSON *jsonData)
    {
    gNavi->projects_.clear();
    cJSON *projects = cJSON_GetObjectItem(jsonData, "projects");
    if(projects != 0)
        {
        for(int i = 0; i < cJSON_GetArraySize(projects); i++)
            {
            cJSON *subitem = cJSON_GetArrayItem(projects, i);
            cJSON *pid = cJSON_GetObjectItem(subitem, "pid");
            cJSON *ptitle = cJSON_GetObjectItem(subitem, "ptitle");
            cJSON *pmode = cJSON_GetObjectItem(subitem, "mode");
            cJSON *ptype = cJSON_GetObjectItem(subitem, "type");
            gNavi->projects_.push_back(project(ptitle->valuestring, pid->valueint, pmode->valueint));
            gNavi->projects_[gNavi->projects_.size() - 1].game_type_ = ptype->valueint;
            gNavi->projects_[gNavi->projects_.size() - 1].audioActivated = gNavi->createAndGetBoolOnJson(subitem, "audioActivated", 1);
            gNavi->projects_[gNavi->projects_.size() - 1].hasShownManual = gNavi->createAndGetBoolOnJson(subitem, "hasShownManual", 0);
            gNavi->projects_[gNavi->projects_.size() - 1].gameVersion = gNavi->createAndGetBoolOnJson(subitem, "gameVersion", 0);
            gNavi->projects_[gNavi->projects_.size() - 1].gameSpecificData = gNavi->createAndGetStringOnJSON(subitem, "gameSpecificData", " ");
            }
        registerNRMSpecifications();
        }
    }

int NaVi::createAndGetBoolOnJson(cJSON *jsonData, string name, int initialVal)
    {
    int result = 0;
    cJSON *existingFlag = cJSON_GetObjectItem(jsonData, name.c_str());
    if(existingFlag != 0)
        {
        result = existingFlag->valueint;
        }
    else
        {
        cJSON *newFlag = cJSON_CreateNumber(initialVal);
        cJSON_AddItemToObject(jsonData, name.c_str(), newFlag);
        result = newFlag->valueint;
        }
    return result;
    }

string NaVi::createAndGetStringOnJSON(cJSON *jsonData, string name, string initialVal)
    {
    string result = "";
    cJSON *existingFlag = cJSON_GetObjectItem(jsonData, name.c_str());
    if(existingFlag != 0)
        {
        result = existingFlag->valuestring;
        }
    else
        {
        cJSON *newFlag = cJSON_CreateString(initialVal.c_str());
        cJSON_AddItemToObject(jsonData, name.c_str(), newFlag);
        result = newFlag->valuestring;
        }
    return result;
    }

const char *NaVi::getGameScript(int gameID)
    {
    const char *result = "";
    cJSON *root = cJSON_Parse((char *) getGameDataJSON());
    if(root == 0)
        {
        gNavi->updateDownloadData(gNavi->current_project->id, "0");
        sendNoGameData();
        return result;
        }
    cJSON *gamesInfo = cJSON_GetObjectItem(root, "games");
    if(gamesInfo != 0)
        {
        for(int i = 0; i < cJSON_GetArraySize(gamesInfo); i++)
            {
            cJSON *gameItem = cJSON_GetArrayItem(gamesInfo, i);
            cJSON *projectID = cJSON_GetObjectItem(gameItem, "projectID");
            if(projectID->valueint == gameID)
                {
                cJSON *gameScript = cJSON_GetObjectItem(gameItem, "script");
                result = gameScript->valuestring;
                break;
                }
            }
        }
    return result;
    }

bool NaVi::dataHasErrors(cJSON *jsonData)
    {
    cJSON *error = cJSON_GetObjectItem(jsonData, "error");
    if(error != 0)
        {
        SDL_Log("JSON_ERROR: %s", error->valuestring);
        gNavi->logout();
        gNavi->loading_network = false;
        return true;
        }
    return false;
    }

void NaVi::registerFilesToDownload(cJSON *files)
    {
    for(int i = 0; i < cJSON_GetArraySize(files); i++)
        {
        cJSON *subitem = cJSON_GetArrayItem(files, i);
        int num = StringToInt(subitem->string);
        for(int z = 0; z < cJSON_GetArraySize(subitem); z++)
            {
            cJSON *subsubitem = cJSON_GetArrayItem(subitem, z);
            fileData newData(num, subsubitem->valuestring);
            gNavi->project_files_to_download_.push_back(newData);
            gNavi->file_count_++;
            SDL_Log("SUBITEM: %s", subsubitem->valuestring);
            }
        }
    gNavi->currentDownload = gNavi->project_files_to_download_.begin();
    }

void NaVi::clearAssetsFile(bool stringTokenize)
    {
    TextScanner b;
    std::stringstream ss2;
    ss2 << "interface/data/" << gNavi->current_project->id << ".txt";
    b.LoadFile(ss2.str());
    int i = 0;
    while(b.GetLine(i) != "")
        {
        std::string fname = b.GetLine(i);
        if(stringTokenize)
            {
            std::vector<std::string> res = StringTokenize(fname, "|");
            if(res.size() > 1)
                {
                fname = res[1];
                }
            }
        RemoveFile(fname);
        i++;
        }
    RemoveFile(ss2.str());
    }

void NaVi::erraseGameData(int gameID)
    {
    std::stringstream ssm;
    ssm << "interface/data/" << gameID << "_m_a.txt";
    RemoveFile(ssm.str());

    std::stringstream ssm2;
    ssm2 << "interface/data/" << gameID << "_dt.txt";
    RemoveFile(ssm2.str());

    }

void NaVi::RemoveUploadData()
    {
    std::stringstream ss3;
    ss3 << "interface/data/" << gNavi->current_project->id << "_sv.txt";
    TextScanner b2(ss3.str());
    int i = 0;
    for(i = 0; i < 50; i++)
    {
        if(b2.lineList.size() != 0)
        {
            b2.lineList.erase(b2.lineList.begin());
        }
    }
    if(b2.lineList.size() == 0)
    {
        RemoveFile(ss3.str());
    }
    else
    {
        b2.SaveFile(ss3.str());
    }

    loading_network = false;
#ifdef EMSCRIPTEN
    if (gNavi->load_top10_next == true)
        {
            gNavi->get_top10_insecure();
        }
#endif
#ifdef EMSCRIPTEN
    if (gNavi->menus_[gNavi->current_menu_id].special_ == 3 && gNavi->game_stats_begin == 0)
        {
            gNavi->AddMenu(menu_after_upload);
            g_game->state_ = WAITING;
        }
        else if (gNavi->game_stats_begin == 1)
        {
            gNavi->AddMenu(menu_after_upload);
            g_game->state_ = WAITING;
        }
#endif
    }

std::string NaVi::number_to_commastring(int number)
    {
    std::string NumericString = NumberToString(number); // It should output 1,234,567,890
    unsigned int Length = strlen(NumericString.c_str()); // Get the length of the string, so we know when we have to stop
    std::string FinalString; // Will be our output
    unsigned int CommaOffset = Length % 3; // Get the comma offset
    for(unsigned int i = 0; i < Length; ++i) // Loop each character
        {
        // If our Index%3 == CommaOffset and this isn't first character, add a comma
        if(i % 3 == CommaOffset && i)
            {
            FinalString += ','; // Add the comma
            }
        FinalString += NumericString[i]; // Add the original character
        }
    return FinalString;
    }

void NaVi::processTop10(std::string str)
    {
    if(top10_labels.size() == 0)
        {
        return;
        }
    cJSON *root = cJSON_Parse(str.c_str());
    if(root != 0)
        {
        //leemos todos los datos
        for(int i = 0; i < 10; i++)
            {
            std::string d = "data" + NumberToString(i);
            std::string d2 = "score" + NumberToString(i);

            cJSON *data = cJSON_GetObjectItem(root, d.c_str());
            cJSON *score = cJSON_GetObjectItem(root, d2.c_str());
            if(data == 0 || score == 0)
                {
                top10_labels[i].first->text = "";
                top10_labels[i].second->text = "";
                }
            else
                {
                //cambiamos el texto
                top10_labels[i].first->text = data->valuestring;
                top10_labels[i].second->text = number_to_commastring(StringToInt(score->valuestring));
                }
            }
        }
    SAFE_DELETE(root);
    }

void NaVi::updateDownloadData(int projectID, string newDate)
    {
    bool founded = false;
    int index = 0;
    string line;
    TextScanner data;
    string fileName = "interface/data/downloadData.txt";
    data.LoadFile(fileName);

    while((line = data.GetLine(index)) != "")
        {
        if(StringToInt(line) == projectID)
            {
            founded = true;
            if(data.GetLine(index + 1) != "")
                {
                data.lineList[index + 1] = newDate;
                }
            break;
            }
        index++;
        }
    if(!founded)
        {
        data.lineList.push_back(NumberToString(projectID));
        data.lineList.push_back(newDate);
        }
    data.SaveFile(fileName);
    }

long NaVi::getGameDataDate(int projectID)
    {
    int index = 0;
    string line;
    long result = 0;
    TextScanner data;
    string fileName = "interface/data/downloadData.txt";
    if(!data.LoadFile(fileName))
        {
        data.SaveFile(fileName);
        }
    while((line = data.GetLine(index)) != "")
        {
        if(StringToInt(line) == projectID)
            {
            result = StringToInt(data.GetLine(index + 1));
            break;
            }
        index++;
        }
    return result;
    }

void NaVi::saveTime()
    {
    unsigned long int sec = time(0);
    TextScanner i;
    std::stringstream ss;
    ss << "interface/data/d3.txt";
    std::stringstream ss2;
    ss2 << sec;
    std::string id = naviEncrypt(ss2.str());
    i.lineList.push_back(id);
    i.SaveFile(ss.str());
    }

void NaVi::renameFileForMasterKiwiUse(string fileToMove, string newFile)
    {
    registerDirectories();
    char ch;
    FILE *source, *target;

    source = fopen(fileToMove.c_str(), "r");
    if(source == NULL)
        {
        exit(EXIT_FAILURE);
        }

    target = fopen(newFile.c_str(), "w");
    if(target == NULL)
        {
        fclose(source);
        exit(EXIT_FAILURE);
        }

    fseek(source, 0L, SEEK_END);
    int pos = ftell(source);
    fseek(source, 0L, SEEK_SET);
    while(pos--)
        {
        ch = fgetc(source);
        fputc(ch, target);
        }

    fclose(source);
    fclose(target);
    remove(fileToMove.c_str());
    }

void NaVi::registerNRMSpecifications()
    {
    if(gNavi->current_project->id != 0)
        {
        bool founded = false;
        for(int i = 0; i < gNavi->nrmSpecifics.size(); i++)
            {
            if(gNavi->nrmSpecifics[i]->projectType == gNavi->getProjectWithID(gNavi->current_project->id)->game_type_)
                {
                founded = true;
                }
            }
        if(!founded)
            {
            gNavi->nrmSpecifics.push_back(new NRMTrivia());
            gNavi->nrmSpecifics[gNavi->nrmSpecifics.size() - 1]->dataVersion = gNavi->getProjectWithType(gNavi->nrmSpecifics[gNavi->nrmSpecifics.size() - 1]->projectType)->gameVersion;
            gNavi->nrmSpecifics[gNavi->nrmSpecifics.size() - 1]->specificData << gNavi->getProjectWithType(gNavi->nrmSpecifics[gNavi->nrmSpecifics.size() - 1]->projectType)->gameSpecificData;
            }
        }
    }

int NaVi::getCurrentSpecificationIndex()
    {
    int index = 0;
    int currentIndex = gNavi->getProjectWithID(gNavi->current_project->id)->game_type_;
    for(int i = 0; i < gNavi->nrmSpecifics.size(); i++)
        {
        if(gNavi->nrmSpecifics[i]->projectType == currentIndex)
            {
            index = i;
            break;
            }
        }
    return index;
    }

void NaVi::processForm()
{
    if(gNavi->menu_stats_fields > 0)
    {
        g_game->user_data.push_back(((MK_Textinput*)gNavi->gui_buttons[2])->input_text);
        if(gNavi->menu_stats_fields >= 2)
        {
            g_game->user_data.push_back(((MK_Textinput*)gNavi->gui_buttons[5])->input_text);
        }
        if(gNavi->menu_stats_fields >= 3)
        {
            g_game->user_data.push_back(((MK_Textinput*)gNavi->gui_buttons[8])->input_text);
        }
        if(gNavi->menu_stats_fields >= 4)
        {
            g_game->user_data.push_back(((MK_Textinput*)gNavi->gui_buttons[11])->input_text);
        }
        g_game->SaveScoreAlways();
        gNavi->netManager.upload_data();
    }
}

void NaVi::set_jsonToSquirrelData(string fullJson)
{
    json_to_squirrel = fullJson;
}

void NaVi::set_dataGusanosYEscaleras(int usado_,int posJ1_,int posJ2_,int mapa_,int turno_)
{
    posJ1 = posJ1_;
    posJ2 = posJ2_;
    mapaEnTurno = mapa_;
    turno = turno_;
    usado = usado_;
}