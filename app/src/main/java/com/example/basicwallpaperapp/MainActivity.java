package com.example.basicwallpaperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;
    Button camera, setWall;
    Bitmap bitmap;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        camera = findViewById(R.id.buttonCamera);
        setWall = findViewById(R.id.buttonSetWallpaper);

        camera.setOnClickListener((View.OnClickListener) this);
        setWall.setOnClickListener((View.OnClickListener) this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonCamera:

                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    openCameraMethod();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                }

                break;

            case R.id.buttonSetWallpaper:

                try {
                    getApplicationContext().setWallpaper(bitmap);
                    Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_LONG).show();
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                    }
                };
                thread.start();

                break;
        }
    }

    private void openCameraMethod() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraMethod();
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have camera permission...", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle b = data.getExtras();
            bitmap = (Bitmap) b.get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

}