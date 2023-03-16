package android.englishdictionary.models;

import java.util.ArrayList;

public class Definition {
    private String definition;
    private ArrayList<String> synonyms;
    private ArrayList<String> antonyms;
    private String example;

    public Definition(String definition, ArrayList<String> synonyms, ArrayList<String> antonyms, String example) {
        this.definition = definition;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
        this.example = example;
    }

    public Definition() {
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }

    public ArrayList<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(ArrayList<String> antonyms) {
        this.antonyms = antonyms;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
