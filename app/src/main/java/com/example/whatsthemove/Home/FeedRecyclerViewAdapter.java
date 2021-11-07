package com.example.whatsthemove.Home;


import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.whatsthemove.Constants;
import com.example.whatsthemove.R;
import com.example.whatsthemove.databinding.PostItemListBinding;
import com.example.whatsthemove.models.Post;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.ViewHolder> {


    PostItemListBinding binding;
    Context context;

    private final List<Post> itemList;

    public FeedRecyclerViewAdapter(Context context, List<Post> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public FeedRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = PostItemListBinding.inflate(LayoutInflater.from(parent.getContext()));

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(FeedRecyclerViewAdapter.ViewHolder holder, int position) {

        Post post = itemList.get(position);
        Glide.with(context)
                .load(post.getImageURL())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder_image)
                .into(binding.ivPost);

        holder.binding.tvPostedBy.setText(post.getPostCreatedByUserName());
        holder.binding.tvLocation.setText(post.getLocation());
        holder.binding.tvDescription.setText(post.getDescription());
        holder.binding.tvTime.setText(post.getPostCreationTime());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PostItemListBinding binding;

        public ViewHolder(PostItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
