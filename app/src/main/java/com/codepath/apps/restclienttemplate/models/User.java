package com.codepath.apps.restclienttemplate.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

// User model class to represent users in twitter
@Parcel
@Entity
public class User {

    @ColumnInfo
    @PrimaryKey
    public long id;

    @ColumnInfo
    public String id_str;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String screenName;

    @ColumnInfo
    public String profileImageUrl;

    @ColumnInfo
    public String description;


    public User() {

    }

    // Constructor to se up new User with all information provided
    public User(String description, String name, String screenName, String profileImageUrl, String id) {
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
        this.id_str = id;
        this.description = description;
    }

    // Retrieves necessary user information from JSONObject
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.id_str = jsonObject.getString("id_str");
        user.id = jsonObject.getLong("id");
        return user;

    }

    // Takes an array of tweets and returns a result of users
    public static List<User> fromJsonTweetArray(List<Tweet> tweetsFromNetwork) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < tweetsFromNetwork.size(); i++) {
            users.add(tweetsFromNetwork.get(i).user);
        }
        return users;
    }
}
