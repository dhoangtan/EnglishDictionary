package android.englishdictionary.adapters;

import android.app.Activity;
import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.activities.MainActivity;
import android.englishdictionary.fragments.ChooseWordListBottomSheetFragment;
import android.englishdictionary.fragments.DictionaryFragment;
import android.englishdictionary.models.Definition;
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

    private List<Definition> definitions;
    private LayoutInflater inflater;
    private Context context;
    private FragmentManager fragmentManager;

    public DefinitionAdapter(Context context, List<Definition> definitions, FragmentManager fragmentManager) {
        this.definitions = definitions;
        inflater = LayoutInflater.from(context);
        this.context = context;
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
        holder.definitionTextView.setText(definitions.get(position).getDefinition());
        holder.exampleTextView.setText(definitions.get(position).getExample());
        holder.addToWordListButton.setOnClickListener(view -> {
            showBottomSheet();
        });
    }

    public void showBottomSheet() {
        ChooseWordListBottomSheetFragment bottomSheet = new ChooseWordListBottomSheetFragment();
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
