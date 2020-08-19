package com.amey.mchat.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amey.mchat.MainActivity;
import com.amey.mchat.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsernameActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private Button remove,create;
    private EditText username;
    private ProgressBar pb5;
    private Uri photoUri;
    private StorageReference storage;
    private FirebaseAuth firebaseAuth;
    private String url="";
    private FirebaseFirestore firestore;
    public final static String USERNAME_PATTERN="^[a-z0-9_-]{4,15}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        init();

        storage=FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(UsernameActivity.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        } else {
                            Toast.makeText(UsernameActivity.this, "Please allow Permission", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUri=null;
                profileImage.setImageResource(R.drawable.profile);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setError(null);
                if (username.getText().toString().isEmpty() || username.getText().toString().length()<4){
                    username.setError("Username must contain atleast 4 character long");
                    return;
                }
                if (!username.getText().toString().matches(USERNAME_PATTERN)){
                    username.setError("Invalid Username");
                    return;
                }
                pb5.setVisibility(View.VISIBLE);
                firestore.getInstance().collection("users").whereEqualTo("username",username.getText().toString())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<DocumentSnapshot> document=task.getResult().getDocuments();
                            if (document.isEmpty()){
                                uploadData();
                            }else {
                                pb5.setVisibility(View.INVISIBLE);
                                username.setError("user already exist");
                                return;
                            }
                        }else {
                            String error=task.getException().getMessage();
                            Toast.makeText(UsernameActivity.this,error,Toast.LENGTH_SHORT).show();
                            pb5.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
    private void init(){
        profileImage=findViewById(R.id.profile_image);
        remove=findViewById(R.id.remove_btn);
        create=findViewById(R.id.create_btn);
        username=findViewById(R.id.username);
        pb5=findViewById(R.id.progressbar);
    }
    private void selectImage(){
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityMenuIconColor(getResources().getColor(R.color.colorAccent))
                .setActivityTitle("Profile Image")
                .setFixAspectRatio(true)
                .setAspectRatio(1,1)
                .start(this);
    }
    private void uploadData(){
        if (photoUri!=null){//upload profile image with username
            final StorageReference ref = storage.child("profile/"+firebaseAuth.getCurrentUser().getUid());
            UploadTask uploadTask = ref.putFile(photoUri);

             uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        String error=task.getException().getMessage();
                        Toast.makeText(UsernameActivity.this,error,Toast.LENGTH_SHORT).show();
                        pb5.setVisibility(View.INVISIBLE);

                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url=uri.toString();
                        }
                    });
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        uploadUsername();
                    } else {
                        pb5.setVisibility(View.INVISIBLE);
                        String error=task.getException().getMessage();
                        Toast.makeText(UsernameActivity.this,error,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {//upload username only
            uploadUsername();
        }
    }
    private void uploadUsername(){
        Map<String,Object> map=new HashMap<>();
        map.put("username",username.getText().toString());
        map.put("profile image",url);
        firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(UsernameActivity.this, MainActivity.class));
                            finish();
                        }else {
                            pb5.setVisibility(View.INVISIBLE);
                            String error=task.getException().getMessage();
                            Toast.makeText(UsernameActivity.this,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoUri = result.getUri();
                Glide
                        .with(this)
                        .load(photoUri)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profileImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}