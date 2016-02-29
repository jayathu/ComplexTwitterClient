package com.codepath.apps.complextweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.adapters.SmartFragmentStatePagerAdapter;
import com.codepath.apps.complextweets.fragments.UserFriendsFragment;
import com.codepath.apps.complextweets.fragments.UserTimelineFragment;
import com.codepath.apps.complextweets.fragments.UserFollowersFragment;
import com.codepath.apps.complextweets.models.TweetParcel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    public static final String USER_PROFILE_KEY = "com.complextweets.userprofilekey";
    public static final String USER_TIMELINE_KEY = "com.complextweets.usertimelinekey";

    private SmartFragmentStatePagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the screen name from the activity that launched this
        String screenName = getIntent().getStringExtra(ProfileActivity.USER_TIMELINE_KEY);
        Intent intent = getIntent();
        TweetParcel parcel = Parcels.unwrap(intent.getParcelableExtra(ProfileActivity.USER_PROFILE_KEY));
        populateProfileHeader(parcel);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentManager fm = getSupportFragmentManager();

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(fm);
        pagerAdapter.addFragment(UserTimelineFragment.newInstance(screenName), "Tweets")
                .addFragment(UserFollowersFragment.newInstance(screenName), "Followers")
                .addFragment(UserFriendsFragment.newInstance(screenName), "Following");

        viewPager.setAdapter(pagerAdapter);

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);
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

    // Extend from SmartFragmentStatePagerAdapter now instead for more dynamic ViewPager items
    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {

        List<Fragment> fragments;
        List<String> titles;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fragments = new ArrayList<>();
            titles = new ArrayList<>();
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return fragments.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        public MyPagerAdapter addFragment(Fragment fragment, String title) {
            this.fragments.add(fragment);
            this.titles.add(title);
            return this;
        }

    }
}
