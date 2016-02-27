package com.codepath.apps.complextweets.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.TwitterApplication;
import com.codepath.apps.complextweets.TwitterClient;
import com.codepath.apps.complextweets.fragments.UserTimelineFragment;
import com.codepath.apps.complextweets.models.AccountCredentials;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private TwitterClient client;
    AccountCredentials credentials;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();

        client.getAccountCredientials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                credentials = AccountCredentials.fromJSON(response);
                getSupportActionBar().setTitle("@" + credentials.getScreen_name());

                populateProfileHeader(credentials);
            }
        });

        //get the screen name from the activity that launched this
        String screenName = getIntent().getStringExtra("screen_name");

        if(savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flUserTimeline, fragmentUserTimeline);

            ft.commit();
        }


    }

    private void populateProfileHeader(AccountCredentials credentials) {

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        TextView tvFollower = (TextView) findViewById(R.id.tvFollowers);
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfilePic);

        tvName.setText(credentials.getName());
        tvTagLine.setText(credentials.getTagline());
        tvFollower.setText(credentials.getFollowers_count() + " Followers");
        tvFollowing.setText(credentials.getFollowing() + " Following");
        Glide.with(this).load(credentials.getProfile_image_url()).into(ivProfileImage);

    }
}
