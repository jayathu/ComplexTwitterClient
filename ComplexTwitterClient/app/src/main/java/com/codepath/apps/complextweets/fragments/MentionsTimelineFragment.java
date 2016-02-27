package com.codepath.apps.complextweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.complextweets.models.AccountCredentials;
import com.codepath.apps.complextweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jnagaraj on 2/26/16.
 */
public class MentionsTimelineFragment extends TweetsListFragment {

    private static final boolean TEST_OFFLINE = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Send an API request to get the timeline json
    // Fill the RecyclerView by creating the tweet object from the json
    public void populateTimeline() {

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

    public void populateTimelineOnRefresh() {

    }

    public void getAccountCredentials() {

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

    public ArrayList<Tweet> GetCachedTweets(){
        return new ArrayList<>();
    }

}
