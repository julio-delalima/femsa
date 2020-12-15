#include "mk_button.h"

MK_Button::MK_Button()
{
    image = 0;
    x = 0;
    y = 0;
    width = 720;
    height = 1280;
    type = MKG_BUTTON;
    enabled = true;
    clickCallback = 0;
    action = -322;
    scale = 1;
}

MK_Button::MK_Button(std::string _image, float _x, float _y)
{
    image = new Mk_Image(_image);
    image->SetX(_x);
    image->SetY(_y);
    width = image->GetWidth();
    height = image->GetHeight();
    x = _x-width/2;
    y = _y-height/2;
    type = MKG_BUTTON;
    enabled = true;
    clickCallback = 0;
    action = -322;
    scale = 1;

}

MK_Button::MK_Button(float _x, float _y, float _w, float _h)
{
    image = 0;
    x = _x;
    y = _y;
    width = _w;
    height = _h;
    type = MKG_BUTTON;
    enabled = true;
    clickCallback = 0;
    action = -322;
    scale = 1;
}


bool MK_Button::ProcessEvent(SDL_Event *event)
{
    if (enabled == false)
    {
        return false;
    }
    if (event->type == SDL_FINGERUP)
    {
        float ix = event->tfinger.x*720;
        float iy = event->tfinger.y*1280.0f;
        //If the mouse is over the button
        if( ( ix > x - (width*(scale -1 )) ) && ( ix < x + (width*scale) ) && ( iy > y  - ( height *(scale -1 )) ) && ( iy < y + (height*scale) ) )
        {
            if (clickCallback != 0)
            {
                clickCallback(action, ix,iy);
            }
        }
    }
    else if ( event->type == SDL_MOUSEBUTTONUP )
    {
        if( event->button.button == SDL_BUTTON_LEFT )
        {
            //Get the mouse offsets
            int ix = event->button.x;
            int iy = event->button.y;
            //If the mouse is over the button
            if( ( ix > x - (width*(scale -1 )) ) && ( ix < x + (width*scale) ) && ( iy > y  - ( height *(scale -1 )) ) && ( iy < y + (height*scale) ) )
            {
                if (clickCallback != 0)
                {
                    clickCallback(action, ix,iy);
                }
            }
        }
    }
    return false;
}

void MK_Button::Draw()
{
    if (image != 0)
    {
        image->Draw();
    }
}
