package com.codepath.apps.complextweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.fragments.HomeTimelineFragment;
import com.codepath.apps.complextweets.fragments.MentionsTimelineFragment;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new TweetPageAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);
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

        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);

    }

    public class TweetPageAdapter extends FragmentPagerAdapter {

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
