package com.codepath.apps.complextweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.TwitterClient;
import com.codepath.apps.complextweets.fragments.UserTimelineFragment;
import com.codepath.apps.complextweets.models.AccountCredentials;
import com.codepath.apps.complextweets.models.TweetParcel;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    public static final String USER_PROFILE_KEY = "com.complextweets.userprofilekey";
    public static final String USER_TIMELINE_KEY = "com.complextweets.usertimelinekey";

    private TwitterClient client;
    AccountCredentials credentials;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the screen name from the activity that launched this
        String screenName = getIntent().getStringExtra(ProfileActivity.USER_TIMELINE_KEY);
        Intent intent = getIntent();
        TweetParcel parcel = Parcels.unwrap(intent.getParcelableExtra(ProfileActivity.USER_PROFILE_KEY));
        populateProfileHeader(parcel);

        if(savedInstanceState == null) {

            //UserHeaderFragment fragmentUserHeader = UserHeaderFragment.newInstance(ProfileActivity.USER_PROFILE_KEY, parcel);
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            //ft.replace(R.id.flUserProfileHeader, fragmentUserHeader);
            ft.replace(R.id.flUserTimeline, fragmentUserTimeline);

            ft.commit();
        }
    }

    private void populateProfileHeader(TweetParcel parcel) {

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        TextView tvFollower = (TextView) findViewById(R.id.tvFollowers);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfilePic);

        tvName.setText(parcel.Name);
        tvScreenName.setText("@" + parcel.screenName);
        tvTagLine.setText(parcel.tagLine);
        tvFollower.setText(parcel.followers + " Followers");
        tvFollowing.setText(parcel.following + " Following");
        Glide.with(this).load(parcel.profileImageUrl).into(ivProfileImage);

    }
}
