package com.codepath.apps.complextweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by jnagaraj on 2/21/16.
 */

@Table(name = "Media")
public class Media extends Model{

    @Column(name = "media_url")
    private String media_url;

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "type")
    private String type;

    @Column(name = "video_info")
    private VideoInfo video_info;

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public void setVideo_info(VideoInfo video_info) {
        this.video_info = video_info;
    }

    public VideoInfo getVideo_info() {
        return video_info;
    }

    public String getMedia_url() {
        return media_url;
    }

    public String getType() {
        return type;
    }



}
