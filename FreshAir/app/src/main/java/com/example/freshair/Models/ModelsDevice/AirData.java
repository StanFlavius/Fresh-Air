package com.example.freshair.Models.ModelsDevice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AirData {
    @SerializedName("channel")
    @Expose
    private Channel channel;

    @SerializedName("feeds")
    @Expose
    private List<Feed> feeds;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }
}
