package com.codepath.apps.complextweets.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.TwitterApplication;
import com.codepath.apps.complextweets.TwitterClient;
import com.codepath.apps.complextweets.adapters.TweetRecyclerAdapter;
import com.codepath.apps.complextweets.models.AccountCredentials;
import com.codepath.apps.complextweets.models.Tweet;
import com.codepath.apps.complextweets.models.TweetsPreferences;
import com.codepath.apps.complextweets.utilities.DividerItemDecoration;
import com.codepath.apps.complextweets.utilities.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnagaraj on 2/24/16.
 */
public abstract class TweetsListFragment extends Fragment implements ComposeTweetFragment.ComposeTweetDialogActionListener{

    private ArrayList<Tweet> aTweets;
    private TweetRecyclerAdapter tweetRecyclerAdapter;
    private RecyclerView rvResults;
    public SwipeRefreshLayout swipeContainer;
    public FloatingActionButton fab;
    public AccountCredentials credentials;
    public String account_id;
    protected TwitterClient client;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ACCOUNT_ID", account_id);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        fab = (FloatingActionButton)view.findViewById(R.id.fab);

        rvResults = (RecyclerView)view.findViewById(R.id.rvTweets);
        rvResults.setAdapter(tweetRecyclerAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        rvResults.addItemDecoration(itemDecoration);


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        rvResults.setLayoutManager(linearLayoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComposeTweet(v);
            }
        });


        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                Log.d("SWIPTE TO REFRESH", aTweets.size() + "");
                onEndlessScroll();


            }

        });

        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeUpToRefresh();

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        /*ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Log.d("PARCEL", "tapped on " + aTweets.get(position).text);
                        Tweet tweet = aTweets.get(position);
                        Intent intent = new Intent(getActivity().getApplicationContext(), TweetDetails.class);
                        TweetParcel parcel = new TweetParcel();

                        parcel.Name = tweet.user.name;
                        parcel.screenName = tweet.user.getScreenName();
                        parcel.tagLine = tweet.user.getTagLine();
                        parcel.Text = tweet.getBody();
                        parcel.profileImageUrl = tweet.getUser().getProfileImageUrl();
                        if (tweet.mediaTypePhoto()) {

                            parcel.imageThumbnail = tweet.getTweetImageUrl();

                        } else if (tweet.mediaTypeVideo()) {

                            parcel.videoThumnail = tweet.getTweetVideoUrl();
                        }


                        intent.putExtra("TWEET_DETAILS", Parcels.wrap(parcel));
                        startActivity(intent);
                    }
                }
        );*/




        return view;
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

        aTweets = new ArrayList<>();
        tweetRecyclerAdapter = new TweetRecyclerAdapter(aTweets);

    }

    public void clearListAndAddNew(List<Tweet> tweets) {

        aTweets.clear();
        tweetRecyclerAdapter.notifyDataSetChanged();
        aTweets.addAll(tweets);
        tweetRecyclerAdapter.notifyDataSetChanged();
    }

    public void addMoreToList(List<Tweet> tweets) {

        aTweets.addAll(tweets);
        int curSize = tweetRecyclerAdapter.getItemCount();
        tweetRecyclerAdapter.notifyItemRangeInserted(curSize, aTweets.size() - 1);
    }

    public long getLastTweetId() {

        return aTweets.get(aTweets.size() - 1).getUid();
    }

    public Boolean isNetworkAvailable() {
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
                    TweetsPreferences.setUser(getContext(),response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }
    }

    public void onSwipeUpToRefresh() {
        populateTimeline();
    }

    public void onEndlessScroll() {
        populateTimelineOnRefresh();
    }

    public abstract void populateTimelineOnRefresh();

    public abstract void populateTimeline();

    //implementation of abstract method
    public void onComposeTweet(View view)
    {
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


    public void StoreTweetsToLocalDatabase() {

        /*Log.d("SAVE ", "StoreTweetsToLocalDatabase");
        for(Tweet t : aTweets){

            User user = User.findOrCreate(t.getUser());
            ExtendedEntities entities = ExtendedEntities.createIfExists(t.getExtended_entities());

            Tweet tweet = new Tweet();
            tweet.id = t.getUid();
            tweet.text = t.getBody();
            tweet.relativeTimeAgo = t.getRelativeTimeAgo();
            tweet.created_at = t.created_at;

            if(entities != null) {
                tweet.setExtended_entities(entities);
            }

            tweet.user = user;

            tweet.save();
        }*/
    }
}
