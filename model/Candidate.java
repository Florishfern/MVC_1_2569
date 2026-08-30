package model;

public class Candidate {
    private String id;
    private String name;
    private int score;

    public Candidate(String id, String name) {
        this.id = id;
        this.name = name;
        this.score = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void resetScore() {
        this.score = 0;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
