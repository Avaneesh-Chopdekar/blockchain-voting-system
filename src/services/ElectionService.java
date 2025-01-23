package services;

import entities.Candidate;
import entities.Election;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElectionService {
    private final DatabaseService dbService;

    public ElectionService(DatabaseService dbService) {
        this.dbService = dbService;
    }

    public void createElection(Election election) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "INSERT INTO elections (title, start_date, end_date, created_by) " +
                            "VALUES (?, ?, ?, ?)");
            ps.setString(1, election.getTitle());
            ps.setTimestamp(2, Timestamp.valueOf(election.getStartDate()));
            ps.setTimestamp(3, Timestamp.valueOf(election.getEndDate()));
            ps.setLong(4, election.getCreatedBy());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Election creation failed", e);
        } finally {
            dbService.closeResources(conn, ps, null);
        }
    }

    public void addCandidate(Candidate candidate) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "INSERT INTO candidates (name, election_id) VALUES (?, ?)");
            ps.setString(1, candidate.getName());
            ps.setLong(2, candidate.getElectionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Candidate addition failed", e);
        } finally {
            dbService.closeResources(conn, ps, null);
        }
    }

    public List<Election> getActiveElections() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Election> activeElections = new ArrayList<>();

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "SELECT * FROM elections WHERE start_date <= NOW() AND end_date >= NOW()");
            rs = ps.executeQuery();

            while (rs.next()) {
                Election election = new Election();
                election.setId(rs.getLong("id"));
                election.setTitle(rs.getString("title"));
                election.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
                election.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
                activeElections.add(election);
            }

            return activeElections;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve active elections", e);
        } finally {
            dbService.closeResources(conn, ps, rs);
        }
    }

    public List<Candidate> getCandidates(Long electionId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Candidate> candidates = new ArrayList<>();

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "SELECT * FROM candidates WHERE election_id = ?");
            ps.setLong(1, electionId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getLong("id"));
                candidate.setName(rs.getString("name"));
                candidate.setElectionId(electionId);
                candidates.add(candidate);
            }

            return candidates;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve candidates", e);
        } finally {
            dbService.closeResources(conn, ps, rs);
        }
    }

    public boolean isElectionActive(Long electionId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM elections " +
                            "WHERE id = ? AND start_date <= NOW() AND end_date >= NOW()");
            ps.setLong(1, electionId);
            rs = ps.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check election status", e);
        } finally {
            dbService.closeResources(conn, ps, rs);
        }
    }
}
