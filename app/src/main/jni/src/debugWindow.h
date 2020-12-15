#ifndef DEBUGWINDOW_H
#define DEBUGWINDOW_H

    #ifdef DEBUG_WINDOW
        #include "SDL2/SDL.h"

        class debugWindow
        {
            public:
                debugWindow();
                virtual ~debugWindow();
                void draw();
                SDL_Renderer *renderer;
                void init();
                Uint32 windowID;

                void click(int x, int y);
                SDL_GLContext gContext;
                bool initGL();
                SDL_Window* gWindow;
                void drawGL();

            protected:
            private:


        };

    #endif

#endif
