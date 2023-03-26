package android.englishdictionary.fragments;

import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.activities.SearchResultActivity;
import android.englishdictionary.activities.WordListDetailActivity;
import android.englishdictionary.adapters.ListWordListAdapter;
import android.englishdictionary.models.WordList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ExploreFragment extends Fragment {
    private final String TAG = "EXPLORE_FRAGMENT";
    private RecyclerView cambridgeWordListRecyclerView, userWordListRecyclerView, communityWordListRecyclerView;
    private TextInputLayout searchTextInputLayout;
    private TextInputEditText searchTextInputEditText;
    public ExploreFragment() {
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        return new ExploreFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cambridgeWordListRecyclerView = view.findViewById(R.id.fr_explore_cambridge_wordlist_recycler_view);
        userWordListRecyclerView = view.findViewById(R.id.fr_explore_user_wordlist_recycler_view);
        communityWordListRecyclerView = view.findViewById(R.id.fr_explore_community_wordlist_recycler_view);
        searchTextInputLayout = view.findViewById(R.id.fr_explore_search_text_input_layout);
        searchTextInputEditText = view.findViewById(R.id.fr_explore_search_text_input_edit_text);
    }

    @Override
    public void onResume() {
        super.onResume();

        searchTextInputLayout.setEndIconOnClickListener(view -> {
            String searchString = searchTextInputEditText.getText().toString();

            if(searchString.isEmpty())
                return;

            Intent intent = new Intent(getContext(), SearchResultActivity.class);
            intent.putExtra("search_string", searchString);
            getActivity().startActivity(intent);
        });

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ArrayList<WordList> userWordList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("word_lists")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
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
                        userWordListRecyclerView.setAdapter(new ListWordListAdapter(getContext(), userWordList, wordList -> navigateToWordListDetail(wordList, true)));
                    }
                });

        ArrayList<WordList> cambridgeWordList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("word_lists")
                .whereEqualTo("user_id","system")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            WordList newWordList = new WordList();
                            newWordList.setName(document.getString("name"));
                            ArrayList<Map<String, Object>> wordDataFromFireStore = (ArrayList<Map<String, Object>>)document.get("words");
                            for (Map<String, Object> datum: wordDataFromFireStore) {
                                String word = datum.get("word").toString();
                                String definition = datum.get("definition").toString();
                                WordList.WordListData wordListData = new WordList.WordListData(word, definition);
                                newWordList.getWords().add(wordListData);
                            }
                            cambridgeWordList.add(newWordList);
                        }
                        cambridgeWordListRecyclerView.setAdapter(new ListWordListAdapter(getContext(), cambridgeWordList, wordList -> navigateToWordListDetail(wordList, false)));
                    }
                });

        ArrayList<WordList> communityWordList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("word_lists")
                .whereNotIn("user_id", Arrays.asList(userId, "system"))
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            WordList newWordList = new WordList();
                            newWordList.setName(document.getString("name"));
                            ArrayList<Map<String, Object>> wordDataFromFireStore = (ArrayList<Map<String, Object>>)document.get("words");
                            for (Map<String, Object> datum: wordDataFromFireStore) {
                                String word = datum.get("word").toString();
                                String definition = datum.get("definition").toString();
                                WordList.WordListData wordListData = new WordList.WordListData(word, definition);
                                newWordList.getWords().add(wordListData);
                            }
                            communityWordList.add(newWordList);
                        }
                        communityWordListRecyclerView.setAdapter(new ListWordListAdapter(getContext(), communityWordList, wordList -> navigateToWordListDetail(wordList, false)));
                    }
                });

    }

    private void navigateToWordListDetail(WordList wordList, boolean editable) {
        Intent intent = new Intent(getContext(), WordListDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("word_list", wordList);
        bundle.putBoolean("editable", editable);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }
}