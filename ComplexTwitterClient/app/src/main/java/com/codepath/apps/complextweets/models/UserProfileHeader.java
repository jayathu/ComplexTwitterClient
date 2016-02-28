package com.codepath.apps.complextweets.models;

import org.parceler.Parcel;

/**
 * Created by jnagaraj on 2/27/16.
 */

@Parcel
public class UserProfileHeader {

    public String screenName;
    public String fullName;
    public String profileImageUrl;
    public String tagLine;
    public int followingCount;
    public int followerCount;


    public UserProfileHeader() {

    }
}
