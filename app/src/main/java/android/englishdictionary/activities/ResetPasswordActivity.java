package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.englishdictionary.R;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button okButton;
    private TextInputEditText emailTextInput;
    private TextInputLayout emailTextLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        okButton = findViewById(R.id.ac_reset_password_ok_button);
        emailTextInput = findViewById(R.id.ac_reset_password_email_text_input);
        emailTextLayout = findViewById(R.id.ac_reset_password_email_text_layout);

        emailTextInput.setOnFocusChangeListener((view, hasFocus) -> emailTextLayout.setError(null));

        okButton.setOnClickListener(view -> {
            String email = emailTextInput.getText().toString();

            if (email.isEmpty()) {
                emailTextInput.clearFocus();;
                emailTextLayout.setError("Email is empty");
                return;
            }

            FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener(task ->
                            Toast.makeText(ResetPasswordActivity.this, "Reset password mail has been sent", Toast.LENGTH_SHORT)
                                    .show());
            finish();
        });

    }
}