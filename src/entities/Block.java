package entities;

import java.security.MessageDigest;
import java.time.LocalDateTime;

public final class Block {
    private String previousHash;
    private LocalDateTime timestamp;
    private String voterId;
    private Long electionId;
    private Long candidateId;
    private Long nonce;
    private String hash;
    private String signature;

    public Block(String previousHash, String voterId, Long electionId, Long candidateId, String signature) {
        this.previousHash = previousHash;
        this.timestamp = LocalDateTime.now();
        this.voterId = voterId;
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.nonce = 0L;
        this.hash = calculateHash();
        this.signature = signature;
    }

    public Block(String string, String string0, long aLong, long aLong0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String calculateHash() {
        try {
            String data = previousHash + timestamp.toString() + voterId + electionId +
                    candidateId + nonce + signature;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((previousHash == null) ? 0 : previousHash.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((voterId == null) ? 0 : voterId.hashCode());
        result = prime * result + ((electionId == null) ? 0 : electionId.hashCode());
        result = prime * result + ((candidateId == null) ? 0 : candidateId.hashCode());
        result = prime * result + ((nonce == null) ? 0 : nonce.hashCode());
        result = prime * result + ((hash == null) ? 0 : hash.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Block other = (Block) obj;
        if (previousHash == null) {
            if (other.previousHash != null)
                return false;
        } else if (!previousHash.equals(other.previousHash))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (voterId == null) {
            if (other.voterId != null)
                return false;
        } else if (!voterId.equals(other.voterId))
            return false;
        if (electionId == null) {
            if (other.electionId != null)
                return false;
        } else if (!electionId.equals(other.electionId))
            return false;
        if (candidateId == null) {
            if (other.candidateId != null)
                return false;
        } else if (!candidateId.equals(other.candidateId))
            return false;
        if (nonce == null) {
            if (other.nonce != null)
                return false;
        } else if (!nonce.equals(other.nonce))
            return false;
        if (hash == null) {
            if (other.hash != null)
                return false;
        } else if (!hash.equals(other.hash))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Block [previousHash=" + previousHash + ", timestamp=" + timestamp + ", voterId=" + voterId
                + ", electionId=" + electionId + ", candidateId=" + candidateId + ", nonce=" + nonce + ", hash=" + hash
                + "]";
    }
}