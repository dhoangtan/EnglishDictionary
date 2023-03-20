package android.englishdictionary.fragments;

import android.content.Intent;
import android.englishdictionary.R;
import android.englishdictionary.activities.LoginActivity;
import android.englishdictionary.helpers.LevelEnum;
import android.englishdictionary.helpers.OccupationEnum;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView userAvatarImageView, userGenderImageView;
    private TextView userFullNameTextView, userEmailTextView, userLevelTextView, userOccupationTextView;
    private Button editProfileButton, signOutButton;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAvatarImageView = view.findViewById(R.id.fr_profile_user_avatar_image_view);
        userFullNameTextView = view.findViewById(R.id.fr_profile_user_full_name_text_view);
        userEmailTextView = view.findViewById(R.id.fr_profile_user_email_text_view);
        userLevelTextView = view.findViewById(R.id.fr_profile_user_level_text_view);
        userOccupationTextView = view.findViewById(R.id.fr_profile_user_occupation_text_view);
        userGenderImageView = view.findViewById(R.id.fr_profile_user_gender_image_view);
        editProfileButton = view.findViewById(R.id.fr_profile_user_edit_profile_button);
        signOutButton = view.findViewById(R.id.fr_profile_user_sign_out_button);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String,Object> data = document.getData();
                        String email = data.get("email").toString();
                        String fullName = data.get("full_name").toString();
                        int gender = Integer.parseInt(data.get("gender").toString());
                        int level = Integer.parseInt(data.get("level").toString());
                        int occupation = Integer.parseInt(data.get("occupation").toString());

                        userFullNameTextView.setText(fullName);
                        userEmailTextView.setText(email);
                        userLevelTextView.setText(LevelEnum.values()[level].toString());
                        userOccupationTextView.setText(OccupationEnum.values()[occupation].toString());

                        switch (gender) {
                            case 1: {
                                userGenderImageView.setImageResource(R.drawable.ic_male_24);
                                break;
                            }
                            case 2: {
                                userGenderImageView.setImageResource(R.drawable.ic_female_24);
                                break;
                            }
                            case 3: {
                                userGenderImageView.setImageResource(R.drawable.ic_transgender_24);
                                break;
                            }
                            default: {
                                userGenderImageView.setImageResource(R.drawable.ic_question_mark_24);
                                break;
                            }
                        }
                    }
                }
            }
        });

        userAvatarImageView.setImageResource(R.drawable.avatar);

        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}