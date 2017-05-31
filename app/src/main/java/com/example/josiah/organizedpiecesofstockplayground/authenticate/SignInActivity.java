package com.example.josiah.organizedpiecesofstockplayground.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.josiah.organizedpiecesofstockplayground.MainActivity;
import com.example.josiah.organizedpiecesofstockplayground.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignInActivity extends AppCompatActivity implements LoginFragment.LoginInteractionListener, RegisterFragment.UserAddListener{
    private SharedPreferences mSharedPreferences;
    private static final String LOGIN_URL = "http://cssgate.insttech.washington.edu/~josiah3/PHP_Code/PHP%20Code/login.php?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction().add(R.id.sign_in_container, new LoginFragment()).commit();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }


    @Override
    public void login(String userId, String pwd) {
        AddUserTask a = new AddUserTask();
        a.execute(buildLoginURL());
    }

    public void logInAfterVerification(String userId, String pwd) {
        Intent i = new Intent(this, MainActivity.class);
        mSharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true).commit();
        startActivity(i);
        finish();
    }


    private String buildLoginURL() {
        StringBuilder sb = new StringBuilder(LOGIN_URL);
        try {
            String user = ((EditText) findViewById(R.id.userid_edit)).getText().toString();
            sb.append("username=");
            sb.append(user);
            String password = ((EditText) findViewById(R.id.pwd_edit)).getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(password, "UTF-8"));
            Log.i("Login", sb.toString());
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }

    /**
     * Adds a user to the database
     * @param url: the url with which a user can be added into the database.
     */
    @Override
    public void addUser(String url) {
        AddUserTask task = new AddUserTask();
        task.execute(url.toString());
        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }


    /**
     * This class asyncronously adds users to the database.
     */
    private class AddUserTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
                    response = "Unable to add course, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * It checks
         * to see if
         * there was
         * a problem
         * with the
         * <p>
         * URL(Network) which is when an      *
         * exception is
         * caught.It tries
         * to call
         * the parse
         * Method and
         * checks to
         * see if
         * it was
         * successful.      *
         * If not, it
         * displays the
         * exception.      **
         *
         * @param result
         */

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                Log.v("onPostExecute", result);
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_LONG).show();
                    logInAfterVerification(((EditText) findViewById(R.id.userid_edit)).getText().toString(), ((EditText) findViewById(R.id.pwd_edit)).getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: " + jsonObject.get("error"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
