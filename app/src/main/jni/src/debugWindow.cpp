#include "debugWindow.h"


#ifdef DEBUG_WINDOW
#include "navi.h"
#include <cstdio>
#include "SDL2_gfx/SDL2_gfxPrimitives.h"
#include <sstream>
#include "game.h"
#include <SDL2/SDL_opengl.h>
#include <GL\GLU.h>

debugWindow::debugWindow()
{

}

bool debugWindow::initGL()
{
    bool success = true;
    GLenum error = GL_NO_ERROR;

    //Initialize Projection Matrix
    glMatrixMode( GL_PROJECTION );
    glLoadIdentity();

    //Check for error
    error = glGetError();
    if( error != GL_NO_ERROR )
    {
        SDL_Log( "Error initializing OpenGL!\n" );
        success = false;
    }

    //Initialize Modelview Matrix
    glMatrixMode( GL_MODELVIEW );
    glLoadIdentity();

    //Check for error
    error = glGetError();
    if( error != GL_NO_ERROR )
    {
        SDL_Log( "Error initializing OpenGL!\n" );
        success = false;
    }

     glClearColor( 0.f, 0.f, 0.f, 1.f );
     return success;
}

void debugWindow::init()
{
//Use OpenGL 2.1
        SDL_GL_SetAttribute( SDL_GL_CONTEXT_MAJOR_VERSION, 2 );
        SDL_GL_SetAttribute( SDL_GL_CONTEXT_MINOR_VERSION, 1 );
    //ctor
    SDL_Window* gWindow2 = SDL_CreateWindow( "Master Kiwi Debug", 10, SDL_WINDOWPOS_CENTERED, 200, 300, SDL_WINDOW_SHOWN );
    gWindow = SDL_CreateWindow( "Master Kiwi Debug", 210, SDL_WINDOWPOS_CENTERED, 200, 300, SDL_WINDOW_OPENGL | SDL_WINDOW_SHOWN );
    if( gWindow == NULL )
    {
        SDL_Log( "Window could not be created! SDL_Error: %s\n", SDL_GetError() );
        //success = false;
    }

    renderer = SDL_CreateRenderer(gWindow2, -1, 0);

    windowID = SDL_GetWindowID(gWindow2);

    gContext = SDL_GL_CreateContext( gWindow );
    if( gContext == NULL )
    {
        SDL_Log( "OpenGL context could not be created! SDL Error: %s\n", SDL_GetError() );
    }


    //Use Vsync
    if( SDL_GL_SetSwapInterval( 1 ) < 0 )
    {
        SDL_Log( "Warning: Unable to set VSync! SDL Error: %s\n", SDL_GetError() );
    }

    //Initialize OpenGL
    if( !initGL() )
    {
        SDL_Log( "Unable to initialize OpenGL!\n" );
    }
}

debugWindow::~debugWindow()
{
    //dtor
}

void debugWindow::draw()
{
    SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);
    SDL_RenderClear(renderer);
    if (g_game == 0)
    {
        stringRGBA(renderer, 50, 10, "Waiting for game to start", 255, 255, 255, 255);
        SDL_RenderPresent(renderer);
        return;
    }

    stringRGBA(renderer, 50, 10, "Botones Debug", 255, 255, 255, 255);

    if (g_game->draw_physics  == false)
    {
        boxRGBA(renderer, 10, 30, 90, 30+80, 255, 255, 255, 255);
        stringRGBA(renderer, 23, 46, "Activar", 0, 0, 0, 255);
        stringRGBA(renderer, 23, 66, "Dibujar", 0, 0, 0, 255);
        stringRGBA(renderer, 23, 86, "Fisicas", 0, 0, 0, 255);
    }
    else
    {
        boxRGBA(renderer, 10, 30, 90, 30+80, 120, 120, 120, 255);
        stringRGBA(renderer, 11, 46, "Desactivar", 255, 255, 255, 255);
        stringRGBA(renderer, 23, 66, "Dibujar", 255, 255, 255, 255);
        stringRGBA(renderer, 23, 86, "Fisicas", 255, 255, 255, 255);
    }

    if (g_game->draw_click_area  == false)
    {
        boxRGBA(renderer, 110, 30, 190, 30+80, 255, 255, 255, 255);
        stringRGBA(renderer, 123, 46, "Activar", 0, 0, 0, 255);
        stringRGBA(renderer, 123, 66, "Dibujar", 0, 0, 0, 255);
        stringRGBA(renderer, 111, 86, "Click Areas", 0, 0, 0, 255);
    }
    else
    {
        boxRGBA(renderer, 110, 30, 190, 30+80, 120, 120, 120, 255);
        stringRGBA(renderer, 111, 46, "Desactivar", 255, 255, 255, 255);
        stringRGBA(renderer, 123, 66, "Dibujar", 255, 255, 255, 255);
        stringRGBA(renderer, 111, 86, "Click Area", 255, 255, 255, 255);
    }

    stringRGBA(renderer, 15, 125, "Call function after ms", 255, 255, 255, 255);
    lineRGBA(renderer, 6, 135, 200-6, 135, 255, 255, 255, 255);
    lineRGBA(renderer, 140, 135, 140, 290, 255, 255, 255, 255);

    int i = 0;
    for (std::map <int,Uint32>::iterator it = gNavi->call_function_after_time.begin(); it != gNavi->call_function_after_time.end(); ++it)
    {
        std::stringstream ss2;
        ss2 << "Funcion " << i+1;
        stringRGBA(renderer, 5, 145+i*15, ss2.str().c_str(), 255, 255, 255, 255);

        std::stringstream ss;
        ss << (*it).second - SDL_GetTicks();
        stringRGBA(renderer, 145, 145+i*15, ss.str().c_str(), 255, 255, 255, 255);
        i++;
    }


    SDL_RenderPresent(renderer);



}

void debugWindow::drawGL()
{
        //Clear color buffer
    glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glLoadIdentity();
    static float a = 0;
    a+=0.5f;
    glRotatef(a,1.0f,1.0f,1.0f);

    glColor3f(1.0f,1.0f,1.0f);
    glBegin( GL_QUADS );

    glVertex2f( -0.5f, -0.5f );
    glVertex2f( 0.5f, -0.5f );
    glVertex2f( 0.5f, 0.5f );
    glVertex2f( -0.5f, 0.5f );

    glEnd();

    SDL_GL_SwapWindow( gWindow );
}

void debugWindow::click(int x, int y)
{
    if (g_game == 0)
    {
        return;
    }
    //20 60
    //180 220
    if (x >= 20 && x <= 180 && y >= 60 && y <= 220)
    {
        //std::cout << "Activar / Desactivar fisicas" << std::endl;
        if (g_game->draw_physics == true)
        {
            g_game->draw_physics = false;
        }
        else
        {
            g_game->draw_physics = true;
        }
    }

    //220 60
    //380 220
    if (x >= 220 && x <= 380 && y >= 60 && y <= 220)
    {
        //std::cout << "Activar / Desactivar click area" << std::endl;
        if (g_game->draw_click_area == true)
        {
            g_game->draw_click_area = false;
        }
        else
        {
            g_game->draw_click_area = true;
        }
    }
}

#endif
