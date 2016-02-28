package com.codepath.apps.complextweets.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jnagaraj on 2/19/16.
 */


/*

{
   "account_id":33143481,
   "id_str":"33143481",
   "name":"jayashree",
   "screen_name":"jayathu",
   "location":"",
   "description":"",
   "profile_image_url":"http:\/\/pbs.twimg.com\/profile_images\/501991114090364930\/bSQe0m2B_normal.jpeg"
}
 */

@Table(name = "AccountCredentials")
public class AccountCredentials extends Model {

    @Column(name = "account_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String account_id;

    @Column(name = "name")
    private String name;

    @Column(name = "screen_name")
    private String screen_name;

    @Column(name = "profile_image_url")
    private String profile_image_url;

    @Column(name = "followers_count")
    private int followers_count;

    @Column(name = "friends_count")
    private int following;

    @Column(name = "description")
    private String description;

    public String getAccountId() {
        return account_id;
    }

    public String getName() {
        return name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFollowing() {
        return following;
    }

    public String getTagline() {
        return description;
    }

    public AccountCredentials() {
        super();
    }

    public AccountCredentials(AccountCredentials credentials) {
        super();

        account_id = credentials.getAccountId();
        name = credentials.getName();
        screen_name = credentials.getScreen_name();
        profile_image_url = credentials.getProfile_image_url();
        description = credentials.getTagline();
        followers_count = credentials.getFollowers_count();
        following = credentials.getFollowing();
    }

    public static AccountCredentials fromJSON(JSONObject jsonObject) {

        AccountCredentials credentials = new AccountCredentials();
        try{
            Log.d("CREDENTIALS = ", jsonObject.toString());
            credentials.account_id = jsonObject.getString("id");
            credentials.name = jsonObject.getString("name");
            credentials.screen_name = jsonObject.getString("screen_name");
            credentials.profile_image_url = jsonObject.getString("profile_image_url");
            credentials.followers_count = jsonObject.getInt("followers_count");
            credentials.following = jsonObject.getInt("friends_count");
            credentials.description = jsonObject.getString("description");


        }catch (JSONException e) {
            e.printStackTrace();
        }

        return credentials;
    }

    public static AccountCredentials findCredentials(String id) {
        return new Select().from(AccountCredentials.class).where("account_id = ?", id).executeSingle();
    }
}
