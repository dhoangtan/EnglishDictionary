package android.englishdictionary.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class WordList implements Parcelable {
    private String name;
    private String userId;
    private List<WordListData> words;

    protected WordList(Parcel in) {
        name = in.readString();
        userId = in.readString();
        words = new ArrayList<WordListData>();
        in.readList(words, WordListData.class.getClassLoader());
    }

    public static final Creator<WordList> CREATOR = new Creator<WordList>() {
        @Override
        public WordList createFromParcel(Parcel in) {
            return new WordList(in);
        }

        @Override
        public WordList[] newArray(int size) {
            return new WordList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(userId);
        parcel.writeList(words);
    }

    public static class WordListData implements Parcelable {
        String word;
        String definition;

        public WordListData(String word, String definition) {
            this.word = word;
            this.definition = definition;
        }

        protected WordListData(Parcel in) {
            word = in.readString();
            definition = in.readString();
        }

        public static final Creator<WordListData> CREATOR = new Creator<WordListData>() {
            @Override
            public WordListData createFromParcel(Parcel in) {
                return new WordListData(in);
            }

            @Override
            public WordListData[] newArray(int size) {
                return new WordListData[size];
            }
        };

        public String getWord() {
            return word;
        }

        public String getDefinition() {
            return definition;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(word);
            parcel.writeString(definition);
        }
    }

    public WordList(String name, String userId, List<WordListData> words) {
        this.name = name;
        this.userId = userId;
        this.words = words;
    }

    public WordList() {
        words = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<WordListData> getWords() {
        return words;
    }

    public void setWords(List<WordListData> words) {
        this.words = words;
    }
}
