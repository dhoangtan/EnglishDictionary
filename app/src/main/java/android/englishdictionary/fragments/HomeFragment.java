package android.englishdictionary.fragments;

import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.activities.CreateWordListActivity;
import android.englishdictionary.activities.MainActivity;
import android.englishdictionary.activities.WordListDetailActivity;
import android.englishdictionary.adapters.ListWordListAdapter;
import android.englishdictionary.helpers.WordListClickHandler;
import android.englishdictionary.models.WordList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {

    private TextView userFullNameTextView;
    private RecyclerView userWordListRecyclerView;
    private Button createWordListButton;
    private ImageView userImageView;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userFullNameTextView = view.findViewById(R.id.fr_home_user_full_name_text_view);
        userWordListRecyclerView = view.findViewById(R.id.fr_home_user_wordlist_recycler_view);
        createWordListButton = view.findViewById(R.id.fr_home_create_wordlist_button);
        userImageView = view.findViewById(R.id.fr_home_user_image_view);

        createWordListButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), CreateWordListActivity.class);
            getActivity().startActivity(intent);
        });

        userImageView.setOnClickListener(v -> {
            ((MainActivity)getActivity()).setBottomNavigationSelectedItem(R.id.bottom_navigation_profile);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                            String fullName = data.get("full_name").toString();
                            userFullNameTextView.setText(fullName);
                        }
                    }
                });

        ArrayList<WordList> userWordList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("word_lists")
                .whereEqualTo("user_id", firebaseUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            WordList newWordList = new WordList();
                            newWordList.setName(document.getString("name"));
                            ArrayList<Map<String, Object>> wordDataFromFireStore = (ArrayList<Map<String, Object>>)document.get("words");
                            for (Map<String, Object> datum: wordDataFromFireStore) {
                                String word = datum.get("word").toString();
                                String definition = datum.get("definition").toString();
                                WordList.WordListData wordListData = new WordList.WordListData(word, definition);
                                newWordList.getWords().add(wordListData);
                            }
                            userWordList.add(newWordList);
                        }
                        userWordListRecyclerView.setAdapter(new ListWordListAdapter(getContext(), userWordList, new WordListClickHandler() {
                            @Override
                            public void onItemClick(WordList wordList) {
                                navigateToWordListDetail(wordList);
                            }
                        }));
                    }
                });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/avatars/" + firebaseUser.getUid() + "/avatar.jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                userImageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    private void navigateToWordListDetail(WordList wordList) {
        Intent intent = new Intent(getContext(), WordListDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("word_list", wordList);
        bundle.putBoolean("editable", true);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }
}