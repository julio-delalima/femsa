#ifndef INC_01SABROSITA_NRMTRIVIA_H
#define INC_01SABROSITA_NRMTRIVIA_H

#include <string>
#include <util/cJSON.h>
#include "../NRMSpecifics.h"

using namespace std;

class NRMTrivia : public NRMSpecifics
    {
protected:
    const char *JSON_OBJECT_TO_EVALUATE = "codigo";
    const char *JSON_HAS_NEW_DATA = "200";
    const char *JSON_DATA_OBJECT = "trivia";
    const char *JSON_DATA_VERSION = "version";

    const int QUESTION_INDEX = 0;
    const int CORRECT_ANSWER_INDEX = 1;
    const int FIRST_WRONG_ANSWER_INDEX = 2;
    const int SECOND_WRONG_ANSWER_INDEX = 3;
    const int MIN_LINE_CHARACTERS = 23;
    const char *TRIVIA_ELEMENT_DIVISION = "@@";
    const char *TRIVIA_END_OF_QUESTION = "$$";
    const char *TRIVIA_END_OF_DIFFICULTY = "&&";
    const char *TRIVIA_TEXT_NEWLINE_CHARACTER = "%%";
    const char *TRIVIA_SCRIPT_REPLACE_INIT = "DATA = \"";
    const char *TRIVIA_SCRIPT_REPLACE_FINISH = "\";";

    bool hasNewData(cJSON *jsonData);

    void processQuestions(cJSON *jsonData);

    void registerQuestion(cJSON *questionData);

    string addNewLine(string currentText);

    string replaceOnString(unsigned int startIndex, unsigned int endIndex, string toModify, string newValue);

public:
    NRMTrivia();

    virtual bool networkRequest(NetworkManager &netManager);

    virtual void networkResponse(NetworkManager &netManager);

    virtual void modifyGameProject(project &currentProject);

    virtual void saveCurrentVersion();
    };


#endif
