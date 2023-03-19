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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button okButton;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        okButton = findViewById(R.id.ac_reset_password_ok_button);
        emailEditText = findViewById(R.id.ac_reset_password_email_edit_text);

        okButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();

            if (email.isEmpty()) {
                Toast.makeText(ResetPasswordActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener(task ->
                            Toast.makeText(ResetPasswordActivity.this, "Email sent", Toast.LENGTH_SHORT)
                                    .show());
            finish();
        });

    }
}