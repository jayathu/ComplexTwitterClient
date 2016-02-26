package com.codepath.apps.complextweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.activities.TweetDetails;
import com.codepath.apps.complextweets.adapters.TweetRecyclerAdapter;
import com.codepath.apps.complextweets.models.Tweet;
import com.codepath.apps.complextweets.models.TweetParcel;
import com.codepath.apps.complextweets.utilities.DividerItemDecoration;
import com.codepath.apps.complextweets.utilities.EndlessRecyclerViewScrollListener;
import com.codepath.apps.complextweets.utilities.ItemClickSupport;

import org.parceler.Parcels;

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


        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Log.d("PARCEL", "tapped on " + aTweets.get(position).text);
                        Tweet tweet = aTweets.get(position);
                        Intent intent = new Intent(getActivity().getApplicationContext(), TweetDetails.class);
                        TweetParcel parcel = new TweetParcel();

                        parcel.Name = tweet.user.name;
                        parcel.screenName = tweet.user.getScreenName();
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
        );




        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    public abstract void onComposeTweet(View view);

    public abstract void onSwipeUpToRefresh();

    public abstract void onEndlessScroll();

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
