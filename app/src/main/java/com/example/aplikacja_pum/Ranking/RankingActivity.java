package com.example.aplikacja_pum.Ranking;

import android.content.Context;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.Models.Photo;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.FirebaseMethods;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_SHORT;

public class RankingActivity extends AppCompatActivity
{

    private static final String TAG = "RankingActivity";
    private static final int ActivityNumber = 1;

    private Context mContext = RankingActivity.this;


    private Photo photo;

    private ImageView downloadIMG;
    private TextView title;
    private TextView time;
    private Button buttonDrawMem;
    private Random generator;
    private CircleImageView profilePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Log.d(TAG, "onCreate: starting.");

        setupBottomNavigationView();


        downloadIMG = (ImageView) findViewById(R.id.downloadIMG);
        title = (TextView) findViewById(R.id.image_caption);
        time = (TextView) findViewById(R.id.image_time_posted);
        profilePhoto = (CircleImageView) findViewById(R.id.profile_photo);

        buttonDrawMem = (Button) findViewById(R.id.drawMem);
        buttonDrawMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupGridView();
            }
        });
    }

    public void setupGridView() {

        final int[] index = new int[1];
        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.photos));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    photos.add(singleSnapshot.getValue(Photo.class));
                }


                ArrayList<String> imgUrls = new ArrayList<String>();
                for (int i = 0; i < photos.size(); i++) {
                    imgUrls.add(photos.get(i).getImagePath());
                }

                //losowanie
                generator = new Random();
                index[0] = generator.nextInt(photos.size())+0;

                Log.d("dsf", String.valueOf(index[0]));

                UniversalImageLoader.setImage(imgUrls.get(index[0]),downloadIMG,null,"");
                time.setText(photos.get(index[0]).getDataCreated());


                //ładowanie szczegolow
                String tags = photos.get(index[0]).getTags();
                title.setText(tags);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private String getTimestampDifference(String dateCreated){
        Log.d(TAG, "getTimestampDifference: getting timestamp difference.");

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.GERMANY);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp;
        final String photoTimestamp = dateCreated;
        try{
            timestamp = sdf.parse(photoTimestamp);
            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24 )));
        }catch (ParseException e){
            Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage() );
            difference = "0";
        }

        return difference;
    }

    private void setupBottomNavigationView()
    {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ActivityNumber);
        menuItem.setChecked(true);
    }
}
