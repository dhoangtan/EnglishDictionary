package android.englishdictionary.adapters;

import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.models.Definition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DefinitionAdapter extends BaseAdapter {

    private List<Definition> definitions;
    private LayoutInflater inflater;

    public DefinitionAdapter(Context context, List<Definition> definitions) {
        this.definitions = definitions;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return definitions.size();
    }

    @Override
    public Object getItem(int i) {
        return definitions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_definition_item, null);
            viewHolder = new ViewHolder(
                    view.findViewById(R.id.fr_definition_definition),
                    view.findViewById(R.id.fr_definition_example)
            );

            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();

        Definition definition = (Definition) getItem(i);
        viewHolder.definitionTextView.setText(definition.getDefinition());
        viewHolder.exampleTextView.setText(definition.getExample());
        return view;
    }

    static class ViewHolder {
        TextView definitionTextView;
        TextView exampleTextView;

        public ViewHolder(TextView definitionTextView, TextView exampleTextView) {
            this.definitionTextView = definitionTextView;
            this.exampleTextView = exampleTextView;
        }
    }

}
