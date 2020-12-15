#ifndef MK_BUTTON_H
#define MK_BUTTON_H
#include "mkimage.h"
#include "mk_gui.h"

class MK_Button : public MK_Gui
{
public:
    MK_Button();
    MK_Button(std::string _image, float _x, float _y);
    MK_Button(float _x, float _y, float _w, float _h);
    bool ProcessEvent(SDL_Event *event);
    void Draw();

    float x;
    float y;
    float width;
    float height;
    Mk_Image *image;
    int action;
    float scale;

    void (*clickCallback)(int _action, int x, int y);
};

#endif
