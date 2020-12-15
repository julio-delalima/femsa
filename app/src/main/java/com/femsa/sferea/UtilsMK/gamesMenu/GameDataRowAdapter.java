package com.femsa.sferea.UtilsMK.gamesMenu;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.femsa.sferea.R;
import com.femsa.sferea.UtilsMK.fragment.MisJuegosFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MasterKiwi-Sferea on 09/04/2018.
 */

public class GameDataRowAdapter  extends RecyclerView.Adapter<GameDataRowAdapter.GameRowViewHolder>
{
    public class GameRowViewHolder extends RecyclerView.ViewHolder
    {
        public RecyclerView row;

        public GameRowViewHolder(View itemView)
        {
            super(itemView);
            row = itemView.findViewById(R.id.games_row);
            DisplayMetrics displayMetrics = itemView.getResources().getDisplayMetrics();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(displayMetrics.widthPixels * 0.99f), (int) (displayMetrics.heightPixels * 0.45f));
            params.leftMargin = (int)(displayMetrics.widthPixels * 0.015f);
            row.setLayoutParams(params);
            row.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        }
    }

    private List<GameDataRow> menuRows;
    private List<GameDataAdapter> adapters;
    private MisJuegosFragment.GameMenuSelected callBack;

    public GameDataRowAdapter(List<GameDataRow> contacts,MisJuegosFragment.GameMenuSelected onClick)
    {
        menuRows = contacts;
        callBack = onClick;
        adapters = new ArrayList<>();
    }

    @Override
    public GameDataRowAdapter.GameRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.game_menu_row, parent, false);

        GameDataRowAdapter.GameRowViewHolder viewHolder = new GameDataRowAdapter.GameRowViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GameDataRowAdapter.GameRowViewHolder viewHolder, int position)
    {
        GameDataRow game = menuRows.get(position);
        ArrayList<GameData> games = GameData.createGamesRow(game);
        GameDataAdapter adapter = new GameDataAdapter(games,callBack);
        viewHolder.row.setAdapter(adapter);
        adapters.add(adapter);
    }

    @Override
    public int getItemCount()
    {
        return menuRows.size();
    }

    public void clearData()
    {
        int size = menuRows.size();
        if (size > 0)
        {
            for (int i = 0; i < size; i++)
            {
                menuRows.remove(0);
                adapters.get(0).clearData();
            }
            notifyItemRangeRemoved(0, size);
        }
    }
}
