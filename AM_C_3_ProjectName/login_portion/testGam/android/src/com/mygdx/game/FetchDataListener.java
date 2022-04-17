package com.mygdx.game;

/**
 * Created by Allie on 12/5/2017.
 */

import java.util.List;

public interface FetchDataListener {
    public void onFetchComplete(List<Application> data);
    public void onFetchFailure(String msg);
}