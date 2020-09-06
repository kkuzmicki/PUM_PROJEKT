package com.example.aplikacja_pum.Create;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aplikacja_pum.AddDir.AddActivity;
import com.example.aplikacja_pum.Home.MainActivity;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.FilePaths;
import com.example.aplikacja_pum.Utils.Permissions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class CreateActivity extends AppCompatActivity
{

    private static final String TAG = "CreateActivity";
    private static final int ActivityNumber = 3;
    private static final int RESULT_LOAD_IMAGE = 10;

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;
    private static final int  CAMERA_REQUEST_CODE = 100;

    private Context mContext = CreateActivity.this;

    private TextView generateTv;
    private TextView loadImage;
    private EditText topEt;
    private EditText botEt;
    private ImageView loadMem;
    private Button tryButton;
    private TextView topTV;
    private TextView botTV;
    private TextView cameraTv;
    private SeekBar sizeSeekBar;
    private TextView sizeValue;
    private int size = 0;
    private OutputStream outputStream;
    private String TAG1 = "GENERETOR";

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

                //pobranie imageViev do bitmapy
                BitmapDrawable bitmapDrawable = (BitmapDrawable) loadMem.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                //Tworzenie sciezki oraz dir
                FilePaths paths = new FilePaths();
                File dir = new File(paths.PICTURES);

                if(!dir.exists()) {
                    dir.mkdir();
                }

                //tworzenie sciezki potencjalnego pliku
                File file = new File(dir, "Mem_" + System.currentTimeMillis() + ".jpg");

                //proba zapisu
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //kompresja do jpeg
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(file.exists()){
                    Intent intent = new Intent(CreateActivity.this, AddActivity.class);
                    startActivity(intent);

                    Toast.makeText(CreateActivity.this, "Poprawnie wygenerowano MEMA!", Toast.LENGTH_LONG).show();
                }
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

                    if(topEt.getText().length() == 0 || botEt.getText().length() == 0){
                        Toast.makeText(CreateActivity.this, "Wpisany tekst jest za mały !", Toast.LENGTH_LONG).show();
                    } else
                        if(topEt.getText().length() >= 30 || botEt.getText().length() >= 30){
                            Toast.makeText(CreateActivity.this, "Wpisany tekst jest za duży !", Toast.LENGTH_LONG).show();
                        }else{
                        topTV.setText(topEt.getText().toString());
                        botTV.setText(botEt.getText().toString());
                        }
            }
        });

        //operacja bara
        sizeSeekBar = (SeekBar) findViewById(R.id.size);
        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                size = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                topTV.setTextSize(size);
                botTV.setTextSize(size);
                sizeValue.setText("Font size: " + size);
            }
        });
        //zapytanie o pozwolenie camery (uzyto rowniez w addActivity)
        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            //zaakceptowane wczesniej
            cameraTv = (TextView) findViewById(R.id.camera);
            cameraTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            });
        }else{
            //weryfikacja
            verifyPermissions(Permissions.PERMISSIONS);
            //poprawiono akceptacje
            finish();
        }

        topEt = (EditText) findViewById(R.id.textTop);
        botEt = (EditText) findViewById(R.id.textBot);

        topTV = (TextView) findViewById(R.id.topTV);
        botTV = (TextView) findViewById(R.id.botTV);

        loadMem = (ImageView) findViewById(R.id.loadMem);

        sizeValue = (TextView) findViewById(R.id.sizeValue);

        generateTv.setEnabled(false);
        tryButton.setEnabled(false);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && null != data){
            //sciezka wybranego pliku
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

        if(requestCode == CAMERA_REQUEST_CODE && data != null){
            //pobranie img
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            if(bitmap != null) {
                //ustawienie img
                loadMem.setImageBitmap(bitmap);
                generateTv.setEnabled(true);
            }else {
                finish();
            }
        }

        topTV.setText("");
        botTV.setText("");
        tryButton.setEnabled(true);
    }

    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                CreateActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    //zapytanie
    public boolean checkPermissions(String permission){
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(CreateActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
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
