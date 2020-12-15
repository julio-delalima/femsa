//
// Created by MasterKiwi-Sferea on 01/11/2017.
//

#ifndef INC_09MASTERKIWI_CUSTOMMENU_H
#define INC_09MASTERKIWI_CUSTOMMENU_H

#include <sstream>
#include <iostream>

using namespace std;

class Menu
{
public:
    Menu()
    {
        special_ = 0;
        music_id_ = 0;
    }
    int id_;
    int special_;
    int music_id_;
    string text_;
    string desc_;
    string extra_1;
    string extra_2;
    string extra_3;
    string extra_4;
};

class CustomMenu
{
public:
    bool createMenu(Menu menuInfo);

protected:
    struct elementData
    {
        int assetId;
        int x;
        int y;
        int flip;
        int angle;
        int anim_id;
        int hasAction;
        int actionId = 0;
        int diffX = 0;
        int diffY = 0;
        int width = 0;
        int height = 0;
    };

    struct color
    {
        uint8_t red;
        uint8_t green;
        uint8_t blue;
    };

    const int GAME_OVER_SCORE = -1;
    const int SPECIAL_MENU_TOP_10 = 4;
    const int SPECIAL_MENU_FORM = 3;

    int initialMenuID = -1;

    void configureTop10(string data);
    color getColorFromText(string toParse);
    void addTop10Score(int rank,string rankData,color nameColor,color scoreColor);
    void configureForm(Menu menuInfo);
    void createFormField(int index,string title,color tileColor,color backgroundColor,color inputColor);

    void exitMasterKiwi();
    void showGame();
    void menuConfiguration(string menuInfo);
    void configureHighScore(string data);
    bool isCrrntElemConfigurated(string elementInfo);
    void configureAction();
    void configureImage();
    void configureMusic();
    elementData currentData;

public:
    bool wasForced = false;

    int getMainMenuID();
};

#endif
