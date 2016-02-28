package com.codepath.apps.complextweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.activities.ProfileActivity;
import com.codepath.apps.complextweets.activities.TweetDetails;
import com.codepath.apps.complextweets.models.Tweet;
import com.codepath.apps.complextweets.models.TweetParcel;
import com.codepath.apps.complextweets.models.User;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by jnagaraj on 2/17/16.
 */
public class TweetRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    private final int WITH_IMAGE = 0, WITHOUT_MEDIA = 1, WITH_VIDEO = 3;

    private List<Tweet> mTweets;

    public TweetRecyclerAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TweetItemHolder viewHolder;
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.d("TYPE ", viewType + "");
        switch(viewType) {
            case WITHOUT_MEDIA:
                View v1 = inflater.inflate(R.layout.item_tweet_result, parent, false);
                Log.d("INFLATER ", "item_tweet_result");
                viewHolder = new TweetItemHolder(v1);
                break;
            case WITH_IMAGE:
                View v2 = inflater.inflate(R.layout.item_tweet_with_image, parent, false);
                viewHolder = new TweetWithMediaHolder(v2);
                break;
            case WITH_VIDEO:
                View v3 = inflater.inflate(R.layout.item_tweet_with_video, parent, false);
                viewHolder = new TweetWithVideoHolder(v3);
                break;
            default:
                View v = inflater.inflate(R.layout.item_tweet_result, parent, false);
                viewHolder = new TweetItemHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch(holder.getItemViewType()){
            case WITHOUT_MEDIA:
                TweetItemHolder vh1 = (TweetItemHolder) holder;
                vh1.loadDataIntoViews(mTweets.get(position), context);
                break;
            case WITH_IMAGE:
                TweetWithMediaHolder vh2 = (TweetWithMediaHolder) holder;
                vh2.loadDataIntoViews(mTweets.get(position), context);
                break;
            case WITH_VIDEO:
                TweetWithVideoHolder vh3 = (TweetWithVideoHolder) holder;
                vh3.loadDataIntoViews(mTweets.get(position), context);
                break;

            default:
                TweetItemHolder vh = (TweetItemHolder) holder;
                vh.loadDataIntoViews(mTweets.get(position), context);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    //this method is required to tell the RecyclerView about the type of view to inflate based on the position
    @Override
    public int getItemViewType(int position) {

        if(mTweets.get(position).mediaTypePhoto()) {
            return WITH_IMAGE;
        }else if(mTweets.get(position).mediaTypeVideo()) {
            return WITH_VIDEO;
        }else {
            return WITHOUT_MEDIA;
        }

    }

    public class TweetItemHolder extends RecyclerView.ViewHolder {

        private TextView tvBody;
        private TextView tvUsername;
        private TextView tvDatePosted;
        private ImageView ivProfilePic;

        public  TweetItemHolder(View itemView) {
            super(itemView);

            tvBody = (TextView)itemView.findViewById(R.id.tvBody);
            tvDatePosted = (TextView)itemView.findViewById(R.id.tvDatePosted);
            tvUsername = (TextView)itemView.findViewById(R.id.tvUsername);
            ivProfilePic = (ImageView)itemView.findViewById(R.id.ivProfilePic);
        }

        public void loadDataIntoViews(final Tweet tweet, final Context context) {

            Glide.with(context).load(tweet.getUser().getProfileImageUrl()).into(ivProfilePic);
            tvBody.setText(tweet.getBody());
            tvUsername.setText(tweet.getUser().getScreenName());
            tvDatePosted.setText(tweet.getRelativeTimeAgo());

            ivProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TweetParcel parcel = new TweetParcel();
                    User user = tweet.getUser();
                    parcel.Name = user.getName();
                    parcel.screenName = user.getScreenName();
                    parcel.tagLine = user.getTagLine();
                    parcel.profileImageUrl = user.getProfileImageUrl();
                    parcel.followers = user.getFollowers_count();
                    parcel.following = user.getFollowing();

                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_PROFILE_KEY, Parcels.wrap(parcel));
                    intent.putExtra(ProfileActivity.USER_TIMELINE_KEY, user.screen_name);
                    context.startActivity(intent);
                }
            });

            tvBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, TweetDetails.class);
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
                    context.startActivity(intent);
                }
            });
        }

    }

    public class TweetWithMediaHolder extends TweetItemHolder {

        private ImageView ivMediaImage;

        public TweetWithMediaHolder(View itemView) {
            super(itemView);
            ivMediaImage = (ImageView)itemView.findViewById(R.id.ivMediaImage);
        }

        @Override
        public void loadDataIntoViews(Tweet tweet, Context context) {
            super.loadDataIntoViews(tweet, context);
            Glide.with(context).load(tweet.getTweetImageUrl()).into(ivMediaImage);

        }
    }

    public class TweetWithVideoHolder extends TweetItemHolder {

        private VideoView ivMediaVideo;

        public TweetWithVideoHolder(View itemView) {
            super(itemView);
            ivMediaVideo = (VideoView)itemView.findViewById(R.id.ivMediaVideo);
        }

        @Override
        public void loadDataIntoViews(Tweet tweet, Context context) {
            super.loadDataIntoViews(tweet, context);

            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(ivMediaVideo);
            ivMediaVideo.setVideoPath(tweet.getTweetVideoUrl());
            ivMediaVideo.setMediaController(mediaController);
            ivMediaVideo.requestFocus();

            ivMediaVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    ivMediaVideo.start();
                }
            });

            ivMediaVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivMediaVideo.start();
                }
            });

        }
    }
}
