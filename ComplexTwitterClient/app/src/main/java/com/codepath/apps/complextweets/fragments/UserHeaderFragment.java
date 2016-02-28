package com.codepath.apps.complextweets.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.activities.ProfileActivity;
import com.codepath.apps.complextweets.models.TweetParcel;

import org.parceler.Parcels;

/**
 * Created by jnagaraj on 2/27/16.
 */
public class UserHeaderFragment extends Fragment {


    //Creates a new fragment, gives us an int and title
    public static UserHeaderFragment newInstance(String key, TweetParcel value) {
        UserHeaderFragment userFragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        Parcelable p = Parcels.wrap(value);
        args.putParcelable(ProfileActivity.USER_PROFILE_KEY, p);
        userFragment.setArguments(args);
        return userFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TweetParcel parcel = Parcels.unwrap(getArguments().getParcelable(ProfileActivity.USER_PROFILE_KEY));

        Toast.makeText(getContext(), parcel.screenName, Toast.LENGTH_SHORT).show();

        populateProfileHeader(getView(), parcel);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

      private void populateProfileHeader(View view, TweetParcel userHeader) {

        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvTagLine = (TextView) view.findViewById(R.id.tvTagline);
        TextView tvFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        TextView tvFollower = (TextView) view.findViewById(R.id.tvFollowers);
        TextView tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
        ImageView ivProfileImage = (ImageView)view.findViewById(R.id.ivProfilePic);

        tvName.setText(userHeader.Name);
        tvScreenName.setText("@" + userHeader.screenName);
        tvTagLine.setText(userHeader.tagLine);
        tvFollower.setText(userHeader.followers + " Followers");
        tvFollowing.setText(userHeader.following + " Following");
        Glide.with(this).load(userHeader.profileImageUrl).into(ivProfileImage);

    }
}
