#ifdef __WINPHONE__
    #include "SDL.h"
#else
    #ifdef __ANDROID__
        #include <jni.h>
        #include "SDL.h"
    #elif __APPLE__
        #include "SDL.h"
        //#include "../../SDL2_image-2.0.0/SDL_image.h"
    #else
        #include "SDL2/SDL.h"
    #endif
#include <stdbool.h>
#endif

#include "game.h"
#include "IMG_png.h"
#include "androidWrapper/MasterKiwiWrapper.h"
#include <cstdio>
#include <string>

#ifdef EMSCRIPTEN
#include <emscripten.h>
#endif

#include "aes/Blowfish.h"
#include "navi.h"
#include "SDL_ttf.h"
#include "SDL2_gfx/SDL2_framerate.h"

#ifdef DEBUG_WINDOW
    #include "debugWindow.h"
#endif

#ifdef WINAPI
    #include <winsock2.h>
    #include <windows.h>
    #include <TCHAR.h>
#endif

// Resolutions of SDL, the app and games do NOT care about this
#ifdef HORIZONTAL_MODE
    #ifdef EMSCRIPTEN
        float width = 640, height = 360;
    #else
        float width = 1092, height = 320;
    #endif
        float real_width = 1280, real_height = 720;
#else
    #ifdef EMSCRIPTEN
        float width = 360, height = 640;
    #else
        float width = 360, height = 640;
    #endif
        float real_width = 720, real_height = 1280;
#endif

SDL_Window* gWindow = NULL;

void setup()
{
    SDL_Init(SDL_INIT_EVERYTHING);

    #ifdef __APPLE__
        IMG_Init(IMG_INIT_PNG);
    #else
        IMG_InitPNG();
    #endif

    if (TTF_Init() != 0)
    {
        SDL_Log("TTF_Init error");
        return;
    }

    SDL_SetHint(SDL_HINT_ORIENTATIONS, "Portrait");

    #ifdef __ANDROID__
       // SDL_SetHint(SDL_HINT_ANDROID_SEPARATE_MOUSE_AND_TOUCH, "1");
    #endif

    #ifdef __APPLE__
        //SDL_SetHint(SDL_HINT_ACCELEROMETER_AS_JOYSTICK, 1);TODO: descomentar y ver qué hace
    #endif

    // Create window
    gWindow = SDL_CreateWindow("Master Kiwi",SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED,
                               width, height, SDL_WINDOW_SHOWN);

    if (gWindow == NULL)
    {
        SDL_Log("Window could not be created! SDL_Error: %s\n",SDL_GetError());
    }

    // Make the scaled rendering look smoother
    SDL_SetHint(SDL_HINT_RENDER_SCALE_QUALITY, "linear");

    #ifdef __ANDROID__
        SDL_GL_SetAttribute(SDL_GL_RED_SIZE, 5);
        SDL_GL_SetAttribute(SDL_GL_GREEN_SIZE, 6);
        SDL_GL_SetAttribute(SDL_GL_BLUE_SIZE, 5);
    #endif

    g_game->renderer = SDL_CreateRenderer(gWindow, -1, 0);
    SDL_RenderSetLogicalSize(g_game->renderer, real_width, real_height);

    #ifdef __APPLE__
        SDL_SetWindowFullscreen(gWindow, SDL_WINDOW_FULLSCREEN);
    #endif

    #ifdef FULL_SCREEN_WINDOWS
        SDL_SetWindowFullscreen(gWindow, SDL_WINDOW_FULLSCREEN);
    #endif
}

void gameLoop()
{
    try
    {
        g_game->GameUpdate();
        g_game->Draw();
    }
    catch(const std::exception &e)
    {
        SDL_Log("Uncaught exception %s!\n",e.what());
    }
    catch(...)
    {
        SDL_Log("Uncaught unknown exception!\n");
    }
}

extern "C"
{

#ifdef EMSCRIPTEN
    void testo()
    {
        gNavi->LoadProject();
    }

    void end_upload()
    {
        gNavi->RemoveUploadData();
    }

    void end_top10(char* str)
    {
        gNavi->processTop10(str);
    }

    void error_top10()
    {}

    void error_upload()
    {
        gNavi->AddMenu(gNavi->menu_after_upload);
        g_game->state_ = WAITING;
    }
#endif


#ifdef EMSCRIPTEN
    int main(int argc, char *argv[])
#else
    int SDL_main(int argc, char *argv[])
#endif
    {
        SDL_Log("Iniciando Master Kiwi %s",gNavi->current_project->name.c_str());
        g_game = new game();
        setup();
        g_game->win = gWindow;
        if(gNavi->initGame())
        {
            gNavi->Init();
            g_game->Init();

            #ifdef EMSCRIPTEN
                // we call a javascript function when we load the game the js is allReady()
                EM_ASM(allReady());
                emscripten_set_main_loop(gameLoop, 30, true);  // 30 fps
            #else
                FPSmanager * manager = new FPSmanager();
                SDL_initFramerate(manager);
                SDL_setFramerate(manager, 30);
                #ifdef DEBUG_WINDOW
                    gNavi->dw.init();
                #endif

                #ifdef WINAPI
                    WSADATA wsaData;
                    WSAStartup(0x202, & wsaData);
                #endif

                SDL_Rect r;
                r.x = 0;
                r.y = 0;
                r.w = 720;
                r.h = 280;
                SDL_SetTextInputRect(&r);

                sendStartingGame();
                while (g_game->state_ != GAME_OVER)
                {
                    gameLoop();
                }
                delete(manager);
            #endif
        }
        SDL_RenderClear(g_game->renderer);
        SDL_DestroyRenderer(g_game->renderer);
        SDL_DestroyWindow(g_game->win);
        Mix_CloseAudio();
        Mix_Quit();
        SDL_Quit();
        delete(g_game);
        return 0;
        }
    }
#ifdef EMSCRIPTEN
    void playSoundEM(std::string str)
    {
        std::string str_new = "playSound('";
        str_new.append(str);
        str_new.append("');");
        emscripten_run_script(str_new.data());
    }

    void stopSoundEM()
    {
        emscripten_run_script("stopSound();");
    }

    void playEffectEM(std::string str)
    {
        std::string str_new = "playEffect('";
        str_new.append(str);
        str_new.append("');");
        emscripten_run_script(str_new.data());
    }

    void stopEffectEM()
    {
        emscripten_run_script("stopEffect();");
    }

    void enviar_datos_upload(std::map<string,string> &mapa)
    {
        std::string cmd2 = "myArray = {";
        for (std::map<string,string>::iterator it = mapa.begin(); it != mapa.end(); it++)
        {
            cmd2.append("'");
            cmd2.append((*it).first);
            cmd2.append("': '");
            cmd2.append((*it).second);
            cmd2.append("',");
        }
        cmd2.append("};");

        emscripten_run_script( cmd2.data() );

        std::string cmd3 = "save_data();";
        emscripten_run_script( cmd3.data() );
    }

    void descargar_top10(std::map<string,string> &mapa)
    {
        std::string cmd2 = "myArray2 = {";
        for (std::map<string,string>::iterator it = mapa.begin(); it != mapa.end(); it++)
        {
            cmd2.append("'");
            cmd2.append((*it).first);
            cmd2.append("': '");
            cmd2.append((*it).second);
            cmd2.append("',");
        }
        cmd2.append("};");

        emscripten_run_script( cmd2.data() );

        std::string cmd3 = "descargar_top10();";
        emscripten_run_script( cmd3.data() );
    }
#endif
