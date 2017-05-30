package com.example.josiah.organizedpiecesofstockplayground.UtilityClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Josiah on 5/29/2017.
 */

public class Purchases {
    public static final String USERNAME = "owner_name", PORTFOLIO_NAME="portfolio_name", STOCK_SIGNATURE="stock_signature", QUANTITY = "quantity";
    private String stock_signature;

    public String getStock_signature() {
        return stock_signature;
    }

    public void setStock_signature(String stock_signature) {
        this.stock_signature = stock_signature;
    }

    public Purchases(String stock, String username, String portfolio_name, int quantity) {
        this.stock_signature = stock;
        this.username = username;
        this.portfolio_name = portfolio_name;
        this.quantity = quantity;
    }

    public String getUsername() {
        return username;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPortfolio_name() {
        return portfolio_name;
    }

    public void setPortfolio_name(String portfolio_name) {
        this.portfolio_name = portfolio_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private String username;
    private String portfolio_name;
    private int quantity;

    @Override
    public int hashCode(){
        return (username + portfolio_name + stock_signature).hashCode();
    }

    @Override
    public boolean equals(Object o){
        Purchases p = (Purchases) o;
        return p.getPortfolio_name().equals(this.getPortfolio_name()) && p.getStock_signature().equals(this.getStock_signature()) && p.getUsername().equals(this.getUsername());
    }

    public static String parsePurchaseJSON(String purchaseJSON, Map<Purchases, Purchases> purchasesList) {
        String reason = null;
        if (purchaseJSON
                != null) {
            try {
                JSONArray arr = new JSONArray(purchaseJSON);
                for(int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Purchases toAdd = new Purchases(obj.getString(STOCK_SIGNATURE), obj.getString(USERNAME), obj.getString(PORTFOLIO_NAME), obj.getInt(QUANTITY));
                    purchasesList.put(toAdd, toAdd);
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
