package services;

import entities.Block;
import entities.VoteRecord;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlockchainService {
    private final DatabaseService dbService;
    private final CryptographyService cryptographyService;
    private static final int MINING_DIFFICULTY = 4;

    public BlockchainService(DatabaseService dbService) {
        this.cryptographyService = new CryptographyService();
        this.dbService = dbService;
    }

    public void castVote(String voterId, Long electionId, Long candidateId, PrivateKey privateKey) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbService.getConnection();
            // Get the last block's hash
            ps = conn.prepareStatement("SELECT hash FROM blocks ORDER BY id DESC LIMIT 1");
            rs = ps.executeQuery();
            String previousHash = rs.next() ? rs.getString("hash") : "0";

            // Create signature of vote data
            String voteData = voterId + electionId + candidateId;
            String signature = cryptographyService.signData(voteData, privateKey);

            // Create and mine new block
            Block block = new Block(previousHash, voterId, electionId, candidateId, signature);
            block.mineBlock(MINING_DIFFICULTY);

            // Save the block
            ps = conn.prepareStatement(
                    "INSERT INTO blocks (previous_hash, timestamp, voter_id, election_id, " +
                            "candidate_id, nonce, hash, signature) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, block.getPreviousHash());
            ps.setTimestamp(2, Timestamp.valueOf(block.getTimestamp()));
            ps.setString(3, block.getVoterId());
            ps.setLong(4, block.getElectionId());
            ps.setLong(5, block.getCandidateId());
            ps.setLong(6, block.getNonce());
            ps.setString(7, block.getHash());
            ps.setString(8, block.getSignature());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Vote casting failed", e);
        } finally {
            dbService.closeResources(conn, ps, rs);
        }
    }

    public boolean verifyBlockchain() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "SELECT b.*, u.public_key FROM blocks b " +
                            "JOIN users u ON b.voter_id = u.voter_id " +
                            "ORDER BY b.id");
            rs = ps.executeQuery();

            String previousHash = "0";
            while (rs.next()) {
                // Verify block hash chain
                Block block = new Block(
                        rs.getString("previous_hash"),
                        rs.getString("voter_id"),
                        rs.getLong("election_id"),
                        rs.getLong("candidate_id"),
                        rs.getString("signature"));

                if (!block.getPreviousHash().equals(previousHash)) {
                    return false;
                }

                // Verify vote signature
                String voteData = block.getVoterId() + block.getElectionId() + block.getCandidateId();
                PublicKey publicKey = cryptographyService.decodePublicKey(rs.getString("public_key"));
                if (!cryptographyService.verifySignature(voteData, block.getSignature(), publicKey)) {
                    return false;
                }

                previousHash = block.getHash();
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Blockchain verification failed", e);
        } finally {
            dbService.closeResources(conn, ps, rs);
        }
    }

    public boolean hasVoted(String voterId, Long electionId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM blocks WHERE voter_id = ? AND election_id = ?");
            ps.setString(1, voterId);
            ps.setLong(2, electionId);
            rs = ps.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check voting status", e);
        } finally {
            dbService.closeResources(conn, ps, rs);
        }
    }

    public List<VoteRecord> getVotingHistory(String voterId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<VoteRecord> history = new ArrayList<>();

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "SELECT e.title AS election_title, " +
                            "b.timestamp, " +
                            "c.name AS candidate_name " +
                            "FROM blocks b " +
                            "JOIN elections e ON b.election_id = e.id " +
                            "JOIN candidates c ON b.candidate_id = c.id " +
                            "WHERE b.voter_id = ? " +
                            "ORDER BY b.timestamp DESC");
            ps.setString(1, voterId);
            rs = ps.executeQuery();

            while (rs.next()) {
                VoteRecord record = new VoteRecord(
                        rs.getString("election_title"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("candidate_name"));
                history.add(record);
            }

            return history;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve voting history", e);
        } finally {
            dbService.closeResources(conn, ps, rs);
        }
    }
}