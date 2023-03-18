package android.englishdictionary.adapters;

import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.models.Word;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordAdapter extends BaseAdapter {

    private List<Word> words;
    private Context context;
    private LayoutInflater inflater;

    public WordAdapter(Context context, List<Word> words) {
        this.words = words;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int i) {
        return words.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_definition_word_item, null);
            viewHolder = new ViewHolder(
                    view.findViewById(R.id.fr_definition_word_item_word_text_view),
                    view.findViewById(R.id.fr_definition_word_item_phonetics_recycler_view),
                    view.findViewById(R.id.fr_definition_word_item_meanings_list_view)
            );

            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();

        Word word = words.get(i);
        viewHolder.wordTextView.setText(word.getWord());
        viewHolder.phoneticsRecyclerView.setAdapter(new PhoneticAdapter(context, word.getPhonetics()));
        viewHolder.meaningsListView.setAdapter(new MeaningAdapter(context, word.getMeanings()));
        return view;
    }

    static class ViewHolder {
        TextView wordTextView;
        RecyclerView phoneticsRecyclerView;
        ListView meaningsListView;

        ViewHolder(TextView wordTextView, RecyclerView phoneticsRecyclerView, ListView meaningsListView) {
            this.wordTextView = wordTextView;
            this.phoneticsRecyclerView = phoneticsRecyclerView;
            this.meaningsListView = meaningsListView;
        }
    }
}
