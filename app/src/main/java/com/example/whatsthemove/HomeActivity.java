package com.example.whatsthemove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsthemove.Home.FeedRecyclerViewAdapter;
import com.example.whatsthemove.Home.MyFeedItemList;
import com.example.whatsthemove.databinding.ActivityHomeBinding;
import com.example.whatsthemove.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    List<Post> myListData = new ArrayList<>();
    FeedRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.swipeRefreshLayout.setRefreshing(true);

        setListeners();
        getData();
        recyclerViewAdapter = new FeedRecyclerViewAdapter(this, myListData);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(recyclerViewAdapter);


    }

    private void setListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        binding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddPostActivity.class));
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        Log.d(Constants.TAG, "getData: Called");
        FirebaseDatabase.getInstance().getReference().child("Posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myListData.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Log.d(Constants.TAG, "onDataChange: " + child);
                            Post post = child.getValue(Post.class);
                            myListData.add(post);
                        }

                        checkListVisibility();
                        recyclerViewAdapter.notifyDataSetChanged();
                        binding.swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void checkListVisibility() {
        if(myListData.isEmpty()){
            binding.emptyLayout.getRoot().setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }else{
            binding.emptyLayout.getRoot().setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }
}