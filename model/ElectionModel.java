package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElectionModel {
    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_CLOSED = "CLOSED";
    public static final String STATUS_SUMMARIZED = "SUMMARIZED";

    private String status;
    private List<Candidate> candidates;
    private List<Voter> voters;
    private List<Ballot> ballots;
    private int nextBallotId = 4; // Start from 4 since we have B01, B02, B03 in seed data
    private int[] rankingPoints = { 3, 2, 1 };

    public ElectionModel() {
        this.status = STATUS_OPEN;
        this.candidates = new ArrayList<>();
        this.voters = new ArrayList<>();
        this.ballots = new ArrayList<>();
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public void setVoters(List<Voter> voters) {
        this.voters = voters;
    }

    public void addBallot(Ballot ballot) {
        this.ballots.add(ballot);
    }

    public String getStatus() {
        return status;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public List<Voter> getVoters() {
        return voters;
    }

    public List<Ballot> getBallots() {
        return ballots;
    }

    public void submitVote(String voterId, List<String> ranks) throws Exception {
        if (!status.equals(STATUS_OPEN)) {
            throw new Exception("การลงคะแนนถูกปิดไปแล้ว (Not OPEN)");
        }

        Voter voter = getVoterById(voterId);
        if (voter == null) {
            throw new Exception("ไม่พบผู้มีสิทธิ์นี้");
        }
        if (!voter.isActive()) {
            throw new Exception("ผู้มีสิทธิ์นี้ไม่มีสิทธิ์ Active");
        }
        if (voter.hasVoted()) {
            throw new Exception("ผู้มีสิทธิ์นี้เคยลงคะแนนแล้ว");
        }
        if (ranks.size() != 3) {
            throw new Exception("ต้องเลือกผู้สมัคร 3 คน");
        }
        if (ranks.get(0).equals(ranks.get(1)) || ranks.get(1).equals(ranks.get(2))
                || ranks.get(0).equals(ranks.get(2))) {
            throw new Exception("ผู้สมัครในบัตรต้องแตกต่างกัน");
        }

        String ballotId = String.format("B%02d", nextBallotId++);
        Ballot ballot = new Ballot(ballotId, voterId, ranks);
        ballots.add(ballot);
        voter.setHasVoted(true);
    }

    public void closeVoting() {
        if (!status.equals(STATUS_OPEN)) {
            return;
        }
        this.status = STATUS_CLOSED;

        Map<String, List<Ballot>> patterns = new HashMap<>();
        for (Ballot b : ballots) {
            String pattern = b.getPattern();
            patterns.putIfAbsent(pattern, new ArrayList<>());
            patterns.get(pattern).add(b);
        }

        for (Map.Entry<String, List<Ballot>> entry : patterns.entrySet()) {
            List<Ballot> group = entry.getValue();
            if (group.size() >= 3) {
                for (Ballot b : group) {
                    b.setStatus(Ballot.STATUS_PENDING);
                }
            } else {
                for (Ballot b : group) {
                    b.setStatus(Ballot.STATUS_VERIFIED);
                }
            }
        }
        calculateResults();

        // Check if there are no pending groups to begin with
        if (getPendingGroups().isEmpty()) {
            this.status = STATUS_SUMMARIZED;
        }
    }

    public Map<String, List<Ballot>> getPendingGroups() {
        Map<String, List<Ballot>> pendingGroups = new HashMap<>();
        for (Ballot b : ballots) {
            if (b.getStatus().equals(Ballot.STATUS_PENDING)) {
                String pattern = b.getPattern();
                pendingGroups.putIfAbsent(pattern, new ArrayList<>());
                pendingGroups.get(pattern).add(b);
            }
        }
        return pendingGroups;
    }

    public void verifyGroup(String pattern, boolean isVerified) throws Exception {
        if (!status.equals(STATUS_CLOSED)) {
            throw new Exception("สถานะต้องเป็น CLOSED ถึงจะตัดสินได้");
        }

        boolean foundPending = false;
        for (Ballot b : ballots) {
            if (b.getPattern().equals(pattern) && b.getStatus().equals(Ballot.STATUS_PENDING)) {
                b.setStatus(isVerified ? Ballot.STATUS_VERIFIED : Ballot.STATUS_NOT_COUNTED);
                foundPending = true;
            }
        }

        if (!foundPending) {
            throw new Exception("ไม่พบกลุ่มที่รอตรวจสอบนี้ หรือตัดสินไปแล้ว");
        }

        calculateResults();

        if (getPendingGroups().isEmpty()) {
            this.status = STATUS_SUMMARIZED;
        }
    }

    public void calculateResults() {
        for (Candidate c : candidates) {
            c.resetScore();
        }

        for (Ballot b : ballots) {
            if (b.getStatus().equals(Ballot.STATUS_VERIFIED)) {
                List<String> ranks = b.getRanking();
                for (int i = 0; i < ranks.size(); i++) {
                    Candidate c = getCandidateById(ranks.get(i));
                    if (c != null) {
                        c.addScore(rankingPoints[i]);
                    }
                }
            }
        }
    }

    private Voter getVoterById(String id) {
        for (Voter v : voters) {
            if (v.getId().equals(id))
                return v;
        }
        return null;
    }

    private Candidate getCandidateById(String id) {
        for (Candidate c : candidates) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }
}
