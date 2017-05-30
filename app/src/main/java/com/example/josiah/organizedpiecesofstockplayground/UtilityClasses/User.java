package com.example.josiah.organizedpiecesofstockplayground.UtilityClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Josiah on 5/27/2017.
 */

public class User {

    public List<Portfolio> getMyPortfolios() {
        return myPortfolios;
    }

    public String getMyUsername() {
        return myUsername;
    }

    public List<Group> getMyGroupsIOwn() {
        return myGroupsIOwn;
    }

    public List<Group> getMyGroupsIAmAPartOf() {
        return myGroupsIAmAPartOf;
    }

    public Map<Group, Portfolio> getMyPortfoliosByGroup() {
        return myPortfoliosByGroup;
    }

    private List<Portfolio> myPortfolios;
    private String myUsername;
    private List<Group> myGroupsIOwn;
    private List<Group> myGroupsIAmAPartOf;
    private Map<Group, Portfolio> myPortfoliosByGroup;

    public User(String myUsername) {
        this.myUsername = myUsername;
    }
}
