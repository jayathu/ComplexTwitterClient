package com.codepath.apps.complextweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.adapters.SmartFragmentStatePagerAdapter;
import com.codepath.apps.complextweets.fragments.FABActionListener;
import com.codepath.apps.complextweets.fragments.HomeTimelineFragment;
import com.codepath.apps.complextweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.complextweets.models.AccountCredentials;
import com.codepath.apps.complextweets.models.TweetParcel;
import com.codepath.apps.complextweets.models.TweetsPreferences;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity {

    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private FABActionListener fabActionListener;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_timeline);
        setContentView(R.layout.test_fab_layout);

        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabActionListener != null) {
                    fabActionListener.OnFABClicked(v);
                }
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        FragmentManager fm = getSupportFragmentManager();
        final TweetPageAdapter adapter = new TweetPageAdapter(fm);

        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(
                    int position, float positionOffset, int positionOffsetPixels) {
                // do nothing
            }

            @Override
            public void onPageSelected(int position) {
                fab.setVisibility(View.GONE);

                Fragment frag = adapter.getRegisteredFragment(position);
                if (frag instanceof HomeTimelineFragment) {
                    HomeTimelineFragment homeTimelineFragment = (HomeTimelineFragment) frag;
                    fab.setVisibility(View.VISIBLE);
                    fabActionListener = homeTimelineFragment;
                } else if (frag instanceof MentionsTimelineFragment) {
                   fab.setVisibility(View.INVISIBLE);
                } else {
                    throw new RuntimeException(
                            "Impossible fragment: " + frag + " at position " + position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // do nothing
            }
        };

        viewPager.addOnPageChangeListener(mOnPageChangeListener);

        viewPager.setAdapter(adapter);


        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.rsztwitterlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem item) {

        AccountCredentials credentials = TweetsPreferences.getUser(this);

        TweetParcel parcel = new TweetParcel();

        parcel.Name = credentials.getName();
        parcel.screenName = credentials.getScreen_name();
        parcel.tagLine = credentials.getTagline();
        parcel.profileImageUrl = credentials.getProfile_image_url();
        parcel.followers = credentials.getFollowers_count();
        parcel.following = credentials.getFollowing();

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER_PROFILE_KEY, Parcels.wrap(parcel));
        intent.putExtra(ProfileActivity.USER_TIMELINE_KEY, credentials.getScreen_name());
        startActivity(intent);
        //Toast.makeText(this, credentials.getScreen_name(), Toast.LENGTH_SHORT).show();

    }

    public class TweetPageAdapter extends SmartFragmentStatePagerAdapter {

        private String[] tabTitles = { "Home", "Mentions"};

        //Adapter gets the manager insert or remove from the activity
        public TweetPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                //Once these fragments are created, these will automatically cache for us!
                return new HomeTimelineFragment();
            }else if(position == 1){
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        //Returns page title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        //How many fragments to swipe between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

}
