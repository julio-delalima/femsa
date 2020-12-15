#include "NRMTrivia.h"
#include "navi.h"
#include "SDL.h"

NRMTrivia::NRMTrivia()
    {
    projectType = 1308;
    }

bool NRMTrivia::hasNewData(cJSON *jsonData)
    {
    bool result = false;
    cJSON *status = cJSON_GetObjectItem(jsonData, JSON_OBJECT_TO_EVALUATE);
    if(status != 0)
        {
        if(strcmp(status->valuestring, JSON_HAS_NEW_DATA) == 0)
            {
            result = true;
            }
        }
    return result;
    }

void NRMTrivia::processQuestions(cJSON *jsonData)
    {
    cJSON *difficultiesArray = cJSON_GetObjectItem(jsonData, JSON_DATA_OBJECT);
    int difficultyArraySize = cJSON_GetArraySize(difficultiesArray);
    for(int i = 0; i < difficultyArraySize; i++)
        {
        cJSON *currentDifficulty = cJSON_GetArrayItem(difficultiesArray, i);
        int currentDifficultySize = cJSON_GetArraySize(currentDifficulty);
        for(int j = 0; j < currentDifficultySize; j++)
            {
            cJSON *currentQuestion = cJSON_GetArrayItem(currentDifficulty, j);
            registerQuestion(currentQuestion);
            if(j < (currentDifficultySize - 1))
                {
                specificData << TRIVIA_END_OF_QUESTION;
                }
            }
        specificData << TRIVIA_END_OF_DIFFICULTY;
        specificData << TRIVIA_END_OF_QUESTION;
        }
    }

void NRMTrivia::registerQuestion(cJSON *questionData)
    {
    specificData << addNewLine(cJSON_GetArrayItem(questionData, QUESTION_INDEX)->valuestring);
    specificData << TRIVIA_ELEMENT_DIVISION;
    specificData << addNewLine(cJSON_GetArrayItem(questionData, CORRECT_ANSWER_INDEX)->valuestring);
    specificData << TRIVIA_ELEMENT_DIVISION;
    specificData << addNewLine(cJSON_GetArrayItem(questionData, FIRST_WRONG_ANSWER_INDEX)->valuestring);
    specificData << TRIVIA_ELEMENT_DIVISION;
    specificData << addNewLine(cJSON_GetArrayItem(questionData, SECOND_WRONG_ANSWER_INDEX)->valuestring);
    }

string NRMTrivia::addNewLine(string currentText)
    {
    string result = currentText.c_str();
    bool lookingForSpace = false;
    for(unsigned int i = 0, j = 0; i < result.size(); i++, j++)
        {
        if((j % MIN_LINE_CHARACTERS) == 0 && j > 0)
            {
            lookingForSpace = true;
            }
        if(lookingForSpace && result[i] == ' ')
            {
            lookingForSpace = false;
            j = 0;
            result = replaceOnString(i, i + 1, result, TRIVIA_TEXT_NEWLINE_CHARACTER).c_str();
            }
        }
    return result;
    }

string NRMTrivia::replaceOnString(unsigned int startIndex, unsigned int endIndex, string toModify, string newValue)
    {
    stringstream result;
    result << toModify.substr(0, startIndex);
    result << newValue;
    result << toModify.substr(endIndex);
    return result.str();
    }

bool NRMTrivia::networkRequest(NetworkManager &netManager)
    {
    bool result = false;
    if(gNavi->current_project->id == gNavi->getProjectWithType(projectType)->id)
        {
        netManager.askForQuestions(dataVersion, gNavi->current_project->id);
        result = true;
        }
    return result;
    }

void NRMTrivia::networkResponse(NetworkManager &netManager)
    {
    if(gNavi->netManager.network_action == NetworkManager::ASK_FOR_QUESTIONS)
        {
        cJSON *triviaData = cJSON_Parse(gNavi->netManager.getRequestResult().c_str());
        if(triviaData != 0)
            {
            if(hasNewData(triviaData))
                {
                specificData.str(string());
                processQuestions(triviaData);
                dataVersion = cJSON_GetObjectItem(triviaData, JSON_DATA_VERSION)->valueint;
                }
            else
                {
                specificData << gNavi->getProjectWithType(projectType)->gameSpecificData;
                }
            }
        }
    }

void NRMTrivia::modifyGameProject(project &currentProject)
    {
    if(gNavi->current_project->id == gNavi->getProjectWithType(projectType)->id)
        {
        currentProject.replaceOnScript(TRIVIA_SCRIPT_REPLACE_INIT, TRIVIA_SCRIPT_REPLACE_FINISH, specificData.str());
        }
    }

void NRMTrivia::saveCurrentVersion()
    {
    if(gNavi->current_project->id == gNavi->getProjectWithType(projectType)->id)
        {
        for(int i = 0; i < gNavi->projects_.size(); i++)
            {
            if(gNavi->projects_[i].id == gNavi->current_project->id)
                {
                gNavi->projects_[i].gameVersion = dataVersion;
                gNavi->projects_[i].gameSpecificData = specificData.str().c_str();
                break;
                }
            }
        }
    }