package android.englishdictionary.adapters;

import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.models.Definition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.ViewHolder> {

    private List<Definition> definitions;
    private LayoutInflater inflater;

    public DefinitionAdapter(Context context, List<Definition> definitions) {
        this.definitions = definitions;
        inflater = LayoutInflater.from(context);
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
    }

    @Override
    public int getItemCount() {
        return definitions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView definitionTextView;
        TextView exampleTitleTextView;
        TextView exampleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            definitionTextView = itemView.findViewById(R.id.fr_definition_definition);
            exampleTitleTextView = itemView.findViewById(R.id.fr_definition_example_title);
            exampleTextView = itemView.findViewById(R.id.fr_definition_example);
        }
    }
}
