package android.englishdictionary.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

public class ApplicationUser implements Parcelable {
    private FirebaseUser user;
    private String fullName;
    private int gender;
    private int level;
    private int occupation;

    public ApplicationUser(FirebaseUser user) {
        this.user = user;
        fullName = "";
        gender = 0;
        level = 0;
        occupation = 0;
    }

    public ApplicationUser(FirebaseUser user, int gender, int level, int occupation) {
        this.user = user;
        this.gender = gender;
        this.level = level;
        this.occupation = occupation;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    protected ApplicationUser(Parcel in) {
        user = in.readParcelable(FirebaseUser.class.getClassLoader());
        fullName = in.readString();
        gender = in.readInt();
        level = in.readInt();
        occupation = in.readInt();
    }

    public static final Creator<ApplicationUser> CREATOR = new Creator<ApplicationUser>() {
        @Override
        public ApplicationUser createFromParcel(Parcel in) {
            return new ApplicationUser(in);
        }

        @Override
        public ApplicationUser[] newArray(int size) {
            return new ApplicationUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(user, i);
        parcel.writeString(fullName);
        parcel.writeInt(gender);
        parcel.writeInt(level);
        parcel.writeInt(occupation);
    }
}
