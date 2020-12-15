//
// Created by MasterKiwi-Sferea on 01/11/2017.
//

#include "CustomMenu.h"
#include "navi.h"
#include "game.h"
#include "mk_textinput.h"

void prepareForNewMenu()
{
    SDL_Log("Prepare new Menu");
    for (vector<Asset*>::iterator it = gNavi->loaded_assets.begin(); it != gNavi->loaded_assets.end();++it)
    {
        delete (*it);
    }
    for (vector<MK_Gui*>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end();++it)
    {
        delete (*it);
    }
    gNavi->loaded_assets.clear();
    gNavi->gui_buttons.clear();
    gNavi->top10_labels.clear();
}

void forceFirstTimeInstructions()
{
    gNavi->customMenuGenerator.wasForced = true;
    gNavi->current_project->hasShownManual = 1;
    for(int i = 0; i < gNavi->projects_.size();i++)
    {
        if(gNavi->projects_[i].id == gNavi->current_project->id)
        {
            gNavi->projects_[i].hasShownManual = gNavi->current_project->hasShownManual;
            break;
        }
    }
    g_game->state_ = WAITING;
    MK_Button* secondButton = nullptr;
    for (vector<MK_Gui*>::iterator it = gNavi->gui_buttons.begin(); it != gNavi->gui_buttons.end();++it)
    {
        secondButton = (MK_Button*)*it;
        if(secondButton->action != 0)
        {
            break;
        }
    }
    gNavi->AddMenu(secondButton->action);
}

void onActionReceived(int action, int x, int y)
{
    if (gNavi->already_clicked || g_game->state_ == PLAYING)
    {
        return;
    }
    if (action == 0 && gNavi->current_project->hasShownManual == 1)
    {
        gNavi->current_menu_id = 0;
        gNavi->customMenuGenerator.wasForced = false;
        prepareForNewMenu();
        g_game->state_ = PLAYING;
        g_game->start_level(0);
    }
    else if (action > 0)
    {
      /*  if(gNavi->customMenuGenerator.wasForced && gNavi->customMenuGenerator.getMainMenuID() == action)
        {
            gNavi->customMenuGenerator.wasForced = false;
            prepareForNewMenu();
            g_game->state_ = PLAYING;
            g_game->start_level(0);
        }
        else*/
      //  {
            g_game->state_ = WAITING;
            gNavi->AddMenu(action);
       // }
    }
    else if(action == -322)
    {
        gNavi->processForm();
    }
    else if(gNavi->current_project->hasShownManual == 0)
    {
        forceFirstTimeInstructions();
    }
    gNavi->already_clicked = true;
}

bool CustomMenu::createMenu(Menu menuInfo)
{
    bool result = false;
    SDL_Log("CustomMenu.cpp: Create Menu");
    prepareForNewMenu();

    if(g_game->state_ == GAME_OVER && gNavi->game_has_stats == 1 && gNavi->game_stats_begin == 0 && menuInfo.special_ == 0)
    {
        SDL_Log("CustomMenu.cpp: GameOver, stats 1, stratbegin 0, special 0");
        gNavi->AddMenu(gNavi->menu_stats);
        return result;
    }

    if(menuInfo.text_.length() == 0)
    {
        SDL_Log("CustomMenu.cpp: Exit Menu");
        if(g_game->state_ == GAME_OVER)
        {
            SDL_Log("CustomMenu.cpp: Superexit Menu");
            //exitMasterKiwi();
            result = false;
        }
        else if(g_game->state_ != GAME_OVER)
        {
            SDL_Log("ShowGame Menu");
            showGame();
            result = false;
        }
    }
    else
    {
        SDL_Log("initialMenuID Menu");
        if(initialMenuID < 0)
        {
            SDL_Log("initialMenuID < 0");
            initialMenuID = menuInfo.id_;
        }
        if(menuInfo.special_ == SPECIAL_MENU_TOP_10)
        {
            SDL_Log("menuInfo.special_ == SPECIAL_MENU_TOP_10");
            configureTop10(menuInfo.desc_);
        }
        else if(menuInfo.special_ == SPECIAL_MENU_FORM)
        {
            SDL_Log("menuInfo.special_ == SPECIAL_MENU_FORM");
            configureForm(menuInfo);
        }
        menuConfiguration(menuInfo.text_);
    }

    return result;
}

void CustomMenu::configureTop10(string data)
{
    gNavi->top10_labels.clear();
    string topDataFile = "interface/data/" + NumberToString(gNavi->current_project->id) + "_s1.txt";
    TextScanner top10Data(topDataFile);
    if(top10Data.lineList.size() > 0)
    {
        vector<string> menuColors = StringTokenize(data,"s");
        color scoreColor = getColorFromText(menuColors[0].substr(1));
        color nameColor = getColorFromText(menuColors[1].substr(1));
        for(int i = 0;i < top10Data.lineList.size();i++)
        {
            string rankData = gNavi->naviDecrypt(top10Data.lineList[i]);
            addTop10Score(i,rankData,nameColor,scoreColor);
        }
    }
    else
    {
        //TODO: falta descargar!!!!!!!
    }

}

CustomMenu::color CustomMenu::getColorFromText(string toParse)
{
    color result;
    istringstream(toParse.substr(0,2)) >> hex >> result.red;
    istringstream(toParse.substr(2,2)) >> hex >> result.green;
    istringstream(toParse.substr(4,2)) >> hex >> result.blue;
    return  result;
}

void CustomMenu::addTop10Score(int rank,string rankData,color nameColor,color scoreColor)
{
    vector<string> separatedData = StringTokenize(rankData,"?-?");
    MK_Label* nameDisplay = new MK_Label(separatedData[1],70,125*2+rank*84-60);
    nameDisplay->center = false;
    nameDisplay->color.r = nameColor.red;
    nameDisplay->color.g = nameColor.green;
    nameDisplay->color.b = nameColor.blue;
    nameDisplay->text_size = 60.0f;
    gNavi->gui_buttons.push_back(nameDisplay);

    MK_Label* scoreDisplay = new MK_Label(gNavi->number_to_commastring(StringToInt(separatedData[2])),560,125*2+rank*84-60);
    scoreDisplay->center = true;
    scoreDisplay->color.r = scoreColor.red;
    scoreDisplay->color.g = scoreColor.green;
    scoreDisplay->color.b = scoreColor.blue;
    scoreDisplay->text_size = 60.0f;
    gNavi->gui_buttons.push_back(scoreDisplay);
}

void CustomMenu::configureForm(Menu menuInfo)
{
    vector<string> formInfo = StringTokenize(menuInfo.desc_,"s");
    color titleColor = getColorFromText(formInfo[1].substr(1));
    color backgroundColor = getColorFromText(formInfo[0].substr(1));
    color inputColor = getColorFromText(formInfo[2].substr(1));
    if(formInfo[3] == "1")
    {
        createFormField(0,menuInfo.extra_1,titleColor,backgroundColor,inputColor);
    }
    if(formInfo[4] == "1")
    {
        createFormField(1,menuInfo.extra_2,titleColor,backgroundColor,inputColor);
    }
    if(formInfo[5] == "1")
    {
        createFormField(2,menuInfo.extra_3,titleColor,backgroundColor,inputColor);
    }
    if(formInfo[6] == "1")
    {
        createFormField(3,menuInfo.extra_4,titleColor,backgroundColor,inputColor);
    }
}

void CustomMenu::createFormField(int index,string title, color tileColor, color backgroundColor, color inputColor)
{
    MK_Label* lbl = new MK_Label(title,180*2,((125)*2-60)+(220*index));
    lbl->center = true;
    lbl->color.r = tileColor.red;
    lbl->color.g = tileColor.green;
    lbl->color.b = tileColor.blue;
    lbl->text_size = 60.0f;
    gNavi->gui_buttons.push_back(lbl);

    MK_rounded_rect* lr1 = new MK_rounded_rect((180-(288/2))*2,((165-30)*2)+(220*index),(180+(288/2))*2,((165+30)*2)+(220*index));
    lr1->color.r = backgroundColor.red;
    lr1->color.g = backgroundColor.green;
    lr1->color.b = backgroundColor.blue;
    gNavi->gui_buttons.push_back(lr1);

    MK_Textinput *itxt = new MK_Textinput("",(180-(288/2))*2+20,((165-30)*2+27)+(220*index),520,60);
    itxt->color.r = inputColor.red;
    itxt->color.g = inputColor.green;
    itxt->color.b = inputColor.blue;
    gNavi->gui_buttons.push_back(itxt);

    gNavi->menu_stats_fields = index + 1;
}

void CustomMenu::exitMasterKiwi()
{
    SDL_Log("CustomMenu.cpp: exitMasterKiwi()");
    gNavi->reportGameOver();
    gNavi->SaveCurrentProjects();
    delete(gNavi);
    gNavi = 0;
}

void CustomMenu::showGame()
{
    g_game->state_ = PLAYING;
    g_game->start_level(0);
}

void CustomMenu::menuConfiguration(string menuInfo)
{
    SDL_Log("menuConfiguration %s", &menuInfo);
    gNavi->has_game_over_score = false;
    gNavi->game_over_scores_.clear();
    g_game->state_ = MAIN_MENU;
    vector<string> menuElements = StringTokenize(menuInfo,"d");

    for(int i = 0;i < menuElements.size();i++)
    {
        if(isCrrntElemConfigurated(menuElements[i]))
        {
            configureAction();
            configureImage();
        }
    }
    configureMusic();
}

bool CustomMenu::isCrrntElemConfigurated(string elementInfo)
{
    bool result = false;
    vector<string> parts = StringTokenize(elementInfo,"s");

    if(parts.size() != 0)
    {
        result = true;
        currentData.assetId = StringToInt(parts[0]);
        currentData.x = StringToInt(parts[1])*2;
        currentData.y = StringToInt(parts[2])*2;
        currentData.angle = StringToInt(parts[4]);
        currentData.anim_id = StringToInt(parts[5]);
        currentData.hasAction = StringToInt(parts[6]);
        currentData.actionId = StringToInt(parts[7]);
        currentData.diffX = 0;
        currentData.diffY = 0;
        currentData.width = 0;
        currentData.height = 0;
        currentData.flip = 0;
        if (currentData.assetId == GAME_OVER_SCORE)
        {
            configureHighScore(parts[3]);
        }
        else
        {
            currentData.flip = StringToInt(parts[3]);
        }
        if(currentData.hasAction == 1)
        {
            currentData.diffX = StringToInt(parts[10]);
            currentData.diffY = StringToInt(parts[11]);
            currentData.width = StringToInt(parts[8]);
            currentData.height = StringToInt(parts[9]);
        }
    }
    return result;
}

void CustomMenu::configureHighScore(string data)
{
    gNavi->has_game_over_score = true;
    gNavi->game_over_score_x = currentData.x;
    gNavi->game_over_score_y = currentData.y;
    point p(currentData.x,currentData.y);
    if (data.length() == 6)
    {
        int r,g,b;
        StringReplace("x","d",data);

        std::string clr = data;
        std::istringstream(clr.substr(0,2)) >> std::hex >> r;
        std::istringstream(clr.substr(2,2)) >> std::hex >> g;
        std::istringstream(clr.substr(4,2)) >> std::hex >> b;
        p.color.r = r;
        p.color.g = g;
        p.color.b = b;
        p.color.a = 255;
    }
    gNavi->game_over_scores_.push_back(p);
}

void CustomMenu::configureAction()
{
    if (currentData.hasAction == 1)
    {
        MK_Button *btn = new MK_Button(currentData.x-(currentData.width * 0.5f)+currentData.diffX,
                                       currentData.y-(currentData.height * 0.5f)+currentData.diffY
                                        ,currentData.width,currentData.height);
        btn->clickCallback = &onActionReceived;
        btn->action = currentData.actionId;
        gNavi->gui_buttons.push_back(btn);
    }
}

void CustomMenu::configureImage()
{
    Asset *c = new Asset();
    c->load_asset(currentData.assetId);
    if (c->d_->id_ == 0)
    {
        return;
    }
    if (c->d_->type_ == -1 && c->d_->id_ == -1000)
    {
        return;
    }
    else if (c->d_->type_ != 2 && c->img_ != 0)
    {
        c->move(currentData.x,currentData.y);
        c->img_->x = (float)currentData.x;
        c->img_->rotation = currentData.angle;
        if (currentData.flip == 1)
        {
            c->img_->flip = SDL_FLIP_HORIZONTAL;
            if (currentData.angle > 0 || currentData.angle < 0)
            {
                c->img_->rotation = ((360-(c->img_->rotation)));
            }
        }
    }
    gNavi->loaded_assets.push_back(c);
}

void CustomMenu::configureMusic()
{
    SDL_Log("configure music");
    #if !defined(EMSCRIPTEN) && !defined(__WINPHONE__)
        if (gNavi->currentMenu.music_id_ != 0)
        {
            if (gNavi->music_.find(gNavi->currentMenu.music_id_) != gNavi->music_.end())
            {
                if (gNavi->current_music_ != gNavi->music_[gNavi->currentMenu.music_id_])
                {
                    gNavi->current_music_ = gNavi->music_[gNavi->currentMenu.music_id_];
                    Mix_PlayMusic(gNavi->current_music_,-1);
                }
            }
        }
        else
        {
            gNavi->current_music_ = 0;
            Mix_HaltMusic();
        }
    #endif
}

int CustomMenu::getMainMenuID()
{
    return initialMenuID;
}
