#include "textscanner.h"
#include "miscutils.h"
#include <stdio.h>
#include <iostream>
#include <sstream>

#ifdef __WINPHONE__
	#include <SDL.h>
#else
    #ifdef __ANDROID__
        #include "SDL.h"
        #include <android/log.h>
        #include "androidWrapper/MasterKiwiWrapper.h"
#elif __APPLE__
#include "SDL.h"
    #else
        #include "SDL2/SDL.h"
    #endif
#endif


TextScanner::TextScanner(std::string _fname)
{
    LoadFile(_fname);
}

std::string TextScanner::GetAll()
{

    std::string s;
    for (unsigned int i=0; i < lineList.size(); i++)
    {
        s += StripWhiteSpace(lineList[i])+"\n";
    }
    return s;

}

std::string TextScanner::GetLine( int _linenum )
{
    if ((int)lineList.size() > _linenum && _linenum >= 0)
    {
        return lineList[_linenum];
    }
    //invalid line
    return "";
}

std::string TextScanner::GetParmString( std::string label, int index,  std::string token)
{
	if (lineList.empty())
	{
		std::cout << ("Load a file first") << std::endl;
		return "";
	}
	for (unsigned int i=0; i < lineList.size(); i++)
	{
		if (lineList[i].empty()) continue;
		std::vector<std::string> line = StringTokenize(lineList[i], token);
		if (line[0] == label)
		{
			//found it
			return line[index];
		}
	}
	return "";
}

bool TextScanner::LoadFile(std::string _fname)
{
    bool result = false;
    #if defined(EMSCRIPTEN)
    std::ifstream file;
    file.open(_fname.c_str());
    std::string line;
    while(getline(file,line))
    {
        lineList.push_back(line);
    }
    file.close();
    #else
        #ifdef NEWENGINE_MASTERKIWIWRAPPER_H
            std::string path(getPreferencedPath());
            _fname = path + "/" + _fname;
            std::ifstream file;
            file.open(_fname.c_str());
            if(file != nullptr)
            {
                result = true;
                std::string line;
                while(getline(file,line))
                {
                    lineList.push_back(line);
                }
                file.close();
            }
            else
            {
                SDL_Log("Unable to save data: %s ------with Error: %s",_fname.c_str(),strerror(errno));
            }
        #else
            SDL_RWops *file2 = SDL_RWFromFile(_fname.c_str(), "r");
            if (file2 == 0)
            {
                _fname = SDL_GetPrefPath("","") + _fname;
                file2 = SDL_RWFromFile(_fname.c_str(), "r");
                if (file2 == 0)
                {
                    //std::cout << "File " << _fname << " not found.";
                    return;
                }
            }
            unsigned int fsize = file2->size(file2);
            std::string str("",fsize);
            for( int i = 0; i < fsize; ++i )
            {
                SDL_RWread( file2, &str[ i ], sizeof(char), 1 );
            }
            str[fsize] = '\0';
            SDL_RWclose(file2);
            std::cout << str << std::endl;
            std::istringstream iss(str);

            std::string line;
            while(getline(iss,line))
            {
                lineList.push_back(line);
            }
        #endif
    #endif
    return result;
}

bool TextScanner::SaveFile( std::string f)
{
    #ifdef __ANDROID__
        #ifdef NEWENGINE_MASTERKIWIWRAPPER_H
            std::string path(getPreferencedPath());
            f = path + "/" + f;
        #else
            f = SDL_GetPrefPath("","") + f;
        #endif
    #elif __APPLE__
        f = SDL_GetPrefPath("","") + f;
    	std::cout << "Save: " << f;
    #endif

	std::string lineFeed = "\r\n";

	FILE *fp = fopen( f.c_str(), "wb");

	if (!fp)
	{
		std::cout << ("Unable to save data") << std::endl;
		SDL_Log("Unable to save data: %s ------with Error: %s",f.c_str(),strerror(errno));
		return false;
	}

	for (unsigned int i=0; i < lineList.size(); i++)
	{
		fwrite(lineList[i].c_str(), lineList[i].size(), 1, fp);
		fwrite(lineFeed.c_str(), lineFeed.size(), 1, fp);
	}

	fclose(fp);
	return true;
}

bool TextScanner::SetupFromMemoryAddress(const char *pCharArray)
{
	lineList = StringTokenize(pCharArray, "\n");
	for (unsigned int i=0; i < lineList.size(); i++)
	{
		StringReplace("\r", "", lineList[i]);
	}
	return true;
}
