package entities;

import java.time.LocalDateTime;

public class VoteRecord {
    private String electionTitle;
    private LocalDateTime timestamp;
    private String candidateName;

    public VoteRecord() {
    }

    public VoteRecord(String electionTitle, LocalDateTime timestamp, String candidateName) {
        this.electionTitle = electionTitle;
        this.timestamp = timestamp;
        this.candidateName = candidateName;
    }

    public String getElectionTitle() {
        return electionTitle;
    }

    public void setElectionTitle(String electionTitle) {
        this.electionTitle = electionTitle;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }
}
