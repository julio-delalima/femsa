package com.femsa.sferea.Utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameJSONParser {

    JSONObject jsonObj;

    JSONArray kiwiJson;

    int totalGames;

    String[] name, gameID, script, ucompany;

    String[]  projectID, title, type;

    public void initJson(String json) throws JSONException {

        jsonObj = new JSONObject(json);

        kiwiJson = jsonObj.getJSONArray("games");

        //Log.d("JSON", kiwiJson.toString());
        //projectGames = kiwiJson.getJSONArray("projects");
        setData();
    }

    private void setData() throws JSONException
        {
            totalGames = kiwiJson.length();
            name = new String[totalGames];
            for(int i = 0; i < totalGames; i++)
                {
                    name[i] = kiwiJson.getJSONObject(i).getString("name");
                }
        }

    public void printData()
    {
        for(int i = 0; i < totalGames; i++)
        {
            //Log.d("JSON", name[i]);
        }
    }

///////////////////////////////////////////////

    public void initJsonV2(String json) throws JSONException {

        jsonObj = new JSONObject(json);

        kiwiJson = jsonObj.getJSONArray("projects");

        setGameData();

        printGameData();
        //Log.d("JSON", kiwiJson.toString());
        //projectGames = kiwiJson.getJSONArray("projects");

    }

    private void setGameData() throws JSONException
    {
        totalGames = kiwiJson.length();
        projectID = new String[totalGames];
        type = new String[totalGames];
        title = new String[totalGames];

        for(int i = 0; i < totalGames; i++)
        {
            projectID[i] = kiwiJson.getJSONObject(i).getString("pid");
            type[i] = kiwiJson.getJSONObject(i).getString("type");
            title[i] = kiwiJson.getJSONObject(i).getString("ptitle");
        }
    }

    public void printGameData()
    {
        for(int i = 0; i < totalGames; i++)
        {
            //Log.d("JSON", projectID[i]);
            //Log.d("JSON", type[i]);
            //Log.d("JSON", title[i]);
        }
    }
}
