package android.englishdictionary.adapters;

import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.models.Definition;
import android.englishdictionary.models.Meaning;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MeaningAdapter extends BaseAdapter {

    private List<Meaning> meanings;
    private LayoutInflater inflater;
    private Context context;

    public MeaningAdapter(Context context, List<Meaning> meanings) {
        this.meanings = meanings;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return meanings.size();
    }

    @Override
    public Object getItem(int i) {
        return meanings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_definition_meaning_item, null);
            viewHolder = new ViewHolder(
                    view.findViewById(R.id.fr_definition_meaning_item_part_of_speech),
                    view.findViewById(R.id.fr_definition_meaning_item_definition_list_view),
                    view.findViewById(R.id.fr_definition_meaning_item_synonyms_list_view),
                    view.findViewById(R.id.fr_definition_meaning_item_antonyms_list_view)
            );

            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();

        Meaning meaning = (Meaning) getItem(i);
        viewHolder.partOfSpeechTextView.setText(meaning.getPartOfSpeech());
        viewHolder.definitionsListView.setAdapter(new DefinitionAdapter(context, meaning.getDefinitions()));
        viewHolder.synonymsListView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, meaning.getSynonyms()));
        viewHolder.antonymsListView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, meaning.getAntonyms()));
        return view;
    }

    static class ViewHolder {
        TextView partOfSpeechTextView;
        ListView definitionsListView;
        ListView synonymsListView;
        ListView antonymsListView;

        public ViewHolder(TextView partOfSpeechTextView, ListView definitionsListView, ListView synonymsListView, ListView antonymsListView) {
            this.partOfSpeechTextView = partOfSpeechTextView;
            this.definitionsListView = definitionsListView;
            this.synonymsListView = synonymsListView;
            this.antonymsListView = antonymsListView;
        }
    }
}
