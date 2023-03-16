package android.englishdictionary.models;

import java.util.ArrayList;

public class Word {
    private String word;
    private String phonetic;
    private ArrayList<Phonetic> phonetics;
    private ArrayList<Meaning> meanings;
    private License license;
    private String sourceUrl;

    public Word(String word, String phonetic, ArrayList<Phonetic> phonetics, ArrayList<Meaning> meanings, License license, String sourceUrl) {
        this.word = word;
        this.phonetic = phonetic;
        this.phonetics = phonetics;
        this.meanings = meanings;
        this.license = license;
        this.sourceUrl = sourceUrl;
    }

    public Word() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public ArrayList<Phonetic> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(ArrayList<Phonetic> phonetics) {
        this.phonetics = phonetics;
    }

    public ArrayList<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<Meaning> meanings) {
        this.meanings = meanings;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
