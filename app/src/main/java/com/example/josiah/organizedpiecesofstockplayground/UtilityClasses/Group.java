package com.example.josiah.organizedpiecesofstockplayground.UtilityClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Josiah on 5/27/2017.
 */

public class Group {
    private final static String OWNER = "Owner", MEMBER = "user", NAME ="group_name";
    public static final String PORTFOLIO_NAME = "portfolio_name", PORTFOLIO_VALUE = "portfolio_value";
    private String myOwnerUsername;
    private Map<String, Map<String, Double>> myMemberInfo;
    private String myName;
    private double portfolio_value;
    public final static String ALL_GROUP_URL ="http://cssgate.insttech.washington.edu/~josiah3/PHP_Code/PHP%20Code/list.php?cmd=groups&username=NA";
    public final static String MEMBER_GROUP_URL ="http://cssgate.insttech.washington.edu/~josiah3/PHP_Code/PHP%20Code/list.php?cmd=groups&member=";
    public final static String OWNER_GROUP_URL ="http://cssgate.insttech.washington.edu/~josiah3/PHP_Code/PHP%20Code/list.php?cmd=groups&owner=";

    public Group(String ownerUsername, String myName) {
        this.myOwnerUsername = ownerUsername;
        this.myMemberInfo = new HashMap<>();
        this.myName = myName;
        this.portfolio_value = 10000;
    }

    public Group(String ownerUsername, String myName, double portfolio_value){
        this.myOwnerUsername = ownerUsername;
        this.myMemberInfo = new HashMap<>();
        this.myName = myName;
        this.portfolio_value = portfolio_value;
    }

    public String getMyOwnerUsername() {
        return myOwnerUsername;
    }

    public void setMyOwnerUsername(String myOwnerUsername) {
        this.myOwnerUsername = myOwnerUsername;
    }

    public Map<String, Map<String, Double>> getMyMembers() {
        return this.myMemberInfo;
    }


    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void addMember(String username, String portfolioName, Double value){
        HashMap portfolioHolder = new HashMap<String, Double>();
        portfolioHolder.put(portfolioName, value);
        if(this.myMemberInfo == null){
            this.myMemberInfo = new HashMap<String, Map<String, Double>>();
        }
        this.myMemberInfo.put(username, portfolioHolder);


    }

    public static String parseStockJSON(String stockJSON, List<Group> groupList) {
        String reason = null;
        if (stockJSON != null) {
            try {
                Log.e("About to add new groups","");
                JSONArray arr = new JSONArray(stockJSON);
                Map<String, Group> groupNameToGroup = new HashMap<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String owner = obj.getString(OWNER);
                    String member = obj.getString(MEMBER);
                    String name = obj.getString(NAME);
                    String portfolioName = obj.getString(PORTFOLIO_NAME);
                    Double value = obj.getDouble(PORTFOLIO_VALUE);
                    if(groupNameToGroup.containsKey(name)){
                        groupNameToGroup.get(name).addMember(member, portfolioName, value);
                    } else {
                        Group adding = new Group(owner, name);
                        adding.addMember(member, portfolioName, value);
                        groupNameToGroup.put(name, adding);
                    }
                }
                groupList.addAll(groupNameToGroup.values());
            } catch (JSONException e) {
                reason = "Unable to parse stock data. Reason is as follows: " + e.getMessage();
            }
        } else {
            Log.e("Stock JSON error", "trying to parse a null json string");
        }
        return reason;
    }

    public double getPortfolioValue() {
        return portfolio_value;
    }

    public boolean isUserMember(String username){
        return this.myMemberInfo.containsKey(username);
    }
}
