 #include "mk_label.h"

#include "navi.h"
#include "game.h"
#include "SDL2_gfx/SDL2_gfxPrimitives.h"

MK_rounded_rect::MK_rounded_rect()
{
     //ctor
    color.r = 0;
    color.g = 0;
    color.b = 0;
    color.a = 255;
    x = 0;
    y = 0;
    x2 = 0;
    y2 = 0;

}

MK_rounded_rect::~MK_rounded_rect()
{
    //dtor
}

MK_rounded_rect::MK_rounded_rect(float _x, float _y,float _x2, float _y2)
{
     //ctor
    color.r = 0;
    color.g = 0;
    color.b = 0;
    color.a = 255;
    x = _x;
    y = _y;
    x2 = _x2;
    y2 = _y2;

}

void MK_rounded_rect::Draw()
{
    roundedBoxRGBA(g_game->renderer, x, y, x2, y2, 10, color.r,color.g,color.b,color.a);
}


MK_Label::MK_Label()
{
    //ctor
    color.r = 0;
    color.g = 0;
    color.b = 0;
    color.a = 255;
    x = 0;
    y = 0;
    center = false;
    separator = false;
}

MK_Label::~MK_Label()
{
    //dtor
}

void removeAccented( std::string &str ) {
    int i = 0;
    std::string tr = "AAAAAAECEEEEIIIIDNOOOOOx0UUUUYPsaaaaaaeceeeeiiiiOnooooo/0uuuuypy";
    for (int i = 0; i < str.length(); i++)
    {
        unsigned char ch = str[i];
        if ( ch >=192 ) {
            str[i] = tr[ ch-192 ];
        }
    }
}

MK_Label::MK_Label(std::string default_text, float _x, float _y)
{
    text_size = 43.0f;
    removeAccented(default_text);
    text = default_text;
    color.r = 0;
    color.g = 0;
    color.b = 0;
    color.a = 255;
    separator = false;
    x = _x;
    y = _y;
    center = false;
}

void MK_Label::Draw()
{
    if (g_game != 0)
    {
        g_game->DrawText(x+9, y+3, text, text_size, center, color);
        return;
    }

    //gNavi->DrawText(x+9, y+3, text, text_size, center, color);

    if (separator)
    {
        SDL_SetRenderDrawColor(g_game->renderer, SeparatorColor.r, SeparatorColor.g, SeparatorColor.b, SeparatorColor.a);
        SDL_Rect bgrect;// = { 0, y+85, 720, 2 };
        bgrect.x = 0;
        bgrect.y = y+85;
        bgrect.w = 720;
        bgrect.h = 2;
        SDL_RenderFillRect(g_game->renderer, &bgrect);
    }

}
