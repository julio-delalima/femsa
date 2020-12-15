#include "mk_textinput.h"
#include "navi.h"
#include "game.h"
#include <algorithm>    // std::remove_if

#ifdef __ANDROID__
    #include <sys/stat.h>
    #include <android/log.h>

#endif // __ANDROID__

MK_Textinput::MK_Textinput()
{
    x = 0;
    y = 0;
    width = 720;
    height = 1280;
    type = MKG_TEXTINPUT;
    enabled = true;
    selected = true;
    max_len = -1;
    is_password = false;

    color.r = 255;
    color.g = 255;
    color.b = 255;
    color.a = 255;
}



MK_Textinput::MK_Textinput(std::string default_text, float _x, float _y, float _w, float _h)
{
    input_default_text = default_text;
    x = _x;
    y = _y;
    width = _w;
    height = _h;
    type = MKG_BUTTON;
    enabled = true;
    selected = false;
    max_len = -1;
    is_password = false;

    color.r = 0;
    color.g = 0;
    color.b = 0;
    color.a = 255;
}

bool my_predicate(char c)
{
    return !(c >= 32 && c<=126);
}


bool MK_Textinput::ProcessEvent(SDL_Event *event)
{
    if (enabled == false)
    {
        return false;
    }

    if (event->type == SDL_FINGERUP)
    {
        //we check all click areas
        #ifdef __ANDROID__
        float ix = (float)event->tfinger.x*720;
        float iy = (float)event->tfinger.y*1280.0f;

        //If the mouse is over the button
        if( ( ix > x ) && ( ix < x + width ) && ( iy > y ) && ( iy < y + height ) )
        {
            if (selected == false)
            {
                selected = true;
                SDL_StartTextInput();
            }
        }
        else if (selected == true)
        {
            selected = false;
            SDL_StopTextInput();
        }
        #endif
    }
    else if ( event->type == SDL_MOUSEBUTTONUP )
    {

        if( event->button.button == SDL_BUTTON_LEFT )
        {
            //Get the mouse offsets
            int ix = event->button.x;
            int iy = event->button.y;

            //If the mouse is over the button
            if( ( ix > x ) && ( ix < x + width ) && ( iy > y ) && ( iy < y + height ) )
            {
                if (selected == false)
                {
                    selected = true;
                    SDL_StartTextInput();
                    gNavi->started_text_input = true;
                }

            }
            else if (selected == true)
            {
                selected = false;
                if (gNavi->started_text_input == false)
                {
                    SDL_StopTextInput();
                }
            }

        }
    }

    if (selected == false)
        return false;
    if (event->type == SDL_TEXTINPUT )
    {
//        __android_log_print(ANDROID_LOG_ERROR, "MK", "asd %s", std::string(event->text.text).c_str());
        if (max_len == -1 || input_text.length() < max_len)
            input_text.append(event->text.text);

        input_text.erase(std::remove_if(input_text.begin(), input_text.end(), my_predicate), input_text.end());
    }
    else if (event->type == SDL_KEYDOWN )
    {
        //__android_log_print(ANDROID_LOG_ERROR, "MK", "asd %d", event->key.keysym.sym);
        switch (event->key.keysym.sym)
        {
            case SDLK_RETURN:
                //input_text = "";
                selected = false;
                SDL_StopTextInput();
                break;
            case SDLK_BACKSPACE:
                if (input_text.length()> 0)
                    input_text.erase(input_text.size() - 1);
                break;
        }

    }
    return false;
}

void MK_Textinput::Draw()
{
    int tempx = x;

    if (g_game != 0)
    {
        std::string cut_text = input_text;
        if (is_password == true)
        {
            std::string p;
            for (int i = 0; i < cut_text.size(); i++)
            {
                p.append("*");
            }
            cut_text = p;
            //std::cout << p << std::endl;
        }
        int text_w = g_game->MeasureTextWidth(cut_text, 43.0f)+5;

        while (text_w > width)
        {
            cut_text.erase(0,1);
            text_w = g_game->MeasureTextWidth(cut_text, 43.0f)+5;
        }

        if (input_text == "" && selected == false)
        {
            g_game->DrawText(tempx+9, y+3, input_default_text, 43.0f, false, color);
        }
        else
        {
            if (is_password == true)
            {
                std::string p;
                for (int i = 0; i < cut_text.size(); i++)
                {
                    p.append("*");
                }
                if (selected == true && SDL_GetTicks()%800 < 400)
                {
                    p.append("|");
                }
                g_game->DrawText(x+9, y+3, p, 43.0f, false, color);
            }
            else
            {
                std::string p = cut_text;
                if (selected == true && SDL_GetTicks()%800 < 400)
                {
                    p.append("|");
                }
                g_game->DrawText(tempx+9, y+3, p, 43.0f, false, color);
            }
        }
        return;
    }


    std::string cut_text = input_text;
    if (is_password == true)
    {
        std::string p;
        for (int i = 0; i < cut_text.size(); i++)
        {
            p.append("*");
        }
        cut_text = p;
        //std::cout << p << std::endl;
    }
    int text_w = gNavi->MeasureTextWidth(cut_text, 43.0f)+5;



    //we remove characters on the left until we have the proper width
    while (text_w > width)
    {
        cut_text.erase(0,1);
        text_w = gNavi->MeasureTextWidth(cut_text, 43.0f)+5;
    }

    if (input_text == "" && selected == false)
    {
        //gNavi->DrawText(tempx+9, y+3, input_default_text, 43.0f, false, color);
    }
    else
    {
        if (is_password == true)
        {
            std::string p;
            for (int i = 0; i < cut_text.length(); i++)
            {
                p.append("*");
            }
            if (selected == true && SDL_GetTicks()%800 < 400)
            {
                p.append("|");
            }
            //gNavi->DrawText(x+9, y+3, p, 43.0f, false, color);
        }
        else
        {
            std::string p = cut_text;
            if (selected == true && SDL_GetTicks()%800 < 400)
            {

                p.append("|");
            }
            //gNavi->DrawText(tempx+9, y+3, p, 43.0f, false, color);
        }
    }
}
