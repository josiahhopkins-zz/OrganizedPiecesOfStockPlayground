package com.example.josiah.organizedpiecesofstockplayground;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.josiah.organizedpiecesofstockplayground.UtilityClasses.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements InitialDisplayFragment.OnFragmentInteractionListener, JoinGroupFragment.OnFragmentInteractionListener, PortfolioListFragment.OnListFragmentInteractionListener, UsersWithinGroupListFragment.OnListFragmentInteractionListener, AllGroupsFragmentListFragment.OnListFragmentInteractionListener, StockListFragment.OnListFragmentInteractionListener, StockPurchaseFragment.OnFragmentInteractionListener {
    public Stock getCurrentStock() {
        return currentStock;
    }
    public static final String PURCHASES_URL = "http://cssgate.insttech.washington.edu/~josiah3/PHP_Code/PHP%20Code/list.php?cmd=purchase";

    public void setCurrentStock(Stock currentStock) {
        this.currentStock = currentStock;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public Portfolio getCurrentPortfolio() {
        return currentPortfolio;
    }

    public void setCurrentPortfolio(Portfolio currentPortfolio) {
        this.currentPortfolio = currentPortfolio;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = new HashSet<>(stockList);
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    private Stock currentStock;
    private User currentUser;
    private Group currentGroup;
    private Portfolio currentPortfolio;

    public Collection<Stock> getStockList() {
        return stockList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    private Collection<Stock> stockList;
    private List<Group> groupList;
    private List<User> userList;
    private Map<Purchases, Purchases> purchaseList;
    private Collection<Portfolio> portfolioList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        purchaseList = new HashMap<>();
        debugSetup();
        DownloadTask d = new DownloadTask();
        d.execute(new String[]{PURCHASES_URL});


        SharedPreferences mySharedPreferences = this.getSharedPreferences("com.example.josiah.organizedpiecesofstockplayground", Context.MODE_PRIVATE);
        mySharedPreferences.edit().putString("Username", "Josiah");

        FragmentManager fm = getSupportFragmentManager();
        InitialDisplayFragment s = new InitialDisplayFragment();
        fm.beginTransaction()
                .add(R.id.fragment_container, s)
                .commit();
    }

    private void debugSetup() {
        this.currentUser = new User("Josiah");
    }

    @Override
    public void stockPurchaseInteraction() {

    }

    @Override
    public void onStockInteraction(Stock item) {
        this.currentStock = item;

        FragmentManager fm = getSupportFragmentManager();
        StockPurchaseFragment s = new StockPurchaseFragment();
        fm.beginTransaction()
                .replace(R.id.fragment_container, s)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onListFragmentInteraction(Group item) {
        this.currentGroup = item;
        Log.e("Username", currentUser.getMyUsername());
        Log.e("List of members:", Arrays.toString(currentGroup.getMyMembers().keySet().toArray()));

        if(currentGroup.isUserMember(currentUser.getMyUsername())) {
            FragmentManager fm = getSupportFragmentManager();
            UsersWithinGroupListFragment s = new UsersWithinGroupListFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, s)
                    .addToBackStack(null)
                    .commit();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            JoinGroupFragment s = new JoinGroupFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, s)
                    .addToBackStack(null)
                    .commit();
        }
        Log.e("List of members:", currentGroup.getMyMembers().keySet().toString());
    }

    @Override
    public void onListFragmentInteraction(UserParticipationInGroup item) {

    }

    @Override
    public void portfolioInteraction(Portfolio item) {
        currentPortfolio = item;
        FragmentManager fm = getSupportFragmentManager();
        StockListFragment s = new StockListFragment();
        fm.beginTransaction()
                .replace(R.id.fragment_container, s)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void JoinGroupInteraction() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of courses, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                return;
            }
            result = Purchases.parsePurchaseJSON(result, purchaseList);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private class DownloadPortfoliosTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of courses, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                return;
            }
            result = Portfolio.parsePortfolioJSON(result, portfolioList);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                return;
            }
        }
    }
}
