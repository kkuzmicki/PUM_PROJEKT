package com.example.aplikacja_pum.Create;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.AddDir.AddActivity;
import com.example.aplikacja_pum.Home.MainActivity;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class CreateActivity extends AppCompatActivity
{

    private static final String TAG = "CreateActivity";
    private static final int ActivityNumber = 3;
    private static final int RESULT_LOAD_IMAGE = 1;

    private Context mContext = CreateActivity.this;

    private TextView generateTv;
    private TextView loadImage;
    private EditText topEt;
    private EditText botEt;
    private ImageView loadMem;
    private Button tryButton;
    private TextView topTV;
    private TextView botTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Log.d(TAG, "onCreate: starting.");

        setupBottomNavigationView();

        //ikona zamykania
        ImageView  imageView = (ImageView) findViewById(R.id.ivClose);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //button generuj
        generateTv = (TextView) findViewById(R.id.generate);
        generateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Generator","Generuje mema");

            }
        });

        //wczytaj img
        loadImage = (TextView) findViewById(R.id.load);
        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

            }
        });

        //button proby dodania stringa
        tryButton = (Button) findViewById(R.id.tryButton);
        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topTV.setText(topEt.getText().toString());
                botTV.setText(botEt.getText().toString());
            }
        });

        topEt = (EditText) findViewById(R.id.textTop);
        botEt = (EditText) findViewById(R.id.textBot);

        topTV = (TextView) findViewById(R.id.topTV);
        botTV = (TextView) findViewById(R.id.botTV);

        loadMem = (ImageView) findViewById(R.id.loadMem);

        generateTv.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && requestCode == RESULT_OK && data != null){
            Uri selectedImg = data.getData();
            String [] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImg, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //pobranie image view
            loadMem.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            generateTv.setEnabled(true);
        }
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
