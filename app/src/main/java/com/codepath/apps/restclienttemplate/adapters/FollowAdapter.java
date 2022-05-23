package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

// Adapter for the Followers/Following Page to display Users
public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    // List of users (changes based on following or follower tab)
    public List<User> users;
    Context context;

    // Constructor to set users list and context
    public FollowAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }


    // Override method to create view holder and set layout to item_follow.xml
    @NonNull
    @NotNull
    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_follow, parent, false);
        return new ViewHolder(view);
    }

    // Binds the user as specified position to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull @NotNull FollowAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    // Number of users
    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Handle of user
        TextView userName;
        // User's actual name
        TextView name;
        // Description of user
        TextView description;
        // Profile Image of User
        ImageView ivProfileImage;

        // Bind view components to variables
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tvUser);
            name = itemView.findViewById(R.id.tvName);
            description = itemView.findViewById(R.id.tvDescription);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
        }

        // Set view components
        public void bind(User user) {
            userName.setText(user.screenName);
            name.setText(user.name);
            description.setText(user.description);
            Glide.with(context)
                    .load(user.profileImageUrl)
                    .transform(new RoundedCornersTransformation(30, 0))
                    .into(ivProfileImage);
        }
    }
}
