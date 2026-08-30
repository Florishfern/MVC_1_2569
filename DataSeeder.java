import model.Ballot;
import model.Candidate;
import model.ElectionModel;
import model.Voter;

import java.util.Arrays;
import java.util.List;

public class DataSeeder {
    public static void seed(ElectionModel model) {
        // Candidates
        model.setCandidates(Arrays.asList(
                new Candidate("C01", "Null Pointer"),
                new Candidate("C02", "Merge Conflict"),
                new Candidate("C03", "Works on My Machine"),
                new Candidate("C04", "404 Policy Not Found"),
                new Candidate("C05", "Ctrl+Z Nation")
        ));

        // Voters
        List<Voter> voters = Arrays.asList(
                new Voter("V01", "โพยอยู่ไหน", true),
                new Voter("V02", "บังเอิญเหมือนกัน", true),
                new Voter("V03", "เลือกเองจริง ๆ", true),
                new Voter("V04", "ใจตรงกันเฉย ๆ", true),
                new Voter("V05", "ขอดูอีกที", true),
                new Voter("V06", "บัตรสุดท้าย", true),
                new Voter("V07", "ไม่ได้อยู่กลุ่มไลน์", true)
        );
        model.setVoters(voters);

        // Pre-existing Ballots
        Ballot b01 = new Ballot("B01", "V01", Arrays.asList("C01", "C02", "C03"));
        Ballot b02 = new Ballot("B02", "V02", Arrays.asList("C01", "C02", "C03"));
        Ballot b03 = new Ballot("B03", "V03", Arrays.asList("C02", "C03", "C04"));
        
        model.addBallot(b01);
        model.addBallot(b02);
        model.addBallot(b03);

        // Set voted status
        voters.get(0).setHasVoted(true);
        voters.get(1).setHasVoted(true);
        voters.get(2).setHasVoted(true);
    }
}
