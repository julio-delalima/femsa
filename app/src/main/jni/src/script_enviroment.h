#ifndef SCRIPT_ENVIROMENT_H
#define SCRIPT_ENVIROMENT_H


#include "sqrat/sqrat.h"
#include "navi.h"
#include <bitset>
#include <vector>
#include <map>
#include "Box2D/Box2D.h"


class MyContactListener : public b2ContactListener
{
    void BeginContact(b2Contact* contact);

    void EndContact(b2Contact* contact);

    void PreSolve(b2Contact* contact, const b2Manifold* oldManifold)
    {}
};

enum
{
    EVENT_EMPTY,
    EVENT_MOVE,
    EVENT_ROTATE,
    EVENT_ADD_FIXTURE
};

class EventData
{
public:
    EventData()
    {
        type = EVENT_EMPTY;
    }
    EventData(int _type,b2Body* _body, float _val1, float _val2)
	{
		type = _type;
		body = _body;
		val1 = _val1;
		val2 = _val2;
    };

    EventData(int _type,b2Body* _body, b2FixtureDef _fixDef)
	{
        type = _type;
        body = _body;
        val1 = 0;
        val2 = 0;
        fixDef = _fixDef;
    };

    ~EventData(){};
    int type;
    b2Body* body;
    float val1;
    float val2;

    b2FixtureDef fixDef;
};

class collision_handlers
{
public:
	collision_handlers(int _type1, int _type2)
	{
		type1_ = _type1;
		type2_ = _type2;
	}

	int type1_;
	int type2_;
	Sqrat::Function func1_;
	Sqrat::Function func2_;
	Sqrat::Function func3_;
	Sqrat::Function func4_;
};

class vector2f
{
public:
    float x, y;
};

class ClickArea
{
public:
    ClickArea()
    {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
        name = "";
    }
    ClickArea(int _x, int _y, int _w, int _h, std::string _name)
    {
        name = _name;
        x = _x;
        y = _y;
        w = _w;
        h = _h;
    }

    std::string name;
    int x;
    int y;
    int w;
    int h;
};

/**
* we declare the console here
*/

class script_enviroment
{
public:
	script_enviroment();
	~script_enviroment();
    void destroySquirrelVM();
	void init();

	void load_scripts();

    void load_game(std::string game_name);

	void BindSquirrel();

	HSQUIRRELVM vm_squirrel_;

	Sqrat::Function *funcA2;

	void reset();
	void forceGameOver();

    void call_game_draw();
	void call_start_level(int level_id);
	void call_game_update();

	void load_game_from_raw(std::string &scr_text);

    Sqrat::Function draw_func_;
	Sqrat::Function start_level_func_;
	Sqrat::Function update_func_;
	Sqrat::Function get_arrow_keys_func_;
	Sqrat::Function accel_func_;

	Sqrat::Function touch_start_;
	Sqrat::Function touch_move_;
	Sqrat::Function touch_end_;


    std::list<b2Body*> body_remove_box2d;

	std::map<int, Sqrat::Function> functors_;

	bool is_moving_to_menu_;

	std::vector<collision_handlers*> handlers_;
	std::map<int, std::map<int,Sqrat::Function> > handlers_box2d_;
	std::map<int, std::map<int,Sqrat::Function> > handlers_box2d_end_;
	std::map <int, ClickArea> click_area_list;
	std::vector <EventData> event_queue;
    Uint32 last_tick;
    int functor_count;
	float frameRate;

	bool allowGamePrint = true;
    bool activePhysics = true;
};

extern script_enviroment *g_script_enviroment;

#endif
