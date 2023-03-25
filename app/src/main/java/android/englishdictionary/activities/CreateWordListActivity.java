package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.models.WordList;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateWordListActivity extends AppCompatActivity {

    private EditText wordListNameEditText;
    private Button createWordListButton;
    private final String TAG = "CREATE_WORD_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_word_list);

        wordListNameEditText = findViewById(R.id.ac_create_wordlist_name_edit_text);
        createWordListButton = findViewById(R.id.ac_create_wordlist_submit_button);

        createWordListButton.setOnClickListener(view -> {
            createWordListEvent();
        });
    }

    private void createWordListEvent() {
        String name = wordListNameEditText.getText().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (name.isEmpty()) {
            Toast.makeText(this, "Word list's name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore.getInstance().collection("word_lists")
                .whereEqualTo("name", name)
                .whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isWordListExists = task.getResult().getDocuments().size() > 0;
                        if(!isWordListExists) {
                            Intent intent = getIntent();
                            String word = intent.getExtras().getString("word");
                            String definition = intent.getExtras().getString("definition");
                            Map<String, Object> data = new HashMap<>();

                            ArrayList<WordList.WordListData> wordListData = new ArrayList<>();
                            wordListData.add(new WordList.WordListData(word, definition));

                            data.put("name", name);
                            data.put("user_id", userId );
                            data.put("words", wordListData);

                            FirebaseFirestore.getInstance().collection("word_lists")
                                    .add(data)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(CreateWordListActivity.this, "Create wordlist successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(CreateWordListActivity.this, "Word list with this name is already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailed: FAILED");
                    }
                });
    }
}