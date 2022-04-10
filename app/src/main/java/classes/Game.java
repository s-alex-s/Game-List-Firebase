package classes;

import java.io.Serializable;

public class Game implements Serializable {
    private String name;
    private int release_date;
    private String genre;
    private String key;

    public Game() {}

    public Game(String name, int release_date, String genre) {
        this.name = name;
        this.release_date = release_date;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRelease_date() {
        return release_date;
    }

    public void setRelease_date(int release_date) {
        this.release_date = release_date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
