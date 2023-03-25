package android.englishdictionary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.adapters.WordListDetailAdapter;
import android.englishdictionary.models.WordList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class WordListDetailActivity extends AppCompatActivity {
    private TextView wordListNameTextView;
    private RecyclerView wordsRecyclerView;
    private Button sortButton, modifyButton;
    private boolean editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist_detail);

        wordListNameTextView = findViewById(R.id.ac_wordlist_detail_wordlist_name_text_view);
        wordsRecyclerView = findViewById(R.id.ac_wordlist_detail_words_recycler_view);
        sortButton = findViewById(R.id.ac_wordlist_detail_sort_button);
        modifyButton = findViewById(R.id.ac_wordlist_detail_modify_button);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        WordList wordList = (WordList) bundle.get("word_list");
        editable = (boolean) bundle.get("editable");

        if (!editable) {
            modifyButton.setVisibility(View.GONE);
        }

        WordListDetailAdapter detailAdapter = new WordListDetailAdapter(getApplicationContext(), (ArrayList<WordList.WordListData>) wordList.getWords());

        modifyButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditWordListDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        });

        wordListNameTextView.setText(wordList.getName());
        wordsRecyclerView.setAdapter(detailAdapter);

        sortButton.setOnClickListener(view -> {
            wordList.getWords().sort((w1, w2) -> w1.getWord().compareTo(w2.getWord()));
            detailAdapter.notifyDataSetChanged();
        });
    }
}