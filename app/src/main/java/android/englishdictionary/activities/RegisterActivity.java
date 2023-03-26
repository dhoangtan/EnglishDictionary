package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.englishdictionary.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText usernameTextInput, passwordTextInput, confirmPasswordTextInput, fullNameTextInput;
    private TextInputLayout usernameTextLayout, passwordTextLayout, confirmPasswordTextLayout, fullNameTextLayout;
    private Button registerButton;

    private Boolean isPasswordVisible;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private final String TAG = "REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        usernameTextInput = findViewById(R.id.ac_register_username_text_input);
        passwordTextInput = findViewById(R.id.ac_register_password_text_input);
        confirmPasswordTextInput = findViewById(R.id.ac_register_confirm_password_text_input);
        fullNameTextInput = findViewById(R.id.ac_register_full_name_text_input);

        usernameTextLayout = findViewById(R.id.ac_register_username_text_layout);
        passwordTextLayout = findViewById(R.id.ac_register_password_text_layout);
        confirmPasswordTextLayout = findViewById(R.id.ac_register_confirm_password_text_layout);
        fullNameTextLayout = findViewById(R.id.ac_register_full_name_text_layout);

        usernameTextInput.setOnFocusChangeListener((view, hasFocus) -> clearError(usernameTextLayout));
        passwordTextInput.setOnFocusChangeListener((view, hasFocus) -> clearError(passwordTextLayout));
        confirmPasswordTextInput.setOnFocusChangeListener((view, hasFocus) -> clearError(confirmPasswordTextLayout));
        fullNameTextInput.setOnFocusChangeListener((view, hasFocus) -> clearError(fullNameTextLayout));

        isPasswordVisible = false;

        passwordTextLayout.setEndIconOnClickListener(view -> {
            changeVisiblePassword();
        });

        confirmPasswordTextLayout.setEndIconOnClickListener(view -> {
            changeVisiblePassword();
        });


        registerButton = findViewById(R.id.ac_register_register_button);

        registerButton.setOnClickListener(view -> {
            register();
        });

    }

    private void register() {
        String email, password, confirmPassword, fullName;
        email = usernameTextInput.getText().toString();
        password = passwordTextInput.getText().toString();
        confirmPassword = confirmPasswordTextInput.getText().toString();
        fullName = fullNameTextInput.getText().toString();

        clearFocus();

        if (!validateField(email, password, confirmPassword, fullName)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.w(TAG, "createUserWithEmail:success", task.getException());
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        Map<String, Object> storedUser = new HashMap<>();
                        storedUser.put("email", currentUser.getEmail());
                        storedUser.put("full_name", fullName);
                        storedUser.put("gender", 0);
                        storedUser.put("level", 0);
                        storedUser.put("occupation", 0);

                        fireStore.collection("users")
                                .document(currentUser.getUid())
                                .set(storedUser)
                                .addOnSuccessListener(success -> {
                                })
                                .addOnFailureListener(failure -> {
                                    Log.d(TAG, failure.toString());
                                });

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] uploadData = baos.toByteArray();

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReference("images/avatars/" + currentUser.getUid() + "/avatar.jpg");
                        UploadTask uploadTask = storageReference.putBytes(uploadData);
                        uploadTask.addOnFailureListener(exception -> {
                            Log.d(TAG, exception.toString());
                        }).addOnSuccessListener(taskSnapshot -> {
                        });

                        Toast.makeText(RegisterActivity.this, "Register successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Oops. Something went wrong!", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void clearFocus() {
        usernameTextInput.clearFocus();
        passwordTextInput.clearFocus();
        confirmPasswordTextInput.clearFocus();
        fullNameTextInput.clearFocus();
    }

    private void clearError(TextInputLayout view) {
        view.setError(null);
    }

    private void changeVisiblePassword() {
        if (!isPasswordVisible) {
            passwordTextInput.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_off_24);

            confirmPasswordTextInput.setInputType(InputType.TYPE_CLASS_TEXT);
            confirmPasswordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_off_24);

            isPasswordVisible = true;
        }
        else {
            passwordTextInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_on_24);

            confirmPasswordTextInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            confirmPasswordTextLayout.setEndIconDrawable(R.drawable.ic_visibility_on_24);

            isPasswordVisible = false;
        }
    }

    private boolean validateField(String email, String password, String confirmPassword, String fullName) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            usernameTextLayout.setError("Email is empty!");
            isValid = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordTextLayout.setError("Password is empty!");
            isValid = false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordTextLayout.setError("Confirm password is empty!");
            isValid = false;
        }
        if (TextUtils.isEmpty(fullName)) {
            fullNameTextLayout.setError("Name is empty!");
            isValid = false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordTextLayout.setError("Password does not match");
            isValid = false;
        }
        return isValid;
    }

}