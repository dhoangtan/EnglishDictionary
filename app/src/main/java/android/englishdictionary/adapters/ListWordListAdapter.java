package android.englishdictionary.adapters;

import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.helpers.WordListClickHandler;
import android.englishdictionary.models.WordList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListWordListAdapter extends RecyclerView.Adapter<ListWordListAdapter.ViewHolder> {
    private List<WordList> wordLists;
    private LayoutInflater inflater;
    private WordListClickHandler eventListener;

    public ListWordListAdapter(Context context, List<WordList> wordLists, WordListClickHandler eventListener) {
        this.wordLists = wordLists;
        inflater = LayoutInflater.from(context);
        this.eventListener = eventListener;
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
        }
        else {
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
        }
        holder.listItemLinearLayout.setOnClickListener(view -> this.eventListener.onItemClick(current));
    }

    @Override
    public int getItemCount() {
        return wordLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordListNameTextView;
        TextView wordListPreviewTextView;
        LinearLayout listItemLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordListNameTextView = itemView.findViewById(R.id.fr_wordlist_item_name_label);
            wordListPreviewTextView = itemView.findViewById(R.id.fr_wordlist_item_word_list_preview_text_view);
            listItemLinearLayout = itemView.findViewById(R.id.fr_wordlist_item_linear_layout);
        }
    }
}
