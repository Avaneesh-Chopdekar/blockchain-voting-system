package services;

import entities.User;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.sql.*;
import java.util.UUID;
import utils.PasswordUtils;

public class UserService {
    private final DatabaseService dbService;
    private final CryptographyService cryptographyService;

    public UserService(DatabaseService dbService) {
        this.cryptographyService = new CryptographyService();
        this.dbService = dbService;
    }

    public User authenticate(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE username = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (PasswordUtils.verifyPassword(password, storedHash)) {
                    return new User(rs.getLong("id"), rs.getString("username"), storedHash, rs.getString("role"),
                            rs.getString("voter_id"), rs.getBoolean("is_voter_id_valid"));
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Authentication failed", e);
        } finally {
            dbService.closeResources(conn, ps, rs);
        }
    }

    public void registerUser(User user, String password) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // Generate key pair
            KeyPair keyPair = cryptographyService.generateKeyPair();
            String publicKeyStr = cryptographyService.encodeKey(keyPair.getPublic());
            String privateKeyStr = cryptographyService.encodeKey(keyPair.getPrivate());

            conn = dbService.getConnection();
            ps = conn.prepareStatement(
                    "INSERT INTO users (username, password_hash, role, public_key, private_key) " +
                            "VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, user.getUsername());
            ps.setString(2, PasswordUtils.hashPassword(password));
            ps.setString(3, user.getRole());
            ps.setString(4, publicKeyStr);
            ps.setString(5, privateKeyStr);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("User registration failed", e);
        } finally {
            dbService.closeResources(conn, ps, null);
        }
    }

    public void generateVoterId(Long userId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dbService.getConnection();
            String voterId = UUID.randomUUID().toString();
            ps = conn.prepareStatement(
                    "UPDATE users SET voter_id = ?, is_voter_id_valid = TRUE WHERE id = ?");
            ps.setString(1, voterId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Voter ID generation failed", e);
        } finally {
            dbService.closeResources(conn, ps, null);
        }
    }

    public PrivateKey getUserPrivateKey(Long userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbService.getConnection();
            ps = conn.prepareStatement("SELECT private_key FROM users WHERE id = ?");
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String privateKeyStr = rs.getString("private_key");
                return cryptographyService.decodePrivateKey(privateKeyStr);
            }
            throw new RuntimeException("User not found");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve private key", e);
        } finally {
            dbService.closeResources(conn, ps, rs);
        }
    }
}
