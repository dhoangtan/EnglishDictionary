package android.englishdictionary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.adapters.ListWordListAdapter;
import android.englishdictionary.models.WordList;
import android.os.Bundle;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {

    RecyclerView searchResultRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        searchResultRecyclerView = findViewById(R.id.ac_search_result_recycler_view);
        String searchString = getIntent().getStringExtra("search_string");

        ArrayList<WordList> userWordList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("word_lists")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String wordListName = document.getString("name");
                            if (wordListName.equalsIgnoreCase(searchString) || wordListName.toLowerCase(Locale.ROOT).contains(searchString.toLowerCase(Locale.ROOT))) {
                                WordList newWordList = new WordList();
                                newWordList.setName(document.getString("name"));
                                ArrayList<Map<String, Object>> wordDataFromFireStore = (ArrayList<Map<String, Object>>)document.get("words");
                                for (Map<String, Object> datum: wordDataFromFireStore) {
                                    String word = datum.get("word").toString();
                                    String definition = datum.get("definition").toString();
                                    WordList.WordListData wordListData = new WordList.WordListData(word, definition);
                                    newWordList.getWords().add(wordListData);
                                }
                                userWordList.add(newWordList);
                            }
                        }
                        searchResultRecyclerView.setAdapter(new ListWordListAdapter(SearchResultActivity.this, userWordList, wordList -> {
                            Intent intent = new Intent(SearchResultActivity.this, WordListDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("word_list", wordList);
                            bundle.putBoolean("editable", false);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }));
                    }
                });

    }
}