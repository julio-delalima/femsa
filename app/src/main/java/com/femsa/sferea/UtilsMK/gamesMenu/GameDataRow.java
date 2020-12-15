package com.femsa.sferea.UtilsMK.gamesMenu;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by MasterKiwi-Sferea on 09/04/2018.
 */

public class GameDataRow
{
    protected ArrayList<Boolean> doesElementHasImage;
    protected ArrayList<Integer> rowGameIds;
    protected ArrayList<Bitmap> rowGameImages;

    public GameDataRow()
    {
        doesElementHasImage = new ArrayList<>();
        rowGameIds = new ArrayList<>();
        rowGameImages = new ArrayList<>();
    }

    public void registerGameData(int gameID,Bitmap gameImage)
    {
        rowGameImages.add(gameImage);
        registerGameData(gameID,true);
    }

    public void registerGameData(int gameID)
    {
        registerGameData(gameID,false);
    }

    protected void registerGameData(int gameID,boolean hasImage)
    {
        rowGameIds.add(gameID);
        doesElementHasImage.add(hasImage);
    }

    public int getRowSize()
    {
        return rowGameIds.size();
    }

    public boolean hasImageOnPosition(int position)
    {
        return  doesElementHasImage.get(position);
    }

    public int getGameIdOnPosition(int position)
    {
        return  rowGameIds.get(position);
    }

    public Bitmap getGameImageOnPosition(int position)
    {
        return  rowGameImages.get(position);
    }
}
