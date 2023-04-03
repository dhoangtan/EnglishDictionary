package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.englishdictionary.R;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputLayout currentPasswordTextLayout, newPasswordTextLayout, confirmPasswordTextLayout;
    private TextInputEditText currentPasswordTextInput, newPasswordTextInput, confirmPasswordTextInput;
    private Button confirmButton, cancelButton;
    private SharedPreferences sharedPref;
    private Boolean isPasswordVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPasswordTextLayout = findViewById(R.id.ac_change_password_current_password_text_layout);
        newPasswordTextLayout = findViewById(R.id.ac_change_password_new_password_text_layout);
        confirmPasswordTextLayout = findViewById(R.id.ac_change_password_confirm_password_text_layout);

        currentPasswordTextInput = findViewById(R.id.ac_change_password_current_password_text_input);
        newPasswordTextInput = findViewById(R.id.ac_change_password_new_password_text_input);
        confirmPasswordTextInput = findViewById(R.id.ac_change_password_confirm_password_text_input);

        confirmButton = findViewById(R.id.ac_change_password_confirm_button);
        cancelButton = findViewById(R.id.ac_change_password_cancel_button);

        sharedPref = this.getSharedPreferences("DictionaryPreferences", Context.MODE_PRIVATE);
        isPasswordVisible = false;

        currentPasswordTextLayout.setEndIconOnClickListener(view -> changeVisiblePassword());
        newPasswordTextLayout.setEndIconOnClickListener(view -> changeVisiblePassword());
        confirmPasswordTextLayout.setEndIconOnClickListener(view -> changeVisiblePassword());

        currentPasswordTextInput.setOnFocusChangeListener(((view, b) -> clearError(currentPasswordTextLayout)));
        newPasswordTextInput.setOnFocusChangeListener(((view, b) -> clearError(newPasswordTextLayout)));
        confirmPasswordTextInput.setOnFocusChangeListener(((view, b) -> clearError(confirmPasswordTextLayout)));

        confirmButton.setOnClickListener(view -> {
            String currentPassword = currentPasswordTextInput.getText().toString();
            String newPassword = newPasswordTextInput.getText().toString();
            String confirmPassword = confirmPasswordTextInput.getText().toString();

            clearFocus();

            if (!validatePassword(currentPassword, newPassword, confirmPassword)) {
                return;
            }

            if (currentPassword.equals(newPassword)) {
                Toast.makeText(ChangePasswordActivity.this, "New password and old password cannot be the same", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                                Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("password", newPassword);
                                editor.commit();
                                finish();
                            });
                        }
                    });
        });

        cancelButton.setOnClickListener(view -> {
            new AlertDialog.Builder(ChangePasswordActivity.this)
                    .setTitle("Confirm")
                    .setMessage("Do you want to cancel changes?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

    }

    private void changeVisiblePassword() {
        if (!isPasswordVisible) {
            currentPasswordTextInput.setInputType(InputType.TYPE_CLASS_TEXT);
            currentPasswordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_off_24);

            newPasswordTextInput.setInputType(InputType.TYPE_CLASS_TEXT);
            newPasswordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_off_24);

            confirmPasswordTextInput.setInputType(InputType.TYPE_CLASS_TEXT);
            confirmPasswordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_off_24);

            isPasswordVisible = true;
        }
        else {
            currentPasswordTextInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            currentPasswordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_on_24);

            newPasswordTextInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newPasswordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_on_24);

            confirmPasswordTextInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            confirmPasswordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_on_24);

            isPasswordVisible = false;
        }
    }

    private void clearFocus() {
        currentPasswordTextInput.clearFocus();
        newPasswordTextInput.clearFocus();
        confirmPasswordTextInput.clearFocus();
    }

    private void clearError(TextInputLayout view) {
        view.setError(null);
    }

    private boolean validatePassword(String currentPassword, String newPassword, String confirmPassword) {
       boolean isValid = true;

        String userOldPassword = sharedPref.getString("password", "");

        if (!currentPassword.equals(userOldPassword)) {
            currentPasswordTextLayout.setError("Current password does not match");
            isValid = false;
        }
        if (newPassword.isEmpty()) {
            newPasswordTextLayout.setError("New password cannot be empty");
            isValid = false;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordTextLayout.setError("Confirm password cannot be empty");
            isValid = false;
        }
        if (!newPassword.equals(confirmPassword)) {
            confirmPasswordTextLayout.setError("Confirm password does not match");
            isValid = false;
        }
        if (currentPassword.isEmpty()) {
            currentPasswordTextLayout.setError("Current password cannot be empty");
            isValid = false;
        }

       return isValid;
    }
}