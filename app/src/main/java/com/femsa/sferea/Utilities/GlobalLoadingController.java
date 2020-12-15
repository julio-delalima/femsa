package com.femsa.sferea.Utilities;

/**
 * Created by MasterKiwi-Sferea on 18/04/2018.
 */

public interface GlobalLoadingController {
    void startLoading();
    void finishedLoading();
    void sendLoginData(String name,String company,String mail,Double phone);
}
