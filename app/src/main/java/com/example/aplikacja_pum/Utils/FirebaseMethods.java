package com.example.aplikacja_pum.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.aplikacja_pum.Models.Photo;
import com.example.aplikacja_pum.Models.User;
import com.example.aplikacja_pum.Models.UserAccountSettings;
import com.example.aplikacja_pum.Models.UserInfo;
import com.example.aplikacja_pum.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import static android.widget.Toast.LENGTH_SHORT;

public class FirebaseMethods
{
    private static final String TAG = "FirebaseMethods";

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference mStorageReference ;

    private String userID;

    private Context context;

    private double photoUploadProgress = 0;

    public FirebaseMethods(Context context)
    {
        mAuth = FirebaseAuth.getInstance();
        //private FirebaseAuth.AuthStateListener mAuthListener;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        mStorageReference = FirebaseStorage.getInstance().getReference();

        this.context = context;


        if(mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public int getImageCount(DataSnapshot dataSnapshot){
        int count = 0;

        for (DataSnapshot ignored : dataSnapshot
                .child(context.getString(R.string.user_photos))
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .getChildren()){
                    count ++;
        }

        return count;
    }

    public void sendEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(context, "Verification email has been sent!", LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, "nie udalo sie wyslac maila", LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void registerNewEmail(final String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        sendEmail();

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    }
                    else
                    {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(context, "Authentication failed.",
                                LENGTH_SHORT).show();
                    }
                });
    }

    /*public boolean checkIfNameExists(String name, DataSnapshot dataSnapshot)
    {
        Log.d(TAG, "checkIfNameExists: checking if " + name + " already exists.");

        User user = new User();

        for(DataSnapshot ds: dataSnapshot.child(userID).getChildren())
        {
            user.setName(ds.getValue(User.class).getName());

            if(StringModifier.expandName(user.getName()).equals(name))
            {
                Log.d(TAG, "Podany name istnieje!");
                return true;
            }
        }

        return false;
    }*/

    public void addNewUser(String email, String name)
    {
        Log.d(TAG, "Adding new user!");

        User user = new User(userID, email, name);

        databaseReference.child("users")
            .child(userID)
            .setValue(user);

        UserAccountSettings settings = new UserAccountSettings("", "", 0, 0, name, 0, "PL");

        databaseReference.child("user_account_settings")
                .child(userID)
                .setValue(settings);
    }

    public void uploadNewPhoto(String type, String title, int imageCount, String imgUrl) {
        FilePaths filePaths = new FilePaths();

        if(type.equals(context.getString(R.string.profile_photo))) {
            //sciazka na Firebase (photos/users/userID/photoCount)
            StorageReference storageReference = mStorageReference.child(filePaths.FIREBASE_IMAGE_STORAGE + "/"
                    + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profile_photo");

            //convert url to bitmap
            Bitmap bitmap = ConvertImage.getBitmap(imgUrl);

            //upload img
            UploadTask uploadTask;
            byte[] bytes = ConvertImage.getBytesFromBitmap(bitmap, 100);

            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                //success
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                Toast.makeText(context, "Photo upload success ", LENGTH_SHORT).show();


                //dodanie z profilu
                setProfilePhoto(downloadUrl.toString());

            }).addOnFailureListener(e -> {
                //fail
                Toast.makeText(context, "Photo upload failed !!!", LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                //progress
                // (100 * przeslane) / calosc
                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                if(progress - 15 > photoUploadProgress){

                    Toast.makeText(context, "Photo upload progress: "
                            + String.format("%.0f", progress) + "%", LENGTH_SHORT).show();
                    photoUploadProgress = progress;
                }
            });
        }else if(type.equals(context.getString(R.string.new_photo))){
            //sciazka na Firebase (photos/users/userID/photoCount)
            StorageReference storageReference = mStorageReference.child(filePaths.FIREBASE_IMAGE_STORAGE + "/"
                    + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/photo" + (imageCount + 1));

            //convert url to bitmap
            Bitmap bitmap = ConvertImage.getBitmap(imgUrl);

            //upload img
            UploadTask uploadTask = null;
            byte[] bytes = ConvertImage.getBytesFromBitmap(bitmap, 100);

            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                //success
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                //dodanie informacji o zdj do bazy
                addPhotoToDatabase(title, downloadUrl.toString());

                setProfilePhoto(downloadUrl.toString());

            }).addOnFailureListener(e -> {
                //fail
                Toast.makeText(context, "Photo upload failed !!!", LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                //progress
                // (100 * przeslane) / calosc
                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                if(progress - 15 > photoUploadProgress){

                    Toast.makeText(context, "Photo upload progress: "
                            + String.format("%.0f", progress) + "%", LENGTH_SHORT).show();
                    photoUploadProgress = progress;
                }
            });
        }
    }

    private void setProfilePhoto(String url){

        databaseReference.child(context.getString(R.string.user_account_settings))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("avatar")
                .setValue(url);

    }
    private void addPhotoToDatabase(String title, String url){

        String tags = StringModifier.getTags(title);
        String newPhotoKey = databaseReference.child(context.getString(R.string.photos)).push().getKey();
        Photo photo = new Photo();
        photo.setTitle(title);
        photo.setDataCreated(getTime());
        photo.setImagePath(url);
        photo.setTags(tags);
        photo.setUserId(FirebaseAuth.getInstance().getUid());
        photo.setPhotoId(newPhotoKey);

        //wysylanie danych do bazy
        databaseReference.child(context.getString(R.string.user_photos)).child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child(newPhotoKey).setValue(photo);

        databaseReference.child(context.getString(R.string.photos))
                .child(newPhotoKey).setValue(photo);

    }


    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.GERMANY);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
        return simpleDateFormat.format(new Date());
    }

    public UserInfo getUserInfo(DataSnapshot dataSnapshot)
    {
        Log.d(TAG, "pobieranie informacji o uzytkowniku z bazy");

        User user = new User();
        UserAccountSettings userAccountSettings = new UserAccountSettings();

        for(DataSnapshot ds: dataSnapshot.getChildren())
        {
            if(ds.getKey().equals("user_account_settings"))
            {
                Log.d(TAG, "getUserInfo: " + ds);

                userAccountSettings.setName(Objects.requireNonNull(ds.child(userID).getValue(UserAccountSettings.class)).getName());
                userAccountSettings.setDescription(Objects.requireNonNull(ds.child(userID).getValue(UserAccountSettings.class)).getDescription());
                userAccountSettings.setAvatar(Objects.requireNonNull(ds.child(userID).getValue(UserAccountSettings.class)).getAvatar());
                userAccountSettings.setPosts(Objects.requireNonNull(ds.child(userID).getValue(UserAccountSettings.class)).getPosts());
                userAccountSettings.setFollowers(Objects.requireNonNull(ds.child(userID).getValue(UserAccountSettings.class)).getFollowers());
                userAccountSettings.setFollowing(Objects.requireNonNull(ds.child(userID).getValue(UserAccountSettings.class)).getFollowing());
                userAccountSettings.setNationality(Objects.requireNonNull(ds.child(userID).getValue(UserAccountSettings.class)).getNationality());
            }

            if(ds.getKey().equals("users"))
            {
                Log.d(TAG, "getUserInfo: " + ds);

                user.setName(Objects.requireNonNull(ds.child(userID).getValue(User.class)).getName());
                user.setEmail(Objects.requireNonNull(ds.child(userID).getValue(User.class)).getEmail());
                user.setUser_id(Objects.requireNonNull(ds.child(userID).getValue(User.class)).getUser_id());
            }
        }

        return new UserInfo(user, userAccountSettings);
    }
}
