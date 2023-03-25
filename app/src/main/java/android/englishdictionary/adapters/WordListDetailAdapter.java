package android.englishdictionary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.models.WordList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WordListDetailAdapter extends RecyclerView.Adapter<WordListDetailAdapter.ViewHolder> {
    private ArrayList<WordList.WordListData> wordListData;
    private boolean isInEditState;
    private LayoutInflater inflater;

    @SuppressLint("NotifyDataSetChanged")
    public void setEditState(boolean isInEditState) {
        this.isInEditState = isInEditState;
        notifyDataSetChanged();
    }

    public WordListDetailAdapter(Context context, ArrayList<WordList.WordListData> wordListData) {
        this.wordListData = wordListData;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WordListDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_wordlist_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListDetailAdapter.ViewHolder holder, int position) {
        WordList.WordListData current = wordListData.get(position);
        holder.wordListDetailWordTextView.setText(current.getWord());
        holder.wordListDetailDefinitionTextView.setText(current.getDefinition());
        if (!isInEditState)
            holder.wordListDetailRemoveButton.setVisibility(View.GONE);
        else
            holder.wordListDetailRemoveButton.setVisibility(View.VISIBLE);

        holder.wordListDetailRemoveButton.setOnClickListener(v -> {
            wordListData.remove(current);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return wordListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordListDetailWordTextView;
        TextView wordListDetailDefinitionTextView;
        Button wordListDetailRemoveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordListDetailWordTextView = itemView.findViewById(R.id.fr_wordlist_detail_word_text_view);
            wordListDetailDefinitionTextView = itemView.findViewById(R.id.fr_wordlist_detail_definition_text_view);
            wordListDetailRemoveButton = itemView.findViewById(R.id.fr_wordlist_detail_remove_button);
        }
    }
}
