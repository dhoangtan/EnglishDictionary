package android.englishdictionary.fragments;

import android.content.Context;
import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.activities.CreateWordListActivity;
import android.englishdictionary.adapters.ListWordListAdapter;
import android.englishdictionary.helpers.WordListClickHandler;
import android.englishdictionary.models.Word;
import android.englishdictionary.models.WordList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseWordListBottomSheetFragment extends BottomSheetDialogFragment{
    final String TAG = "CHOOSE_WORD_LIST_FRAGMENT";
    private Button createWordListButton, cancelButton;
    private RecyclerView wordListRecyclerView;

    private Word word;
    private String definition;

    public ChooseWordListBottomSheetFragment(Word word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public static ChooseWordListBottomSheetFragment newInstance(Word word, String definition) {
        return new ChooseWordListBottomSheetFragment(word, definition);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_word_list_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createWordListButton = view.findViewById(R.id.fr_choose_word_list_add_new_word_list_button);
        cancelButton = view.findViewById(R.id.fr_choose_word_list_cancel_button);
        wordListRecyclerView = view.findViewById(R.id.fr_choose_word_list_word_list_recycler_view);

        ArrayList<WordList> userWordList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("word_lists")
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
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
                        ListWordListAdapter adapter = new ListWordListAdapter(getContext(), userWordList, new WordListClickHandler() {
                            @Override
                            public void onItemClick(WordList wordList) {
                                wordList.getWords().add(new WordList.WordListData(word.getWord(), definition));
                                updateWordList(wordList);
                            }
                        });
                        wordListRecyclerView.setAdapter(adapter);
                    }
                });

        createWordListButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), CreateWordListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("word", word.getWord());
            bundle.putString("definition", definition);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        Log.d(TAG, "Data:\n" + word.getWord() + "\n" + definition);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updateWordList(WordList wordList) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("word_lists")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("name", wordList.getName()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        DocumentReference ref = task.getResult().getDocuments().get(0).getReference();
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", wordList.getName());
                        data.put("user_id", userId);
                        data.put("words", wordList.getWords());
                        ref.update(data)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "onComplete: COMPLETE");
                                        Toast.makeText(getContext(), "Successfully add to word list", Toast.LENGTH_SHORT).show();
                                        dismiss();
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


    }
}
