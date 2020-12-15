#include "miscutils.h"
#include <sstream>
#include <cstdio>      /* printf, fgets */
#include <cstdlib>     /* atoi */
#include <cstring>
#include <iostream>

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
	#include <unistd.h>
#endif




using namespace std;

string TrimLeft (const string & s, const string & t)
{
    string d (s);
    return d.erase (0, s.find_first_not_of (t)) ;
} // end of trim_left

string StripWhiteSpace(const string & s)
{
    string d (s);
    return TrimLeft (TrimRight (d, UTIL_SPACES), UTIL_SPACES) ;
}


inline string TrimRight (const string & s, const string & t)
{
    string d (s);
    string::size_type i (d.find_last_not_of (t));
    if (i == string::npos)
        return "";
    else
        return d.erase (d.find_last_not_of (t) + 1) ;
} // end of trim_right

int StringToInt( const std::string &s )
{
    return atoi(s.c_str());
}

vector<string> StringTokenize (const  string  & theString,  const  string  & theDelimiter )
{
    vector<string> theStringVector;

    if (!theString.empty())
    {
        size_t  start = 0, end = 0;

        while ( end != string::npos )
        {
            end = theString.find( theDelimiter, start );

            // If at end, use length=maxLength.  Else use length=end-start.
            theStringVector.push_back( theString.substr( start,
                                                         (end == string::npos) ? string::npos : end - start ) );

            // If at end, use start=maxSize.  Else use start=end+delimiter.
            start = (   ( end > (string::npos - theDelimiter.size()) )
                        ?  string::npos  :  end + theDelimiter.size()    );
        }
    }

    return theStringVector;
}

string GetFileExtension(string fileName)
{
    size_t index = fileName.find_last_of('.');
    if (index == string::npos)
    {
        return "";
    }

    return fileName.substr(index+1, fileName.length());
}

string ModifyFileExtension(string fileName, string extension)
{
    size_t index = fileName.find_last_of('.');
    if (index == string::npos)
    {
        //std::cout << "The file has no extension lol" << std::endl;
        return fileName;
    }
    return fileName.substr(0, index+1) + extension;
}


string GetFileNameFromString(const string &path)
{
    //std::cout << path << std::endl;
    if(path.size() > 0)
    {
        for (size_t i=path.size()-1; i > 0; i--)
        {
            if (path[i] == '/' || path[i] == '\\')
            {
                //well, this must be the cutoff point
                return path.substr(i+1, path.size()-i);
            }
        }
    }
    return path;
}

string NumberToString ( int Number )
{
    ostringstream ss;
    ss << Number;
    return ss.str();
}


//snippet from Zahlman's post on gamedev: http://www.gamedev.net/community/forums/topic.asp?topic_id=372125
void StringReplace(const std::string& what, const std::string& with, std::string& in)
{
    size_t pos = 0;
    size_t whatLen = what.length();
    size_t withLen = with.length();
    while ((pos = in.find(what, pos)) != std::string::npos)
    {
        in.replace(pos, whatLen, with);
        pos += withLen;
    }
}

int Random(int range)
{
    return rand()%range;
}

int RandomRange(int rangeMin, int rangeMax)
{
    if (rangeMin == rangeMax) return rangeMin; //avoid divide by zero error
        return rand()% (rangeMax-rangeMin)+rangeMin;
}

float RandomRangeFloat(float rangeMin, float rangeMax)
{
    float random = ((float) rand()) / (float) RAND_MAX;
    float diff = rangeMax - rangeMin;
    float r = random * diff;
    return rangeMin + r;
}

bool SeparateString (const char str[], int num, char delimiter, char *return1)
{
	int l = 0;
	return1[0] = 0;
	for (unsigned int k = 0; str[k] != 0; k++)
	{
		if (str[k] == delimiter)
		{
			l++;
			if (l == num+1)
            {
                break;
            }
			if (k < strlen(str))
            {
                strcpy(return1,"");
            }
		}
		if (str[k] != delimiter)
        {
            sprintf(return1, "%s%c",return1 ,str[k]);
        }
	}
	if (l < num)
	{
		return1[0] = 0;
		return(false);
	}
	return true;
}

string SeparateStringSTL(string input, int index, char delimiter)
{
	//yes, this is pretty crap
	//assert(input.size() < 4048 && "Fix this function..");
	char stInput[4048];
	if (SeparateString(input.c_str(), index, delimiter, stInput))
	{
		return stInput;
	}

	return "";
}

void RemoveFile( string fileName)
{
    #ifdef __ANDROID__
    if (FileExists(fileName) == false)
    {
        #ifdef NEWENGINE_MASTERKIWIWRAPPER_H
            std::string path(getPreferencedPath());
            fileName = path + "/" + fileName;
        #else
            fileName = SDL_GetPrefPath("","") + fileName;
        #endif
    }
    #elif __APPLE__
    if (FileExists(fileName) == false)
    {
        fileName = SDL_GetPrefPath("","") + fileName;
    }
    #endif
    int ret = unlink( (fileName).c_str());
}

bool FileExists(const std::string &fName)
{
	FILE *fp = fopen( (fName).c_str(), "rb");
	if (!fp)
	{
		return NULL;
	}
	fclose(fp);
	return true;
}

bool isOrdinaryChar(char c, bool bStrict)
{
	if (!bStrict)
	{
		if (c >= 32 && c <= 126)
		{
			return true;
		}
		return false;
	}
	if (
		(c >= 45 && c <= 46) ||
		(c >= 48 && c <= 57) ||
		(c >= 65 && c <= 90) ||
		(c == 95) || // _
		(c >= 97 && c <= 122)) return true;
	return false;
}
std::string FilterToValidAscii(const std::string &input, bool bStrict)
{
	std::string output;
	for (unsigned int i=0; i < input.length(); i++)
	{
		if ( isOrdinaryChar(input[i], bStrict))
		{
			output += input[i];
		}
	}
	return output;
}
