package com.codepath.apps.restclienttemplate.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.activities.ProfileDetailActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.ReplyTweetDialogFragment;
import com.codepath.apps.restclienttemplate.activities.TweetDetailActivity;
import com.codepath.apps.restclienttemplate.other.TwitterApp;
import com.codepath.apps.restclienttemplate.other.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

// Adapter for tweets on TimelineActivity
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    // List of tweets to display
    List<Tweet> tweets;

    // Constructor for TweetsAdapter
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate a layout
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with view holder
        holder.bind(tweet);
    }

    // Returns number of items in RecyclerView
    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }


    // Define a ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // All components of View for each Tweet
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTime;
        ImageView ivEntity;
        ImageButton btnReply;
        ImageButton btnHeart;
        ImageButton btnRetweetMain;
        TextView tvName;
        TextView tvNumLiked;
        TextView tvNumRetweet;


        // Bind each variable above to view component
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfile);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivEntity = itemView.findViewById(R.id.ivEntity);
            btnReply = itemView.findViewById(R.id.btnReply);
            tvName = itemView.findViewById(R.id.tvName);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            btnRetweetMain = itemView.findViewById(R.id.btnRetweetMain);
            tvNumLiked = itemView.findViewById(R.id.tvNumLiked);
            tvNumRetweet = itemView.findViewById(R.id.tvNumRetweet);
            itemView.setOnClickListener(this);
        }

        // binds each tweet to recycler view item
        public void bind(final Tweet tweet) {
            /* Determines image for retweet and like dependent on if tweet has
            / been liked or retweeted before*/
            if (tweet.retweeted) {
                btnRetweetMain.setImageResource(R.drawable.ic_vector_retweet);
            }
            if (tweet.liked) {
                btnHeart.setImageResource(R.drawable.ic_vector_heart);
            }

            // Sets TextView for number of likes and number of retweets
            tvNumLiked.setText(tweet.numLikes + "");
            tvNumRetweet.setText(tweet.numRetweets + "");

            // Sets TextViews for tweet body, user screen name, user name, tweet time
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvName.setText(tweet.user.name);
            tvTime.setText(tweet.time);

            // If media in the tweet exits, loads the media
            Log.d("TweetsAdapter", tweet.mediaUrl + "");
            if (tweet.mediaUrl != "") {
                ivEntity.setVisibility(View.VISIBLE);
                Log.d("TweetsAdapter", "loading media");
                Glide.with(context).
                        load(tweet.mediaUrl + ":thumb")
                        .into(ivEntity);
            } else {
                ivEntity.setVisibility(View.GONE);
            }

            // Sets the Tint of the buttons to be TwitterBlue
            btnReply.setColorFilter(Color.rgb(29,161,242), android.graphics.PorterDuff.Mode.SRC_IN);
            btnHeart.setColorFilter(Color.rgb(29,161,242), android.graphics.PorterDuff.Mode.SRC_IN);
            btnRetweetMain.setColorFilter(Color.rgb(29,161,242), android.graphics.PorterDuff.Mode.SRC_IN);

            // Loads the user's profile image
            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCornersTransformation(30, 0))
                    .into(ivProfileImage);

            // Listener for Reply Button, Launches ReplyTweetDialogFragment and passes tweet
            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Tweet", Parcels.wrap(tweet));
                    FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                    ReplyTweetDialogFragment replyTweetDialogFragment = ReplyTweetDialogFragment.newInstance("Some Title");
                    replyTweetDialogFragment.setArguments(bundle);
                    replyTweetDialogFragment.show(fm, "fragment_reply_tweet_dialog");
                }
            });

            // Listener for ProfileImageButton, launches ProfileDetailActivity and passes user
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // intent to lead to a profile detail view that shows followers and following
                    Intent intent = new Intent(itemView.getContext(), ProfileDetailActivity.class);
                    intent.putExtra("User", Parcels.wrap(tweets.get(getAdapterPosition()).user));
                    itemView.getContext().startActivity(intent);
                }
            });

            // Listener for heart button click, likes/unlikes tweet
            btnHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // new client to like/unlike tweet
                    TwitterClient client = TwitterApp.getRestClient(itemView.getContext());

                    /* If the tweet is not already liked, like it and update necessary variables
                    else the tweet has been liked and we should unlike it and update necessary
                    variables
                     */
                    if (!tweet.liked) {
                        btnHeart.setImageResource(R.drawable.ic_vector_heart);
                        tweet.liked = !tweet.liked;
                        tweet.numLikes += 1;
                        tvNumLiked.setText((tweet.numLikes) + "");
                        client.favoriteTweet(tweet, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.d("TweetDetailActivity", "Successfully liked");
                            }
                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.d("TweetDetailActivity", "Failed to like");
                            }
                        });
                    } else {
                        btnHeart.setImageResource(R.drawable.ic_vector_heart_stroke);
                        tweet.liked = !tweet.liked;
                        tweet.numLikes -= 1;
                        tvNumLiked.setText((tweet.numLikes) + "");
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


            // Listener for retweet button click, retweets/unretweets tweet
            btnRetweetMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // gets access to twitter client to performing retweeting actions
                    TwitterClient client = TwitterApp.getRestClient(itemView.getContext());

                    /* If the tweet is not already retweeted, retweet it and update necessary variables
                    else the tweet has been retweeted and we should unretweet it and update necessary
                    variables
                     */
                    if (!tweet.retweeted) {
                        btnRetweetMain.setImageResource(R.drawable.ic_vector_retweet);
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
                        btnRetweetMain.setImageResource(R.drawable.ic_vector_retweet_stroke);
                        tweet.retweeted = !tweet.retweeted;
                        tweet.numRetweets -= 1;
                        tvNumRetweet.setText((tweet.numRetweets) + "");
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
        }

        // Click listener for whole RecyclerView item to take to DetailView and pass tweet
        @Override
        public void onClick(View view) {
            Log.d("TweetsAdapter", "in here");
            Tweet tweet = tweets.get(getAdapterPosition());
            Intent intent = new Intent(view.getContext(), TweetDetailActivity.class);
            intent.putExtra("tweet", Parcels.wrap(tweet));
            // animation for shared profile image
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) view.getContext(), (View) ivProfileImage, "profile");
            view.getContext().startActivity(intent, options.toBundle());
        }
    }
}
