package com.codepath.apps.complextweets.fragments;

import android.view.View;

import com.codepath.apps.complextweets.models.Tweet;

import java.util.ArrayList;

/**
 * Created by jnagaraj on 2/26/16.
 */
public class UserTimelineFragment extends TweetsListFragment {

    public void populateTimeline() {

    }

    @Override
    public void onComposeTweet(View view) {

    }

    public void populateTimelineOnRefresh() {}


    public ArrayList<Tweet> GetCachedTweets() {

        return new ArrayList<>();
    }
}
