package model;

import java.util.List;

public class Ballot {
    public static final String STATUS_RECORDED = "RECORDED";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_VERIFIED = "VERIFIED";
    public static final String STATUS_NOT_COUNTED = "NOT_COUNTED";

    private String id;
    private String voterId;
    private List<String> ranking;
    private String status;

    public Ballot(String id, String voterId, List<String> ranking) {
        this.id = id;
        this.voterId = voterId;
        this.ranking = ranking;
        this.status = STATUS_RECORDED;
    }

    public String getId() {
        return id;
    }

    public String getVoterId() {
        return voterId;
    }

    public List<String> getRanking() {
        return ranking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPattern() {
        return String.join(" > ", ranking);
    }
}
