package com.codepath.apps.complextweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;
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
 * Created by jnagaraj on 2/25/16.
 */
public class HomeTimelineFragment
        extends TweetsListFragment
        implements ComposeTweetFragment.ComposeTweetDialogActionListener, FABActionListener {

    private static final boolean TEST_OFFLINE = false;

    final long INIT_ID = 1;
    private long lastTweetId = INIT_ID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Send an API request to get the timeline json
    // Fill the RecyclerView by creating the tweet object from the json
    @Override
    public void populateTimeline() {

        if (TEST_OFFLINE) {
            LoadTweetsOffline();
            clearListAndAddNew(GetCachedTweets());
            return;
        }

        if (!isOnline()) {
            clearListAndAddNew(GetCachedTweets());
        } else {
            client.getLatestTweets(new JsonHttpResponseHandler() {

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


    @Override
    public void populateTimelineOnRefresh() {
        if(!isOnline()){
            Toast.makeText(this.getActivity(), "Please connect to Internet.", Toast.LENGTH_SHORT).show();
        }else {
            client.getOlderTweets(new JsonHttpResponseHandler() {

                // SUCCESS
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    addMoreToList(Tweet.fromGSONArray(response));
                    lastTweetId = getLastTweetId();
                    Log.d("Refresh Id", lastTweetId + "");
                }

                // FAILURE
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            }, lastTweetId);
        }
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

    @Override
    public void OnFABClicked(View view) {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance("Compose", credentials.getProfile_image_url());
        composeTweetFragment.setTargetFragment(this, 300);
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
}
