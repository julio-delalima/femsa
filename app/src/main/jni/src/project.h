#ifndef INC_01SABROSITA_PROJECT_H
#define INC_01SABROSITA_PROJECT_H

#include <string>
#include <sstream>

using namespace std;

    class project
    {
    public:
        string name;
        int id;
        int mode;
        int parent;
        int game_type_;
        string pjct_script;
        int audioActivated;
        int hasShownManual;
        int gameVersion;
        string gameSpecificData;

        project(string _name, int _id, int _mode, int _parent = 0);
        project(const project& projectInfo);

        void replaceOnScript(string firstReference,string secondReference,string newValue);
        void replaceOnScript(unsigned long startIndex,unsigned long secondIndex,string newValue);
    };

#endif
