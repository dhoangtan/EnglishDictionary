package android.englishdictionary.adapters;

import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.models.Meaning;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.ViewHolder> {

    private List<Meaning> meanings;
    private LayoutInflater inflater;
    private Context context;
    private FragmentManager fragmentManager;

    public MeaningAdapter(Context context, List<Meaning> meanings, FragmentManager fragmentManager) {
        this.meanings = meanings;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MeaningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_definition_meaning_item, parent, false) ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeaningAdapter.ViewHolder holder, int position) {
        Meaning current = meanings.get(position);
        holder.partOfSpeechTextView.setText(current.getPartOfSpeech());
        holder.definitionsRecyclerView.setAdapter(new DefinitionAdapter(context, current.getDefinitions(), fragmentManager));

        holder.definitionsRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public int getItemCount() {
        return meanings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView partOfSpeechTextView;
        RecyclerView definitionsRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            partOfSpeechTextView = itemView.findViewById(R.id.fr_definition_meaning_item_part_of_speech);
            definitionsRecyclerView = itemView.findViewById(R.id.fr_definition_meaning_item_definition_recycler_view);
        }
    }
}
