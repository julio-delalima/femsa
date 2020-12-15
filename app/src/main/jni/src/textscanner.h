#ifndef TEXTSCANNER_H
#define TEXTSCANNER_H

#include <fstream>
#include <vector>

#ifdef __WINPHONE__
	#include <SDL.h>
#else
    #ifdef __ANDROID__
        #include "SDL.h"
        #include <android/log.h>
#elif __APPLE__
#include "SDL.h"
    #else
        #include "SDL2/SDL.h"
    #endif
#endif


class TextScanner
{
public:
    TextScanner(std::string _fname);
    TextScanner() {};
    std::string GetAll();
    std::string GetLine( int _linenum );
    std::string GetParmString( std::string label, int index,  std::string token);
    bool LoadFile(std::string _fname);
    bool SaveFile(std::string _fname);
    void readLine(SDL_RWops *f, std::string& buffer);

    bool SetupFromMemoryAddress(const char *pCharArray);


    std::vector<std::string> lineList;
};

#endif // TEXTSCANNER_H
