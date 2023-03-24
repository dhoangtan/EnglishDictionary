package android.englishdictionary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.adapters.WordListDetailAdapter;
import android.englishdictionary.models.WordList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WordListDetailActivity extends AppCompatActivity {
    private TextView wordListNameTextView;
    private RecyclerView wordsRecyclerView;
    private Button sortButton, modifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist_detail);

        Intent intent = getIntent();
        WordList wordList = (WordList) intent.getExtras().get("word_list");

        wordListNameTextView = findViewById(R.id.ac_wordlist_detail_wordlist_name_text_view);

        wordsRecyclerView = findViewById(R.id.ac_wordlist_detail_words_recycler_view);

        wordListNameTextView.setText(wordList.getName());
        wordsRecyclerView.setAdapter(new WordListDetailAdapter(getApplicationContext(), (ArrayList<WordList.WordListData>) wordList.getWords()));
    }
}