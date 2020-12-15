#include "navi.h"
#include "SDL.h"

struct Wrapper
{
    Wrapper();

    void NaviSetGame(int);
    bool NaviHasGameData(int);
    bool NaviHasGameDataPath(int, const char*);
    void NaviAskDataDate(int);
    void NaviMoveFile(const char*,const char*);
    void NaviChangeAudio();
    bool NaviIsAudioActive();
    void NaviSetJsonSquirrel(const char*);
    void NaviSetDataGusanosYEscaleras(int usado_,int posJ1_,int posJ2,int mapa_,int turno_);
};