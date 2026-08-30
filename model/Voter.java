package model;

public class Voter {
    private String id;
    private String name;
    private boolean active;
    private boolean hasVoted;

    public Voter(String id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.hasVoted = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    @Override
    public String toString() {
        return id + " - " + name + (active ? "" : " (Inactive)");
    }
}
