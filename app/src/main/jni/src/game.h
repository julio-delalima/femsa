#ifndef GAME_H
#define GAME_H
#include <map>
#include <vector>
#include "navi.h"
#include "Box2D/Box2D.h"

#if defined(DEBUG_PHYSICS) || defined(DEBUG_WINDOW)
    class FooDraw : public b2Draw
    {
        public:
            void DrawPolygon(const b2Vec2* vertices, int32 vertexCount, const b2Color& color) {}
            void DrawSolidPolygon(const b2Vec2* vertices, int32 vertexCount, const b2Color& color);
            void DrawCircle(const b2Vec2& center, float32 radius, const b2Color& color) {}
            void DrawSolidCircle(const b2Vec2& center, float32 radius, const b2Vec2& axis, const b2Color& color);
            void DrawSegment(const b2Vec2& p1, const b2Vec2& p2, const b2Color& color) {}
            void DrawTransform(const b2Transform& xf) {}
            void DrawPoint(const b2Vec2& p, float32 size, const b2Color& color) {}
    };
#endif

//this shall be used by you?
enum game_states{
    WAITING,
    MAIN_MENU,
    PLAYING,
    PAUSED,
    GAME_OVER,
    GAME_OVER_STATS,
    NEXT_LEVEL,
    CREDITS,
    REGISTER_DATA
};

class game
{
    public:

        void GameUpdate();
        SDL_Window* win;
        std::map<int,bool> key_pressed;
        bool touch_started;
        void Init();
        void Draw();
        SDL_Renderer *renderer;
        /*
         * DONT USE THIS!
         * CONSTRUCTOR
         */
        game();

        /*
         * DONT USE THIS
         * DESTRUCTOR
         */
        virtual ~game();

        /*
         * DONT USE THIS
         * This is used internally by this class
         */
        void init_game();


        int MeasureTextWidth(std::string t, float font_size);

        /*
         * You can use this for anything you want, is a proton entity
         */
        //Entity *entity_;

        /*
         * We call this when we start this class, this does not mean that the gme will start right away, but it is loading, so load stuff here is the riht thing
         */
        virtual void init() {};

        /*
         * Called before update, if the game is paused it is not called
         */
        virtual void update();

        /*
         * Draw in here, it is called AFTER update, it is also called when the game is paused to keep drawing it
         */
        virtual void draw();

        /*
         * Free all the memory you have allocated please, the game is ending
         */
        virtual void kill() {};

        /*
         * we start a level, if is an endless game we call start_level(0);
         */
        virtual void start_level(int level_id);

        //function to call when its gameover
        virtual void game_over() {};

        /*
         * Accelerometer
         */
        virtual void acceleration_change(float _x, float _y, float _z);

        /*
         * touch
         */
        virtual void touch_start(float _x, float _y);
        virtual void touch_move(float _x, float _y);
        virtual void touch_end(float _x, float _y);

        /*
        * We only get arrows and cntrl key
        * 1 = Pressed
        * 0 = Released
        */
        virtual void key_press(int _key, int _state);

    #ifdef DEBUG_WINDOW
        bool draw_physics;
        bool draw_click_area;
    #endif
        void clear_space();

        int game_id;

        /*
        * Create a shape and body then add it to the space
        * Why return a shape and not a body? because its easier to get its body
        */

        //cpShape* create_circle(float _x, float _y, float _rad, bool _is_static = false);
        //cpShape* create_rect(float _x, float _y, float _w, float _h, bool _is_static = true);


        b2Body* create_circle_box2d(float _x, float _y, float _rad, bool _is_static = false);
        b2Body* create_rect_box2d(float _x, float _y, float _w, float _h, bool _is_static /* = true*/);
        b2Body* create_rect_poly(float _x1, float _y1, float _x2, float _y2, float _x3, float _y3, float _x4, float _y4, bool _is_static);

        //this will load a required or optional asset
        Asset* load_asset(int type_2, bool optional, bool manual_draw = false);

        bool load_errors_;
        game_states state_;
        game_states previousState;

        void GetAccel(float &x, float &y, float &z);
        SDL_Joystick *accelerometer;
        void SaveScoreAlways();

        bool isAudioAvailable;
        void addSoundEffect(int id, std::string wav_file);
        void playSoundEffect(int id, int channel = -1);

        int mode_;

        //the settings with an int value
        std::map<int,int> settings_int_;

        std::vector<Asset*> loaded_assets;

        //cpSpace *space_;
        b2World *world_;
        b2Body* groundBody;
        #if !defined(EMSCRIPTEN)
            std::map<int, Mix_Chunk*> loaded_sound_effects;
        #else
            std::map<int, std::string> loaded_sound_effects;
        #endif

        std::vector<std::string> user_data;

        Mk_Font game_font;

        bool show_register_data; //mostramos datos de registro

        void SaveScore();
        void SaveTop10(std::string name, int value);
        void DrawText(float x, float y, std::string t, float font_size, bool align_center, SDL_Color &color);
};

//singleton
extern game *g_game;
#endif
