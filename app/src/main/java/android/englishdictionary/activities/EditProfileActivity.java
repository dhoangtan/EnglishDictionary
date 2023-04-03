package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.models.ApplicationUser;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    private EditText userFullNameEditText;
    private RadioGroup userGenderRadioGroup, userLevelRadioGroup, userOccupationRadioGroup;
    private Button saveButton, cancelButton;
    private ArrayList<RadioButton> activityRadioButtons;

    private final int GENDER_RADIO_BUTTON_ID = 10;
    private final int LEVEL_RADIO_BUTTON_ID = 20;
    private final int OCCUPATION_RADIO_BUTTON_ID = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userFullNameEditText = findViewById(R.id.ac_edit_profile_full_name_edit_text);
        userGenderRadioGroup = findViewById(R.id.ac_edit_profile_gender_radio_group);
        userLevelRadioGroup = findViewById(R.id.ac_edit_profile_level_radio_group);
        userOccupationRadioGroup = findViewById(R.id.ac_edit_profile_occupation_radio_group);

        saveButton = findViewById(R.id.ac_edit_profile_save_button);
        cancelButton = findViewById(R.id.ac_edit_profile_cancel_button);

        ApplicationUser user = getIntent().getExtras().getParcelable("user");
        Log.d("USER_DEBUG", user.getFullName());

        loadUserInfo(user);

        cancelButton.setOnClickListener(view -> {
            new AlertDialog.Builder(EditProfileActivity.this)
                    .setTitle("Confirm")
                    .setMessage("Do you want to cancel changes?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        saveButton.setOnClickListener(view -> {
            String newFullName = userFullNameEditText.getText().toString();

            RadioButton checkedGenderRadioButton = null,
                    checkedLevelRadioButton = null,
                    checkedOccupationRadioButton = null;
            for (int i = 0; i < userGenderRadioGroup.getChildCount(); i++) {
                RadioButton current = (RadioButton) userGenderRadioGroup.getChildAt(i);
                if (current.isChecked())
                    checkedGenderRadioButton = current;
            }
            for (int i = 0; i < userLevelRadioGroup.getChildCount(); i++) {
                RadioButton current = (RadioButton) userLevelRadioGroup.getChildAt(i);
                if (current.isChecked())
                    checkedLevelRadioButton = current;
            }
            for (int i = 0; i < userOccupationRadioGroup.getChildCount(); i++) {
                RadioButton current = (RadioButton) userOccupationRadioGroup.getChildAt(i);
                if (current.isChecked())
                    checkedOccupationRadioButton = current;
            }

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(user.getUser().getUid());
            documentReference.update(
                    "full_name", newFullName,
                    "gender", checkedGenderRadioButton.getId()-GENDER_RADIO_BUTTON_ID,
                    "level", checkedLevelRadioButton.getId()-LEVEL_RADIO_BUTTON_ID,
                    "occupation", checkedOccupationRadioButton.getId()-OCCUPATION_RADIO_BUTTON_ID
                ).addOnCompleteListener(task -> {
                Toast.makeText(EditProfileActivity.this, "Update profile successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void loadUserInfo (ApplicationUser user) {
        Log.d("USER_DEBUG", user.getFullName());
        userFullNameEditText.setText(user.getFullName());

        activityRadioButtons = new ArrayList<>();
        CollectionReference gendersCollection = FirebaseFirestore
                .getInstance()
                .collection("genders");

        gendersCollection.get().addOnCompleteListener(task -> {
            int gender = user.getGender();
            task.getResult().forEach(queryDocumentSnapshot -> {
                int id = Integer.parseInt(queryDocumentSnapshot.getId());
                String name = queryDocumentSnapshot.getString("name");

                RadioButton newRadioButton = new RadioButton(EditProfileActivity.this);
                newRadioButton.setId(GENDER_RADIO_BUTTON_ID + id);
                newRadioButton.setText(name);
                newRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                newRadioButton.setVisibility(View.VISIBLE);
                activityRadioButtons.add(newRadioButton);
            });
            for (RadioButton button : activityRadioButtons) {
                gender--;
                if (gender == 0)
                    button.setChecked(true);
                userGenderRadioGroup.addView(button);
            }
            activityRadioButtons.clear();
        });

        gendersCollection = FirebaseFirestore
                .getInstance()
                .collection("levels");

        gendersCollection.get().addOnCompleteListener(task -> {
            int level = user.getLevel();
            task.getResult().forEach(queryDocumentSnapshot -> {
                int id = Integer.parseInt(queryDocumentSnapshot.getId());
                String name = queryDocumentSnapshot.getString("name");

                RadioButton newRadioButton = new RadioButton(EditProfileActivity.this);
                newRadioButton.setId(LEVEL_RADIO_BUTTON_ID + id);
                newRadioButton.setText(name);
                newRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                newRadioButton.setVisibility(View.VISIBLE);
                activityRadioButtons.add(newRadioButton);
            });
            for (RadioButton button : activityRadioButtons) {
                level--;
                if (level == 0)
                    button.setChecked(true);
                userLevelRadioGroup.addView(button);
            }
            activityRadioButtons.clear();
        });

        gendersCollection = FirebaseFirestore
                .getInstance()
                .collection("occupations");

        gendersCollection.get().addOnCompleteListener(task -> {
            int occupation = user.getOccupation();
            task.getResult().forEach(queryDocumentSnapshot -> {
                int id = Integer.parseInt(queryDocumentSnapshot.getId());
                String name = queryDocumentSnapshot.getString("name");

                RadioButton newRadioButton = new RadioButton(EditProfileActivity.this);
                newRadioButton.setId(OCCUPATION_RADIO_BUTTON_ID + id);
                newRadioButton.setText(name);
                newRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                newRadioButton.setVisibility(View.VISIBLE);
                activityRadioButtons.add(newRadioButton);
            });
            for (RadioButton button : activityRadioButtons) {
                occupation--;
                if (occupation == 0)
                    button.setChecked(true);
                userOccupationRadioGroup.addView(button);
            }
            activityRadioButtons.clear();
        });
    }
}