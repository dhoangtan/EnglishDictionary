package android.englishdictionary.fragments;

import android.englishdictionary.R;
import android.englishdictionary.adapters.ListWordListAdapter;
import android.englishdictionary.models.WordList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExploreFragment extends Fragment {
    private final String TAG = "EXPLORE_FRAGMENT";
    RecyclerView cambridgeWordListRecyclerView;
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
        cambridgeWordListRecyclerView = view.findViewById(R.id.fr_explore_cambridge_word_list_recycler_view);

        ArrayList<WordList> wordLists = new ArrayList<>();
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
                            wordLists.add(newWordList);
                        }
                        cambridgeWordListRecyclerView.setAdapter(new ListWordListAdapter(getContext(), wordLists));
                    }
                });
    }
}