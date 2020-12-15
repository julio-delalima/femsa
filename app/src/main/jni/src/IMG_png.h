#ifndef IMGPNG_H
#define IMGPNG_H
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

#ifdef __cplusplus
extern "C" {
#endif

extern SDL_Surface *IMG_LoadTyped_RW(SDL_RWops *src, int freesrc, const char *type);
extern SDL_Surface *IMG_Load(const char *file);
extern SDL_Surface *IMG_Load_RW(SDL_RWops *src, int freesrc);

extern int IMG_InitPNG();

extern void IMG_QuitPNG();

/* See if an image is contained in a data source */
extern int IMG_isPNG(SDL_RWops *src);

/* Load a PNG type image from an SDL datasource */
extern SDL_Surface *IMG_LoadPNG_RW(SDL_RWops *src);

#ifdef __cplusplus
}
#endif

#endif // NAVI_H

