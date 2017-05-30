package com.example.josiah.organizedpiecesofstockplayground.UtilityClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Josiah on 5/29/2017.
 */

public class UserParticipationInGroup {
    public static final String USERNAME="user", PORTFOLIO_NAME="portfolio_name", PORTFOLIO_VALUE="portfolio_value";

    private String username;

    public UserParticipationInGroup(String username, String portfolioName, Double portfolioValue) {

        this.username = username;
        this.portfolioName = portfolioName;
        this.portfolioValue = portfolioValue;
    }

    public String getUsername() {
        return username;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public Double getPortfolioValue() {
        return portfolioValue;
    }

    private String portfolioName;
    private Double portfolioValue;

    public static String parseStockJSON(String stockJSON, List<UserParticipationInGroup> stockList) {
        String reason = null;
        if (stockJSON != null) {
            try {
                JSONArray arr = new JSONArray(stockJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    UserParticipationInGroup user = new UserParticipationInGroup(obj.getString(USERNAME), obj.getString(PORTFOLIO_NAME), obj.getDouble(PORTFOLIO_VALUE));

                    stockList.add(user);
                }
            } catch (JSONException e) {
                reason = "Unable to parse stock data. Reason is as follows: " + e.getMessage();
            }
        } else {
            Log.e("Stock JSON error", "trying to parse a null json string");
        }
        return reason;
    }

}
