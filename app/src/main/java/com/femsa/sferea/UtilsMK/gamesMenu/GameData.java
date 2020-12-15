package com.femsa.sferea.UtilsMK.gamesMenu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by MasterKiwi-Sferea on 06/04/2018.
 */

public class GameData
{
    public boolean hasImage;

    protected Integer gameID;
    protected Bitmap gameImage;

    public GameData(int id,Bitmap image)
    {
        gameID = id;
        gameImage = image;
        hasImage = true;
    }

    public GameData(int id)
    {
        gameID = id;
        hasImage = false;
    }

    public static ArrayList<GameData> createGamesRow(GameDataRow gameData)
    {
        ArrayList<GameData> contacts = new ArrayList<GameData>();
        for (int i = 0; i < gameData.getRowSize(); i++)
        {
            if(gameData.hasImageOnPosition(i))
            {
                contacts.add(new GameData(gameData.getGameIdOnPosition(i),gameData.getGameImageOnPosition(i)));
            }
            else
            {
                contacts.add(new GameData(gameData.getGameIdOnPosition(i)));
            }
        }
        return contacts;
    }

    public Bitmap getGameImage()
    {
        return  roundCornerImage(gameImage,50);
    }

    public Bitmap roundCornerImage(Bitmap raw, float round)
    {
        int width = raw.getWidth();
        int height = raw.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0, 0, 0, 0);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#000000"));
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF, round, round, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(raw, rect, rect, paint);
        return result;
    }

    public int getGameID()
    {
        return  gameID;
    }
}
