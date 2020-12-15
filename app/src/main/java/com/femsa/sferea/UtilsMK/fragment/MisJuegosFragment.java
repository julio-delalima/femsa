package com.femsa.sferea.UtilsMK.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.AnimationManager;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.GameLoadingActivity;
import com.femsa.sferea.UtilsMK.gamesMenu.GameDataRow;
import com.femsa.sferea.UtilsMK.gamesMenu.GameDataRowAdapter;
import com.femsa.sferea.masterkiwi.MasterKiwiArgs;
import com.femsa.sferea.masterkiwi.MasterKiwiListener;
import com.femsa.sferea.masterkiwi.MasterKiwiWrapper;
import com.femsa.sferea.Utilities.GlobalLoadingController;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

interface GameMenuCallBacks
{
    void onPressed(int gameID);
}

public class MisJuegosFragment extends Fragment implements MasterKiwiListener {
    public static final String RECEIVING_USER = "userFromInput";
    public static final String RECEIVING_PASSWORD = "passwordFromInput";
    public static final String SENDING_GAME_ID = "selectedGameID";
    public static final String SENDING_GAME_TYPE = "selectedGameType";
    public static final String SENDING_GAME_NAME = "selectedGameName";
    public static final String SENDING_GAME_IMAGE = "selectedGameImage";
    public static final String IS_MULTIPLAYER_GAME = "multijugadorGYE";
    public static final String MULTIPLAYER_GAME_DATA = "multijugadorGYEData";
    public static final String SENDING_SQUIRREL_JSON = "SQJson";

    private View mVistaFragmento;

    public String userName;
    public String password;
    public String userEmail;
    public String userCompany;
    public Double userPhone;
    private final int ELEMENTS_PER_ROW = 2;
    private final String USER_EMAIL = "umail";
    private final String USER_PHONE = "uphone";
    private final String USER_COMPANY = "ucompany";
    private final String GAMES_ARRAY = "projects";
    private final String GAMES_ID = "pid";
    private final String GAMES_TITLE = "ptitle";
    private final String GAMES_TYPE = "type";

    protected MasterKiwiWrapper wrapper;
    protected boolean isWorking;
    protected int selectedGame = 0;
    protected ArrayList<Integer> allGamesIds;
    protected ArrayList<Integer> allGamesTypes;
    protected ArrayList<String> allGamesNames;
    protected ArrayMap<Integer, Bitmap> allBitmaps;
    protected MenuImageDownload asyncTextures;

    protected ArrayList<GameDataRow> games;
    protected GameDataRowAdapter rowsAdapter;

    protected AnimationManager animManager;
    protected RecyclerView gamesView;
    protected RelativeLayout fragmentLayout;
    protected ImageView bottomGradient;
    protected Button updateButton;
    protected boolean forceUpdate;

    public int downloadIndex;
    public String currentUrl;
    public Boolean canDownload = true;
    public GlobalLoadingController loadingController;

    public class GameMenuSelected implements GameMenuCallBacks {
        @Override
        public void onPressed(int gameID) {
            pressed(gameID);
        }
    }

    protected GameMenuSelected callBack;

    public static MisJuegosFragment newInstance(String userName, String password)
    {
        Bundle args = new Bundle();
        args.putString(RECEIVING_USER, userName);
        args.putString(RECEIVING_PASSWORD, password);
        MisJuegosFragment fragment = new MisJuegosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        forceUpdate = false;
        animManager = new AnimationManager();
        mVistaFragmento = inflater.inflate(R.layout.mis_juegos_fragment, container, false);
        userName = getArguments().getString(RECEIVING_USER);
        password = getArguments().getString(RECEIVING_PASSWORD);
        loadingController = (GlobalLoadingController) getActivity();

        allGamesIds = new ArrayList<>();
        allGamesTypes = new ArrayList<>();
        allGamesNames = new ArrayList<>();
        allBitmaps = new ArrayMap<Integer, Bitmap>();
        games = new ArrayList<>();
        callBack = new GameMenuSelected();
        isWorking = false;
        wrapper = MasterKiwiWrapper.GetInstance();
        wrapper.setActivity(getActivity());
        wrapper.registerForEvents(this);
        wrapper.loginToMasterKiwi(userName, password,true);

        getResourcesFromXML();
        adjustSizes();
        positionateObjects();
        loadingController.startLoading();
        return mVistaFragmento;
    }

    protected void getResourcesFromXML()
    {
        bottomGradient = mVistaFragmento.findViewById(R.id.bottom_gradient);
        updateButton = mVistaFragmento.findViewById(R.id.update_button);
        fragmentLayout = mVistaFragmento.findViewById(R.id.fragment_layout);
    }

    protected void adjustSizes()
    {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        if(allGamesIds.size() < 2)
        {
            fragmentLayout.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels,displayMetrics.heightPixels));
        }
        bottomGradient.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels, (int) (displayMetrics.heightPixels * 0.3f)));
        updateButton.setLayoutParams(new RelativeLayout.LayoutParams((int) (displayMetrics.widthPixels * 0.6f), (int) (displayMetrics.heightPixels * 0.07f)));
        bottomGradient.setScaleType(ImageView.ScaleType.FIT_XY);
        updateButton.setText(getActivity().getResources().getString(R.string.update_button));
        updateButton.setTextColor(getResources().getColor(R.color.femsa_red_b));
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                forceUpdate = true;
                loadingController.startLoading();
                clearMenu();
                wrapper.loginToMasterKiwi(userName, password,false);
            }
        });
    }

    protected void clearMenu()
    {
        allGamesIds.clear();
        allGamesTypes.clear();
        allGamesNames.clear();
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                gamesView = getActivity().findViewById(R.id.main_content);
                gamesView.setLayoutManager(new LinearLayoutManager(getActivity()));
                if (rowsAdapter != null)
                {
                    rowsAdapter.clearData();
                }
            }
        });
    }

    protected void positionateObjects()
    {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        animManager.getNewTranslateAnimForView(bottomGradient, 0, displayMetrics.heightPixels * 0.75f, 0, 0);
        animManager.getNewTranslateAnimForView(updateButton, displayMetrics.widthPixels * 0.2f, displayMetrics.heightPixels * 0.78f, 0, 0);
    }

    protected void loadGameList()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                gamesView = getActivity().findViewById(R.id.main_content);
                gamesView.setLayoutManager(new LinearLayoutManager(getActivity()));
                if (rowsAdapter != null)
                {
                    rowsAdapter.clearData();
                }
            }
        });
        downloadIndex = 0;
        requestImageDownload();
    }

    @Override
    public void onDestroy()
    {
        wrapper.unResgisterForEvents(this);
        super.onDestroy();
    }

    @Override
    public void onLogin(Object sender, final MasterKiwiArgs eventArgs)
    {
        try
        {
            JSONObject userData = new JSONObject(eventArgs.userData);
            userEmail = userData.getString(USER_EMAIL);
            userCompany = userData.getString(USER_COMPANY);
            userPhone = userData.getDouble(USER_PHONE);
            loadingController.sendLoginData(userName,userCompany,userEmail,userPhone);
            JSONArray gamesList = userData.getJSONArray(GAMES_ARRAY);
            for (int i = 0; i < gamesList.length(); i++)
            {
                JSONObject game = gamesList.getJSONObject(i);
                addButtonWithData(game);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        loadGameList();
    }

    protected void addButtonWithData(JSONObject gameData)
    {
        try
        {
            allGamesIds.add(gameData.getInt(GAMES_ID));
            allGamesTypes.add(gameData.getInt(GAMES_TYPE));
            allGamesNames.add(gameData.getString(GAMES_TITLE));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    void pressed(int gameID)
    {
        if (!isWorking)
        {
            String absolutePath = getAbsoultPath();
            File image = new File(absolutePath, String.format("%s.mktx",getFileNameForIndex(allGamesIds.indexOf(gameID))));
            Intent mainIntent = new Intent().setClass(getActivity(), GameLoadingActivity.class);
            mainIntent.putExtra(SENDING_GAME_ID, gameID);
            mainIntent.putExtra(SENDING_GAME_TYPE, allGamesTypes.get(allGamesIds.indexOf(gameID)));
            mainIntent.putExtra(SENDING_GAME_NAME, allGamesNames.get(allGamesIds.indexOf(gameID)));
            mainIntent.putExtra(SENDING_GAME_NAME, allGamesNames.get(allGamesIds.indexOf(gameID)));
            mainIntent.putExtra(SENDING_GAME_IMAGE,image.getAbsolutePath());
            startActivity(mainIntent);
        }
    }

    public void requestImageDownload()
    {
        if (downloadIndex < allGamesIds.size())
        {
            getCover();
        }
        else
        {
            forceUpdate = false;
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    int j = 0;
                    for (int i = 0; i < allGamesIds.size(); i += ELEMENTS_PER_ROW) {
                        games.add(new GameDataRow());
                        j = 0;
                        while (j < ELEMENTS_PER_ROW && (i + j) < allGamesIds.size()) {
                            if (allBitmaps.containsKey(allGamesIds.get(i + j))) {
                                games.get(games.size() - 1).registerGameData(allGamesIds.get(i + j), allBitmaps.get(allGamesIds.get(i + j)));
                            } else {
                                games.get(games.size() - 1).registerGameData(allGamesIds.get(i + j));
                            }
                            j++;
                        }
                    }
                    rowsAdapter = new GameDataRowAdapter(games, callBack);
                    gamesView.setAdapter(rowsAdapter);
                    loadingController.finishedLoading();
                }
            });
        }
    }

    protected void getCover()
    {
        String absolutePath = getAbsoultPath();
        File image = new File(absolutePath, String.format("%s.mktx",getFileNameForIndex(downloadIndex)));
        if(image.exists() && !forceUpdate)
        {
            downloadIndex++;
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            if (bitmap != null)
            {
                setBitmap(bitmap);
            }
            requestImageDownload();
        }
        else
        {
            currentUrl = "https://www.masterkiwi.com/getgamebackground?pid=" + allGamesIds.get(downloadIndex);
            //Log.d("...", currentUrl);
            downloadIndex++;
            asyncTextures = new MenuImageDownload();
            asyncTextures.execute(this);
        }
    }

    public void setBitmap(Bitmap bmp)
    {
        allBitmaps.put(allGamesIds.get(downloadIndex - 1), bmp);
    }

    public String getAbsoultPath()
    {
        String absolutePath;
        if (Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED))
        {
            absolutePath = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        }
        else
        {
            absolutePath = getActivity().getFilesDir().getAbsolutePath();
        }
        return absolutePath;
    }

    public String getFileNameForIndex(int index)
    {
        String result = userName + "_" + allGamesNames.get(index) + "_" + allGamesIds.get(index);
        return result;
    }

    @Override
    public void onLogout(Object sender, MasterKiwiArgs eventArgs)
    {
        loadingController.finishedLoading();
    }

    @Override
    public void onNetError(Object sender, MasterKiwiArgs eventArgs) {
        isWorking = false;
    }

    @Override
    public void onInitiatingCompresure(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void onInitiatingTextures(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void onInitiatingAsstesList(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void onInitiatingDownload(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void onDownloadFinished(Object sender, MasterKiwiArgs eventArgs) {
        isWorking = false;
    }

    @Override
    public void onAssetsDownload(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void onSatrtingGame(Object sender, MasterKiwiArgs eventArgs)
    {
        if (isWorking)
        {
            isWorking = false;
        }
    }

    @Override
    public void onGameOver(Object sender, MasterKiwiArgs eventArgs)
    {
        isWorking = false;
    }

    @Override
    public void onExitGame(Object sender, MasterKiwiArgs eventArgs) {
        isWorking = false;
    }

    @Override
    public void onNoneUserLogged(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void onNoGameData(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void jsonSQData(Object sender, MasterKiwiArgs eventArgs) {
    }

    @Override
    public void mandarDatos(Object sender, MasterKiwiArgs eventArgs) {

    }
}

class MenuImageDownload extends AsyncTask<MisJuegosFragment, Void, Void> {
    @Override
    protected Void doInBackground(MisJuegosFragment... activity) {
        if (isCancelled()) {
            return null;
        }
        String imageUrl = activity[0].currentUrl;
        try {
            java.net.URL url = new java.net.URL(imageUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            //String absolutePath = activity[0].getAbsoultPath();
            //File image = new File(absolutePath, String.format("%s.mktx",activity[0].getFileNameForIndex(activity[0].downloadIndex-1)));
            //imageUrl = image.getAbsolutePath();
            //if (activity[0].canDownload) {
                //FileOutputStream fileOutputStream = new FileOutputStream(imageUrl);
            //    Bitmap bitmap = BitmapFactory.decodeStream(input);
             //   if (bitmap != null) {
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
             //       activity[0].setBitmap(bitmap);
             //   }
             //   activity[0].requestImageDownload();
                //fileOutputStream.close();
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
