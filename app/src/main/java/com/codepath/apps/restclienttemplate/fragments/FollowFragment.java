package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.FollowAdapter;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.other.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import okhttp3.Headers;

// Fragment to display followers and following
public class FollowFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public int page;
    private User user;
    public ArrayList<User> users = new ArrayList<>();
    RecyclerView rvFollow;
    FollowAdapter followAdapter;

    public FollowFragment() {
        // Required empty public constructor
    }

    public FollowFragment(int page) {
        // Required empty public constructor
        this.page = page;
    }

    // returns a new instance of the follower class
    public static FollowFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FollowFragment fragment = new FollowFragment(page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("User"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        setUpFollow(view);
        return view;
    }

    // gets followers or following based on tav
    public void setUpFollow(View view) {
        if (this.page == 1) {
            populateFollowers();
        } else {
            populateFollowing();
        }
        rvFollow = view.findViewById(R.id.rvFollow);

        // Create the adapter
        followAdapter = new FollowAdapter(getActivity(), users);
        rvFollow.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        // Set the adapter on the recycler view
        rvFollow.setAdapter(followAdapter);
        // Set a Layout Manager the recycler view
        rvFollow.setLayoutManager(new LinearLayoutManager(getActivity()));
        // retrieves and displays related movies
    }

    // populates followers by making a request to TwitterAPI
    public void populateFollowers() {
        TwitterClient client = new TwitterClient(this.getContext());
        client.getFollowers(user.id_str, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    // get user data and figure out how to parse it
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for (int i = 0; i<20; i++) {
                        JSONObject user = jsonArray.getJSONObject(i);
                        users.add(new User(user.getString("description"), user.getString("name"), user.getString("screen_name"), user.getString("profile_image_url_https"), user.getString("id_str")));
                    }
                    followAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
            }
        });
    }

    // populates following by making a request to TwitterAPI
    public void populateFollowing() {
        TwitterClient client = new TwitterClient(this.getContext());
        client.getFollowing(user.id_str, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    // get user data and figure out how to parse it
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for (int i = 0; i<jsonArray.length(); i++) {
                        JSONObject user = jsonArray.getJSONObject(i);
                        users.add(new User(user.getString("description"), user.getString("name"), user.getString("screen_name"), user.getString("profile_image_url_https"), user.getString("id_str")));
                    }
                    followAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
            }
        });
    }


}