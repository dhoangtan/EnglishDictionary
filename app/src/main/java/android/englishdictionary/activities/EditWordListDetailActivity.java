package android.englishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.englishdictionary.R;
import android.englishdictionary.adapters.WordListDetailAdapter;
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

public class EditWordListDetailActivity extends AppCompatActivity {

    private EditText wordListNameEditText;
    private RecyclerView wordsRecyclerView;
    private Button confirmButton, cancelButton, deleteButton;
    private final String TAG = "EDIT_WORD_LIST_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word_list_detail);

        Bundle bundle = getIntent().getExtras();
        WordList wordList = (WordList) bundle.get("word_list");

        wordListNameEditText = findViewById(R.id.ac_edit_wordlist_detail_wordlist_name_edit_text);
        confirmButton = findViewById(R.id.ac_edit_wordlist_detail_confirm_button);
        cancelButton = findViewById(R.id.ac_edit_wordlist_detail_cancel_button);
        deleteButton = findViewById(R.id.ac_edit_wordlist_detail_remove_button);
        wordsRecyclerView = findViewById(R.id.ac_edit_wordlist_detail_words_recycler_view);

        WordListDetailAdapter detailAdapter = new WordListDetailAdapter(getApplicationContext(), (ArrayList<WordList.WordListData>) wordList.getWords());
        detailAdapter.setEditState(true);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String name = wordList.getName();
        ArrayList<WordList.WordListData> wordListData = (ArrayList<WordList.WordListData>) wordList.getWords();

        wordListNameEditText.setText(wordList.getName());
        wordsRecyclerView.setAdapter(detailAdapter);
        confirmButton.setOnClickListener(view -> {
            FirebaseFirestore.getInstance().collection("word_lists")
                    .whereEqualTo("user_id", uid)
                    .whereEqualTo("name", name).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            DocumentReference ref = task.getResult().getDocuments().get(0).getReference();
                            Map<String, Object> data = new HashMap<>();
                            data.put("name", name);
                            data.put("user_id", uid);
                            data.put("words", wordListData);
                            ref.update(data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(EditWordListDetailActivity.this, "Successfully modify word list", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: FAILED");
                                        }
                                    });
                        }
                    });
        });
        cancelButton.setOnClickListener(view -> {
            new AlertDialog.Builder(EditWordListDetailActivity.this)
                    .setTitle("Confirm")
                    .setMessage("Do you want to cancel changes?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        deleteButton.setOnClickListener(view -> {
            new AlertDialog.Builder(EditWordListDetailActivity.this)
                    .setTitle("Confirm")
                    .setMessage("Do you want to delete this word list?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        FirebaseFirestore.getInstance().collection("word_lists")
                                .whereEqualTo("user_id", uid)
                                .whereEqualTo("name", name)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        queryDocumentSnapshots.getDocuments()
                                                .forEach(documentSnapshot -> {
                                                    documentSnapshot.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.d(TAG, "onSuccess: SUCCESS DELETE DOCUMENT");
                                                        }
                                                    });
                                                });
                                    }
                                });
                        Toast.makeText(EditWordListDetailActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}