package com.codepath.apps.complextweets.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.apps.complextweets.activities.ProfileActivity;
import com.codepath.apps.complextweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jnagaraj on 2/26/16.
 */
public class UserTimelineFragment extends TweetsListFragment {

    //Creates a new fragment, gives us an int and title
    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(ProfileActivity.USER_TIMELINE_KEY, screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    public void populateTimeline() {

        String screenName = getArguments().getString(ProfileActivity.USER_TIMELINE_KEY);
        if (!isOnline()) {
            clearListAndAddNew(GetCachedTweets());
        } else {
            client.getUserTimeline(screenName, new JsonHttpResponseHandler() {

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
            });
        }

    }

    @Override
    public void onComposeTweet(View view) {

    }

    public void populateTimelineOnRefresh() {}


    public ArrayList<Tweet> GetCachedTweets() {

        return new ArrayList<>();
    }
}
