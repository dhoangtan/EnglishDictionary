package android.englishdictionary.fragments;

import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.models.Word;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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
}
