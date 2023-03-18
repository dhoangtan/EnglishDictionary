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
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DictionaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DictionaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button openCameraButton;
    private Button searchButton;
    private EditText wordEditText;
    private RecyclerView wordDefinitionRecyclerView;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DictionaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DictionaryFragment newInstance(String param1, String param2) {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        openCameraButton = view.findViewById(R.id.fr_dictionary_open_camera_button);
        searchButton = view.findViewById(R.id.fr_dictionary_search_button);
        wordEditText = view.findViewById(R.id.fr_dictionary_word_edit_text);
        wordDefinitionRecyclerView = view.findViewById(R.id.fr_dictionary_list_word_definition_list_view);


        openCameraButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityForResult(intent, 101);
        });

        searchButton.setOnClickListener(v -> {
            String word = wordEditText.getText().toString().toLowerCase(Locale.ROOT).trim();
            ((MainActivity)getActivity()).searchForWord(word);
            wordEditText.setText("");
        });
    }

    public void wordResult(Context context, List<Word> words) {
        if (words.isEmpty()) {
            return;
        }
        wordDefinitionRecyclerView.setAdapter(new WordAdapter(context, words));
        wordDefinitionRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }
}