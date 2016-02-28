package com.codepath.apps.complextweets.models;

import android.content.Context;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jnagaraj on 2/27/16.
 */
public class TweetsPreferences {

    public static final String ACCCOUNT_CREDENTIALS = "ACCOUNT_CREDENTIALS";

    public static AccountCredentials getUser(Context context) {
        String jsonString = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(ACCCOUNT_CREDENTIALS, null);

        if (jsonString == null) {
            return null;
        } else {
            try {
                return AccountCredentials.fromJSON(new JSONObject(jsonString));
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void setUser(Context context, JSONObject jsonObject) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(ACCCOUNT_CREDENTIALS, jsonObject.toString())
                .apply();
    }
}
