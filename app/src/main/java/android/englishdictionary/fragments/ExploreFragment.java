package android.englishdictionary.fragments;

import android.englishdictionary.R;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExploreFragment extends Fragment {
    private TextView myWordListLabel;
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
        myWordListLabel = view.findViewById(R.id.fr_explore_my_word_list_label);
        myWordListLabel.setOnClickListener(v -> {
            showBottomSheet();
        });
    }

    public void showBottomSheet() {
        ChooseWordListBottomSheetFragment bottomSheet = new ChooseWordListBottomSheetFragment();
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }
}