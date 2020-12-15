#include "project.h"

project::project(string _name, int _id, int _mode, int _parent)
{
    name = _name;
    id = _id;
    mode = _mode;
    parent = _parent;
    game_type_ = 1;
    pjct_script  = "";
    audioActivated = 1;
    hasShownManual = 0;
    gameSpecificData = "";
    gameVersion = 0;
}

project::project(const project& projectInfo)
{
    name = projectInfo.name;
    id = projectInfo.id;
    mode = projectInfo.mode;
    parent = projectInfo.parent;
    game_type_ = projectInfo.game_type_;
    pjct_script = projectInfo.pjct_script;
    audioActivated = projectInfo.audioActivated;
    hasShownManual = projectInfo.hasShownManual;
    gameSpecificData = projectInfo.gameSpecificData;
    gameVersion = projectInfo.gameVersion;
}

void project::replaceOnScript(string firstReference,string secondReference,string newValue)
{
    unsigned long start = pjct_script.find(firstReference.c_str());
    unsigned long finish = pjct_script.find(secondReference.c_str(),start);

    replaceOnScript(start+firstReference.length(),finish,newValue);
}

void project::replaceOnScript(unsigned long startIndex,unsigned long secondIndex,string newValue)
{
    stringstream result;
    result << pjct_script.substr(0,startIndex);
    result << newValue;
    result << pjct_script.substr(secondIndex);

    pjct_script = result.str();
}
