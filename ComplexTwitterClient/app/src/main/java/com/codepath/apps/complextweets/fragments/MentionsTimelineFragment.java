package com.codepath.apps.complextweets.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.activeandroid.query.Select;
import com.codepath.apps.complextweets.TwitterApplication;
import com.codepath.apps.complextweets.TwitterClient;
import com.codepath.apps.complextweets.models.AccountCredentials;
import com.codepath.apps.complextweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by jnagaraj on 2/26/16.
 */
public class MentionsTimelineFragment extends TweetsListFragment {

    private static final boolean TEST_OFFLINE = false;

    final long INIT_ID = 1;
    private long lastTweetId = INIT_ID;
    private TwitterClient client;
    private AccountCredentials credentials;

    private String account_id;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ACCOUNT_ID", account_id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account_id = "";
        if (savedInstanceState != null) {
            account_id = savedInstanceState.getString("ACCOUNT_ID");
        }

        client = TwitterApplication.getRestClient(); //singleton client
        getAccountCredentials();
        populateTimeline();
    }

    // Send an API request to get the timeline json
    // Fill the RecyclerView by creating the tweet object from the json
    private void populateTimeline() {

        if(TEST_OFFLINE){
            LoadTweetsOffline();
            clearListAndAddNew(GetCachedTweets());
            return;
        }

        if(!isOnline()) {
            clearListAndAddNew(GetCachedTweets());
        }
        else
        {
            client.getMentionTweets(new JsonHttpResponseHandler() {

                // SUCCESS
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    clearListAndAddNew(Tweet.fromGSONArray(response));
                    long lastTweetId = getLastTweetId();
                    Log.d("lastTweedId", lastTweetId + "");
                    swipeContainer.setRefreshing(false);
                    StoreTweetsToLocalDatabase();
                }

                // FAILURE
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            }, 1);
        }
    }

    public void onSwipeUpToRefresh() {

    }

    public void onEndlessScroll() {

    }

    private void getAccountCredentials() {

        if(!isOnline()) {
            Log.d("CREDENTIALS", account_id);
            credentials = AccountCredentials.findCredentials(account_id);
        }else {
            client.getAccountCredientials(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    credentials = AccountCredentials.fromJSON(response);
                    account_id = credentials.getAccountId();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }
    }


    //implementation of abstract method
    public void onComposeTweet(View view)
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance("Compose", credentials.getProfile_image_url());
        composeTweetFragment.show(fm, "dialog_compose_tweet");

    }

    public void onComposeTweet(final String tweet) {

        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAILED", errorResponse.toString());

            }
        }, tweet);

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }



    private List<Tweet> GetCachedTweets() {

        return new Select().from(Tweet.class).limit(100).execute();
    }

    private void LoadTweetsOffline(){
        String jsonObjectString = loadJSONFromAsset();
        try {
            JSONArray jsonArray = new JSONArray(jsonObjectString);
            clearListAndAddNew(Tweet.fromGSONArray(jsonArray));

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private  String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getActivity().getAssets().open("json/home_timeline.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}
