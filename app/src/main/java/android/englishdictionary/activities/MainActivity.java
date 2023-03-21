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
import android.englishdictionary.models.Word;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_CODE = 101;
    public static final int CAMERA_PERMISSION_CODE = 102;
    public static final int LOCAL_PICTURE_CODE = 103;
    private static final String TAG = "DICTIONARY";
    private Bitmap bitmap;
    private ActionBar toolbar;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        queue = Volley.newRequestQueue(this);
        requestForPermission();

        toolbar = getSupportActionBar();
        BottomNavigationView navigationView = findViewById(R.id.ac_main_bottom_navigation);
        navigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Home");
        loadFragment(new HomeFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == CAMERA_CODE && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");

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
                        Toast.makeText(this, category.getLabel(), Toast.LENGTH_SHORT).show();
                        searchForWord(category.getLabel());
                    });
                });
            }
            catch (IOException e) {
                Log.d(TAG, "onActivityResult: " + e.getMessage());
            }

        }
         if (requestCode == LOCAL_PICTURE_CODE && data != null) {
             try {
                 bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());


                 ((ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.ac_main_frame_container)).updateAvatar(bitmap);
             } catch (IOException e) {
                 throw new RuntimeException(e);
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
        transaction.replace(R.id.ac_main_frame_container, fragment);
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

    public void searchForWord(String word) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
        DictionaryFragment dictionaryFragment = (DictionaryFragment) getSupportFragmentManager().findFragmentById(R.id.ac_main_frame_container);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Gson gson = new Gson();
                    TypeToken<Collection<Word>> collectionTypeToken = new TypeToken<Collection<Word>>(){};
                    ArrayList<Word> wordsFromJson = gson.fromJson(response, collectionTypeToken.getType());
                    dictionaryFragment.wordResult(getApplicationContext(), wordsFromJson);
                    Log.d(TAG, "RESPONSE COMPLETE");
                },
                error -> {
                    dictionaryFragment.wordResult(getApplicationContext(), new ArrayList<>());
                });
        queue.add(stringRequest);
    }
}