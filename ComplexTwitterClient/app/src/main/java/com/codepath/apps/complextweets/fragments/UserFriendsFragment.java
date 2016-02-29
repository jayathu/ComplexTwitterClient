package com.codepath.apps.complextweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.TwitterApplication;
import com.codepath.apps.complextweets.TwitterClient;
import com.codepath.apps.complextweets.activities.ProfileActivity;
import com.codepath.apps.complextweets.adapters.UserListAdapter;
import com.codepath.apps.complextweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jnagaraj on 2/28/16.
 */
public class UserFriendsFragment extends Fragment{

    private ArrayList<User> aUsers;
    private UserListAdapter userListAdapter;
    private ListView listview;
    protected TwitterClient client;

    //Creates a new fragment, gives us an int and title
    public static UserFollowersFragment newInstance(String screen_name) {
        UserFollowersFragment userFragment = new UserFollowersFragment();
        Bundle args = new Bundle();
        Log.d("Screen Name ", screen_name);
        args.putString(ProfileActivity.USER_TIMELINE_KEY, screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);

        listview = (ListView) view.findViewById(R.id.lvUsers);
        listview.setAdapter(userListAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); //singleton client

        aUsers = new ArrayList<>();
        userListAdapter = new UserListAdapter(this.getContext(), aUsers);

        populateTimeline();

    }

    public void clearListAndAddNew(ArrayList<User> users) {

        aUsers.clear();
        userListAdapter.notifyDataSetChanged();
        aUsers.addAll(users);
        userListAdapter.notifyDataSetChanged();
    }

    public void populateTimeline() {
        String screenName = getArguments().getString(ProfileActivity.USER_TIMELINE_KEY);

        client.getFollowing(screenName, new JsonHttpResponseHandler() {

            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try{
                    JSONArray jsonArray = response.getJSONArray("users");
                    Log.d("USERS", response.toString());
                    clearListAndAddNew(User.getFollowers(jsonArray));
                }catch(JSONException e) {
                    e.printStackTrace();
                }

            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }



}
