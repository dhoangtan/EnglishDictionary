package android.englishdictionary.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.englishdictionary.fragments.DictionaryFragment;
import android.englishdictionary.fragments.ExploreFragment;
import android.englishdictionary.fragments.HomeFragment;
import android.englishdictionary.fragments.ProfileFragment;
import android.englishdictionary.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.opencv.android.OpenCVLoader;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_CODE = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;
    private static final String TAG = "DICTIONARY";
    private Bitmap bitmap;
    private ActionBar toolbar;
    private Button dictionaryFragmentOpenCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        requestForPermission();

        if(OpenCVLoader.initDebug())
            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

        toolbar = getSupportActionBar();
        BottomNavigationView navigationView= findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Home");
        loadFragment(new HomeFragment());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == CAMERA_CODE && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
//            image.setImageBitmap(bitmap);
//
//            mat = new Mat();
//            Utils.bitmapToMat(bitmap, mat);
//
//            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
//
//            Utils.matToBitmap(mat, bitmap);
//            image.setImageBitmap(bitmap);


            ObjectDetector.ObjectDetectorOptions options = ObjectDetector.ObjectDetectorOptions.builder()
                    .setBaseOptions(BaseOptions.builder().build())
                    .setMaxResults(1)
                    .build();

            TensorImage tensorImage = TensorImage.fromBitmap(bitmap);

            try {
                ObjectDetector detector = ObjectDetector.createFromFileAndOptions(this, "efficientdet-lite0.tflite", options);
                List<Detection> results = detector.detect(tensorImage);
                results.forEach(detection -> {
                    detection.getCategories().forEach(category -> {
                        Log.d(TAG, category.getLabel());
                        Toast.makeText(this, category.getLabel(), Toast.LENGTH_SHORT).show();
                    });
                    detection.getBoundingBox();
                });
            }
            catch (IOException e) {
                Log.d(TAG, "onActivityResult: " + e.getMessage());
            }

        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.bottom_navigation_home:
                    toolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.bottom_navigation_explore:
                    toolbar.setTitle("Explore");
                    fragment = new ExploreFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.bottom_navigation_dictionnary:
                    toolbar.setTitle("Dictionary");
                    fragment = new DictionaryFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.bottom_navigation_profile:
                    toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    void requestForPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.length>0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestForPermission();
            }
        }
    }
}