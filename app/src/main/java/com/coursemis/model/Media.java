package com.coursemis.model;

import java.io.Serializable;


/**
 * Created by 74000 on 2018/4/1.
 */

public class Media implements Serializable {
    private String path;
    private String mediaName;


    public String getMediaName() {
        return mediaName;
    }
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
