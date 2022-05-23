package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.other.TwitterApp;
import com.codepath.apps.restclienttemplate.other.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

// Class to display tweets in a larger detail view
public class TweetDetailActivity extends AppCompatActivity {

    ImageView ivProfile;
    TextView tvScreenName;
    TextView tvBody;
    TextView tvTime;
    ImageView ivEntity;
    ImageButton btnRetweet;
    TextView tvNumLikes;
    TextView tvNumRetweet;
    ImageButton btnLike;
    ImageButton btnReply;
    Tweet tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        // bind to view
        ivProfile = findViewById(R.id.ivProfile);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvBody = findViewById(R.id.tvBody);
        tvTime = findViewById(R.id.tvTime);
        btnRetweet = findViewById(R.id.btnRetweetMain);
        btnReply = findViewById(R.id.btnReply);
        ivEntity = findViewById(R.id.ivEntity);
        btnLike = findViewById(R.id.btnHeart);
        tvNumLikes = findViewById(R.id.tvNumLiked);
        tvNumRetweet = findViewById(R.id.tvNumRetweet);

        // Sets tint for necessary buttons
        btnLike.setColorFilter(Color.rgb(29,161,242), android.graphics.PorterDuff.Mode.SRC_IN);
        btnRetweet.setColorFilter(Color.rgb(29,161,242), android.graphics.PorterDuff.Mode.SRC_IN);
        btnReply.setColorFilter(Color.rgb(29,161,242), android.graphics.PorterDuff.Mode.SRC_IN);

        // On Click listener for like button - same structure as in TweetsAdapter
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterClient client = TwitterApp.getRestClient(TweetDetailActivity.this);
                if (!tweet.liked) {
                    btnLike.setImageResource(R.drawable.ic_vector_heart);
                    tweet.liked = !tweet.liked;
                    tweet.numLikes += 1;
                    tvNumLikes.setText((tweet.numLikes) + "");
                    client.favoriteTweet(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d("TweetDetailActivity", "succesfully liked");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d("TweetDetailActivity", "failed to liked");
                        }
                    });
                } else {
                    btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                    tweet.liked = !tweet.liked;
                    tweet.numLikes -= 1;
                    tvNumLikes.setText((tweet.numLikes) + "");
                    client.unlikeTweet(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d("TweetDetailActivity", "succesfully liked");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d("TweetDetailActivity", "failed to liked");
                        }
                    });
                }
            }
        });

        // Sets ActionBar to correct color and title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1DA1F2")));
        actionBar.setTitle("Twitter");

        // On Click listener for retweet button - same structure as in TweetsAdapter
        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterClient client = TwitterApp.getRestClient(TweetDetailActivity.this);
                if (!tweet.retweeted) {
                    btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
                    tweet.retweeted = !tweet.retweeted;
                    tweet.numRetweets += 1;
                    tvNumRetweet.setText((tweet.numRetweets) + "");
                    client.reTweet(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d("TweetDetailActivity", "succesfully retweeted");
                        }
                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d("TweetDetailActivity", "failed to retweet");
                        }
                    });
                } else {
                    tweet.numRetweets -= 1;
                    tvNumRetweet.setText((tweet.numRetweets) + "");
                    btnRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                    tweet.retweeted = !tweet.retweeted;
                    client.unreTweet(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d("TweetDetailActivity", "succesfully unretweeted");
                        }
                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d("TweetDetailActivity", "failed to unretweet");
                        }
                    });
                }
            }
        });


        // get information from intent and loads tweet - similar to TweetsAdapter
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screenName);
        tvTime.setText(tweet.time);
        if (tweet.mediaUrl != "") {
            Log.d("TweetsAdapter", "loading media");
            Glide.with(this).load(tweet.mediaUrl + ":thumb").into(ivEntity);
        } else {
            ivEntity.setVisibility(View.GONE);
        }
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfile);
        tvNumLikes.setText(tweet.numLikes + "");
        tvNumRetweet.setText(tweet.numRetweets + "");
        if (tweet.retweeted) {
            btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
        }
        if (tweet.liked) {
            btnLike.setImageResource(R.drawable.ic_vector_heart);
        }
    }

}