package com.example.josiah.organizedpiecesofstockplayground.UtilityClasses;

import android.util.Log;

import com.example.josiah.organizedpiecesofstockplayground.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Josiah on 5/27/2017.
 */

public class Portfolio {
    public static final String MONEY_LEFT="money_left", PORTFOLIO_NAME = "portfolio_name", TOTAL_VALUE = "portfolio_value", OWNER_NAME ="owner_name";

    private double moneyLeft;
    private Set<Purchases> myUsefulPurchases;

    public void setMyPurchases(Map<String, Map<Stock, Integer>> myPurchases) {
        this.myPurchases = myPurchases;
    }

    public void setMyPurchases(Set<Purchases> thePurchases){
        myUsefulPurchases = thePurchases;
    }

    private Map<String, Map<Stock, Integer>> myPurchases;
    private double totalValue;

    public double getMoneyLeft() {
        return moneyLeft;
    }



    public void setMoneyLeft(double moneyLeft) {
        this.moneyLeft = moneyLeft;
    }

    public Map<String, Map<Stock, Integer>> getMyPurchases() {
        return myPurchases;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Portfolio(double moneyLeft, Map<Stock, Integer> myPurchases, double totalValue, String name, String owner) {
        this.moneyLeft = moneyLeft;
        this.myPurchases = new HashMap<>();

        this.totalValue = totalValue;
        this.name = name;
        this.owner = owner;
    }

    public Portfolio(double moneyLeft, double totalValue, String name, String owner_name) {
        this.moneyLeft = moneyLeft;
        this.myPurchases = new HashMap<>();

        this.totalValue = totalValue;
        this.name = name;
        this.owner = owner_name;
    }

    private String name;
    private String owner;

    public boolean purchaseStock(String stockSignature, int quantity, Stock theStock) throws Exception {
        if(quantity < 0 || quantity * theStock.getTodaysPrice() > this.getMoneyLeft()){
            return false;
        } else{
            this.moneyLeft = this.moneyLeft - quantity * theStock.getTodaysPrice();
//            if(myPurchases.containsKey(stockSignature)){
//                this.getMyPurchases().get(stockSignature).put(theStock, this.getMyPurchases().get(stockSignature).get(theStock) + quantity);
//            } else {
//                this.getMyPurchases().put(stockSignature, new HashMap<Stock, Integer>());
//            }
            return true;
        }
    }

    public static String parsePortfolioJSON(String stockJSON, Collection<Portfolio> stockList) {
        String reason = null;
        if (stockJSON != null) {
            try {
                JSONArray arr = new JSONArray(stockJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Portfolio portfolio = new Portfolio(obj.getDouble(MONEY_LEFT), obj.getDouble(TOTAL_VALUE), obj.getString(PORTFOLIO_NAME), obj.getString(OWNER_NAME));

                    stockList.add(portfolio);
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
