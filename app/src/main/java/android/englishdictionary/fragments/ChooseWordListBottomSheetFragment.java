package android.englishdictionary.fragments;

import android.content.Context;
import android.englishdictionary.R;
import android.os.Bundle;
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
    private Button createWordListButton, cancelButton;
    private RecyclerView wordListRecyclerView;


    public static ChooseWordListBottomSheetFragment newInstance() {
        return new ChooseWordListBottomSheetFragment();
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

//    @Override
//    public void onClick(View view) {
//        TextView tvSelected = (TextView) view;
//        itemClickListener.onItemClick(tvSelected.getText().toString());
//        dismiss();
//    }
//
//    public interface ItemClickListener {
//        void onItemClick(String item);
//    }

}
