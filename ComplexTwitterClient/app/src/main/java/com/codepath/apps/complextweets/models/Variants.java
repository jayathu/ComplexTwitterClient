package com.codepath.apps.complextweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by jnagaraj on 2/21/16.
 */

@Table(name = "Variants")
public class Variants extends Model {

    @Column(name = "url")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    @Column(name = "content_type")
    private String content_type;

    public String getContent_type() {
        return content_type;
    }

    public String getUrl() {
        return url;
    }
}
