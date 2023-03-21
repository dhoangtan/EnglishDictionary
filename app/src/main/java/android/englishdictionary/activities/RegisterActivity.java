package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.englishdictionary.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, fullNameEditText;
    private Button registerButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private final String TAG = "REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

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