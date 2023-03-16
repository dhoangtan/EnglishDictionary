package android.englishdictionary.models;

public class Phonetic {
    private String text;
    private String audio;
    private String sourceUrl;
    private License license;

    public Phonetic(String text, String audio, String sourceUrl, License license) {
        this.text = text;
        this.audio = audio;
        this.sourceUrl = sourceUrl;
        this.license = license;
    }

    public Phonetic() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }
}
