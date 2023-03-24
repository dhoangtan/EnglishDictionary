package android.englishdictionary.adapters;

import android.app.Activity;
import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.activities.MainActivity;
import android.englishdictionary.fragments.ChooseWordListBottomSheetFragment;
import android.englishdictionary.fragments.DictionaryFragment;
import android.englishdictionary.models.Definition;
import android.englishdictionary.models.Word;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.ViewHolder> {

    private Word word;
    private List<Definition> definitions;
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;

    public DefinitionAdapter(Context context, Word word, List<Definition> definitions, FragmentManager fragmentManager) {
        this.word = word;
        this.definitions = definitions;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public DefinitionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_definition_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DefinitionAdapter.ViewHolder holder, int position) {
        Definition current = definitions.get(position);
        if (current.getExample() == null) {
            holder.exampleTitleTextView.setVisibility(View.GONE);
            holder.exampleTextView.setVisibility(View.GONE);
        }
        holder.definitionTextView.setText(current.getDefinition());
        holder.exampleTextView.setText(current.getExample());
        holder.addToWordListButton.setOnClickListener(view -> {
            showBottomSheet(current.getDefinition(), current.getExample());
        });
    }

    public void showBottomSheet(String definition, String example) {
        ChooseWordListBottomSheetFragment bottomSheet = ChooseWordListBottomSheetFragment.newInstance(word, definition);
        bottomSheet.show(fragmentManager, bottomSheet.getTag());
    }

    @Override
    public int getItemCount() {
        return definitions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView definitionTextView;
        TextView exampleTitleTextView;
        TextView exampleTextView;
        Button addToWordListButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            definitionTextView = itemView.findViewById(R.id.fr_definition_definition);
            exampleTitleTextView = itemView.findViewById(R.id.fr_definition_example_title);
            exampleTextView = itemView.findViewById(R.id.fr_definition_example);
            addToWordListButton = itemView.findViewById(R.id.fr_definition_add_to_word_list_button);
        }
    }
}
