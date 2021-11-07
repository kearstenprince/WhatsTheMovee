package com.example.whatsthemove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.whatsthemove.databinding.ActivityAddPostBinding;
import com.example.whatsthemove.models.Post;
import com.example.whatsthemove.utils.DialogUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddPostActivity extends AppCompatActivity {

    ActivityAddPostBinding binding;
    Post post = new Post();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddPostActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        LocalTime localTime = LocalTime.of(selectedHour, selectedMinute);
                        String localTimeStr = localTime.format(DateTimeFormatter.ofPattern(Constants.TIME_FORMAT));
                        post.setPostCreationTime(localTimeStr);
//                        binding.etTime.setText(selectedHour + ":" + selectedMinute);
                        binding.etTime.setText(localTimeStr);
                    }
                }, hour, minute, false);//Yes 12 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        binding.ivSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddPostActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(512)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void uploadImage() {
        DialogUtil.showSimpleProgressDialog(this);
        if (fileUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("images")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("post-image-" + fileUri.getLastPathSegment());
            UploadTask uploadTask = storageReference.putFile(fileUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    DialogUtil.closeProgressDialog();
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        post.setImageURL(downloadUri.toString());
                        uploadPostNow();
                        Log.d(Constants.TAG, "onComplete: " + downloadUri);
                    } else {
                        Log.d(Constants.TAG, "onComplete: Task is Failed");
                    }
                }
            });

        } else {
            Toast.makeText(this, "You haven't selected an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPostNow() {
        String description = binding.etDescription.getText().toString();
        String location = binding.etLocation.getText().toString();
        post.setPostCreatedByUserId(FirebaseAuth.getInstance().getUid());
        post.setPostCreatedByUserName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        post.setDescription(description);
        post.setLocation(location);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Posts")
                .push();
        post.setPostID(databaseReference.getKey());
        databaseReference.setValue(post)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(AddPostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(Constants.TAG, "onComplete: " + task.getException().getMessage());
                        }
                        Toast.makeText(AddPostActivity.this, "Post uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            if (data.getData() != null) {
                fileUri = data.getData();
                binding.ivSelectImg.setImageURI(fileUri);
            }

            // Use Uri object instead of File to avoid storage permissions
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                        binding.ivSelectImg.setImageBitmap(selectedImage);
//                    }
//
//                    break;
//                case 1:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//
//
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                binding.ivSelectImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }
//
//                    }
//                    break;
//            }
//        }
//    }

}