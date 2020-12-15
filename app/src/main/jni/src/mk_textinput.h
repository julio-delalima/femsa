#ifndef MK_TEXTINPUT_H
#define MK_TEXTINPUT_H

#include "mk_gui.h"


class MK_Textinput : public MK_Gui
{
public:
    MK_Textinput();
    MK_Textinput(std::string default_text, float _x, float _y, float _w, float _h);
    bool ProcessEvent(SDL_Event *event);
    void Draw();

    float x;
    float y;
    float width;
    float height;
    bool selected;
    int max_len;
    bool is_password;
    SDL_Color color;

    std::string input_text;
    std::string input_default_text;
};

#endif // MK_TEXTINPUT_H
