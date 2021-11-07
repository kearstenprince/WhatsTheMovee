package com.example.whatsthemove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsthemove.databinding.ActivityRegisterBinding;
import com.example.whatsthemove.models.User;
import com.example.whatsthemove.utils.DialogUtil;
import com.example.whatsthemove.utils.SnackBarUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());
        setListeners();

    }

    private void setListeners() {

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser(view);
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void signUpUser(View view) {
        registerUser();
    }

    private void registerUser() {
        DialogUtil.showSimpleProgressDialog(this);
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String pass = binding.etPassword.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            if (firebaseUser != null) {
                                firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        DialogUtil.closeProgressDialog();
                                        User user = new User(email, firebaseUser.getUid(), username);
                                        FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(user.getUserId())
                                                .setValue(user);
                                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                });


                            }

                        } else {
                            if (task.getException() != null)
                                SnackBarUtils.showSnackBar(binding.getRoot(), task.getException().getMessage());
                            else
                                SnackBarUtils.showSnackBar(binding.getRoot(), "Something went wrong");
                        }
                    }
                });
    }
}