package android.englishdictionary.adapters;

import android.content.Context;
import android.englishdictionary.R;
import android.englishdictionary.models.Phonetic;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhoneticAdapter extends RecyclerView.Adapter<PhoneticAdapter.ViewHolder> {

    private List<Phonetic> phonetics;
    private LayoutInflater inflater;
    private Context context;

    public PhoneticAdapter(Context context, List<Phonetic> phonetics) {
        this.phonetics = phonetics;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PhoneticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_definition_phonetic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneticAdapter.ViewHolder holder, int position) {
        holder.phoneticTextView.setText(phonetics.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return phonetics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView phoneticTextView;
        Button playAudioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneticTextView = itemView.findViewById(R.id.fr_definition_phonetic_item_phonetic_text_view);
            playAudioButton = itemView.findViewById(R.id.fr_definition_phonetic_item_play_audio_button);

            // TODO: Implement play audio feature
            playAudioButton.setOnClickListener(view -> {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
