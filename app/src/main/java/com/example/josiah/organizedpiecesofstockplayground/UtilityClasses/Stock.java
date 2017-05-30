package com.example.josiah.organizedpiecesofstockplayground.UtilityClasses;

/**
 * Created by Josiah on 5/27/2017.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josiah on 5/23/2017.
 */

public class Stock implements Serializable {

    private String stockSignature;
    private double todaysPrice;
    private double todaysChange;
    private String name;
    public static final String STOCK_URL = "http://cssgate.insttech.washington.edu/~josiah3/PHP_Code/PHP%20Code/list.php?cmd=stocks";

    public static final String STOCK_SIGNATURE = "stock_signature", TODAYS_PRICE = "stock_today_value", TODAYS_CHANGE = "stock_today_change", NAME = "stock_name";

    public double getTodaysPrice() {
        return todaysPrice;
    }

    public void setTodaysPrice(double todaysPrice) {
        this.todaysPrice = todaysPrice;
    }

    public double getTodaysChange() {
        return todaysChange;
    }

    public void setTodaysChange(double todaysChange) {
        this.todaysChange = todaysChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStockSignature() {
        return stockSignature;
    }

    public void setStockSignature(String stockSignature) {
        this.stockSignature = stockSignature;
    }

    public Stock(double todaysPrice, double todaysChange, String name, String stockSignature) {
        this.todaysPrice = todaysPrice;
        this.todaysChange = todaysChange;
        this.name = name;
        this.stockSignature = stockSignature;

    }


    public static String parseStockJSON(String stockJSON, List<Stock> stockList) {
        String reason = null;
        if (stockJSON != null) {
            try {
                JSONArray arr = new JSONArray(stockJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Stock stock = new Stock(obj.getDouble(TODAYS_PRICE), obj.getDouble(TODAYS_CHANGE), obj.getString(STOCK_SIGNATURE), obj.getString(NAME));

                    stockList.add(stock);
                }
            } catch (JSONException e) {
                reason = "Unable to parse stock data. Reason is as follows: " + e.getMessage();
            }
        } else {
            Log.e("Stock JSON error", "trying to parse a null json string");
        }
        return reason;
    }

    public static List<Stock> getDummyItems() {

        List<Stock> toReturn = new ArrayList<>();
        toReturn.add(new Stock(5,10,"YHOO", "Yahoo"));
        return toReturn;
    }

    @Override
    public boolean equals(Object o){
        return ((Stock) o).getStockSignature().equals(this.getStockSignature());
    }

    @Override
    public int hashCode(){
        return this.getStockSignature().hashCode();
    }
}