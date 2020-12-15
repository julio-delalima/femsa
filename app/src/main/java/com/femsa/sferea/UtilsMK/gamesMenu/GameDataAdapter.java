package com.femsa.sferea.UtilsMK.gamesMenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.femsa.sferea.R;
import com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment;

import java.util.List;

/**
 * Created by MasterKiwi-Sferea on 06/04/2018.
 */

public class GameDataAdapter extends RecyclerView.Adapter<GameDataAdapter.ViewHolder>
{
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageButton gameButton;

        public ViewHolder(View itemView)
        {
            super(itemView);
            gameButton = itemView.findViewById(R.id.start_game_button);
            DisplayMetrics displayMetrics = itemView.getResources().getDisplayMetrics();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(displayMetrics.widthPixels * 0.48f), (int) (displayMetrics.heightPixels * 0.43f));
            gameButton.setLayoutParams(params);
            gameButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    callBack.onPressed(view.getId());
                }
            });
        }
    }

    private List<GameData> gamesData;
    private MisJuegosFragment.GameMenuSelected callBack;

    public GameDataAdapter(List<GameData> games,MisJuegosFragment.GameMenuSelected buttonOnClick)
    {
        gamesData = games;
        callBack = buttonOnClick;
    }

    @Override
    public GameDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.game_menu_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GameDataAdapter.ViewHolder viewHolder, int position)
    {
        GameData game = gamesData.get(position);
        viewHolder.gameButton.setId(game.getGameID());
        viewHolder.gameButton.setBackgroundResource(R.drawable.login_button_background);
        if(game.hasImage)
        {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(game.getGameImage());
            viewHolder.gameButton.setBackgroundDrawable(bitmapDrawable);
        }
        else
        {
            viewHolder.gameButton.setBackgroundColor(Color.rgb(175,204,35));
        }
    }

    @Override
    public int getItemCount()
    {
        return gamesData.size();
    }

    public void clearData()
    {
        int size = gamesData.size();
        if (size > 0)
        {
            for (int i = 0; i < size; i++)
            {
                gamesData.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }
}
