package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.englishdictionary.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, fullNameEditText;
    private Button registerButton;

    private FirebaseAuth mAuth;
    private final String TAG = "REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.ac_register_username_edit_text);
        passwordEditText = findViewById(R.id.ac_register_password_edit_text);
        confirmPasswordEditText = findViewById(R.id.ac_register_confirm_password_edit_text);
        fullNameEditText = findViewById(R.id.ac_register_full_name_edit_text);

        registerButton = findViewById(R.id.ac_register_register_button);

        registerButton.setOnClickListener(view -> {
            register();
        });

    }

    private void register() {
        String email, password, confirmPassword, fullName;
        email = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();
        fullName = fullNameEditText.getText().toString();

        if (!validateField(email, password, confirmPassword, fullName)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.w(TAG, "createUserWithEmail:success", task.getException());
                            Toast.makeText(RegisterActivity.this, "Register successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Oops. Something went wrong!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private boolean validateField(String email, String password, String confirmPassword, String fullName) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(RegisterActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}