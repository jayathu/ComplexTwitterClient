package com.codepath.apps.complextweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jnagaraj on 2/17/16.
 */


/*
"user": {
      "name": "OAuth Dancer",
      "profile_sidebar_fill_color": "DDEEF6",
      "profile_background_tile": true,
      "profile_sidebar_border_color": "C0DEED",
      "profile_image_url":
 */
//ActiveAndroid Model: USER

@Table(name = "Users")
public class User extends Model {

    @Column(name = "name")
    public String name;

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long id;

    @Column(name = "screen_name")
    public String screen_name;

    @Column(name = "profile_image_url")
    public String profile_image_url;

    @Column(name = "description")
    private String description;

    @Column(name = "followers_count")
    private int followers_count;

    @Column(name = "friends_count")
    private int friends_count;

    public String getTagLine() {
        return description;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFollowing() {
        return friends_count;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return id;
    }

    public String getScreenName() {
        return screen_name;
    }

    public String getProfileImageUrl() {
        return profile_image_url;
    }


    /*public static User fromJSON(JSONObject jsonObject) {

        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("uid");
            user.screenName = jsonObject.getString("screen_name");
            user.profile_image_url = jsonObject.getString("profile_image_url");

        }catch (JSONException e){
            e.printStackTrace();
        }


        return user;
    }*/

    public static ArrayList<Tweet> fromGSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);

                Tweet tweet = Tweet.fromGSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    public static ArrayList getFollowers(JSONArray jsonArray) {

        ArrayList<User> followers = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject userJson = jsonArray.getJSONObject(i);

                User user = User.fromGSON(userJson);
                if (user != null) {
                    followers.add(user);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return followers;

    }

    public static User fromGSON(JSONObject jsonObject) {
        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(jsonObject.toString(), User.class);
        user = User.findOrCreate(user);
        return user;
    }

    public static User findOrCreate(User user) {
        User existingUser = new Select().from(User.class).where("uid = ?", user.getUid()).executeSingle();
        if(existingUser == null) {
            existingUser = user;
            existingUser.save();
        }
        return existingUser;

    }
}
