#ifndef MK_LABEL_H
#define MK_LABEL_H
#include "mk_gui.h"


class MK_rounded_rect : public MK_Gui
{
    public:
        MK_rounded_rect();
        MK_rounded_rect(float _x, float _y,float _x2, float _y2);
        virtual ~MK_rounded_rect();

        void Draw();

        SDL_Color color;
        float x;
        float y;
        float x2;
        float y2;
};

class MK_Label : public MK_Gui
{
    public:
        MK_Label();
        MK_Label(std::string default_text, float _x, float _y);
        virtual ~MK_Label();

        void Draw();

        SDL_Color color;
        float x;
        float y;
        float text_size;
        std::string text;
        bool center;
        bool separator;
        SDL_Color SeparatorColor;

    protected:
    private:
};

#endif
