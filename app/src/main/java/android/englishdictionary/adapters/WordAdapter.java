package android.englishdictionary.adapters;

import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.models.Word;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private List<Word> words;
    private Context context;
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;

    public WordAdapter(Context context, List<Word> words, FragmentManager fragmentManager) {
        this.words = words;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_definition_word_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.ViewHolder holder, int position) {
        Word current = words.get(position);
        holder.wordTextView.setText(current.getWord());
        holder.phoneticsRecyclerView.setAdapter(new PhoneticAdapter(context, current.getPhonetics()));
        holder.meaningsRecyclerView.setAdapter(new MeaningAdapter(context,current, current.getMeanings(), fragmentManager));

        holder.phoneticsRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        RecyclerView phoneticsRecyclerView;
        RecyclerView meaningsRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.fr_definition_word_item_word_text_view);
            phoneticsRecyclerView = itemView.findViewById(R.id.fr_definition_word_item_phonetics_recycler_view);
            meaningsRecyclerView = itemView.findViewById(R.id.fr_definition_word_item_meanings_list_view);
        }
    }
}
