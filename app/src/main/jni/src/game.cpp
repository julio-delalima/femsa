/* Copyright Master Kiwi 2015, Vicente Benavent */
#include "game.h"

#include <string>
#include <map>
#include <vector>
#include <ctime>

#include "script_enviroment.h"
#include "./textscanner.h"

#if defined(DEBUG_PHYSICS) || defined(DEBUG_WINDOW)
    #include "SDL2_gfx/SDL2_gfxPrimitives.h"
#endif

#ifdef EMSCRIPTEN
    extern void playSoundEM(std::string str);
    extern void stopSoundEM();
    extern void playEffectEM(std::string str);
    extern void stopEffectEM();
#endif

extern std::string MakeFileName(const char *path, bool bUserDir = false);

#ifdef __ANDROID__
    #include <sys/stat.h>
    #include <android/log.h>
#endif

#ifdef EMSCRIPTEN
    extern void playSoundEM(std::string str);
    extern void stopSoundEM();
#endif

#include <sstream>
#include <iostream>
#include "aes/Blowfish.h"

extern std::string base64_encode(unsigned char const* bytes_to_encode,unsigned int in_len);
extern std::string base64_decode(std::string const& encoded_string);

game *g_game;
#ifndef __WINPHONE__
    /*void shapeFreeWrap(cpSpace *space, cpShape *shape, void *unused)
    {
        cpSpaceRemoveShape(space, shape);
        cpShapeFree(shape);
    }

    void postShapeFree(cpShape *shape, cpSpace *space)
    {
        cpSpaceAddPostStepCallback(space, (cpPostStepFunc)shapeFreeWrap,shape, NULL);
    }

    void bodyFreeWrap(cpSpace *space, cpBody *body, void *unused)
    {
        cpSpaceRemoveBody(space, body);
        cpBodyFree(body);
    }

    void postBodyFree(cpBody *body, cpSpace *space)
    {
        cpSpaceAddPostStepCallback(space, (cpPostStepFunc)bodyFreeWrap, body, NULL);
    }*/
#endif

game::game()
{
    state_ = CREDITS;
    #ifdef DEBUG_WINDOW
        draw_physics = false;
        draw_click_area = false;
    #endif
}

game::~game()
{
    if (g_game->loaded_assets.begin() != g_game->loaded_assets.end())
    {
        for (std::vector<Asset *>::iterator it = g_game->loaded_assets.end();it != g_game->loaded_assets.begin();it--)
        {
            SDL_DestroyTexture((*it)->img_->sdl_texture);
            delete (*it);
        }
    }
    g_game->loaded_assets.clear();
    if (g_game->loaded_sound_effects.begin() != g_game->loaded_sound_effects.end())
    {
        for (map<int,Mix_Chunk*>::iterator it = g_game->loaded_sound_effects.end();it != g_game->loaded_sound_effects.begin();it--)
        {
            Mix_FreeChunk(it->second);
        }
    }
    g_game->loaded_sound_effects.clear();
    delete(g_script_enviroment);
}

bool flag = false;

void game::GameUpdate()
{
    g_script_enviroment->last_tick = SDL_GetTicks();

    SDL_Event event;
    while(SDL_PollEvent(&event))
    {
        if(event.type == SDL_APP_WILLENTERBACKGROUND)
        {
            g_game->previousState = g_game->state_;
            g_game->state_ = PAUSED;
        }
        if(event.type == SDL_APP_WILLENTERFOREGROUND)
        {
            g_game->state_ = g_game->previousState;
            if(!flag)
            {
                g_script_enviroment->frameRate *= 0.5f;
                flag = true;
            }
        }
        if (event.type == SDL_QUIT || (event.type == SDL_WINDOWEVENT && event.window.event == SDL_WINDOWEVENT_CLOSE))
        {
            #ifdef DEBUG_WINDOW
                SDL_DestroyWindow(SDL_GetWindowFromID(dw.windowID));
            #endif
            SDL_DestroyWindow(win);
            SDL_Quit();
            exit(0);
        }
        #ifdef DEBUG_WINDOW
            if (event.type == SDL_MOUSEMOTION)
            {
                if (event.motion.windowID == dw.windowID)
                    continue;
            }
            else if (event.type == SDL_MOUSEBUTTONDOWN)
            {
                if (event.motion.windowID == dw.windowID)
                    continue;
            }
            else if (event.type == SDL_MOUSEBUTTONUP)
            {
                if (event.motion.windowID == dw.windowID)
                {
                    dw.click(event.button.x,event.button.y);
                    continue;
                }
            }
        #endif
        #ifdef __ANDROID__
            if ( event.type == SDL_KEYDOWN  && event.key.keysym.sym == SDLK_AC_BACK)
            {
            #ifdef DEBUG_WINDOW
                SDL_DestroyWindow(SDL_GetWindowFromID(dw.windowID));
            #endif
                g_game->state_ = GAME_OVER;
                sendExitGame();
                //gNavi->SaveCurrentProjects();
                delete(gNavi);
                gNavi = 0;
                return;
            }
        #endif
        if (g_game->state_ == PLAYING)
        {
            float x = 0;
            float y = 0;
            switch(event.type)
            {
                case(SDL_KEYDOWN):
                    if (key_pressed[event.key.keysym.sym] == 0)
                    {
                        key_pressed[event.key.keysym.sym] = 1;
                        g_game->key_press(event.key.keysym.sym,1);
                    }
                    break;
                case(SDL_KEYUP):
                    if (key_pressed[event.key.keysym.sym] == 1)
                    {
                        key_pressed[event.key.keysym.sym] = 0;
                        g_game->key_press(event.key.keysym.sym,0);
                    }
                    break;
                case(SDL_MOUSEMOTION):
                    if (touch_started == true)
                    {
                        g_game->touch_move(event.motion.x, event.motion.y);
                    }
                    break;
                case(SDL_MOUSEBUTTONDOWN):
                    touch_started = true;
                    g_game->touch_start(event.button.x, event.button.y);
                    break;
                case(SDL_MOUSEBUTTONUP):
                    touch_started = false;
                    g_game->touch_end(event.button.x, event.button.y);
                    break;
//                case(SDL_FINGERMOTION): //los valores originales eran 720 y 1280 pero le subi para ajustar desface :0
//                    #ifdef __ANDROID__
//                        x = (event.tfinger.x) * 720.0f;
//                        y = (event.tfinger.y)* 1280.0f;
//                        g_game->touch_move(x, y);
//                    #endif
//                    break;
//                case(SDL_FINGERDOWN):
//                    #ifdef __ANDROID__
//                    SDL_Log("Picando x:%f, y: %f", event.tfinger.x, event.tfinger.y);
//                    x = (event.tfinger.x) * 720.0f;
//                    y = (event.tfinger.y)* 1280.0f;
//                    SDL_Log("Picando x:%f, y: %f", x, y);
//                        g_game->touch_start(x, y);
//                    #endif
//                    break;
//                case(SDL_FINGERUP):
//                    #ifdef __ANDROID__
//                    x = (event.tfinger.x) * 720.0f;
//                    y = (event.tfinger.y)* 1280.0f;
//                        g_game->touch_end(x, y);
//                    #endif
//                    break;
            }
        }
        else if(g_game->state_ == MAIN_MENU)
        {
            for (std::vector<MK_Gui*>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end(); ++it)
            {
                if(!gNavi->already_clicked)
                {
                    (*it)->ProcessEvent(&event);
                }
                else
                {
                    break;
                }
            }
        }
    }
    if (g_game->state_ == PLAYING)
    {
        #ifdef __ANDROID__
            static float x = 0;
            static float y = 0;
            static float z = 0;
            g_game->acceleration_change(x,y,z);
        #elif __APPLE__
            static float x = 0;
            static float y = 0;
            static float z = 0;
            g_game->acceleration_change(x,y,z);
        #endif
        g_game->update();
    }
    if(gNavi != 0)
    {
        gNavi->already_clicked = false;
    }
}

void game::Init()
{
    g_game->touch_started = false;

    int num_joysticks = SDL_NumJoysticks();
    if (num_joysticks == 0)
    {
        SDL_Log("No joysticks were found");
    }
    else
    {
        for(int joy_idx = 0; joy_idx < num_joysticks; ++joy_idx)
        {
            SDL_Joystick* joy = SDL_JoystickOpen(joy_idx);
            if (!joy)
            {
                SDL_Log("Unable to open joystick %d", joy_idx);
            }
            else
            {
                SDL_GameController* gamepad = SDL_GameControllerOpen(joy_idx);
                SDL_JoystickGUID guid = SDL_JoystickGetGUID(joy);
                char guid_str[1024];
                SDL_JoystickGetGUIDString(guid, guid_str, sizeof(guid_str));
                std::string n = FilterToValidAscii(SDL_JoystickName(joy),false);
                std::string n2 = FilterToValidAscii("Android Accelerometer",false);
                std::string n3 = FilterToValidAscii("iOS Accelerometer",false);
                if (n == n2 || n == n3)
                {
                    accelerometer = joy;
                }
                else
                {
                    if (gamepad)
                    {
                        SDL_GameControllerClose(gamepad);
                    }
                    SDL_JoystickClose(joy);
                }
            }
        }
    }
}

void game::Draw()
{
    SDL_SetRenderDrawColor(g_game->renderer, 0, 0, 0, 255);
    SDL_RenderClear(g_game->renderer);

    if (g_game->state_ == PLAYING)
    {
        g_game->draw();
        #ifdef SCREEN_PRINT
            int i = 0;
            for (std::list<message>::iterator it =  print_list.begin();  it != print_list.end(); it++)
            {
                DrawText(10, 1200 - (i++ *40), (*it).msg, 50.0f, false, white);
                if ((*it).die_time < SDL_GetTicks())
                {
                    it = print_list.erase(it);
                    it--;
                }
            }
        #endif
    }
    else if(g_game->state_ == MAIN_MENU)
    {
        for (std::vector<Asset*>::iterator it = gNavi->loaded_assets.begin(); it != gNavi->loaded_assets.end();++it)
        {
            (*it)->draw();
        }
        for (std::vector<MK_Gui*>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end();++it)
        {
            (*it)->Draw();
        }
        if (gNavi->has_game_over_score)
        {
            std::stringstream ss;
            ss << gNavi->game_over_score_;
            for (int i = 0; i < gNavi->game_over_scores_.size(); i++)
            {
                g_game->DrawText(gNavi->game_over_scores_[i].x,gNavi->game_over_scores_[i].y-48, ss.str(), 48, true,gNavi->game_over_scores_[i].color);
            }
        }
    }
    #ifdef DEBUG_WINDOW
        dw.draw();
        dw.drawGL();
    #endif
    SDL_RenderPresent(g_game->renderer);
}

int game::MeasureTextWidth(std::string t, float font_size)
{
    int biggest_width = 0;
    TTF_Font* myFont = game_font.GetFont(font_size);
    if (myFont == 0)
    {
        return 0;
    }

    std::vector<std::string> strs = StringTokenize(t, "\n");

    for (std::vector<std::string>::iterator it = strs.begin(); it != strs.end(); ++it)
    {
        t = (*it);

        // This creates the text surface
        SDL_Surface *stext = TTF_RenderText_Blended(myFont, t.c_str(), SDL_Color());

        // If it worked, then blit it to the main screen
        // If you wrap this function
        if (stext)
        {
            SDL_Texture *texture = SDL_CreateTextureFromSurface(renderer, stext);
            int w, h;
            SDL_QueryTexture(texture, NULL, NULL, &w, &h);

            if (w > biggest_width)
            {
                biggest_width = w;
            }
            SDL_DestroyTexture(texture);
        }

        SDL_FreeSurface(stext);
    }
    return biggest_width;
}

void game::clear_space()
{
    #ifndef __WINPHONE__
        /*cpSpaceEachShape(space_, (cpSpaceShapeIteratorFunc)postShapeFree, space_);
        cpSpaceEachBody(space_, (cpSpaceBodyIteratorFunc)postBodyFree, space_);
        cpSpaceStep(space_, 0.1);*/
    #endif
}
#ifndef __WINPHONE__
    /*cpShape* game::create_circle(float _x, float _y, float _rad, bool _is_static)
    {
        cpFloat radius = _rad;
        cpFloat mass = 1;
        if (_is_static == false)
        {
            // The moment of inertia is like mass for rotation
            // Use the cpMomentFor*() functions to help you approximate it.
            cpFloat moment = cpMomentForCircle(mass, 0, radius, cpvzero);

            // The cpSpaceAdd*() functions return the thing that you are adding.
            // It's convenient to create and add an object in one line.
            cpBody *ballBody = cpSpaceAddBody(space_, cpBodyNew(mass, moment));
            cpBodySetPosition(ballBody, cpv(_x, _y));

            // Now we create the collision shape for the ball.
            // You can create multiple collision shapes that point to the same body.
            // They will all be attached to the body and move around to follow it.
            cpShape *ballShape = cpSpaceAddShape(space_,cpCircleShapeNew(ballBody, radius,cpvzero));
            cpShapeSetFriction(ballShape, 0.7);

            return ballShape;
        }
        else
        {
            cpBody *body;
            cpShape *shape;
            body = cpBodyNew(INFINITY, INFINITY);
            shape = cpCircleShapeNew(body, radius, cpvzero);
            cpSpaceAddShape(space_, shape);
            cpBodySetPosition(body, cpv(_x, _y));

            cpShapeSetFriction(shape, 0.0f);
            return shape;
        }
    }*/
#endif

b2Body* game::create_rect_box2d(float _x, float _y, float _w, float _h,bool _is_static)
{
    if (world_->IsLocked())
    {
        b2PolygonShape *dynamicBox = new b2PolygonShape();
        dynamicBox->SetAsBox(_w/20.0f, _h/20.0f);

        b2FixtureDef fixtureDef;
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;

        b2BodyDef bodyDef;
        if (_is_static == false)
        {
            bodyDef.type = b2_dynamicBody;
        }
        bodyDef.position.Set(_x/10.0f, _y/10.0f);

        b2Body* body = world_->CreateBodyByDontAddToWorld(&bodyDef);
        g_script_enviroment->event_queue.push_back(EventData(EVENT_ADD_FIXTURE,body, fixtureDef));
        return body;
    }

    b2PolygonShape dynamicBox;
    dynamicBox.SetAsBox(_w/20.0f, _h/20.0f);

    b2FixtureDef fixtureDef;
    fixtureDef.shape = reinterpret_cast<b2Shape*>(&dynamicBox);

    fixtureDef.density = 1.0f;
    fixtureDef.friction = 0.0f;

    b2BodyDef bodyDef;
    if (_is_static == false)
    {
        bodyDef.type = b2_dynamicBody;
    }
    bodyDef.position.Set(_x/10.0f, _y/10.0f);


    b2Body* body = world_->CreateBody(&bodyDef);
    body->CreateFixture(&fixtureDef);
    body->SetSleepingAllowed(false);

    return body;
}

b2Body* game::create_rect_poly(float _x1, float _y1, float _x2, float _y2,
    float _x3, float _y3, float _x4, float _y4, bool _is_static)
{
    _x1 /= 10.0f;
    _x2 /= 10.0f;
    _x3 /= 10.0f;
    _x4 /= 10.0f;
    _y1 /= 10.0f;
    _y2 /= 10.0f;
    _y3 /= 10.0f;
    _y4 /= 10.0f;
    if (world_->IsLocked())
    {
        b2PolygonShape *dynamicBox = new b2PolygonShape();

        b2Vec2 vertices[4];
        vertices[0].Set(_x1, _y1);
        vertices[1].Set(_x2, _y2);
        vertices[2].Set(_x3, _y3);
        vertices[3].Set(_x4, _y4);

        dynamicBox->Set(vertices, 4);  // pass array to the shape

        b2FixtureDef fixtureDef;
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;

        b2BodyDef bodyDef;
        if (_is_static == false)
        {
            bodyDef.type = b2_dynamicBody;
        }
        bodyDef.position.Set((_x2-_x1)+_x1, (_y3-_y1)+_y1);

        b2Body* body = world_->CreateBodyByDontAddToWorld(&bodyDef);
        g_script_enviroment->event_queue.push_back(EventData(EVENT_ADD_FIXTURE,body, fixtureDef));
        return body;
    }

    b2PolygonShape dynamicBox;

    b2Vec2 vertices[4];
    vertices[0].Set(_x1, _y1);
    vertices[1].Set(_x2, _y2);
    vertices[2].Set(_x3, _y3);
    vertices[3].Set(_x4, _y4);

    dynamicBox.Set(vertices, 4);  // pass array to the shape

    b2FixtureDef fixtureDef;
    fixtureDef.shape = reinterpret_cast<b2Shape*>(&dynamicBox);
    fixtureDef.density = 1.0f;
    fixtureDef.friction = 0.0f;

    b2BodyDef bodyDef;
    if (_is_static == false)
    {
        bodyDef.type = b2_dynamicBody;
    }
    bodyDef.position.Set((_x2-_x1)+_x1, (_y3-_y1)+_y1);


    b2Body* body = world_->CreateBody(&bodyDef);
    body->CreateFixture(&fixtureDef);
    body->SetSleepingAllowed(false);

    return body;
}

b2Body* game::create_circle_box2d(float _x, float _y, float _rad,bool _is_static/* = false*/)
{
    if (world_->IsLocked())
     {
        b2CircleShape *circle = new b2CircleShape();
        circle->m_radius = _rad/10.0f;

        b2FixtureDef fixtureDef;
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        b2BodyDef bodyDef;
        if (_is_static == false)
        {
            bodyDef.type = b2_dynamicBody;
        }
        bodyDef.position.Set(_x/10.0f, _y/10.0f);

        b2Body* body = world_->CreateBodyByDontAddToWorld(&bodyDef);
        g_script_enviroment->event_queue.push_back(EventData(EVENT_ADD_FIXTURE,body, fixtureDef));
        return body;
    }
    b2CircleShape circle;
    circle.m_radius = _rad/10.0f;

    b2FixtureDef fixtureDef;
    fixtureDef.shape = reinterpret_cast<b2Shape*>(&circle);
    fixtureDef.density = 1.0f;
    fixtureDef.friction = 0.0f;
    fixtureDef.restitution = 0.0f;

    b2BodyDef bodyDef;
    if (_is_static == false)
    {
        bodyDef.type = b2_dynamicBody;
    }
    bodyDef.position.Set(_x/10.0f, _y/10.0f);
    b2Body* body = world_->CreateBody(&bodyDef);

    body->CreateFixture(&fixtureDef);
    body->SetSleepingAllowed(false);

    return body;
}

void game::init_game()
{
    SDL_Log("init game");
    if (game_id != 1)
    {
        b2Vec2 gravity(0.0f, 0.0f);
        world_ = new b2World(gravity);
        g_game->world_->SetWarmStarting(false);
        g_game->world_->SetContinuousPhysics(false);
        MyContactListener* myContactListenerInstance = new MyContactListener();
        world_->SetContactListener(myContactListenerInstance);

        #if defined(DEBUG_PHYSICS) || defined(DEBUG_WINDOW)
            FooDraw* f = new FooDraw();
            world_->SetDebugDraw(f);
            f->SetFlags(b2Draw::e_shapeBit);
        #endif
    }
    else
    {
		#if !defined(__WINPHONE__)
        /*space_ = cpSpaceNew();
        cpSpaceSetGravity(space_, cpv(0, 0));
        cpSpaceStep(space_, 0.1);*/
		#endif
    }

    load_errors_ = false;
    init();
}

void game::playSoundEffect(int id, int channel /* =-1*/)
{
    #if !defined(EMSCRIPTEN)
        if (loaded_sound_effects.find(id) != loaded_sound_effects.end() && isAudioAvailable)
        {
            if (channel != -1 && Mix_Playing(channel))
            {
                return;
            }
            int result =  Mix_PlayChannel(channel, loaded_sound_effects[id], 0);
            if(result == -1)
            {
                SDL_Log("Unable to play WAV file: %s", Mix_GetError());
            }
        }
    #else
        if (loaded_sound_effects.find(id) != loaded_sound_effects.end() && isAudioAvailable)
        {
            playEffectEM(loaded_sound_effects[id]);
        }
    #endif
}

void game::addSoundEffect(int id, std::string wav_file)
{
    #if !defined(EMSCRIPTEN) && !defined(__WINPHONE__)
        std::string path(getPreferencedPath());
        std::ifstream file;
        path = path + "/interface/data/" + wav_file;
        #ifdef __APPLE__
            music_name = MakeFileName(music_name.c_str(),true);
        #endif
        Mix_Chunk *soundEffect = Mix_LoadWAV(path.c_str());
        if( soundEffect == NULL )
        {
            SDL_Log("Failed to load sound effect! SDL_mixer Error: %s %s\n", Mix_GetError(),wav_file.c_str());
            return;
        }
        SDL_Log("Registro: %s",wav_file.c_str());
        loaded_sound_effects[id] = soundEffect;
    #else
        //loaded_sound_effects[id] = wav_file;
    #endif
}

#if defined(DEBUG_PHYSICS) || defined(DEBUG_WINDOW)
    void FooDraw::DrawSolidCircle(const b2Vec2& center, float32 radius,const b2Vec2& axis, const b2Color& color)
    {
        filledCircleColor(gNavi->renderer, center.x*10.0f, center.y*10.0f,radius*10.0f, 0x77ffffff);
    }

    void FooDraw::DrawSolidPolygon(const b2Vec2* vertices, int32 vertexCount,const b2Color& color)
    {
        int c = vertexCount;
        //  ChipmunkDebugDrawPolygon(poly->numVerts, poly->tVerts);
        Sint16 *xlist = new Sint16[c];
        Sint16 *ylist = new Sint16[c];

        for (int i=0; i < c; i++)
        {
            xlist[i] = static_cast<int>(vertices[i].x*10.0f);
            ylist[i] = static_cast<int>(vertices[i].y*10.0f);
        }

        filledPolygonColor(gNavi->renderer, xlist, ylist, c, 0x77ffffff);
        delete (xlist);
        delete (ylist);
    }
#endif

void game::draw()
{
    if (g_game->state_ != GAME_OVER && g_game->state_ != WAITING)
    {
        g_script_enviroment->call_game_draw();
        #if defined(DEBUG_PHYSICS)
            world_->DrawDebugData();
        #endif

        #ifdef DEBUG_WINDOW
            if (draw_click_area == true)
            {
                for (std::map <int, ClickArea>::iterator it =
                    g_script_enviroment->click_area_list.begin();
                    it != g_script_enviroment->click_area_list.end();
                    ++it)
               {
                    ClickArea *ca = &(*it).second;
                    boxColor(gNavi->renderer, ca->x, ca->y, ca->x+ca->w,ca->y+ca->h, 0x44000000);
                }
            }

            if (draw_physics == true)
            {
                //world_->DrawDebugData();
            }
        #endif
    }
}

void game::update()
{
    //SDL_Log("State %s", state_);
    if (state_ != GAME_OVER && state_ != WAITING)
    {
        g_script_enviroment->call_game_update();
    }
}

void game::touch_start(float _x, float _y)
{
    SDL_Log("touch start");
//     _x = (_x * 1.02f);
//     _y = (_y * 1.02f);
    if (g_script_enviroment->is_moving_to_menu_ == true)
    {
        SDL_Log("in moving to menu");
        return;
    }

    if (state_ != GAME_OVER && state_ != WAITING)
    {
        if (g_script_enviroment->touch_start_.IsNull() == false)
        {
            g_script_enviroment->touch_start_.Execute(_x, _y);
        }
    }
}

void game::touch_end(float _x, float _y)
{
    SDL_Log("Touch end %f, %f", _x, _y);
//    _x = (_x * 1.02f);
//    _y = (_y * 1.02f);
    SDL_Log("Touch end post %f, %f", _x, _y);
    if (g_script_enviroment->is_moving_to_menu_ == true)
    {
        return;
    }

    if (state_ != GAME_OVER && state_ != WAITING)
    {
        if (g_game->state_ != PLAYING|| g_script_enviroment->is_moving_to_menu_ == true)
        {}
        else
        {
            for (std::map <int, ClickArea>::iterator it =
                g_script_enviroment->click_area_list.begin();
                it != g_script_enviroment->click_area_list.end();
                ++it)
            {
                ClickArea *ca = &(*it).second;
                // If the mouse is over the button
                if ((_x > ca->x) && (_x < ca->x + ca->w) && (_y > ca->y) && (_y < ca->y + ca->h))
                {
                    SDL_Log("Click area %f, %f", ca->x, ca->y);
                    try
                    {
                        g_script_enviroment->functors_[(*it).first].Execute(_x, _y, ca->name);
                    }
                    catch (Sqrat::Exception e)
                    {
                        SDL_Log("Error calling area function error: %s",e.Message().c_str());
                    }
                }
            }
        }
        if (g_script_enviroment->touch_end_.IsNull() == false)
        {
                g_script_enviroment->touch_end_.Execute(_x, _y);
                SDL_Log("Touch end execute %f, %f", _x, _y);
        }
    }
}

void game::touch_move(float _x, float _y)
{
    if (g_script_enviroment->is_moving_to_menu_ == true)
    {
        return;
    }

    if (state_ != GAME_OVER && state_ != WAITING)
    {
        if (g_script_enviroment->touch_move_.IsNull() == false)
        {
                g_script_enviroment->touch_move_.Execute(_x, _y);
        }
    }
}

void game::start_level(int level_id)
{
    SDL_Log("Game.cpp - Start_level");
    if (!gNavi->next_is_play && g_game->state_ != GAME_OVER_STATS && gNavi->game_has_stats == 1 && gNavi->game_stats_begin == 1)
    {
        SDL_Log("Next is play");
        gNavi->next_is_play = true;
        g_game->state_ = GAME_OVER_STATS;
        gNavi->AddMenu(gNavi->menu_stats);
        return;
    }

    gNavi->next_is_play = false;
    gNavi->game_over_score_ = 0;

    if (show_register_data)
    {
        state_ = REGISTER_DATA;
        gNavi->load_register_data();
        return;
    }

    g_script_enviroment->call_start_level(level_id);

	#if !defined(EMSCRIPTEN) && !defined(__WINPHONE__)
        if (gNavi->gameplay_music != 0)
        {
            if (gNavi->music_.find(gNavi->gameplay_music) != gNavi->music_.end())
            {
                if (gNavi->current_music_ != gNavi->music_[gNavi->gameplay_music])
                {
                    gNavi->current_music_ = gNavi->music_[gNavi->gameplay_music];
                    Mix_PlayMusic(gNavi->music_[gNavi->gameplay_music], -1);
                }
            }
        }
        else
        {
            gNavi->current_music_ = 0;
            Mix_HaltMusic();
        }
	#elif defined(__WINPHONE__)
    #else
        if (gNavi->gameplay_music != 0)
        {
            if (gNavi->music_.find(gNavi->gameplay_music) != gNavi->music_.end())
            {
                std::string str = gNavi->music_[gNavi->gameplay_music];
                playSoundEM(str);
            }
        }
        else
        {
            stopSoundEM();
        }
    #endif
}

void game::SaveTop10(std::string name, int value)
{
    SDL_Log("Save top 10");
    if (name == "")
    {
        name = "Sin registro";
    }
    std::stringstream ss3;
    ss3 << "interface/data/" << gNavi->current_project->id << "_s1.txt";
    TextScanner b2(ss3.str());

    std::stringstream ss_save;
    ss_save << RandomRange(0, 322) << "?-?";
    ss_save << name << "?-?";
    ss_save << value;

    std::string v = encrypt_text("!origin_OS_V1.0!",(char*)(ss_save.str().c_str()),ss_save.str().length());
    std::string res_string = base64_encode((byte*)v.c_str(), v.length());

    std::vector<std::string> parts;
    bool inserted = false;
    int i = 0;
    for (i = 0; i < b2.lineList.size(); i++)
    {
        std::string anyData = base64_decode(b2.lineList[i]);
        anyData = decrypt_text("!origin_OS_V1.0!",(char*)anyData.c_str(),anyData.length());

        parts.clear();
        parts = StringTokenize(anyData, "?-?");

        if (StringToInt(parts[2]) < value)
        {
            b2.lineList.insert(b2.lineList.begin()+(i), res_string);
            inserted = true;
            break;
        }
    }

    if (inserted == false && i < 10)
    {
        b2.lineList.push_back(res_string);
    }
    if (inserted == true && b2.lineList.size() > 10)
    {
        b2.lineList.erase(b2.lineList.end()-1);
    }

    b2.SaveFile(ss3.str());
}

void game::SaveScoreAlways()
{
    SDL_Log("Save score as always");
    std::stringstream ss3;
    ss3 << "interface/data/" << gNavi->current_project->id << "_sv.txt";
    TextScanner b2(ss3.str());

    std::stringstream ss_save;
    ss_save << 1337 << "?-?";
    ss_save << RandomRange(0, 322) << "?-?";
    //we add php time
    time_t seconds = time(NULL);
    ss_save << seconds << "?-?";

    for (int i = 0; i < g_game->user_data.size(); i++)
    {
        ss_save << g_game->user_data[i] << "?-?";
    }

    #if defined(WIN32)
        int platform_id = 1; // Windows Exe
    #elif defined(IS_WEB_PREVIEW)
        int platform_id = 2; // Preview
    #elif defined(FACEBOOK)
        int platform_id = 7; // facebook
    #elif defined(EMSCRIPTEN)
        int platform_id = 3; // Web
    #elif defined(__ANDROID__) && defined(IS_DOWNLOAD_APP)
        int platform_id = 4; // Android App
    #elif defined(__ANDROID__)
        int platform_id = 5; // Android
    #elif defined(__APPLE__) && defined(IS_DOWNLOAD_APP)
        int platform_id = 6; // iOS App
    #elif defined(__APPLE__)
        int platform_id = 8; // iOS
    #endif

    ss_save << platform_id << "?-?";

    ss_save << gNavi->game_over_score_;

    string newData =  gNavi->naviEncrypt(ss_save.str());
    b2.lineList.push_back(newData);

    b2.SaveFile(ss3.str());

    if (g_game->user_data.size() > 0)
    {
        SaveTop10(g_game->user_data[0], gNavi->game_over_score_);
    }
    else
    {
        SaveTop10("Sin registro", gNavi->game_over_score_);
    }

    g_game->user_data.clear();
}

void game::SaveScore()
{
    SDL_Log("save escore");
    if (gNavi->game_has_stats == 1  && gNavi->game_stats_begin == 1)
    {
        SaveScoreAlways();
    }
}

void game::key_press(int _key, int _state)
{
    if (g_script_enviroment->is_moving_to_menu_ )
    {
        return;
    }

    if (_key == SDLK_LEFT)
    {
        _key = 0;
    }
    else if (_key == SDLK_RIGHT)
    {
        _key = 1;
    }
    else if (_key == SDLK_UP)
    {
        _key = 2;
    }
    else if (_key == SDLK_DOWN)
    {
        _key = 3;
    }

    if (g_script_enviroment->get_arrow_keys_func_.IsNull() == false)
    {
        g_script_enviroment->get_arrow_keys_func_.Execute(_key, _state);
    }
}


void game::acceleration_change(float _x, float _y, float _z)
{
    if (g_script_enviroment->accel_func_.IsNull() == false)
    {
        GetAccel(_x, _y, _z);
        g_script_enviroment->accel_func_.Execute(_x, _y, _z);
    }
    else
    {
        //SDL_Log("No encontre accel");
    }
}

void game::GetAccel(float &x, float &y, float &z)
{
    if (accelerometer != 0)
    {
        int _x = 0;
        int _y = 0;
        int _z = 0;
        _x = SDL_JoystickGetAxis(accelerometer,0);
        _y = SDL_JoystickGetAxis(accelerometer,1);
        _z = SDL_JoystickGetAxis(accelerometer,2);
        x = _x;
        y = _y;
        z = _z;
        x *= (1.0f/8.3f);
        y *= (1.0f/8.3f);
        z *= (1.0f/8.3f);
        x*=0.0026f;
        y*=0.0026f;
        z*=0.0026f;
        return;
    }
    x = 0;
    y = 0;
    z = 0;
}

// this will load a required or optional asset
Asset* game::load_asset(int type_2, bool optional, bool manual_draw)
{
    Asset *c = new Asset();

    int x = 0;
    int y = 0;
    int flip = 0;
    int angle = 0;
    c->load_asset_by_type_2(type_2);
    if (c->d_ == 0)
    {
        if (optional)
        {
            return 0;
        }
        SDL_Log("The required asset dosen't exists please create it at the website");
        load_errors_ = true;
        return 0;
    }
    if (c->d_->type_ == -1)
    {
        if (!optional)
        {
            SDL_Log("The required asset dosen't exists please create it at the website");
            load_errors_ = true;
        }
        return 0;
    }
    if (c->d_->type_ != 2)
    {
        c->move(x, y);
        c->img_->rotation = (angle);
        if (flip == 1)
        {
            c->img_->flip = SDL_FLIP_HORIZONTAL;
            if (angle > 0 || angle < 0)
            {
                c->img_->rotation = ((360-(c->img_->rotation)));
            }
        }
    }
    else
    {
        load_errors_ = true;
    }
    if (!manual_draw)
    {
        loaded_assets.push_back(c);
    }
    return c;
}

void game::DrawText(float x, float y, std::string t, float font_size,bool align_center, SDL_Color &color)
{
    TTF_Font* myFont = game_font.GetFont(font_size);
    std::vector<std::string> strs = StringTokenize(t, "\n");
    for (std::vector<std::string>::iterator it = strs.begin();it != strs.end();++it)
     {
        t = (*it);
        SDL_Surface *stext = TTF_RenderText_Blended(myFont, t.c_str(), color);
        if (stext)
        {
            SDL_Texture *texture = SDL_CreateTextureFromSurface(g_game->renderer,stext);
            static int w, h;
            SDL_QueryTexture(texture, NULL, NULL, &w, &h);
            static SDL_Rect DestR;
            DestR.x = x;
            DestR.y = y;
            DestR.w = w;
            DestR.h = h;
            if (align_center == true)
            {
                DestR.x = x-w/2;
            }
            if(gNavi->current_project->game_type_ == 1307)
            {
                SDL_RenderCopyEx(g_game->renderer,texture,0,&DestR,90,NULL,SDL_FLIP_NONE);
            }
            else
            {
                SDL_RenderCopy(g_game->renderer,texture,0,&DestR);
            }
            SDL_FreeSurface(stext);
            SDL_DestroyTexture(texture);
            y+=h;
        }
    }
}
