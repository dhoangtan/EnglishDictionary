package android.englishdictionary.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.englishdictionary.R;
import android.englishdictionary.activities.EditProfileActivity;
import android.englishdictionary.activities.LoginActivity;
import android.englishdictionary.activities.MainActivity;
import android.englishdictionary.helpers.LevelEnum;
import android.englishdictionary.helpers.OccupationEnum;
import android.englishdictionary.models.ApplicationUser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private ImageView userAvatarImageView, userGenderImageView;
    private TextView userFullNameTextView, userEmailTextView, userLevelTextView, userOccupationTextView;
    private Button editProfileButton, signOutButton;
    private ApplicationUser appUser;
    // TODO: Rename and change types of parameters

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        appUser = new ApplicationUser(firebaseUser);
        DocumentReference documentReference = FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(firebaseUser.getUid());

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String,Object> data = document.getData();
                    String email = data.get("email").toString();
                    String fullName = data.get("full_name").toString();
                    int gender = Integer.parseInt(data.get("gender").toString());
                    int level = Integer.parseInt(data.get("level").toString());
                    int occupation = Integer.parseInt(data.get("occupation").toString());

                    appUser.setFullName(fullName);
                    appUser.setGender(gender);
                    appUser.setLevel(level);
                    appUser.setOccupation(occupation);

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
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/avatars/" + appUser.getUser().getUid() + "/avatar.jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                userAvatarImageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        userAvatarImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            getActivity().startActivityForResult(intent, MainActivity.LOCAL_PICTURE_CODE);
        });

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), EditProfileActivity.class);
            Bundle bundle = new Bundle();
            Log.d("USER_DEBUG", appUser.getFullName());
            bundle.putParcelable("user", appUser);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        signOutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DictionaryPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    public void updateAvatar(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] uploadData = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("images/avatars/" + appUser.getUser().getUid() + "/avatar.jpg");
        UploadTask uploadTask = storageReference.putBytes(uploadData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity().getApplicationContext(), "There's was an error when uploading file", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity().getApplicationContext(), "Update avatar successfully", Toast.LENGTH_SHORT).show();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/avatars/" + appUser.getUser().getUid() + "/avatar.jpg");
                final long ONE_MEGABYTE = 1024 * 1024;
                storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        userAvatarImageView.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getActivity().getApplicationContext(), "Failed to load user avatar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}