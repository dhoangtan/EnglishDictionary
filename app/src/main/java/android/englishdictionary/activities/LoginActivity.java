package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.englishdictionary.R;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView createAccountTextView, forgotPasswordTextView;
    private TextInputLayout usernameTextInputLayout, passwordTextInputLayout;
    private TextInputEditText usernameTextInputEditText, passwordTextInputEditText;
    private boolean isPasswordVisible;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPref;
    private final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        sharedPref = this.getSharedPreferences("DictionaryPreferences", Context.MODE_PRIVATE);

        loginButton = findViewById(R.id.ac_login_login_button);
        createAccountTextView = findViewById(R.id.ac_login_create_account_text_view);
        forgotPasswordTextView = findViewById(R.id.ac_login_forgot_password_text_view);
        usernameTextInputLayout = findViewById(R.id.ac_login_username_text_input_layout);
        usernameTextInputEditText = findViewById(R.id.ac_login_username_text_input_edit_text);
        passwordTextInputEditText = findViewById(R.id.ac_login_password_text_input_edit_text);
        passwordTextInputLayout = findViewById(R.id.ac_login_password_text_input_layout);
        isPasswordVisible = false;


        usernameTextInputEditText.setOnFocusChangeListener((view, hasFocus) -> usernameTextInputLayout.setError(null));

        passwordTextInputEditText.setOnFocusChangeListener((view, hasFocus) -> passwordTextInputLayout.setError(null));

        passwordTextInputLayout.setEndIconOnClickListener(view -> {
            if (!isPasswordVisible) {
                passwordTextInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_visibility_off_24);
                isPasswordVisible = true;
            }
            else {
                passwordTextInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_visibility_on_24);
                isPasswordVisible = false;
            }
        });

        forgotPasswordTextView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            passwordTextInputEditText.clearFocus();
            usernameTextInputEditText.clearFocus();

            String email, password;

            email = usernameTextInputEditText.getText().toString();
            password = passwordTextInputEditText.getText().toString();

            boolean isInformationValid = true;

            if (TextUtils.isEmpty(email)) {
                usernameTextInputLayout.setError("Email is empty!");
                isInformationValid = false;
            }

            if (TextUtils.isEmpty(password)) {
                passwordTextInputLayout.setError("Password is empty");
                isInformationValid = false;
            }

            if(!isInformationValid)
                return;



            authenticate(email, password);

        });

        String email = sharedPref.getString("username", "");
        String password = sharedPref.getString("password", "");

        if (!email.isEmpty() && !password.isEmpty()) {
            authenticate(email, password);
        }

        createAccountTextView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void authenticate(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("username", email);
                            editor.putString("password", password);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            passwordTextInputLayout.setError("Password is wrong");
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });

    }
}