#ifndef MKIMAGE_H
#define MKIMAGE_H

#ifdef __WINPHONE__
	#include <SDL.h>
#else
    #ifdef __ANDROID__
        #include "SDL.h"
    #elif __APPLE__
        #include "SDL.h"
    #else
        #include "SDL2/SDL.h"
    #endif
#endif

#include <string>
using namespace std;

class Mk_Image
{
public:
    Mk_Image(std::string name);

    void Draw();

    int GetWidth();

    int GetHeight();

    void SetX(float _x);
    void SetY(float _y);

    void SetWidth(float w);
    void SetHeight(float h);

    void SetScale(float _scale_x, float _scale_y);

    void SetRotation(float _r);

    float GetRotation();
    void Destroy();
    void ChangeImage(std::string name);

    string sdlTextureName;
    SDL_Texture *sdl_texture;

    float x;
    float y;
    int width;
    int height;
    float scale_x;
    float scale_y;
    float rotation;
    SDL_RendererFlip flip;

    float tx_x;
    float tx_y;

    float half_width;
    float half_height;
};

#endif
