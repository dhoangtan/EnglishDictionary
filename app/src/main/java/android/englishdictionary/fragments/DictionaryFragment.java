package android.englishdictionary.fragments;

import android.content.Context;
import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.activities.MainActivity;
import android.englishdictionary.adapters.WordAdapter;
import android.englishdictionary.models.Word;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Locale;

public class DictionaryFragment extends Fragment {

    private TextInputEditText wordTextInput;
    private TextInputLayout wordTextLayout;
    private RecyclerView wordDefinitionRecyclerView;

    public DictionaryFragment() {
    }
    public static DictionaryFragment newInstance(String param1, String param2) {
        return new DictionaryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wordTextLayout = view.findViewById(R.id.fr_dictionary_search_text_layout);
        wordTextInput = view.findViewById(R.id.fr_dictionary_search_text_input);
        wordDefinitionRecyclerView = view.findViewById(R.id.fr_dictionary_list_word_definition_list_view);


        wordTextLayout.setStartIconOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityForResult(intent, MainActivity.CAMERA_CODE);
        });

        wordTextLayout.setEndIconOnClickListener(v -> {
            String word = wordTextInput.getText().toString().toLowerCase(Locale.ROOT).trim();
            ((MainActivity)getActivity()).searchForWord(word);
            wordTextInput.setText("");
        });
    }

    public void wordResult(Context context, List<Word> words) {
        if (words.isEmpty()) {
            return;
        }
        wordDefinitionRecyclerView.setAdapter(new WordAdapter(context, words, getActivity().getSupportFragmentManager()));
        wordDefinitionRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }
}