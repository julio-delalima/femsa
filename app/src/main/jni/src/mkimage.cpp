#include "mkimage.h"
#include "IMG_png.h"
#ifdef __APPLE__
    #include "../../SDL2_image-2.0.0/SDL_image.h"
#endif
#include "navi.h"
#include "miscutils.h"
#include "game.h"

Mk_Image::Mk_Image(std::string name)
{
    rotation = 0.0f;
    scale_x = 1.0f;
    scale_y = 1.0f;
    x = 0.0f;
    y = 0.0f;
    showDebug("Me llego de ruta");
    showDebug(name.c_str());
    ChangeImage(name);
}

void Mk_Image::ChangeImage(std::string name)
{
    #ifdef __EMSCRIPTEN__
    #else // __EMSCRIPTEN__
    #endif

    if (gNavi->loaded_textures.find(GetFileNameFromString(name)) == gNavi->loaded_textures.end()) //Todavia no cargamos esta textura
    {
        sdlTextureName = name.c_str();
        std::string external = "/data/data/com.femsa.sferea/files/Android/data/com.femsa.sferea/files/Download/";;
        SDL_RWops *file = SDL_RWFromFile((external + name).c_str(), "rb");
        if( file == NULL )
        {
            //we check on a different folder
            name = SDL_GetPrefPath("","") + name;
            file = SDL_RWFromFile(name.c_str(), "rb");
            if( file == NULL )
            {
                SDL_Log("Unable to load file %s! SDL Error: %s",name.c_str(),SDL_GetError());
                g_game->state_ = GAME_OVER;
                gNavi->updateDownloadData(gNavi->current_project->id,"0");
                sendExitGame();
                delete(gNavi);
                gNavi = 0;
                return;
            }
        }
        #ifdef __APPLE__
            SDL_Surface *surf = IMG_LoadPNG_RW(file);
        #else
            SDL_Surface *surf = IMG_Load_RW(file, 1);
        #endif
        if( surf == NULL )
        {
            SDL_Log("Unable to load image %s! SDL Error: %s",name.c_str(),SDL_GetError());
            g_game->state_ = GAME_OVER;
            gNavi->updateDownloadData(gNavi->current_project->id,"0");
            sendExitGame();
            delete(gNavi);
            gNavi = 0;
            return;
        }

        sdl_texture = SDL_CreateTextureFromSurface(g_game->renderer, surf);
        SDL_FreeSurface(surf);
    }
    else
    {
        sdl_texture = gNavi->loaded_textures[GetFileNameFromString(name)];
		if (sdl_texture == NULL)
		{
			SDL_Log("Unable to load image %s! SDL Error: %s",name.c_str(),SDL_GetError());
			half_width = 1;
            half_height = 1;
            g_game->state_ = GAME_OVER;
            gNavi->updateDownloadData(gNavi->current_project->id,"0");
            sendExitGame();
            delete(gNavi);
            gNavi = 0;
			return;
		}
    }

    SDL_QueryTexture(sdl_texture, NULL, NULL, &width, &height);

    half_width = (float)width/2.0f;
    half_height = (float)height/2.0f;

    gNavi->loaded_textures[GetFileNameFromString(name)] = sdl_texture;

    flip = SDL_FLIP_NONE;
    tx_x = 0;
    tx_y = 0;
}

void Mk_Image::Destroy()
{
    if (sdl_texture == NULL)
    {
        return;
    }
    //Force It
    SDL_DestroyTexture(sdl_texture);
}

void Mk_Image::Draw()
{
    if (sdl_texture == NULL)
    {
        return;
    }
    //este rect es donde lo vamos a dibujar
    static SDL_Rect rect;
    rect.x = x-half_width*scale_x;
    rect.y = y-half_height*scale_y;
    rect.w = width*scale_x;
    rect.h = height*scale_y;

    static SDL_Rect srcrect;
    srcrect.x = tx_x;
    srcrect.y = tx_y;
    srcrect.w = width;
    srcrect.h = height;

    if(SDL_RenderCopyEx(g_game->renderer, sdl_texture, &srcrect, &rect, rotation, NULL, flip) < 0)
    {
        SDL_Log("Error en el dibujado!!!!!: -%s- %s",sdlTextureName.c_str(),SDL_GetError());
    }
}

int Mk_Image::GetWidth()
{
    return width;
}

int Mk_Image::GetHeight()
{
    return height;
}

void Mk_Image::SetWidth(float w)
{
    width = w;
    half_width = width/2;
}

void Mk_Image::SetHeight(float h)
{
    height = h;
    half_height = height/2;
}

void Mk_Image::SetScale(float _scale_x, float _scale_y)
{
    scale_x = _scale_x;
    scale_y = _scale_y;
}

void Mk_Image::SetRotation(float _r)
{
    rotation = _r;
}

float Mk_Image::GetRotation()
{
    return rotation;
}

void Mk_Image::SetX(float _x)
{
    x = _x;
}

void Mk_Image::SetY(float _y)
{
    y = _y;
}
