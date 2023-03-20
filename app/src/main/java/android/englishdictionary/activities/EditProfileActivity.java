package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.englishdictionary.R;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    EditText userFullNameEditText;
    RadioGroup userGenderRadioGroup, userLevelRadioGroup, userOccupationRadioGroup;
    ArrayList<RadioButton> activityRadioButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        activityRadioButtons = new ArrayList<>();
        userGenderRadioGroup = findViewById(R.id.ac_edit_profile_gender_radio_group);
        userLevelRadioGroup = findViewById(R.id.ac_edit_profile_level_radio_group);
        userOccupationRadioGroup = findViewById(R.id.ac_edit_profile_occupation_radio_group);
        CollectionReference gendersCollection = FirebaseFirestore
                .getInstance()
                .collection("genders");

        gendersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                task.getResult().forEach(queryDocumentSnapshot -> {
                    int id = Integer.parseInt(queryDocumentSnapshot.getId());
                    String name = queryDocumentSnapshot.getString("name");

                    RadioButton newRadioButton = new RadioButton(EditProfileActivity.this);
                    newRadioButton.setText(name);
                    newRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    newRadioButton.setVisibility(View.VISIBLE);
                    activityRadioButtons.add(newRadioButton);
                });
                for (RadioButton button : activityRadioButtons) {
                    userGenderRadioGroup.addView(button);
                }
                activityRadioButtons.clear();
            }
        });

        gendersCollection = FirebaseFirestore
                .getInstance()
                .collection("levels");

        gendersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                task.getResult().forEach(queryDocumentSnapshot -> {
                    int id = Integer.parseInt(queryDocumentSnapshot.getId());
                    String name = queryDocumentSnapshot.getString("name");

                    RadioButton newRadioButton = new RadioButton(EditProfileActivity.this);
                    newRadioButton.setText(name);
                    newRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    newRadioButton.setVisibility(View.VISIBLE);
                    activityRadioButtons.add(newRadioButton);
                });
                for (RadioButton button : activityRadioButtons) {
                    userLevelRadioGroup.addView(button);
                }
                activityRadioButtons.clear();
            }
        });

        gendersCollection = FirebaseFirestore
                .getInstance()
                .collection("occupations");

        gendersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                task.getResult().forEach(queryDocumentSnapshot -> {
                    int id = Integer.parseInt(queryDocumentSnapshot.getId());
                    String name = queryDocumentSnapshot.getString("name");

                    RadioButton newRadioButton = new RadioButton(EditProfileActivity.this);
                    newRadioButton.setText(name);
                    newRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    newRadioButton.setVisibility(View.VISIBLE);
                    activityRadioButtons.add(newRadioButton);
                });
                for (RadioButton button : activityRadioButtons) {
                    userOccupationRadioGroup.addView(button);
                }
                activityRadioButtons.clear();
            }
        });
    }
}