package com.codepath.apps.complextweets.models;

import org.parceler.Parcel;

/**
 * Created by jnagaraj on 2/21/16.
 */

@Parcel
public class TweetParcel {

    public String Name;
    public String Text;
    public String screenName;
    public String profileImageUrl;
    public String tagLine;
    public String imageThumbnail = null;
    public String videoThumnail = null;
    public int followers;
    public int following;
    public TweetParcel() {

    }
}
