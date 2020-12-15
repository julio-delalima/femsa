#include "script_enviroment.h"
#include "game.h"
#include "miscutils.h"
#include "textscanner.h"
#include "sqrat/sqstdmath.h"
#include "sqrat/sqstdstring.h"
#include "sqrat/sqstdsystem.h"
#include "sqrat/sqstdblob.h"
#include <iostream>
#include "script_enviroment.h"
#include "SDL2_gfx/SDL2_gfxPrimitives.h"

#ifdef __ANDROID__
    #include <android/log.h>
#endif


script_enviroment *g_script_enviroment;

script_enviroment::script_enviroment()
{
	vm_squirrel_ = 0;
    frameRate = 1 / 45.0f;
}

void script_enviroment::init()
{
	BindSquirrel();
	load_scripts();
	functor_count = 0;
}

script_enviroment::~script_enviroment()
{
	//delete img;
	for (std::vector<collision_handlers*>::iterator it = handlers_.begin(); it != handlers_.end(); ++it)
	{
		delete (*it);
	}
	handlers_.clear();

    g_script_enviroment->destroySquirrelVM();
}

void script_enviroment::destroySquirrelVM()
{
    for (std::map<int, Sqrat::Function>::iterator it = functors_.begin(); it != functors_.end(); ++it)
    {
        (*it).second.Release();
    }
    functors_.clear();

    for (std::map<int, std::map<int,Sqrat::Function> >::iterator it = handlers_box2d_.begin(); it != handlers_box2d_.end(); ++it)
    {
        for (std::map<int, Sqrat::Function>::iterator it2 = (*it).second.begin(); it2 != (*it).second.end(); ++it2)
        {
            (*it2).second.Release();
        }
    }
    handlers_box2d_.clear();

    for (std::map<int, std::map<int,Sqrat::Function> >::iterator it = handlers_box2d_end_.begin(); it != handlers_box2d_end_.end(); ++it)
    {
        for (std::map<int, Sqrat::Function>::iterator it2 = (*it).second.begin(); it2 != (*it).second.end(); ++it2)
        {
            (*it2).second.Release();
        }
    }
    handlers_box2d_end_.clear();

    draw_func_.Release();
    start_level_func_.Release();
    update_func_.Release();
    get_arrow_keys_func_.Release();
    accel_func_.Release();
    touch_start_.Release();
    touch_move_.Release();
    touch_end_.Release();
    //Sqrat::RootTable().Release();
    sq_clear(vm_squirrel_,0);
    sq_close(vm_squirrel_);
}

void print_zax(std::string string_to_print)
{
    if(g_script_enviroment->allowGamePrint)
    {
        SDL_Log("GamePrint: %s", string_to_print.c_str());
        #ifdef SCREEN_PRINT
            gNavi->print_list.push_back(message(string_to_print,SDL_GetTicks()+600));
        #endif
    }
}

void script_enviroment::load_scripts()
{}

void script_enviroment::load_game_from_raw(std::string &scr_text)
{
    SDL_Log("Load game data from raw: %s", &scr_text);
	Sqrat::Script script_game;
	try
	{
        script_game.CompileString(scr_text);
		script_game.Run();
	}
	catch (Sqrat::Exception e)
	{
        SDL_Log("Compile and run error: %s",e.Message().c_str());
	}

	Sqrat::Function init_func = Sqrat::RootTable().GetFunction("init");
	if (init_func.IsNull())
	{
        SDL_Log("Init function not found");
		return;
	}
	try
	{
		is_moving_to_menu_ = false;
		init_func.Execute();
	}
	catch (Sqrat::Exception e)
	{
        SDL_Log("Init function error: %s",e.Message().c_str());
	}

	start_level_func_ = Sqrat::RootTable().GetFunction("start_level");
	if (start_level_func_.IsNull())
	{
        SDL_Log("Start level function not found");
		return;
	}
	update_func_ = Sqrat::RootTable().GetFunction("update");
	if (update_func_.IsNull())
	{
        SDL_Log("Update function not found");
		return;
	}
	draw_func_ = Sqrat::RootTable().GetFunction("draw");
	if (draw_func_.IsNull())
	{
        SDL_Log("Draw function not found");
		return;
	}
	accel_func_ = Sqrat::RootTable().GetFunction("on_accelerometer_update");
	if (accel_func_.IsNull())
	{
        SDL_Log("Accelerometer function not found!");
	}
    SDL_Log("Game loaded properly");
	get_arrow_keys_func_ = Sqrat::RootTable().GetFunction("get_arrow_keys");
	touch_start_ = Sqrat::RootTable().GetFunction("touch_start");
	touch_move_ = Sqrat::RootTable().GetFunction("touch_move");
	touch_end_ = Sqrat::RootTable().GetFunction("touch_end");
}

void script_enviroment::load_game(std::string game_name)
{
#ifdef USE_ENCRYPTION
    std::string scr = gNavi->decrypt_file(ModifyFileExtension("interface/g/" + game_name, "bin"));
#else
    TextScanner b("interface/g/" + game_name);
#endif

	std::string scr_end;

#ifdef USE_ENCRYPTION
	scr_end = scr;
#else
	scr_end = b.GetAll();
#endif
	SDL_Log("Script: %s",scr_end.c_str());
	load_game_from_raw(scr_end);
}

void script_enviroment::call_start_level(int level_id)
{
    SDL_Log("Call start level");
	is_moving_to_menu_ = false;
    activePhysics = true;
	try
	{
	    gNavi->call_function_after_time.clear();
	    gNavi->goto_menu_after_time.clear();
		start_level_func_.Execute(level_id);
	}
	catch (Sqrat::Exception e)
	{
        SDL_Log("StartLevel Function Error: %s",e.Message().c_str());
	}
}

Asset* load_asset(int id, bool optional)
{
	if (g_game != 0)
	{
        Asset* a = g_game->load_asset(id, optional, true);
		return a;
	}
	return 0;
}

void draw_asset(Asset* _a)
{
	_a->draw();
}

void script_enviroment::call_game_draw()
{
	try
	{
		draw_func_.Execute();
	}
	catch (Sqrat::Exception e)
	{
        SDL_Log("Draw Function Error: %s",e.Message().c_str());
	}
}

void goto_menu(std::string menu)
{
    SDL_Log("goto menu");
	g_script_enviroment->is_moving_to_menu_ = true;
	SDL_Log("Holi %d", g_game);
	if (g_game == 0)
	{
		return;
	}
	g_game->state_ = GAME_OVER;
    if (gNavi->game_over_menu == -1)
	{
        SDL_Log("Menu not found, please add a menu (srcipEnviroment)");
		g_game->state_ = WAITING;
		return;
	}
    gNavi->call_function_after_time.clear();
    gNavi->goto_menu_after_time.clear();
    SDL_Log("Cargando menu [%d]", gNavi->game_over_menu);
    gNavi->AddMenu(gNavi->game_over_menu);
}
void removeBodySafely(b2Body* body)
{
    void *ud = body->GetUserData();
    if(ud)
	{
		delete (ud);
	}
    g_game->world_->DestroyBody(body);
}

void clear_functions_after_time()
{
    SDL_Log("clear_functions_after_time");
    gNavi->call_function_after_time.clear();
}


void script_enviroment::call_game_update()
{
	try
	{
        std::map <int,Uint32>::iterator it2;
        for (std::map <int,Uint32>::iterator it = gNavi->call_function_after_time.begin(); it != gNavi->call_function_after_time.end(); ++it)
        {
            if (SDL_GetTicks() >= (*it).second)
            {
                g_script_enviroment->functors_[(*it).first].Execute();
                it2 = gNavi->call_function_after_time.begin();
                if (it2 == gNavi->call_function_after_time.end())
                {
					return;
				}
                gNavi->call_function_after_time.erase(it);
                it = gNavi->call_function_after_time.begin();
                if (it == gNavi->call_function_after_time.end())
				{
					return;
				}
            }
        }

        for (std::vector <Uint32>::iterator it = gNavi->goto_menu_after_time.begin(); it != gNavi->goto_menu_after_time.end(); ++it)
        {
            if (SDL_GetTicks() >= (*it))
            {
                gNavi->goto_menu_after_time.erase(it);
                goto_menu("test");
                --it;
                break;
            }
        }
	}
	catch (Sqrat::Exception e)
	{
        SDL_Log("1Update Function Error: %s",e.Message().c_str());
	}

    if(activePhysics)
    {
        g_game->world_->ClearForces();
        if (body_remove_box2d.size() > 0)
        {
            for (std::list<b2Body *>::iterator it = body_remove_box2d.begin();it != body_remove_box2d.end(); it++)
            {
                removeBodySafely((*it));
            }
            body_remove_box2d.clear();
        }
        if (g_script_enviroment->is_moving_to_menu_)
        {
            event_queue.clear();
            return;
        }
        g_game->world_->Step(frameRate, 6, 2);
    }

    //TODO: CAN OPTIMIZE
    for (std::vector <EventData>::iterator it = event_queue.begin(); it != event_queue.end(); ++it)
    {
        switch ((*it).type)
        {
        case EVENT_ADD_FIXTURE:
            g_game->world_->AddBodyAndAddToWorld((*it).body);
            (*it).body->CreateFixture(&(*it).fixDef);
            (*it).body->SetSleepingAllowed(false);
            delete ((*it).fixDef.shape );
            break;
        case EVENT_MOVE:
            (*it).body->SetTransform(b2Vec2((*it).val1/10.0f,(*it).val2/10.0f),(*it).body->GetAngle());
            break;
        case EVENT_ROTATE:
            (*it).body->SetTransform((*it).body->GetPosition(),(*it).val1);
            break;
        }
    }
    event_queue.clear();

	try
	{
		update_func_.Execute();
	}
	catch (Sqrat::Exception e)
	{
        SDL_Log("2Update Function Error: %s",e.Message().c_str());
	}
}

void switchPhysics()
{
    g_script_enviroment->activePhysics = !g_script_enviroment->activePhysics;
}

void phy_set_gravity(float x, float y)
{
    g_game->world_->SetGravity(b2Vec2(x/10.0f,y/10.0f));
}

void phy_move_shape(b2Body* _s,float x, float y)
{
    if (g_game->world_->IsLocked())
    {
        g_script_enviroment->event_queue.push_back(EventData(EVENT_MOVE,_s,x,y));
        return;
    }
    _s->SetTransform(b2Vec2(x/10.0f,y/10.0f),_s->GetAngle());
}

vector2f phy_get_shape_position(b2Body* _s)
{
    vector2f v;
    b2Vec2 cv = _s->GetPosition();
    v.x = cv.x*10.0f;
    v.y = cv.y*10.0f;
	return v;
}

vector2f phy_get_shape_velocity(b2Body* _s)
{
    vector2f v;
    b2Vec2 cv = _s->GetLinearVelocity();
    v.x = cv.x*10.0f;
    v.y = cv.y*10.0f;
	return v;
}

void phy_shape_set_collision_type(b2Body* _s, int _type)
{
    int *n = new int(_type);
	_s->SetUserData(n);
}

b2Body* create_circle_shape_for_asset_static(Asset* _a, float radius, bool is_static)
{
	return g_game->create_circle_box2d(_a->get_x(), _a->get_y(), radius, is_static);
}

b2Body* create_circle_shape_for_asset(Asset* _a, float radius)
{
	return g_game->create_circle_box2d(_a->get_x(), _a->get_y(), radius,false);
}

b2Body* create_shape_for_asset(Asset* _a, bool is_static)
{
	return g_game->create_rect_box2d(_a->get_x(), _a->get_y(), _a->get_width(40), _a->get_height(40), is_static);
}

b2Body* create_rect_shape(float x, float y, float w, float h, bool is_static)
{
	return g_game->create_rect_box2d(x, y, w, h, is_static);
}

void rotate_asset(Asset* _a, float angle)
{
	if (_a->img_ != 0)
	{
        _a->img_->rotation = (angle);
	}
}

void move_asset(Asset* _a, float pos_x, float pos_y)
{
	_a->move(pos_x, pos_y);
}

void goto_menu_after_ms(std::string menu, int after_ms)
{
    SDL_Log("goto_menu_after_ms");
    gNavi->goto_menu_after_time.push_back(SDL_GetTicks()+after_ms);
    g_script_enviroment->is_moving_to_menu_ = true;
}

void MyContactListener::EndContact(b2Contact* contact)
{
    SDL_Log("EndContact");
    void* bodyAUserData = contact->GetFixtureA()->GetBody()->GetUserData();
    void* bodyBUserData = contact->GetFixtureB()->GetBody()->GetUserData();

    if (bodyAUserData == 0 || bodyBUserData == 0)
	{
		return;
	}

    int _type1 = *((int*)bodyAUserData);
    int _type2 = *((int*)bodyBUserData);

    b2Body* b1 = contact->GetFixtureA()->GetBody();
    b2Body* b2 = contact->GetFixtureB()->GetBody();

	//lower first
	if (_type2 < _type1)
	{
		int temp = _type1;
		_type1 = _type2;
		_type2 = temp;
		b2Body* btemp = b2;
		b2 = b1;
		b1 = btemp;
	}

    if (g_script_enviroment->handlers_box2d_end_[_type1][_type2].IsNull() == false)
    {
        try
        {
            g_script_enviroment->handlers_box2d_end_[_type1][_type2].Execute(b1, b2);
    	}
        catch (Sqrat::Exception e)
        {
            SDL_Log("Contact function run error: ",e.Message().c_str());
        }
    }
}

void MyContactListener::BeginContact(b2Contact* contact)
{
    SDL_Log("BeginContact");
    void* bodyAUserData = contact->GetFixtureA()->GetBody()->GetUserData();
    void* bodyBUserData = contact->GetFixtureB()->GetBody()->GetUserData();

    if (bodyAUserData == 0 || bodyBUserData == 0)
        return;

    int _type1 = *((int*)bodyAUserData);
    int _type2 = *((int*)bodyBUserData);

    b2Body* b1 = contact->GetFixtureA()->GetBody();
    b2Body* b2 = contact->GetFixtureB()->GetBody();

	//lower first
	if (_type2 < _type1)
	{
		int temp = _type1;
		_type1 = _type2;
		_type2 = temp;
		b2Body* btemp = b2;
		b2 = b1;
		b1 = btemp;
	}

    if (g_script_enviroment->handlers_box2d_[_type1][_type2].IsNull() == false)
    {
        try
        {
            g_script_enviroment->handlers_box2d_[_type1][_type2].Execute(b1, b2);
    	}
        catch (Sqrat::Exception e)
        {
            SDL_Log("Contact function run error: %s",e.Message().c_str());
        }

    }
}

void phy_set_sensor(b2Body* s, bool sensor)
{
    s->GetFixtureList()->SetSensor(true);
}

void phy_create_collision_handler_begin(int _type1, int _type2, Sqrat::Function _functor)
{
	//lower first
	if (_type2 < _type1)
	{
		int temp = _type1;
		_type1 = _type2;
		_type2 = temp;
	}
	g_script_enviroment->handlers_box2d_[_type1][_type2] = _functor;
}

void phy_create_collision_handler_end(int _type1, int _type2, Sqrat::Function _functor)
{
	//lower first
	if (_type2 < _type1)
	{
		int temp = _type1;
		_type1 = _type2;
		_type2 = temp;
	}
	g_script_enviroment->handlers_box2d_end_[_type1][_type2] = _functor;
}

void phy_remove_shape(b2Body* _s)
{
    g_script_enviroment->body_remove_box2d.push_back(_s);
}

void create_click_area(std::string name, float x, float y, float w, float h, Sqrat::Function functor)
{
    SDL_Log("Click area en x: %f, y: %f, w: %f, h: %f", x, y, w, h);
	if (functor.IsNull())
	{
        SDL_Log("Functor is null");
		return;
	}
    g_script_enviroment->functors_[g_script_enviroment->functor_count] = functor;
    g_script_enviroment->click_area_list[g_script_enviroment->functor_count++] = ClickArea(x-w/2,y-h/2,w,h, name);
    //g_script_enviroment->click_area_list[g_script_enviroment->functor_count++] = ClickArea(x-w/2,y-h/2,x+w/2,y+h/2, name);
}

std::string substring(std::string str, int val1, int val2)
{
	str = str.substr(val1, val2);
	return str;
}

int random_int(int min, int max)
{
	return RandomRange(min, max + 1);
}

void script_enviroment::reset()
{
    SDL_Log("Reset, not works");

	for (std::vector<collision_handlers*>::iterator it = handlers_.begin(); it != handlers_.end(); ++it)
	{
		delete (*it);
	}
	handlers_.clear();
	functors_.clear();
}

void script_enviroment::forceGameOver()
{
    Sqrat::Function gameOver = Sqrat::RootTable().GetFunction("realGameOver");
    gameOver.Execute();
}

bool compare_shapes(b2Body* p1, b2Body *p2)
{
	if (p1 == p2)
	{
		return true;
	}
	return false;
}

bool compare_vectors(vector2f* p1, vector2f* p2)
{
	if (p1 == p2)
	{
		return true;
	}
	return false;
}

bool compare_assets(Asset* p1, Asset* p2)
{
	if (p1 == p2)
	{
		return  true;
	}

	return false;
}

float asset_get_width(Asset* a)
{
    return a->get_width(64.0f);
}

void asset_set_scale(Asset* a, float scalex, float scaley)
{
    a->img_->flip = SDL_FLIP_NONE;
    if(scalex < 0)
    {
        a->img_->flip = SDL_FLIP_HORIZONTAL;
    }
    if(scaley < 0)
    {
        a->img_->flip = SDL_FLIP_VERTICAL;
    }
    a->img_->scale_x = abs(scalex);
    a->img_->scale_y = abs(scaley);
}

float asset_get_height(Asset* a)
{
	return a->get_height(64.0f);
}

unsigned int get_time()
{
    return SDL_GetTicks();
}

void call_function_after_time(Sqrat::Function f, int ms)
{
//    SDL_Log("call_function_after_time");
    g_script_enviroment->functors_[g_script_enviroment->functor_count] = f;
    gNavi->call_function_after_time[g_script_enviroment->functor_count++] = SDL_GetTicks()+ms;
}

void phy_reset_forces(b2Body* p1)
{
    p1->SetLinearVelocity(b2Vec2(0,0));
    p1->SetAngularVelocity(0);
}

void phy_shape_add_impulse(b2Body* p1, float x, float y)
{
    p1->ApplyLinearImpulse(b2Vec2(x*2.5f,y*2.5f),p1->GetPosition(),true);
}

void phy_shape_add_force(b2Body* p1, float x, float y)
{
    p1->ApplyForceToCenter(b2Vec2(x*8.0f,y*8.0f),true);
}

void phy_shape_set_velocity(b2Body* p1, float x, float y)
{
    p1->SetLinearVelocity(b2Vec2(x*5.0f,y*5.0f));
}

void phy_shape_set_angle(b2Body* p1, float a)
{
    if (g_game->world_->IsLocked())
    {
        g_script_enviroment->event_queue.push_back(EventData(EVENT_ROTATE,p1,TO_RADIANS(a),0.0f));
        return;
    }
	p1->SetTransform(p1->GetPosition(),TO_RADIANS(a));
}

float phy_shape_get_angle(b2Body* p1)
{
    return TO_DEGREES(p1->GetAngle());
}


void phy_shape_set_elasticity(b2Body* p1, float a)
{
    p1->GetFixtureList()->SetRestitution(a);
}

void phy_shape_set_friction(b2Body* p1, float f)
{
    p1->GetFixtureList()->SetFriction(f*10.0f);
}

void draw_rect(float pos_x, float pos_y, float width, float height, long color)
{
    boxColor(g_game->renderer, pos_x - width / 2.0, pos_y - height / 2.0f,pos_x + width / 2.0, pos_y + height / 2.0f, 0xffffffff);
}

int make_rgba(int r, int g, int b, int a)
{
	uint32_t color = 0;
	color |= (a & 255) << 24;
	color |= (r & 255) << 16;
	color |= (g & 255) << 8;
	color |= (b & 255);
    return color;
}

char needsToModify(char first,char second)
{
    /*
     * MasterKiwi
    %%                = \n
     UTF-8       ASCII
    \xc2 \xbf    \xBF = ¿
    \xc3 \xa1    \xE1 = á
    \xc3 \xa9    \xE9 = é
    \xc3 \xad    \xED = í
    \xc3 \xb3    \xF3 = ó
    \xc3 \xba    \xFA = ú
    \xc3 \xb1    \xF1 = ñ
    \xc3 \x81    \xC1 = Á
    \xc3 \x89    \xC9 = É
    \xc3 \x8d    \xCD = Í
    \xc3 \x93    \xD3 = Ó
    \xc3 \x9a    \xDA = Ú
    */
    int result = ' ';
    if(first == '%' && second == '%')//Simbolos para ser remplazado por  salto de linea
    {
        result = '\n';
    }
    else if(first == '\xc2' && second == '\xbf')//Caracter de '¿' en los strings de los .nut
    {
        result = '\xBF';
    }
    else if(first == '\xc3')
    {
        switch(second)
        {
            case('\xa1')://Caracter de 'á' en los strings de los .nut
                result = '\xE1';
                break;
            case('\xa9')://Caracter de 'é' en los strings de los .nut
                result = '\xE9';
                break;
            case('\xad')://Caracter de 'í' en los strings de los .nut
                result = '\xED';
                break;
            case('\xb3')://Caracter de 'ó' en los strings de los .nut
                result = '\xF3';
                break;
            case('\xba')://Caracter de 'ú' en los strings de los .nut
                result = '\xFA';
                break;
            case('\xb1')://Caracter de 'ñ' en los strings de los .nut
                result = '\xF1';
                break;
            case('\x81')://Caracter de 'Á' en los strings de los .nut
                result = '\xC1';
                break;
            case('\x89')://Caracter de 'É' en los strings de los .nut
                result = '\xC9';
                break;
            case('\x8d')://Caracter de 'Í' en los strings de los .nut
                result = '\xCD';
                break;
            case('\x93')://Caracter de 'Ó' en los strings de los .nut
                result = '\xD3';
            case('\x9a')://Caracter de 'Ú' en los strings de los .nut
                result = '\xDA';
                break;
            default:
                result = ' ';
                break;
        }
    }
    return (char)result;
}

void scriptAdjustmentForTextDisplay(string &script)
{
    string result(script.size(),' ');
    char methodResult = ' ';
    for(int i = 0,j = 0;i < script.size();i++,j++)
    {
        if(i < script.size()-1)
        {
            methodResult = needsToModify(script[i],script[i+1]);
        }
        if(methodResult != ' ')
        {
            result[j] = methodResult;
            i++;
        }
        else
        {
            result[j] = script[i];
        }
        methodResult = ' ';
    }
    script = result;
}

void draw_text(float x, float y, float s, std::string t)
{
    scriptAdjustmentForTextDisplay(t);
    g_game->DrawText(x,y,t,s*20,false,gNavi->white);
}

std::string  get_data_json()
{
    return gNavi->json_to_squirrel;
}

int getDataGusanosYEscaleras(int dataQueQuieroQueMeRegreseMK)
{
    switch (dataQueQuieroQueMeRegreseMK) {
        case 0: //usado
            SDL_Log("ScriptEnvironment: Gusanos usado(%d)", gNavi -> usado);
            return gNavi -> usado;
        case 1: //posj1
            SDL_Log("ScriptEnvironment: Gusanos j1(%d)", gNavi -> posJ1);
            return gNavi -> posJ1;
        case 2: //posj2
            SDL_Log("ScriptEnvironment: Gusanos j2(%d)", gNavi -> posJ2);
            return gNavi -> posJ2;
        case 3: //mapa
            SDL_Log("ScriptEnvironment: Gusanos mapa(%d)", gNavi -> mapaEnTurno);
            return gNavi -> mapaEnTurno;
        case 4: //turno
            SDL_Log("ScriptEnvironment: Gusanos turno(%d)", gNavi -> turno);
            return gNavi -> turno;
        default:
            return 0;
    }
}

void mandarDatos(int usado_, int posJ1_, int posJ2_, int mapa_, int turno_)
{
    SDL_Log("script_environment:mandarDatos(%d, %d, %d, %d, %d) Gusanos",usado_, posJ1_, posJ2_, mapa_, turno_);
    gNavi->usado = usado_;
    gNavi-> posJ1 = posJ1_;
    gNavi->posJ2 = posJ2_;
    gNavi->mapaEnTurno = mapa_;
    gNavi->turno = turno_;
    gNavi->mandarDatos();
    g_game->state_ = GAME_OVER;
    //gNavi->reportGameOver();
}

void draw_text(float x, float y, float s, std::string t, int color_)
{
	SDL_Color color;
	color.a = (color_ >> 24) & 255;
	color.r = (color_ >> 16) & 255;
	color.g = (color_ >> 8) & 255;
	color.b = color_ & 255;
    scriptAdjustmentForTextDisplay(t);
    g_game->DrawText(x,y,t,s*20,false,color);
}

void draw_text_center(float x, float y, float s, std::string t)
{
    scriptAdjustmentForTextDisplay(t);
    g_game->DrawText(x,y,t,s*20,true,gNavi->white);
}

void draw_text_center(float x, float y, float s, std::string t, int color_)
{
	SDL_Color color;
	color.a = (color_ >> 24) & 255;
	color.r = (color_ >> 16) & 255;
	color.g = (color_ >> 8) & 255;
	color.b = color_ & 255;
    scriptAdjustmentForTextDisplay(t);
    g_game->DrawText(x,y,t,s*20,true,color);
}

std::string number_to_commastring(int number)
{
    std::string NumericString = NumberToString(number); // It should output 1,234,567,890
    unsigned int Length = strlen(NumericString.c_str()); // Get the length of the string, so we know when we have to stop
    std::string FinalString; // Will be our output
    unsigned int CommaOffset = Length%3; // Get the comma offset
    for(unsigned int i = 0; i < Length; ++i) // Loop each character
    {
        // If our Index%3 == CommaOffset and this isn't first character, add a comma
        if(i%3 == CommaOffset && i)
        {
            FinalString += ','; // Add the comma
        }
        FinalString += NumericString[i]; // Add the original character
    }

    return FinalString;
}

void save_highscore_gameover(long hs)
{
	gNavi->game_over_score_ = hs;
    g_game->SaveScore();
}

void flip_asset(Asset* a, bool flip)
{
    if (flip)
	{
		a->img_->flip =SDL_FLIP_HORIZONTAL;
	}
    else
	{
		a->img_->flip = SDL_FLIP_NONE;
	}
}

void scale_asset(Asset* a, float scale_x, float scale_y)
{
    a->img_->scale_x = scale_x;
    a->img_->scale_y = scale_y;
}

b2Body* phy_create_rect_poly(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, bool is_static)
{
    return g_game->create_rect_poly(x1,y1,x2,y2,x3,y3,x4,y4,is_static);
}

void phy_shape_set_static(b2Body* p1, bool d)
{
    if (d == true)
	{
		p1->SetType(b2_staticBody);
	}
    else
	{
		p1->SetType(b2_dynamicBody);
	}
}

void play_effect(int effect_id)
{
    g_game->playSoundEffect(effect_id);
}

void play_effect_channel(int effect_id, int channel)
{
    g_game->playSoundEffect(effect_id,channel);
}

void play_effect_stop_others(int effect_id)
{
    #if !defined(EMSCRIPTEN)
        Mix_HaltChannel(-1);
    #endif
}

void stop_channel(int effect_id)
{
    #if !defined(EMSCRIPTEN)
        Mix_HaltChannel(effect_id);
    #endif
}

void script_enviroment::BindSquirrel()
{
    SDL_Log("Binding Squirrel");

	vm_squirrel_ = sq_open(4096); //creates a VM with initial stack size 1024

	Sqrat::DefaultVM::Set(vm_squirrel_);

	sq_pushroottable(Sqrat::DefaultVM::Get());
	sqstd_register_mathlib(Sqrat::DefaultVM::Get());
	sqstd_register_stringlib(Sqrat::DefaultVM::Get());
	sqstd_register_systemlib(Sqrat::DefaultVM::Get());
	sqstd_register_bloblib(Sqrat::DefaultVM::Get());
	sq_pop(Sqrat::DefaultVM::Get(), 1);

	Sqrat::RootTable().Func<void(*)(std::string)>("print", &print_zax);
    Sqrat::RootTable().Func<void(*)(void)>("switchPhysics", &switchPhysics);

	//we overload compare
	Sqrat::RootTable().Func<bool(*)(b2Body*, b2Body*)>("compare_shapes", &compare_shapes);
    Sqrat::RootTable().Func<bool(*)(vector2f*, vector2f*)>("compare_vectors", &compare_vectors);
    Sqrat::RootTable().Func<bool(*)(Asset*, Asset*)>("compare_assets", &compare_assets);
    Sqrat::RootTable().Func<void(*)(Asset*, bool)>("flip_asset", &flip_asset);

    Sqrat::RootTable().Func<Asset* (*)(int, bool)>("load_asset", &load_asset);
    Sqrat::RootTable().Func<void(*)(Asset*)>("draw_asset", &draw_asset);
    Sqrat::RootTable().Func<float(*)(Asset*)>("asset_get_width", &asset_get_width);
    Sqrat::RootTable().Func<float(*)(Asset*)>("asset_get_height", &asset_get_height);
    Sqrat::RootTable().Func<void(*)(Asset*,float,float)>("asset_set_scale", &asset_set_scale);
    Sqrat::RootTable().Func<void(*)(Asset*, float, float)>("scale_asset", &scale_asset);
    Sqrat::RootTable().Func<void(*)(Asset*, float)>("rotate_asset", &rotate_asset);
    Sqrat::RootTable().Func<void(*)(Asset*, float, float)>("move_asset", &move_asset);
	Sqrat::RootTable().Func<std::string(*)(std::string, int, int)>("substr", &substring);
	Sqrat::RootTable().Func<int(*)(int, int)>("random_int", &random_int);
	Sqrat::RootTable().Func<void(*)(std::string)>("goto_menu", &goto_menu);
	Sqrat::RootTable().Func<void(*)(std::string, int)>("goto_menu_after_ms", &goto_menu_after_ms);
    Sqrat::RootTable().Func<void(*)(float, float, float, float, long)>("draw_rect", &draw_rect);
	Sqrat::RootTable().Func<int(*)(int, int, int, int)>("make_rgba", &make_rgba);
	Sqrat::RootTable().Func<void(*)(float, float, float, std::string)>("draw_text", &draw_text);
    Sqrat::RootTable().Func<void(*)(float, float, float, std::string, int color)>("draw_text2", &draw_text);
	Sqrat::RootTable().Func<void(*)(float, float, float, std::string)>("draw_text_center", &draw_text_center);
    Sqrat::RootTable().Func<void(*)(float, float, float, std::string, int color)>("draw_text_center2", &draw_text_center);
	Sqrat::RootTable().Func<unsigned int(*)()>("get_time", &get_time);
	Sqrat::RootTable().Func<void(*)(Sqrat::Function, int)>("call_function_after_time", &call_function_after_time);
	Sqrat::RootTable().Func<void(*)(std::string, float, float, float, float, Sqrat::Function)>("add_click_area", &create_click_area);

	Sqrat::RootTable().Func<b2Body* (*)(float, float, float, float, bool)>("phy_create_rect_shape", &create_rect_shape);

	Sqrat::RootTable().Func<void(*)(b2Body*, bool)>("phy_set_sensor", &phy_set_sensor);
	Sqrat::RootTable().Func<void(*)(float, float)>("phy_set_gravity", &phy_set_gravity);
    Sqrat::RootTable().Func<b2Body*(*)(Asset*, bool)>("phy_create_shape_for_asset", &create_shape_for_asset);
	Sqrat::RootTable().Func<void(*)(b2Body*, float, float)>("phy_move_shape", &phy_move_shape);
	Sqrat::RootTable().Func<void(*)(b2Body*, int)>("phy_shape_set_collision_type", &phy_shape_set_collision_type);
	Sqrat::RootTable().Func<void(*)(int, int, Sqrat::Function)>("phy_create_collision_handler_begin", &phy_create_collision_handler_begin);
	Sqrat::RootTable().Func<void(*)(int, int, Sqrat::Function)>("phy_create_collision_handler_end", &phy_create_collision_handler_end);
	Sqrat::RootTable().Func<void(*)(b2Body*)>("phy_remove_shape", &phy_remove_shape);
    Sqrat::RootTable().Func<vector2f(*)(b2Body*)>("phy_get_shape_position", &phy_get_shape_position);
	Sqrat::RootTable().Func<void(*)(b2Body*, float, float)>("phy_shape_add_impulse", &phy_shape_add_impulse);
	Sqrat::RootTable().Func<void(*)(b2Body*, float, float)>("phy_shape_add_force", &phy_shape_add_force);
	Sqrat::RootTable().Func<void(*)(b2Body*, float, float)>("phy_shape_set_velocity", &phy_shape_set_velocity);
	Sqrat::RootTable().Func<void(*)(b2Body*, float)>("phy_shape_set_friction", &phy_shape_set_friction);
	Sqrat::RootTable().Func<void(*)(b2Body*, bool)>("phy_shape_set_static", &phy_shape_set_static);

	Sqrat::RootTable().Func<void(*)(b2Body*)>("phy_reset_forces", &phy_reset_forces);
	Sqrat::RootTable().Func<void(*)(b2Body*, float)>("phy_shape_set_angle", &phy_shape_set_angle);
	Sqrat::RootTable().Func<float(*)(b2Body*)>("phy_shape_get_angle", &phy_shape_get_angle);
    Sqrat::RootTable().Func<vector2f(*)(b2Body*)>("phy_get_shape_velocity", &phy_get_shape_velocity);
    Sqrat::RootTable().Func<b2Body*(*)(Asset*, float)>("phy_create_circle_shape_for_asset", &create_circle_shape_for_asset);
    Sqrat::RootTable().Func<b2Body*(*)(Asset*, float, bool)>("phy_create_circle_shape_for_asset_static", &create_circle_shape_for_asset_static);
    Sqrat::RootTable().Func<b2Body*(*)(float, float, float, float, float, float, float, float, bool)>("phy_create_rect_poly", &phy_create_rect_poly);
	Sqrat::RootTable().Func<void(*)(b2Body*, float)>("phy_shape_set_elasticy", &phy_shape_set_elasticity);
	Sqrat::RootTable().Func<void(*)(long)>("save_highscore_gameover", &save_highscore_gameover);
	Sqrat::RootTable().Func<std::string(*)(int)>("number_to_commastring", &number_to_commastring);
	Sqrat::RootTable().Func<void(*)()>("clear_functions_after_time", &clear_functions_after_time);

	Sqrat::RootTable().Func<void(*)(int)>("play_effect", &play_effect);

    Sqrat::RootTable().Func<std::string(*)()>("get_data_json", &get_data_json);

    Sqrat::RootTable().Func<int(*)(int)>("getDataGusanosYEscaleras", &getDataGusanosYEscaleras);
    Sqrat::RootTable().Func<void(*)(int, int, int, int, int)>("mandarDatos", &mandarDatos);
	Sqrat::Class<b2Body> Shapes(vm_squirrel_);
	Sqrat::RootTable().Bind("phy_shape", Shapes);

    Sqrat::Class<Asset> Assets(vm_squirrel_);
	Sqrat::RootTable().Bind("file_asset", Assets);

    Sqrat::Class<vector2f> Vector2(vm_squirrel_);
    Vector2.Var("x", &vector2f::x);
    Vector2.Var("y", &vector2f::y);
	Sqrat::RootTable().Bind("vector_2", Vector2);
}
