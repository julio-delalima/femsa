#ifndef MK_GUI_H
#define MK_GUI_H
#include <iostream>
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

enum mk_type
{
    MKG_UNKNOWN,
    MKG_BUTTON,
    MKG_TEXTINPUT
};

class MK_Gui
{
public:
    MK_Gui();
    int type;

    //if this function returns true it should no longer be handled
    virtual bool ProcessEvent(SDL_Event *event) {return false;};
    virtual void Draw() {};

    bool enabled;
};

#endif
