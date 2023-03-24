package android.englishdictionary.adapters;

import android.content.Context;
import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.activities.WordListDetailActivity;
import android.englishdictionary.models.Definition;
import android.englishdictionary.models.Word;
import android.englishdictionary.models.WordList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListWordListAdapter extends RecyclerView.Adapter<ListWordListAdapter.ViewHolder> {
    private List<WordList> wordLists;
    private LayoutInflater inflater;
    private Context context;

    public ListWordListAdapter(Context context, List<WordList> wordLists) {
        this.wordLists = wordLists;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_list_wordlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWordListAdapter.ViewHolder holder, int position) {
        WordList current = wordLists.get(position);
        ArrayList<WordList.WordListData> wordListData = (ArrayList<WordList.WordListData>) current.getWords();
        holder.wordListNameTextView.setText(current.getName());
        if (current.getWords().size() == 0) {
            holder.wordListPreviewTextView.setText("Word list is empty!");
            return;
        }
        StringBuilder preview = new StringBuilder();
        int wordListSize = wordListData.size();
        for (int i = 0; i < wordListSize; i++) {
            if (i == 3)
                break;
            preview.append(wordListData.get(i).getWord()).append("\n");
        }
        if (wordListSize > 3)
            preview.append("...+").append(wordListSize - 3);
        holder.wordListPreviewTextView.setText(preview);

        holder.listItemCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WordListDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("word_list", current);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return wordLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordListNameTextView;
        TextView wordListPreviewTextView;
        CardView listItemCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordListNameTextView = itemView.findViewById(R.id.fr_word_list_item_name_label);
            wordListPreviewTextView = itemView.findViewById(R.id.fr_word_list_item_word_list_preview_text_view);
            listItemCardView = itemView.findViewById(R.id.fr_word_list_item_card_view);
        }
    }
}
