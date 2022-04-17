package classes;

public class Comment {
    private String value;
    private String key;
    private String game_key;

    public Comment() {}

    public Comment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGame_key() {
        return game_key;
    }

    public void setGame_key(String game_key) {
        this.game_key = game_key;
    }
}